#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h> 
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include "file.h"
#include "data.h"
#include "server.h"


int get_data_from_client_package(char* data, char file_names[][64], char** number_of_bytes);

/*
 * Startet einen Server, der mehrere Anfragen von Clienten gleichzeitig
 * beantworten kann. Parameter ist der Port auf dem der Server nach einkommenden
 * Verbindungen horchen soll. Der Server benutzt IPv4 und TCP.
 */
int run_server(int port) {


    // Variabeln für den Socket.
    int sock, new_sock, pid;
    struct sockaddr_in server, client;
    socklen_t sock_len;

    // Variabeln für den Datenaustausch und Datenverarbeitung.
    char* data = malloc(MAX_DATA_SIZE);
    char file_names[MAX_FILE_TO_ACCEPT][MAX_FILE_NAME_SIZE];
    char* number_of_bytes_string;
    int number_of_bytes = 0;

    /*
     *  Bereinigen des Speichers des Servers und setzten der Parameter:
     * AF_INET -> IPv4
     * INADDR_ANY -> Alle IP-Adressen werden akzeptiert
     * port -> Der zu benutzende Port
     *  
     */
    memset(&server, 0, sizeof (server));
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = htonl(INADDR_ANY);
    server.sin_port = htons(port);

    // Erstellen des TCP Sockets.
    sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        perror("Server Error");
        free(data);
        return -1;
    }

    // Binden des Sockets.
    if (bind(sock, (struct sockaddr*) &server, sizeof (server)) < 0) {
        perror("Server Error");
        close(sock);
        free(data);
        return -1;
    }

    // Sag dem Server, dass er auf einkommende Verbindungen horchen soll.
    if (listen(sock, 5) < 0) {
        perror("Server Error");
        close(sock);
        free(data);
        return -1;
    }


    //schleife die 
    sock_len = sizeof (client);
    while (1) {
        new_sock = accept(sock, (struct sockaddr*) &client, &sock_len);
        if (new_sock < 0) {
            //TODO check if error came because client abborted connection then 
            // continue otherwise call perror and return -1
            fprintf(stderr, "%s", "accepting error continue\n");
            continue;
        }
         
       

    }




    //    // Empfängt ein Datenpacket des Clienten.
    //    if (recv(new_sock, data, MAX_DATA_SIZE, MSG_WAITALL) < 0) {
    //        perror("Server Error");
    //        return -1;
    //    }

    //    printf(data);

    //    // Sortiert die Daten des Packets in die Dateinamen und die Anzahl der Bytes.
    //    get_data_from_client_package(data, file_names, &number_of_bytes_string);
    //    
    //    /*
    //     * Wenn die Anzahl der Bytes kleiner ist als die Anzahl der Bytes für
    //     * "Datei nicht gefunden!", dann legen wir die Anzahl der Bytes auf 22.
    //     */
    //    number_of_bytes = atoi(number_of_bytes_string);
    //    if(number_of_bytes < 21){
    //        number_of_bytes = 21;
    //    }
    //    
    //    // Allokierte Speicher für die Bytes plus jeweils ein Nullbyte.
    //    char* bytes_from_file = malloc(5 * (number_of_bytes + 1));
    //
    //    // Lese die Bytes aus den Dateinen und speichere sie in bytes_from_file.
    //    get_bytes_from_file(&bytes_from_file , file_names, number_of_bytes);
    //    
    //    
    //   // TODO: Schicke Client eine Antwort und Speicher freigeben.
    //   // TODO: Einen neuen Prozess für jede einkommende Verbindung erstellen. 
    //    for(int i= 0; i < sizeof(bytes_from_file); i++){
    //        printf("%c", bytes_from_file[i]);
    //    }
    //    
    //    
    //    //closesocket(sock);
    return 0;
}

/*
 * Parameter sind das Datenpaket des Clienten (data), ein Stringarray für die
 * Dateinamen (file_names) und ein String für die Anzahl der Bytes (number_of_bytes).
 * 
 * Die Funktion splittet das Paket immer da wo der Newline-Character auftritt (\n)
 * und schreibt die Dateinamen in file_names. strtok ersetzt außerdem Newline mit
 * dem Nullbyte-Character.
 * 
 * data sieht etwa so aus:
 * test.txt\ntest2.txt\ntest3.txt\ntest4.txt\ntest5.txt\n1000\n ...
 * 
 */
int get_data_from_client_package(char* data, char file_names[][64], char** number_of_bytes) {

    printf(data);
    char delimiter[] = "\n";
    // Splitte den data String und kopiere das Ergebnis in file_names.
    strncpy(file_names[0], strtok(data, delimiter), 64);
    strncpy(file_names[1], strtok(NULL, delimiter), 64);
    strncpy(file_names[2], strtok(NULL, delimiter), 64);
    strncpy(file_names[3], strtok(NULL, delimiter), 64);
    strncpy(file_names[4], strtok(NULL, delimiter), 64);
    // Der letzte Abschnitt ist die Anzahl der Bytes
    *number_of_bytes = strtok(NULL, delimiter);
    return 0;
}