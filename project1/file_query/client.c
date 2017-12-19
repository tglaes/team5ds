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
        fprintf(stderr, "%s", "Client Error: receiving user data from stdin!\n");
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
        fprintf(stderr, "%s", "Client Error:wrong ip Adress format!\n");
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
        perror("Client Error: sock erstellen");
        free(data);
        free(answer_data);
        return -1;
    }

    // Versucht mit dem Server eine Verbindung einzugehen.
    if (connect(sock, (struct sockaddr*) &client, sizeof (client)) < 0) {
        perror("Client Error:verbinden");
        close(sock);
        free(data);
        free(answer_data);
        return -1;
    }



    // Sendet die Daten an den Server return1 falls fehler
    if (send_data(sock, data) < 0) {
        perror("Client Error: daten senden fehlgeschlagen");
        close(sock);
        free(data);
        free(answer_data);
        return -1;
    }
    printf("debug: gesendet:\n %s\n", data);


    if (recv_data(sock, answer_data) < 0) {
        perror("Client Error:fehler beim empfang der antwort");
        free(data);
        free(answer_data);
        return -1;
    }

    printf("debug:Antwort erhalten\n");
    //gebe antowrt aus
    printf("Antwort der Sucheanfrage:\n %s\n", answer_data);


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
 * ersteze fget durch getline()
 *  
 * 
 */
int get_data_from_user(char* data) {

    char* file_name;
    size_t len;
    int chars_read;
    char* number_of_bytes;
    size_t len2;
    int number;


    printf("Enter the first filename : ");
    if ((chars_read = getline(&file_name, &len, stdin)) > MAX_FILE_NAME_SIZE - 1) {
        strncat(data, file_name, MAX_FILE_NAME_SIZE - 2);
        strncat(data, "\n", 2);
    } else
        strncat(data, file_name, chars_read + 1); //+1 da chars read nur groesse ohne null byte speichert


    printf("Enter the second filename: ");

    if ((chars_read = getline(&file_name, &len, stdin)) > MAX_FILE_NAME_SIZE - 1) {
        strncat(data, file_name, MAX_FILE_NAME_SIZE - 2);
        strncat(data, "\n", 2);
    } else
        strncat(data, file_name, chars_read + 1); //+1 da chars read nur groesse ohne null byte speichert
    printf("Enter the third filename : ");

    if ((chars_read = getline(&file_name, &len, stdin)) > MAX_FILE_NAME_SIZE - 1) {
        strncat(data, file_name, MAX_FILE_NAME_SIZE - 2);
        strncat(data, "\n", 2);
    } else
        strncat(data, file_name, chars_read + 1); //+1 da chars read nur groesse ohne null byte speichert

    printf("Enter the forth filename : ");
    if ((chars_read = getline(&file_name, &len, stdin)) > MAX_FILE_NAME_SIZE - 1) {
        strncat(data, file_name, MAX_FILE_NAME_SIZE - 2);
        strncat(data, "\n", 2);
    } else
        strncat(data, file_name, chars_read + 1); //+1 da chars read nur groesse ohne null byte speichert

    printf("Enter the fifth filename  : ");
    if ((chars_read = getline(&file_name, &len, stdin)) > MAX_FILE_NAME_SIZE - 1) {
        strncat(data, file_name, MAX_FILE_NAME_SIZE - 2);
        strncat(data, "\n", 2);
    } else
        strncat(data, file_name, chars_read + 1); //+1 da chars read nur groesse ohne null byte speichert

    printf("Enter a number of bytes  : ");
     chars_read= getline(&number_of_bytes, &len2, stdin);
     number = atoi(number_of_bytes)  ;
     if( number > MAX_BYTES_TO_READ || number == 0 )
         strncat(data,MAX_BYTES_TO_READ_STRING,sizeof MAX_BYTES_TO_READ_STRING);
     else 
          strncat(data, number_of_bytes, chars_read + 1);;

   

    return 0;
}


