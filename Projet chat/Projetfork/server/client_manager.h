#ifndef CLIENT_MANAGER_H
#define CLIENT_MANAGER_H

void ajouter_client_pipe(int pid, int fd_pipe);
void supprimer_client_pipe(int pid);
void diffuser_message_parent(char *msg, int sender_pid);

#endif
