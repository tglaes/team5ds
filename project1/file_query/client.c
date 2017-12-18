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
#include "socket_helper.h"
#include "client.h"

int run_client(char* ip_adress, int port) {

    // Variabeln für den Socket.
    int sock;
    struct sockaddr_in client;
    struct in_addr ipaddr;
    socklen_t addr_size;
    // Variabel für das Datenpacket, dass an den Server geschickt wird.
    char* data = malloc(MAX_DATA_SIZE);
    // Variabel für das Datenpacket, dass vom  Server empfangen  wird.
    char* answer_data = malloc(MAX_DATA_SIZE);


    // Liest die 5 Dateinamen und die Anzahl der Bytes des Clienten ein.
    if (get_data_from_user(data) < 0) {
        fprintf(stderr, "%s", "Client Error while receiving user data from stdin!\n");
        free(data);
        free(answer_data);
        return -1;
    }


    /* 
     * Bereinigen des Speichers und setzten der Parameter
     * AF_INET -> IPv4
     * ip_adress -> Die IPv4 Adresse des Servers
     * port -> Der Port auf dem der Server horcht.
     * 
     */


    //formatiere ipadress in korrekte format  gebe fehler aus falls  format error
    if (inet_aton(ip_adress, &ipaddr) == 0) {
        fprintf(stderr, "%s", "Client Error wrong ip Adress format!\n");
        free(data);
        free(answer_data);
        return -1;
    }
    //fuelle speicher fur struct mit null auf
    memset(&client, 0, sizeof (client));
    client.sin_family = AF_INET;
    client.sin_addr = ipaddr;
    client.sin_port = htons(port);

    // Erstellen eines TCP Sockets.
    sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        perror("Client Error");
        free(data);
        free(answer_data);
        return -1;
    }

    // Versucht mit dem Server eine Verbindung einzugehen.
    if (connect(sock, (struct sockaddr*) &client, sizeof (client)) < 0) {
        perror("Client Error");
        close(sock);
        free(data);
        free(answer_data);
        return -1;
    }

    printf("log gesendet:\n %s\n", data);

    // Sendet die Daten an den Server return1 falls fehler
    if (send_data(sock, data) < 0) {
        perror("Client Error: daten senden fehlgeschlagen");
        close(sock);
        free(data);
        free(answer_data);
        return -1;
    }

    if (recv_data(sock, answer_data) < 0) {
        perror("Server Error:fehler beim empfang der antwort");
        free(data);
        free(answer_data);
        return -1;
    }

    printf("log Antwort erhalten  %s\n", answer_data);


    close(sock);
    free(data);
    free(answer_data);
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
 * wird beim nachsten fgets aufruft die ubrigen zeichen eingelsen statt ein neue 
 * eingabe
 *  
 * 
 */
int get_data_from_user(char* data) {

    char file_name[MAX_FILE_NAME_SIZE + 2]; // +2 wegen \n und \0
    char number_of_bytes[sizeof MAX_BYTES_TO_READ_STRING + 2];

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
    printf("Enter the fifth filename  : ");
    fgets(file_name, MAX_FILE_NAME_SIZE, stdin);
    strncat(data, file_name, MAX_FILE_NAME_SIZE);
    printf("Enter a number of bytes  : ");//TODO kontrolle für die zhal damit es nicht über 10 geht
    fgets(number_of_bytes, MAX_FILE_NAME_SIZE, stdin);
    strncat(data, number_of_bytes, (MAX_BYTES_TO_READ / sizeof (char)) + 2);

    return 0;
}


