#include <stdlib.h>
#include <stdio.h>
#include <string.h> 
#include <unistd.h> 
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include "data.h"
#include "client.h"


int run_client(char* ip_adress, int port) {

    // Variabeln für den Socket.
    int sock;
    struct sockaddr_in client;
    struct in_addr ipaddr;
    socklen_t addr_size;
    // Variabel für das Datenpacket, dass an den Server geschickt wird.
    char* data = malloc(MAX_DATA_SIZE);

    /* 
     * Bereinigen des Speichers und setzten der Parameter
     * AF_INET -> IPv4
     * ip_adress -> Die IPv4 Adresse des Servers
     * port -> Der Port auf dem der Server horcht.
     * 
     */


    //load and format  ipadress  exit if wrng format
    if (inet_aton(ip_adress, &ipaddr) == 0) {
        perror("Client Error wrong ip Adress format");
        return -1;
    }
    //set memory of  sockaddr_in struct to null
    memset(&client, 0, sizeof (client));
    client.sin_family = AF_INET;
    client.sin_addr = ipaddr;
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
        close(sock);
        return -1;
    }

    // Liest die 5 Dateinamen und die Anzahl der Bytes des Clienten ein.
    if (get_data_from_user(data) < 0) {
        printf("Client Error while receiving user data from stdin!\n");
        return -1;
    }

    // Sendet die Daten an den Server.
    send(sock, data, MAX_DATA_SIZE, MSG_NOSIGNAL);
    
    send_data(sock,data);


    close(sock);
    return 0;
}

/*
 * Liest 5 Dateinamen und eine Zahl ein, die getrennt mit Newline in
 * data gespeichert werden.
 * 
 * Data sieht nach der Eingabe des Users etwa so aus:
 * test.txt\ntest2.txt\ntest3.txt\ntest4.txt\nfile.lib\n1000\n ...
 * 
 *  muss verbesser werden falls man filenamen groesser MAX_FILE_NAME_SIZE eingibt 
 * wird beim nachsten fgets aufruft die ubrigen zeichen eingelsen statt ein neues
 *  
 * 
 */
int get_data_from_user(char* data) {

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

int send_data(int sock, char* data) {




    return 0;
}