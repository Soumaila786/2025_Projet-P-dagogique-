#ifndef CLIENT_MANAGER_H
#define CLIENT_MANAGER_H

void ajouter_client(int client_fd);
void supprimer_client(int client_fd);
void diffuser_message(char *message, int sender_fd);

#endif
