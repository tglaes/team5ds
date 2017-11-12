/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

/*
 * Schreibt die ersten n Bytes (Zeichen) aus file in content.
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