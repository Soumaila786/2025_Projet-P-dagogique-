#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>
#include <signal.h>

#define BUFFER_SIZE 1024

int sockfd;

// Fonction pour recevoir les messages du serveur
void *recv_messages(void *arg) {
    char buffer[BUFFER_SIZE];
    int bytes_read;

    while (1) {
        memset(buffer, 0, sizeof(buffer));
        bytes_read = recv(sockfd, buffer, sizeof(buffer), 0);
        if (bytes_read <= 0) {
            printf("Déconnecté du serveur.\n");
            exit(0);
        }
        printf("%s", buffer);
    }

    pthread_exit(NULL);
}

// Fonction pour gérer Ctrl+C
void handle_sigint(int sig) {
    printf("\nDéconnexion...\n");
    close(sockfd);
    exit(0);
}

int main(int argc, char *argv[]) {
    if (argc != 3) {
        printf("Usage: %s <IP du serveur> <Port>\n", argv[0]);
        return 1;
    }

    char *server_ip = argv[1];
    int server_port = atoi(argv[2]);
    struct sockaddr_in server_addr;
    pthread_t tid;

    // Gestion Ctrl+C
    signal(SIGINT, handle_sigint);

    // Création de la socket
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) {
        perror("socket");
        exit(EXIT_FAILURE);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(server_port);
    if (inet_pton(AF_INET, server_ip, &server_addr.sin_addr) <= 0) {
        perror("inet_pton");
        exit(EXIT_FAILURE);
    }

    // Connexion au serveur
    if (connect(sockfd, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("connect");
        exit(EXIT_FAILURE);
    }

    printf("Connecté au serveur %s:%d\n", server_ip, server_port);

    // Création d'un thread pour recevoir les messages
    pthread_create(&tid, NULL, recv_messages, NULL);
    pthread_detach(tid);

    // Boucle pour envoyer des messages
    char buffer[BUFFER_SIZE];
    while (1) {
        memset(buffer, 0, sizeof(buffer));
        fgets(buffer, sizeof(buffer), stdin);

        if (strlen(buffer) > 1) { // éviter envoi de lignes vides
            send(sockfd, buffer, strlen(buffer), 0);
        }
    }

    close(sockfd);
    return 0;
}
