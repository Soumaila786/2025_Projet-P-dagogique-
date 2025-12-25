#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <signal.h>
#include <pthread.h>
#include <semaphore.h>
#include "server.h"
#include "client_manager.h"
#include "logger.h"

int server_fd;
sem_t sem_clients;

void start_server(int *server_fd_ptr) {
    struct sockaddr_in server_addr, client_addr;
    socklen_t client_len = sizeof(client_addr);
    sem_init(&sem_clients, 0, MAX_CLIENTS);

    *server_fd_ptr = socket(AF_INET, SOCK_STREAM, 0);
    if (*server_fd_ptr < 0) { perror("socket"); exit(1); }

    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(PORT);

    bind(*server_fd_ptr, (struct sockaddr*)&server_addr, sizeof(server_addr));
    listen(*server_fd_ptr, MAX_CLIENTS);

    printf("Serveur démarré sur le port %d\n", PORT);

    while (1) {
        int client_fd = accept(*server_fd_ptr, (struct sockaddr*)&client_addr, &client_len);
        if (client_fd < 0) continue;

        if (sem_trywait(&sem_clients) != 0) {
            printf("Serveur plein!\n");
            close(client_fd);
            continue;
        }

        int pipe_fd[2];
        if (pipe(pipe_fd) < 0) { perror("pipe"); exit(1); }

        pid_t pid = fork();
        if (pid == 0) { // processus enfant
            close(pipe_fd[1]); // enfant lit du pipe
            char buffer[BUFFER_SIZE];
            while (1) {
                memset(buffer, 0, BUFFER_SIZE);
                int n = read(pipe_fd[0], buffer, BUFFER_SIZE);
                if (n <= 0) break;
                send(client_fd, buffer, strlen(buffer), 0);
            }
            close(client_fd);
            exit(0);
        } else { // processus parent
            close(pipe_fd[0]); // parent écrit dans le pipe
            ajouter_client_pipe(pid, pipe_fd[1]);

            char buffer[BUFFER_SIZE];
            while (1) {
                memset(buffer, 0, BUFFER_SIZE);
                int n = recv(client_fd, buffer, BUFFER_SIZE, 0);
                if (n <= 0) break;
                diffuser_message_parent(buffer, pid);
                log_message(buffer);
            }

            supprimer_client_pipe(pid);
            close(client_fd);
            sem_post(&sem_clients);
        }
    }
}
