#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <cilk/cilk.h>

#include "scanUtils.h"
#include "patterns.h"
#include "farmUtils.h"
#include <math.h>

static void workerInc(void* a, const void* b, const void* c) {
    // a = b + c
    *(int *)a = *(int *)b + *(int *)c;
}

void map (void *dest, void *src, size_t nJob, size_t sizeJob, void (*worker)(void *v1, const void *v2)) {
    assert (dest != NULL);
    assert (src != NULL);
    assert (worker != NULL);

    cilk_for (register unsigned int i=0; i < nJob; i++) {
        worker(dest + i * sizeJob, src + i * sizeJob);
    }
}

//Worker function HAS TO BE ASSOCIATIVE - elements order doesn't matter
//Those confusing loops trying to pretend to behave like working on tree structure
//overall shift formula is as follows: cellA = j * 2^n, cellB = j * 2^n + 2^n, where in cellA operation result is saved.
//And as easily reckon on, final result is in src[0]
void reduce (void *dest, void *src, size_t nJob, size_t sizeJob, void (*worker)(void *v1, const void *v2, const void *v3)) {
    assert (dest != NULL);
    assert (src != NULL);
    assert (worker != NULL);
    assert (nJob > 1);

    int treeLevels = ceil(log2(nJob));

    for (long treeLevel = 0; treeLevel < treeLevels; treeLevel++){
        long levelCellShift = pow(2, treeLevel);
        long revertedLoopCondition = ceil(((nJob - levelCellShift)/levelCellShift));
        cilk_for(long i = 0; i <= revertedLoopCondition; i+=2){
            long cell1 = i * levelCellShift;
            long cell2 = cell1 + levelCellShift;
            worker(src + cell1 * sizeof(sizeJob), src + cell1 * sizeof(sizeJob), src + cell2 * sizeof(sizeJob));
        }
    }
    dest = src;
}

void scan (void *dest, void *src, size_t nJob, size_t sizeJob, void (*worker)(void *v1, const void *v2, const void *v3)) {
    assert (dest != NULL);
    assert (src != NULL);
    assert (worker != NULL);
    if (nJob > 1) {
        cilk_scan(dest, src, nJob, sizeJob, worker);
    }
}

int pack (void *dest, void *src, size_t nJob, size_t sizeJob, const int *filter) {
    
    int *bitsum = malloc(nJob * sizeof(int));

    scan(bitsum, (void*)filter, nJob, sizeof(int), workerInc);
    
    cilk_for(int i=0; i < nJob; i++){
        if(filter[i]==1)
            memcpy(dest + (bitsum[i]-1) * sizeJob, src + i * sizeJob, sizeJob);
    }

    return bitsum[nJob-1];
}

void gather (void *dest, void *src, size_t nJob, size_t sizeJob, const int *filter, int nFilter) {

    cilk_for (int i=0; i < nFilter; i++) {
         memcpy(dest + i * sizeJob, src + filter[i] * sizeJob, sizeJob);
     }

}


void scatter (void *dest, void *src, size_t nJob, size_t sizeJob, const int *filter) {

    cilk_for (int i=0; i < nJob; i++) {
        memcpy(dest + filter[i] * sizeJob, src + i * sizeJob, sizeJob);
    }
}

void f(void *dest, void *src, size_t sizeJob, void (*workerList[])(void *v1, const void *v2), int* threadState, int currentWorker, size_t nWorkers, int currentJob) {
    threadState[currentWorker] = 1; //occupied thread

    workerList[currentWorker](dest + currentJob * sizeJob, dest + currentJob * sizeJob);

    if(currentWorker + 1 < nWorkers) {
        while(threadState[currentWorker+1] == 1) { } //wait till thread is free
        cilk_spawn f(dest, src, sizeJob, workerList, threadState, currentWorker + 1, nWorkers, currentJob);
    }
    threadState[currentWorker] = 0;
}

void pipeline (void *dest, void *src, size_t nJob, size_t sizeJob, void (*workerList[])(void *v1, const void *v2), size_t nWorkers) {

    /*saves the states of the threads (occupied -1 or free - 0 )*/
    int* threadState = malloc(nWorkers*sizeof(int));

    for (int i=0; i < nJob; i++) {
        memcpy(dest+i*sizeJob, src+i*sizeJob, sizeJob);
        while(threadState[0] == 1) {   } //wait till thread is free
        cilk_spawn f(dest, src, sizeJob, workerList, threadState, 0, nWorkers, i);

    }

    free(threadState);
}

//stream of tasks, each task has to be passed to free, available worker.
void farm (void *dest, void *src, size_t nJob, size_t sizeJob, void (*task)(void *v1, const void *v2), size_t nWorkers) {
    assert (dest != NULL);
    assert (src != NULL);
    assert (task != NULL);
    assert (nWorkers > 0);

    int *workers = initWorkers(nWorkers);

    cilk_for(register unsigned int i = 0; i < nJob; i++){
        passTaskToWorker(dest + i * sizeJob, src + i * sizeJob, task, workers, nWorkers);
    }
    //No need for collector since map operates on fragments of same resource map, hence elements stay in same order
    destroyWorkers(workers);
}

