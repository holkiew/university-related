//
// Created by pedro on 27-11-2018
//

#ifndef __SCANUTILS_H
#define __SCANUTILS_H

struct Node {
	int leftRange;
	int rightRange;
	void* sum;
	void* fromLeft;
};

void upsweep(
	struct Node *nodes,
	void *src,
	int pos,
	int low,
	int high,
	size_t sizeJob,
	void (*worker)(void *v1, const void *v2, const void *v3)
);

void downsweep(
	void *dest,
	void *src,
	struct Node *nodes,
	int pos,
	size_t sizeJob,
	void *parentFromLeft,
	void (*worker)(void *v1, const void *v2, const void *v3)
);

void cilk_scan(
	void *dest,           // Target array
	void *src,            // Source array
	size_t nJob,          // # elements in the source array
	size_t sizeJob,       // Size of each element in the source array
	void (*worker)(void *v1, const void *v2, const void *v3) // [ v1 = op (v2, v3) ]
);

#endif