#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include "serveur.h"
#include "client_manager.h"
#include "logger.h"

// Variables globales
int clients[MAX_CLIENTS];
int nb_clients = 0;
pthread_mutex_t clients_mutex = PTHREAD_MUTEX_INITIALIZER;

void ajouter_client(int client_fd) {
    pthread_mutex_lock(&clients_mutex);

    if (nb_clients < MAX_CLIENTS) {
        clients[nb_clients++] = client_fd;
    }

    pthread_mutex_unlock(&clients_mutex);
}

void supprimer_client(int client_fd) {
    pthread_mutex_lock(&clients_mutex);

    for (int i = 0; i < nb_clients; i++) {
        if (clients[i] == client_fd) {
            clients[i] = clients[nb_clients - 1];
            nb_clients--;
            break;
        }
    }

    pthread_mutex_unlock(&clients_mutex);
}

void diffuser_message(char *message, int sender_fd) {
    pthread_mutex_lock(&clients_mutex);

    for (int i = 0; i < nb_clients; i++) {
        if (clients[i] != sender_fd) {
            send(clients[i], message, strlen(message), 0);
        }
    }

    pthread_mutex_unlock(&clients_mutex);

    // Écrire aussi dans le log
    log_message(message);
}
