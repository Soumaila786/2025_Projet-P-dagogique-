#include <stdio.h>
#include <pthread.h>

pthread_mutex_t log_mutex = PTHREAD_MUTEX_INITIALIZER;

void log_message(const char *msg) {
    pthread_mutex_lock(&log_mutex);

    FILE *file = fopen("fichierChat.log", "a");
    if (file != NULL) {
        fprintf(file, "%s\n", msg);
        fclose(file);
    }

    pthread_mutex_unlock(&log_mutex);
}
