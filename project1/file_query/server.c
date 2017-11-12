/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

#include <winsock2.h>
#include <stdlib.h>
#include <stdio.h>
#include <winsock.h>
#include <string.h>
#include "file.h"
#include "data.h"

int get_data_from_client_package(char FAR* data, char file_names[][64], char** number_of_bytes);

int run_server(int port) {

    SOCKET sock, sock2;
    struct sockaddr_in server;
    unsigned sock_len;
    char FAR* data = malloc(MAX_DATA_SIZE);
    char file_names[5][64];
    char* number_of_bytes_string;
    int number_of_bytes = 0;

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
    
    get_data_from_client_package(data, file_names, &number_of_bytes_string);
    
    number_of_bytes = atoi(number_of_bytes_string);
    if(number_of_bytes < 21){
        number_of_bytes = 21;
    }
    
    char* bytes_from_file = malloc(5 * (number_of_bytes + 1));
    
    get_bytes_from_file(&bytes_from_file , file_names, number_of_bytes);
    
    for(int i=0; i < 5 * (number_of_bytes + 1); i++){
        printf(bytes_from_file + i);
    }
    
    //closesocket(sock);
    return 0;
}

// Hier wir das Datenpaket vom Clienten in die 5 Dateinamen und die Anzahl der Bytes eingteilt.

int get_data_from_client_package(char FAR* data, char file_names[][64], char** number_of_bytes) {
    
    //printf(data);
    char delimiter[] = "\n";
    
    strncpy(file_names[0], strtok(data, delimiter), 64);
    strncpy(file_names[1], strtok(NULL, delimiter), 64);
    strncpy(file_names[2], strtok(NULL, delimiter), 64);
    strncpy(file_names[3], strtok(NULL, delimiter), 64);
    strncpy(file_names[4], strtok(NULL, delimiter), 64);
    *number_of_bytes = strtok(NULL, delimiter);
    return 0;
}