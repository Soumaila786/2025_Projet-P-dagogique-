#ifndef SERVER_H
#define SERVER_H

#include <pthread.h>
#include <semaphore.h>

#define PORT 5000
#define MAX_CLIENTS 10
#define BUFFER_SIZE 1024

// Socket serveur global (pour SIGINT)
extern int server_fd;

// Sémaphore pour limiter le nombre de clients
extern sem_t sem_clients;

// Fonction principale du serveur
void start_server(int *server_fd);

#endif
