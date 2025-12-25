#include <stdio.h>
#include <pthread.h>
#include "logger.h"

pthread_mutex_t log_mutex = PTHREAD_MUTEX_INITIALIZER;

void log_message(const char *msg) {
    pthread_mutex_lock(&log_mutex);
    FILE *f = fopen("chat.log", "a");
    if (f) {
        fprintf(f, "%s\n", msg);
        fclose(f);
    }
    pthread_mutex_unlock(&log_mutex);
}
