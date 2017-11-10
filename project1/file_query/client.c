/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

#include <stdlib.h>
#include <stdio.h>
#include <winsock.h>
#include "data.h"

int get_data_from_user(char FAR* data);

int run_client(char* ip_adress, int port) {

    SOCKET sock;
    struct sockaddr_in client;
    char FAR* data = malloc(MAX_DATA_SIZE);

    memset(&client, 0, sizeof (client));
    client.sin_family = AF_INET;
    client.sin_addr.s_addr = inet_addr(ip_adress);
    client.sin_port = htons(port);

    sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        perror("Client Error");
        return -1;
    }

    if (connect(sock, (struct sockaddr*) &client, sizeof (client)) < 0) {
        perror("Client Error");
        return -1;
    }

    if (get_data_from_user(data) < 0) {
        printf("Client Error while receiving user data!\n");
        return -1;
    }
    
    printf(data);
    send(sock, data, sizeof (data), 0);
    return 0;
}

int get_data_from_user(char FAR* data) {
    
    char* file_name = malloc(100);
    char* number_of_bytes = malloc(4);
    
    printf("Enter the first filename : ");
    fgets(file_name, MAX_DATA_SIZE, stdin);
    strncat(data, file_name, 100);
    printf("Enter the second filename: ");
    fgets(file_name, MAX_DATA_SIZE, stdin);
    strncat(data, file_name, 100);
    printf("Enter the third filename : ");
    fgets(file_name, MAX_DATA_SIZE, stdin);
    strncat(data, file_name, 100);
    printf("Enter the forth filename : ");
    fgets(file_name, MAX_DATA_SIZE, stdin);
    strncat(data, file_name, 100);
    printf("Enter the fith filename  : ");
    fgets(file_name, MAX_DATA_SIZE, stdin);
    strncat(data, file_name, 100);
    printf("Enter a number of bytes  : ");
    fgets(number_of_bytes, MAX_DATA_SIZE, stdin);
    strncat(data, number_of_bytes, 100);
    return 0;
}