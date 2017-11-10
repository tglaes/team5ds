/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   main.c
 * Author: Tristan
 *
 * Created on 6. November 2017, 23:35
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "server.h"
#include "client.h"
#include "file.h"

/*
 * Die Main Funktion überprüft und wertet, die über die Kommandozeile eingegebenen Parameter, aus.
 * Erwartet werden: "-s [Port]" oder "-c [IP-Adresse] [Port]"
 * Im Fall "-s" wird ein Server gestartet, der auf dem angegebenen Port lauscht.
 * Im Fall "-c" wird ein Client gestartet, der versucht sich mit dem Server unter der angegebenen IP-Adresse und Port zu verbinden.
 */
int main(int argc, char** argv) {
        
    if (argc == 3) {
        if (strcmp(argv[1], "-s") == 0) {
            int port = atoi(argv[2]);
            if (port != 0) {
                return run_server(port);
            } else {
                printf("Der zweite Parameter ist kein gültiger Port");
                return -1;
            }
        } else {
            printf("Die Anzahl der Argumente für einen Server ist falsch!");
            return -1;
        }
    } else if (argc == 4) {
        if (strcmp(argv[1], "-c") == 0) {
            
            char* ip_adress = argv[2];
            int port = atoi(argv[3]);
            if (port != 0) {
                return run_client(ip_adress, port);
            } else {
                printf("Der dritte Parameter ist kein gültiger Port!");
                return -1;
            }
        } else {
            printf("Die Anzahl der Argumente für einen Clienten ist falsch!");
            return -1;
        }
    } else {
        printf("Die Anzahl der Argumente ist nicht korrekt!");
        return -1;
    }
    return 0;
}