//
// Created by DZONI on 21.09.2018.
//

#ifndef LAB2_C_POINT_H
#define LAB2_C_POINT_H

#include <pthread.h>

struct point {
    double x, y;
};

struct threadFunctionArgs {
    long n, pointsInCircle;
};

struct threadWithArguments {
    pthread_t thread;
    struct threadFunctionArgs *threadArgs;
};

#endif //LAB2_C_POINT_H
