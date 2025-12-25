#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>
#include "server.h"

// Fermeture propre
void handle_sigint(int sig) {
    printf("\n⚡ Arrêt du serveur...\n");
    close(server_fd);
    sem_destroy(&sem_clients);
    printf("Serveur arrêté.\n");
    exit(0);
}

int main() {
    signal(SIGINT, handle_sigint);

    printf("Démarrage du serveur...\n");
    start_server(&server_fd);

    return 0;
}
