#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h> 
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include "file.h"
#include "data.h"
#include "socket_helper.h"
#include "server.h"



int get_data_from_client_package(char* data, char file_names[][64], char** number_of_bytes);
int client_connectin_handler(int client_sock);


/*
 * Startet einen Server, der mehrere Anfragen von Clienten gleichzeitig
 * beantworten kann. Parameter ist der Port auf dem der Server nach einkommenden
 * Verbindungen horchen soll. Der Server benutzt IPv4 und TCP.
 */
int run_server(int port) {

    // Variabeln für den Socket.
    int sock, client_sock, pid;
    struct sockaddr_in server, client;
    socklen_t sock_len;


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
        return -1;
    }

    // Binden des Sockets.
    if (bind(sock, (struct sockaddr*) &server, sizeof (server)) < 0) {
        perror("Server Error");
        close(sock);
        return -1;
    }

    // Sag dem Server, dass er auf einkommende Verbindungen horchen soll.
    if (listen(sock, 5) < 0) {
        perror("Server Error");
        close(sock);
        return -1;
    }


    //schleife die auf ankomende verbindung wartet und fur jeden einen neuen prozess erstellt
    sock_len = sizeof (client);
    while (1) {
        client_sock = accept(sock, (struct sockaddr*) &client, &sock_len);
        if (client_sock < 0) { //fehler
            fprintf(stderr, "%s", "error in accept but continue\n");
            //rufe waitpid nicht blockierend auf umbenendete childprozesse aufzuraumen
            // und somit zombi prozesse zu verhindern
            while (waitpid(-1, NULL, WNOHANG) > 0);
            continue;
        }

        printf("New Connection from:: %s\n", inet_ntoa(client.sin_addr)); //log ip adresse

        pid = fork();
        if (!pid) {//kindprozess     
            close(sock); // passiven sock benenden da nicht gebraucht im kind

            if (client_connectin_handler(client_sock) < 0) {//fuehre client handler aus 
                perror("Server Error");
                close(client_sock);
                exit(1);
            } else {
                close(client_sock);
                exit(0);
            }
        } else { //Vaterprozess 
            close(client_sock); //aktiven client sock beenden da im eltern prozeccs nicht gebraucht
            while (waitpid(-1, NULL, WNOHANG) > 0);
        }
    }

    return 0;
}
/*
 *  function die sich um eine verbindung kuemmert wird von jeden 
 * erstellten verbindprozess aufgerufen  
 * 
 * gibt -1 zuruck bei fehler sonst 0
 */
int client_connectin_handler(int client_sock) {
    char* data = calloc(MAX_DATA_SIZE, sizeof (char));
    char* data_answer = calloc(MAX_DATA_SIZE, sizeof (char));
    char file_names[MAX_FILES_TO_ACCEPT][MAX_FILE_NAME_SIZE];
    char* number_of_bytes_string = malloc(2);
    int number_of_bytes = 0;


    if (recv_data(client_sock, data) < 0) {
        perror("Server Error:fehler beim empfang der anfrage");
        free(data);
        free(data_answer);
        return -1;
    }




            // Sortiert die Daten des Packets in die Dateinamen und die Anzahl der Bytes.
            get_data_from_client_package(data, file_names, &number_of_bytes_string);
            
            printf("get data finsihed");
            char* bytes_from_file;
            // Lese die Bytes aus den Dateinen und speichere sie in bytes_from_file.
            get_bytes_from_file(&bytes_from_file , file_names, number_of_bytes);
            
            

           // TODO: Schicke Client eine Antwort und Speicher freigeben. 
            /*for(int i= 0; i < 50; i++){    
                printf("%c", bytes_from_file[i]);
            } */    


   // zum testen ende zuerst einfach die empfangen daten zuruck bis get_bytes_from_file funktioniert
    if (send_data(client_sock, data) < 0) {
        perror("Client Error: daten senden fehlgeschlagen");
        close(client_sock);
        free(data);
        free(data_answer);
        return -1;
    }
    
    free(data);
    free(data_answer);
    //free(bytes_from_file);
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