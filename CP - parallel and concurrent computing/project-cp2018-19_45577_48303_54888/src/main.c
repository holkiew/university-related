#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <cilk/cilk_api.h>
#include <time.h>
#include <sys/time.h>
#include <limits.h>
#include "unit.h"
#include "debug.h"

#define TYPE double


static long long wall_clock_time(void) {
    struct timeval tv;
    gettimeofday(&tv, NULL);
    return tv.tv_sec * 1e6 + tv.tv_usec;
}


int main(int argc, char *argv[]) {
    int i, N;
    long long start, end, min, max;

    int c;
    while ((c = getopt(argc, argv, "d")) != -1)
        switch (c) {
            case 'd':
                debug = 1;
                break;
            default:
                printf("Invalid option\n");
                abort();
        }
    argc -= optind;
    argv += optind;

    if (argc < 1 || argc > 3) {
        printf("Usage: ./example N test_loops cilk_nworkers");
        return -1;
    }

    srand(time(NULL));
    srand48(time(NULL));

    N = atol(argv[0]);

    printf("Initializing SRC array\n");
    TYPE *src = malloc(sizeof(*src) * N);
    for (i = 0; i < N; i++)
        src[i] = drand48();
    printf("Done!\n");

    if(argv[2] != NULL){
        if ( 0 != __cilkrts_set_param("nworkers", argv[2])){
        printf("Failed to set worker count\n");
        return 1;
        }
    }

    printDouble(src, N, "SRC");
    if (debug)
        printf("\n\n");


    long long testLoops = argv[1] != NULL ? atol(argv[1]) : 1;

    long long executionTime = 0;
    for (int i = 0; i < nTestFunction; i++) {
        start = 0; end = 0, min = LONG_MAX, max = 0;
        for (int j = 0; j < testLoops; j++) {
            long long localStart = wall_clock_time();
            testFunction[i](src, N, sizeof(*src));
            long long localEnd = wall_clock_time();
            start += localStart;
            end += localEnd;
            executionTime = (long)(localEnd - localStart);
            if(executionTime < min) min = executionTime;
            if(executionTime > max) max = executionTime;
        }
        if(testLoops > 1){
            printf("%s: Average time: \t%8lld\tmicroseconds\n", testNames[i], (long) (end - start)/testLoops);
            printf("%s: Minimum time: \t%8lld\tmicroseconds\n", testNames[i], min);
            printf("%s: Maximum time: \t%8lld\tmicroseconds\n", testNames[i], max);
        }
        else {
            printf("%s: \t%8ld\tmicroseconds\n", testNames[i], (long) (end - start));
        }
        if (debug)
            printf("\n\n");
    }

    return 0;
}
