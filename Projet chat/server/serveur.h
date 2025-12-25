#ifndef SERVER_H
#define SERVER_H

#include <pthread.h>
#include <semaphore.h>

#define PORT 5000
#define MAX_CLIENTS 10
#define BUFFER_SIZE 1024

extern int clients[MAX_CLIENTS];
extern int nb_clients;
extern pthread_mutex_t clients_mutex;
extern sem_t sem_clients;

// Déclaration de la fonction du thread client
void *client_handler(void *arg);

#endif
