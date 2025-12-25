#include <stdio.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <pthread.h>
#include <signal.h>
#include <unistd.h>
#include "serveur.h"

int server_fd;

void handle_sigint(int sig) {
    printf("\n⚡ Arrêt du serveur...\n");

    close(server_fd);
    sem_destroy(&sem_clients);
    pthread_mutex_destroy(&clients_mutex);

    printf("Serveur arrêté proprement.\n");
    exit(0);
}

int main() {
    struct sockaddr_in server_addr, client_addr;
    socklen_t client_len = sizeof(client_addr);
    pthread_t tid;

    // Initialisation du sémaphore
    sem_init(&sem_clients, 0, MAX_CLIENTS);

    // Création de la socket
    server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd < 0) {
        perror("socket");
        exit(EXIT_FAILURE);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(PORT);

    bind(server_fd, (struct sockaddr *)&server_addr, sizeof(server_addr));
    listen(server_fd, MAX_CLIENTS);

    printf("Serveur démarré sur le port %d\n", PORT);

    // Gestion Ctrl+C
    signal(SIGINT, handle_sigint);

    while (1) {
        int client_fd = accept(server_fd, (struct sockaddr *)&client_addr, &client_len);
        if (client_fd < 0) continue;

        // Vérifier sémaphore
        if (sem_trywait(&sem_clients) != 0) {
            printf("Serveur plein ! Refus du client.\n");
            close(client_fd);
            continue;
        }

        // Créer un thread pour le client
        pthread_create(&tid, NULL, client_handler, &client_fd);
        pthread_detach(tid);
    }

    return 0;
}
