#include <stdio.h>
#include <stdlib.h>
#include "Point.h"
#include <time.h>
#include <pthread.h>
#include <math.h>

typedef int boolean;
#define true 1
#define false 0

typedef struct point Point;
typedef struct threadFunctionArgs ThreadFunctionArgs;
typedef struct threadWithArguments ThreadWithArguments;

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
boolean clockInitialized = false;
clock_t start_t, end_t;

Point *generateRandomPoints(long n);
boolean isPointInCircle(Point *point);
void *calculatePointsFunction(void *functionArgument);
ThreadWithArguments *createCalculationThreads(long nThreds, long nPoints);

void joinThreads(ThreadWithArguments *pArgumentsArray, long nThreads);

void calculatePiWriteMessageAndFreeMemory(ThreadWithArguments *pArgumentsArray, long nThreads);

long threadAmount = 4;
long pointsNumber = 100000000;

int main() {
    ThreadWithArguments *pArgumentsArray = createCalculationThreads(threadAmount, pointsNumber);
    joinThreads(pArgumentsArray, threadAmount);
    calculatePiWriteMessageAndFreeMemory(pArgumentsArray, threadAmount);
    return 0;
}

ThreadWithArguments *createCalculationThreads(long nThreds, long nPoints) {
    ThreadWithArguments *threadWithArgsArray = malloc(sizeof(ThreadWithArguments) * nThreds);
    int pointsPerThread = nPoints / nThreds;
    for (int i = 0; i < nThreds; ++i) {
        threadWithArgsArray[i].threadArgs = malloc(sizeof(ThreadFunctionArgs));
        threadWithArgsArray[i].threadArgs->n = pointsPerThread;
        pthread_create(&threadWithArgsArray[i].thread, NULL, calculatePointsFunction,
                       (void *) threadWithArgsArray[i].threadArgs);
    }
    return threadWithArgsArray;
}

void joinThreads(ThreadWithArguments *pArgumentsArray, long nThreads) {
    for (int i = 0; i < nThreads; ++i) {
        pthread_join(pArgumentsArray[i].thread, NULL);
    }
}

void calculatePiWriteMessageAndFreeMemory(ThreadWithArguments *pArgumentsArray, long nThreads) {
    long pointsInsideCircle = 0, nPoints = 0;
    for (int i = 0; i < nThreads; ++i) {
        pointsInsideCircle += pArgumentsArray[i].threadArgs->pointsInCircle;
        nPoints += pArgumentsArray[i].threadArgs->n;
        free(pArgumentsArray[i].threadArgs);
    }
    free(pArgumentsArray);
    double piEstimation = 4 * pointsInsideCircle / (double) nPoints;
    double total_t = (double) (end_t - start_t) / CLOCKS_PER_SEC;
    printf("PiEstimation = %f\nTotal points / points inside circle = %d/%d\nExecution time = %.4fs", piEstimation,
           nPoints, pointsInsideCircle, total_t);
}


void *calculatePointsFunction(void *functionArgument) {
    pthread_mutex_lock(&mutex);
    if (!clockInitialized) {
        clockInitialized = true;
        start_t = clock();
    }
    pthread_mutex_unlock(&mutex);
    ThreadFunctionArgs *args = (ThreadFunctionArgs *) functionArgument;
    Point *pointArray = generateRandomPoints(args->n);
    args->pointsInCircle = 0;
    for (int i = 0; i < args->n; ++i) {
        if (isPointInCircle(&pointArray[i])) {
            args->pointsInCircle++;
        }
    }
    end_t = clock();
//    printf("Thread ended, array address = %x\n", pointArray);
    free(pointArray);
    return NULL;
}

Point *generateRandomPoints(long n) {
    srand(time(NULL));
    Point *pointArray = malloc(sizeof(Point) * n);
    for (int i = 0; i < n; ++i) {
        pointArray[i].x = rand() / (double) RAND_MAX;
        pointArray[i].y = rand() / (double) RAND_MAX;
    }
    return pointArray;
}

boolean isPointInCircle(Point *point) {
    double r = 0.5, xp = 0.5, yp = 0.5;
    double rough = pow((xp - point->x), 2) + pow((yp - point->y), 2);
    double aftersqrt = sqrt(rough);
    return aftersqrt < r;
}
