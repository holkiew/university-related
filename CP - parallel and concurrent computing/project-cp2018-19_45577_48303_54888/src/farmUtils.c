#include "farmUtils.h"
#include <pthread.h>
#include <stdlib.h>
#define WORKER_OCCUPIED 1
#define WORKER_FREE 0

pthread_mutex_t farmWorkerMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t isWorkerAvailable = PTHREAD_COND_INITIALIZER;

//consists of list[n] where n is number of workers. If there are no available workers, thread has to wait for one to finish
//marking him as WORKER_FREE and mutex conditional signal.
//returns worker address to set it back to free after work done.
int* getAvailableWorker(int *workers, int nWorkers){
    pthread_mutex_lock(&farmWorkerMutex);
    while(1) {
        for (int i = 0; i < nWorkers; i++) {
            if (!workers[i]) {
                workers[i] = WORKER_OCCUPIED;
                pthread_mutex_unlock(&farmWorkerMutex);
                return workers;
            }
        }
        pthread_cond_wait(&isWorkerAvailable, &farmWorkerMutex);
    }
}

void passTaskToWorker(void *dest, void *src, void (*task)(void *v1, const void *v2), int *workers, int nWorkers){
    int *worker = getAvailableWorker(workers, nWorkers);
    task(dest, src);
    pthread_mutex_lock(&farmWorkerMutex);
    *worker = WORKER_FREE;
    pthread_mutex_unlock(&farmWorkerMutex);
    pthread_cond_signal(&isWorkerAvailable);
}

int* initWorkers(int nWorkers){
    int* workers = (int*)malloc(sizeof(int) * nWorkers);
    for(int i = 0; i < nWorkers; i++){
        workers[i] = 0;
    }
    return workers;
}

void destroyWorkers(int *workers){
    free(workers);
    pthread_cond_destroy(&isWorkerAvailable);
    pthread_mutex_destroy(&farmWorkerMutex);
}