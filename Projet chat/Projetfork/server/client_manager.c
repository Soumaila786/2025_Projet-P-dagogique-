#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include "client_manager.h"

#define MAX_CLIENTS 10
#define BUFFER_SIZE 1024

typedef struct {
    int pid;
    int pipe_fd;
} client_pipe_t;

client_pipe_t clients[MAX_CLIENTS];
int nb_clients = 0;
pthread_mutex_t clients_mutex = PTHREAD_MUTEX_INITIALIZER;

void ajouter_client_pipe(int pid, int fd_pipe) {
    pthread_mutex_lock(&clients_mutex);
    if (nb_clients < MAX_CLIENTS) {
        clients[nb_clients].pid = pid;
        clients[nb_clients].pipe_fd = fd_pipe;
        nb_clients++;
    }
    pthread_mutex_unlock(&clients_mutex);
}

void supprimer_client_pipe(int pid) {
    pthread_mutex_lock(&clients_mutex);
    for (int i = 0; i < nb_clients; i++) {
        if (clients[i].pid == pid) {
            clients[i] = clients[nb_clients - 1];
            nb_clients--;
            break;
        }
    }
    pthread_mutex_unlock(&clients_mutex);
}

void diffuser_message_parent(char *msg, int sender_pid) {
    pthread_mutex_lock(&clients_mutex);
    for (int i = 0; i < nb_clients; i++) {
        if (clients[i].pid != sender_pid) {
            write(clients[i].pipe_fd, msg, strlen(msg));
        }
    }
    pthread_mutex_unlock(&clients_mutex);
}
