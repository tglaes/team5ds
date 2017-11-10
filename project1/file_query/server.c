/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
#include <winsock2.h>
#include <stdlib.h>
#include <stdio.h>
#include <winsock.h>
#include "file.h"
#include "data.h"

int run_server(int port) {

    SOCKET sock, sock2;
    struct sockaddr_in server;
    unsigned sock_len;
    char FAR* data = malloc(MAX_DATA_SIZE);

    memset(&server, 0, sizeof (server));
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = htonl(INADDR_ANY);
    server.sin_port = htons(port);

    sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        perror("Server Error");
        return -1;
    }
    if (bind(sock, (struct sockaddr*) &server, sizeof (server)) < 0) {
        perror("Server Error");
        return -1;
    }
    if (listen(sock, 5) < 0) {
        perror("Server Error");
        return -1;
    }
    sock_len = sizeof (server);
    sock2 = accept(sock, (struct sockaddr*) &server, &sock_len);
    if (sock2 < 0) {
        perror("Server Error");
        return -1;
    }

    char* tmp = malloc(256);
    unsigned n = 0;
    
    while (n < MAX_DATA_SIZE) {
        if ((n = n + recv(sock2, tmp, MAX_DATA_SIZE, MSG_WAITALL)) < 0) {
            perror("Server Error");
            return -1;
        }
        strncat(data,tmp, 256);
        printf("\n%d",n);
    }
    
    printf(data);
    return 0;
}

