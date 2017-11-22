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


    // Liest die 5 Dateinamen und die Anzahl der Bytes des Clienten ein.
    if (get_data_from_user(data) < 0) {
        fprintf(stderr, "%s", "Client Error while receiving user data from stdin!\n");
        free(data);
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
        free(data);
        return -1;
    }

    // Versucht mit dem Server eine Verbindung einzugehen.
    if (connect(sock, (struct sockaddr*) &client, sizeof (client)) < 0) {
        perror("Client Error");
        close(sock);
        free(data);
        return -1;
    }



    // Sendet die Daten an den Server.
    send_data(sock, data);


    close(sock);
    free(data);
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

    char* file_name = malloc(MAX_FILE_NAME_SIZE);
    char* number_of_bytes = malloc(MAX_BYTES_TO_READ / sizeof (char));

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
    strncat(data, number_of_bytes, MAX_BYTES_TO_READ / sizeof (char));

    free(file_name);
    free(number_of_bytes);

    return 0;
}
// send data which must be a null terminatet string over  socket

int send_data(int sock, char* data) {
    // length of data +  null terminated byte which is a null terminated string
    int length = strlen(data) + 1;
    //change data size to networkbyte order
    uint16_t data_size = htons(length);
    //send first size of data message which is firs converted to char array
    char size[sizeof data_size];
    memcpy(size, &data_size, sizeof size);

    if (send_all(sock, size, sizeof size) < 0)
        return -1;

    //send the data message
    if (send_all(sock, data, length) < 0)
        return -1;

    return 0;
}

int send_all(int sock, char *data, int length) {
    int total_send = 0;
    int actual_send;

    while (total_send < length) {
        actual_send = send(sock, data + total_send, length - total_send, MSG_NOSIGNAL);
        if (actual_send < 0)
            return actual_send;

        total_send += actual_send;
    }

    return total_send;
}
