#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include "socket_helper.h"

/*
 * sende den  uebergebenen string(muss null terminiert sein ) per sock zum server
 * zuerst wird die groesse vom string bestimmt diese gesendet danach erst wird
 * der  string gesendet 
 * return =  -1 bei fehler sonst 0 
 * 
 */
int send_data(int sock, char* data) {
    // groesse des strings +1 furs null byte
    int length = strlen(data) + 1;
    //andere byte order von der groesse auf network byte order und lege short als einheit fest
    uint16_t data_size = htons(length);
    //wandle groesse in char array um
    char size[sizeof data_size];
    memcpy(size, &data_size, sizeof size);

    //sende zuerst die groesse mit send_all function
    if (send_all(sock, size, sizeof size) < 0)
        return -1;

    //sende dann die nachricht
    if (send_all(sock, data, length) < 0)
        return -1;

    return 0;
}

/*
 *sende das uebergebene char array der angeben lange ubers socket
 * abtrahiert die socket send function und garantiert dass das packet 
 * in kompletter groesse ubertragen wurde
 *  
 */
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

/*
 * empfange daten und spicher sie im bergebenen char array
 * es letzs zuerst eine gesendet datengroesse ein(2 bytes fur ein short )
 * danach die daten mit der ausgelsen groesse
 * return =  -1 bei fehler sonst empfange groesse
 * 
 */
int recv_data(int client_sock, char* data) {
    int length;
    uint16_t data_size;
    char size[sizeof data_size];

    //empfange  zuerst die groesse mit recv_all function gib -1 zuruck falls fehler auftreten
    if (recv_all(client_sock, size, sizeof size) < 0)
        return -1;
    //wandle  empfnge groess vn char array in short
    memcpy(&data_size, size, sizeof data_size);
    //wandle von networkbyte order zum prozessor verwendten byteorder
    length = ntohs(data_size);

    //empfnge dann die nachricht mit der vorher eingelesen groesse 
    if (recv_all(client_sock, data, length) < 0)
        return -1;

    return length;
}

/*
 * 
 * empfange daten bis zur angeben lange und schreibe sie ins char array
 * abstrahiert die socket recv fnction garantiert dass das packet 
 * in kompletter groesse empfangen wird 
 * 
 */
int recv_all(int client_sock, char* data, int length) {
    int total_recv = 0;
    int actual_recv;

    while (total_recv < length) {
        actual_recv = recv(client_sock, data + total_recv, length - total_recv, MSG_NOSIGNAL);
        if (actual_recv < 0)
            return actual_recv;

        total_recv += actual_recv;
    }

    return total_recv;
}


