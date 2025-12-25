#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>
#include <signal.h>

#define BUFFER_SIZE 1024
int sockfd;

void *recv_messages(void *arg) {
    char buffer[BUFFER_SIZE];
    while (1) {
        memset(buffer,0,BUFFER_SIZE);
        int n = recv(sockfd, buffer, BUFFER_SIZE,0);
        if (n<=0){ printf("❌ Déconnecté.\n"); exit(0);}
        printf("%s", buffer);
    }
}

void handle_sigint(int sig) {
    close(sockfd);
    exit(0);
}

int main(int argc,char *argv[]){
    if(argc!=3){printf("Usage: %s IP PORT\n",argv[0]); return 1;}
    char *ip=argv[1]; int port=atoi(argv[2]);
    struct sockaddr_in server_addr;
    pthread_t tid;
    signal(SIGINT,handle_sigint);

    sockfd=socket(AF_INET,SOCK_STREAM,0);
    server_addr.sin_family=AF_INET;
    server_addr.sin_port=htons(port);
    inet_pton(AF_INET,ip,&server_addr.sin_addr);

    if(connect(sockfd,(struct sockaddr*)&server_addr,sizeof(server_addr))<0){ perror("connect"); exit(1);}
    pthread_create(&tid,NULL,recv_messages,NULL); pthread_detach(tid);

    char buffer[BUFFER_SIZE];
    while(1){
        fgets(buffer,BUFFER_SIZE,stdin);
        if(strlen(buffer)>1) send(sockfd,buffer,strlen(buffer),0);
    }
    close(sockfd);
    return 0;
}
