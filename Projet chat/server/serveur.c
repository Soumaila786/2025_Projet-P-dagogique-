#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include "serveur.h"
#include "client_manager.h"
#include <semaphore.h>

// Sémaphore pour limiter le nombre de clients
sem_t sem_clients;

void *client_handler(void *arg) {
    int client_fd = *(int *)arg;
    char buffer[BUFFER_SIZE];
    int bytes_read;

    ajouter_client(client_fd);

    printf("Client %d connecté (%d clients)\n", client_fd, nb_clients);

    while (1) {
        memset(buffer, 0, BUFFER_SIZE);
        bytes_read = recv(client_fd, buffer, BUFFER_SIZE, 0);

        if (bytes_read <= 0) {
            printf("Client %d déconnecté\n", client_fd);
            break;
        }

        printf("Client %d : %s", client_fd, buffer);

        // Diffuser à tous les autres clients
        diffuser_message(buffer, client_fd);
    }

    supprimer_client(client_fd);
    close(client_fd);
    sem_post(&sem_clients); // libérer la place
    pthread_exit(NULL);
}
