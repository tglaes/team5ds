#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "data.h"



/*
 * Kopiert die ersten number_of_bytes Bytes aus den Dateien aus file_names in
 * bytes_from_file. Falls die Datei nicht gefunden wird, wird "Datei nicht gefunden!"
 * gespeichert.
 * 
 * NOCH NICHT GETESTET!!
 * 
 */
int get_bytes_from_file(char** bytes_from_file, char file_names[][MAX_FILE_NAME_SIZE], int number_of_bytes) {
    
    FILE *file_pointer;
    //unsigned bytes_written = 0;
    *bytes_from_file = calloc(MAX_DATA_SIZE, sizeof(char)); //
    int bytes_read;
    
    for(int i = 0; i<MAX_FILES_TO_ACCEPT; i++){
        if(strlen(file_names[i]) == 0) //ignore empty filenames 
            continue;
        
        file_pointer = fopen(file_names[i], "r");
        if(file_pointer == NULL){
            printf("debug:File not found: %s \n", file_names[i]);
            strncat(*bytes_from_file ,FILE_NOT_FOUND_1, strlen(FILE_NOT_FOUND_1));
            strncat(*bytes_from_file ,file_names[i], MAX_FILE_NAME_SIZE);
            strncat(*bytes_from_file ,FILE_NOT_FOUND_2, strlen(FILE_NOT_FOUND_2));  //
            continue;
        }
        // Speicher für den Inhalt der Datei.
        char* file_content = calloc(number_of_bytes+1, sizeof(char));//+1 für die'\0'
        // Lese die Anzahl der Zeichen aus der Datei.
        bytes_read = fread(file_content, sizeof(char), number_of_bytes, file_pointer);
        strncat(*bytes_from_file ,file_names[i], MAX_FILE_NAME_SIZE);
        strncat(*bytes_from_file ,":", 2);
        file_content[number_of_bytes+1]='\0';//damit wird strcat_p überfüssig und man kann strncat benutzen das problem war das der inhalt immer überschrieben wurde
        strncat(*bytes_from_file ,file_content, MAX_FILE_NAME_SIZE);
        strncat(*bytes_from_file ," |\n", 3);
        
        // Schließen der Datei und freigeben des Speichers

        free(file_content);
        fclose(file_pointer);
    }
   
    return 0;
}

