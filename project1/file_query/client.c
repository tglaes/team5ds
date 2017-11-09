/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

#include <stdlib.h>
#include <stdio.h>
#include <winsock2.h>
#include <winsock.h>
#include <ws2tcpip.h>

int run_client(char* ip_adress, int port) {

    SOCKET sock;
    struct sockaddr_in client;
    char FAR* data = "Test";

    memset(&client, 0, sizeof (client));
    client.sin_family = AF_INET;
    client.sin_addr.s_addr = inet_addr (ip_adress);
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

    send(sock, data, sizeof(data), 0);

    return 0;
}