#include <stdlib.h>
#include <stdio.h>
#include <winsock2.h>
#include <winsock.h>
#include "data.h"

int get_data_from_user(char FAR* data);

int run_client(char* ip_adress, int port) {
    
    // Variabeln für den Socket.
    SOCKET sock;
    struct sockaddr_in client;
    
    // Variabel für das Datenpacket, dass an den Server geschickt wird.
    char FAR* data = malloc(MAX_DATA_SIZE);
    
    /* 
     * Bereinigen des Speichers und setzten der Parameter
     * AF_INET -> IPv4
     * ip_adress -> Die IPv4 Adresse des Servers
     * port -> Der Port auf dem der Server horcht.
     * 
     */
    memset(&client, 0, sizeof (client));
    client.sin_family = AF_INET;
    client.sin_addr.s_addr = inet_addr(ip_adress);
    client.sin_port = htons(port);
    
    // Erstellen eines TCP Sockets.
    sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        perror("Client Error");
        return -1;
    }
    
    // Versucht mit dem Server eine Verbindung einzugehen.
    if (connect(sock, (struct sockaddr*) &client, sizeof (client)) < 0) {
        perror("Client Error");
        return -1;
    }
    
    // Liest die 5 Dateinamen und die Anzahl der Bytes des Clienten ein.
    if (get_data_from_user(data) < 0) {
        printf("Client Error while receiving user data!\n");
        return -1;
    }
    
    // Sendet die Daten an den Server.
    send(sock, data, MAX_DATA_SIZE, 0);
    return 0;
}
/*
 * Liest 5 Dateinamen und eine Zahl ein, die getrennt mit Newline in
 * data gespeichert werden.
 * 
 * Data sieht nach der Eingabe des Users etwa so aus:
 * test.txt\ntest2.txt\ntest3.txt\ntest4.txt\nfile.lib\n1000\n ...
 * 
 */
int get_data_from_user(char FAR* data) {
    
    char* file_name = malloc(MAX_FILE_NAME_SIZE);
    char* number_of_bytes = malloc(5);
    
    printf("Enter the first filename : ");
    fgets(file_name, MAX_FILE_NAME_SIZE, stdin);
    strncat(data, file_name, MAX_FILE_NAME_SIZE);
    printf("Enter the second filename: ");
    fgets(file_name, MAX_FILE_NAME_SIZE, stdin);
    strncat(data, file_name, MAX_FILE_NAME_SIZE);
    printf("Enter the third filename : ");
    fgets(file_name, MAX_FILE_NAME_SIZE, stdin);
    strncat(data, file_name, MAX_FILE_NAME_SIZE);
    printf("Enter the forth filename : ");
    fgets(file_name, MAX_FILE_NAME_SIZE, stdin);
    strncat(data, file_name, MAX_FILE_NAME_SIZE);
    printf("Enter the fith filename  : ");
    fgets(file_name, MAX_FILE_NAME_SIZE, stdin);
    strncat(data, file_name, MAX_FILE_NAME_SIZE);
    printf("Enter a number of bytes  : ");
    fgets(number_of_bytes, MAX_FILE_NAME_SIZE, stdin);
    strncat(data, number_of_bytes, 5);
    return 0;
}