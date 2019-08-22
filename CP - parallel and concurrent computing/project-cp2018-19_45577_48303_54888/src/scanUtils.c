#include <stdlib.h>
#include <stdio.h>
#include <string.h> 
#include <cilk/cilk.h>
#include <math.h>

#include "scanUtils.h"

void upsweep(struct Node *nodes, void *src, int pos, int low, int high, size_t sizeJob, void (*worker)(void *v1, const void *v2, const void *v3)) {

    nodes[pos].sum = malloc(sizeJob);

	if(low + 1 == high) {
		memcpy(nodes[pos].sum, src + low * sizeJob, sizeJob);
	} else {
		int mid = (low + high)/2;
		cilk_spawn upsweep(nodes, src, 2*pos+1, low, mid, sizeJob, worker);
		upsweep(nodes, src, 2*pos+2, mid, high, sizeJob, worker);
		cilk_sync;

		worker(nodes[pos].sum, nodes[2*pos+1].sum, nodes[2*pos+2].sum);
	}
	nodes[pos].leftRange = low;
	nodes[pos].rightRange = high;

}

void downsweep(void *dest, void *src, struct Node* nodes, int pos, size_t sizeJob, void *parentFromLeft, void (*worker)(void *v1, const void *v2, const void *v3)) {
	nodes[pos].fromLeft = malloc(sizeJob);
    nodes[pos].fromLeft = parentFromLeft;

	if(nodes[pos].leftRange + 1 == nodes[pos].rightRange) {
	    worker(dest + nodes[pos].leftRange * sizeJob, src + nodes[pos].leftRange * sizeJob, nodes[pos].fromLeft);
	} else {
		cilk_spawn downsweep(dest, src, nodes, 2*pos+1, sizeJob, parentFromLeft, worker);
		void* temp = malloc(sizeJob);
		worker(temp, parentFromLeft, nodes[2*pos+1].sum);
		downsweep(dest, src, nodes, 2*pos+2, sizeJob, temp, worker);
		cilk_sync;
	}
}

void cilk_scan(void *dest, void *src, size_t nJob, size_t sizeJob, void (*worker)(void *v1, const void *v2, const void *v3)) {

	struct Node* nodes = malloc(sizeof(struct Node) * (2*nJob-1));

	upsweep(nodes, src, 0, 0, nJob, sizeJob, worker);

	nodes[0].fromLeft = malloc(sizeJob);
	memset(nodes[0].fromLeft, 0, sizeJob);

	downsweep(dest, src, nodes, 0, sizeJob, nodes[0].fromLeft, worker);

	free(nodes);
}