//
// Created by dzoni on 27-11-2018.
//

#ifndef CP2018_19_PROJECT_45577_48303_54888_FARMUTILS_H
#define CP2018_19_PROJECT_45577_48303_54888_FARMUTILS_H


int* getAvailableWorker(int *workers, int nWorkers);

void passTaskToWorker(void *dest, void *src, void (*task)(void *v1, const void *v2), int *workers, int nWorkers);

int* initWorkers(int nWorkers);

void destroyWorkers(int *workers);

#endif //CP2018_19_PROJECT_45577_48303_54888_FARMUTILS_H
