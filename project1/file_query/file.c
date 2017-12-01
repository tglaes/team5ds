#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "data.h"


int strcat_p(char* dest, const char* string, unsigned position);

/*
 * Kopiert die ersten number_of_bytes Bytes aus den Dateien aus file_names in
 * bytes_from_file. Falls die Datei nicht gefunden wird, wird "Datei nicht gefunden!"
 * gespeichert.
 * 
 * NOCH NICHT GETESTET!!
 * 
 */
int get_bytes_from_file(char* bytes_from_file, char file_names[][MAX_FILE_NAME_SIZE], int number_of_bytes) {
    
    FILE *file_pointer;
    unsigned bytes_written = 0;
    bytes_from_file = calloc(5 * (number_of_bytes + 1), sizeof(char));
    
    for(int i = 0; i<5; i++){
        file_pointer = fopen(file_names[i], "r");
        if(file_pointer == NULL){
            printf("File not found: %s", file_names[i]);
            strcat_p(bytes_from_file, file_not_found, bytes_written);
            bytes_written = bytes_written + strlen(file_not_found);
            continue;
        }
        // Speicher für den Inhalt der Datei.
        char* file_content = calloc(number_of_bytes, sizeof(char));
        // Lese die Anzahl der Zeichen aus der Datei.
        fread(file_content, sizeof(char), number_of_bytes, file_pointer);
        // Hänge den String an 
        strcat_p(bytes_from_file, file_content, bytes_written);
        // Update der Anzahl der Bytes.
        bytes_written = bytes_written + strlen(file_content);    
        // Schließen der Datei und freigeben des Speichers
        free(file_content);
        fclose(file_pointer);
    }
   
    return 0;
}

/*
 * Hängt den string an die entsprechende Position in dest und hängt einen Newline
 * Character dran.
 */
int strcat_p(char* dest, const char* string, unsigned position){
    
    unsigned last_pos = position + strlen(string);
    
    for(int i = position; i < last_pos; i++){
        dest[i] = string[i];  
    }
    
    dest[last_pos] = '\n';
    
    return 0;
}
