/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

#include <stdlib.h>
#include <stdio.h>

/*
 * Schreibt die ersten n Bytes (Zeichen) aus file in content.
 * Gibt 0 zurück falls die Datei gefunden wurde, sonst -1
 */
int get_bytes_from_file(char* content, char* file, int n){
    
    FILE *file_pointer;
    file_pointer = fopen(file, "r");
    if(file_pointer == NULL){
        return -1;
    }
    
    for(int i=0; i<n; i++){
        content[i] = fgetc(file_pointer);
    }
    content[n] = '\0';
    
    return 0;
}