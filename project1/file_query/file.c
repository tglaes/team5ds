#include <stdlib.h>
#include <stdio.h>
#include <string.h>

/*
 * Kopiert die ersten number_of_bytes Bytes aus den Dateien aus file_names in
 * bytes_from_file. Falls die Datei nicht gefunden wird, wird "Datei nicht gefunden!"
 * gespeichert.
 * 
 * NOCH NICHT GETESTET!!
 * 
 */
int get_bytes_from_file(char** bytes_from_file, char file_names[][64], int number_of_bytes) {

    FILE *file_pointer;
    for (int i = 0; i < 5; i++) {
        file_pointer = fopen(file_names[i], "r");
        if (file_pointer == NULL) {
            strncpy(*bytes_from_file + i * number_of_bytes, "Datei nicht gefunden!\0", number_of_bytes);
        } else {
            for (int k = 0; k < number_of_bytes; k++) { 
                *(*bytes_from_file + (i * number_of_bytes) + k) = fgetc(file_pointer);
            }
            *(*bytes_from_file + (i * number_of_bytes) + number_of_bytes) = '\0';
        }
    }
    return 0;
}