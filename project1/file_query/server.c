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

int get_data_from_client_package(char FAR* data, char* file_names[64], int* number_of_bytes);

int run_server(int port) {

    SOCKET sock, sock2;
    struct sockaddr_in server;
    unsigned sock_len;
    char FAR* data = malloc(MAX_DATA_SIZE);
    char* file_names[64];
    int number_of_bytes;

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

    if (recv(sock2, data, MAX_DATA_SIZE, MSG_WAITALL) < 0) {
        perror("Server Error");
        return -1;
    }
    
    get_data_from_client_package(data, file_names, &number_of_bytes);
    /*
    for(int i = 0; i < 5; i++){
        printf("%s\n", file_names[i]);
    }
    printf("%d\n", number_of_bytes);
     */
    
    //closesocket(sock);
    return 0;
}

// Hier wir das Datenpaket vom Clienten in die 5 Dateinamen und die Anzah der Bytes eingteilt.

int get_data_from_client_package(char FAR* data, char* file_names[64], int* number_of_bytes) {
    printf("got here");
    int i = 0;
    int k = 0;
    for (i; i < 5 * MAX_FILE_NAME_SIZE; i++) {
        if(data[i] == '\n'){
            file_names[k][i] = '\0';
            k++;
        }
        if(k == 5){
            break;
        }
        file_names[k][i] = data[i];
    }
    char number_bytes[4];
    
    for(int l = i + 1; l < i + 5; l++){
        number_bytes[l] = data[i];
    }
    
    *number_of_bytes = atoi(number_bytes);

    return 0;
}