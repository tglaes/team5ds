/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.Scanner;

/**
 *
 * @author Tristan Glaes
 */
public class Dialog {
    
    public static final int START_SERVER           = 1;
    public static final int START_CLIENT           = 2;
    public static final int END                    = 0; 
    private static Scanner input = new Scanner(System.in); 
    /**
     * Liest eingegebene Int, String.
     */
    public static int readlnInt(String stringInput) {
        System.out.print(stringInput);
        String line = input.nextLine();
        return Integer.parseInt(line);
    }

    public static String readlnString(String stringInput) {
        System.out.print(stringInput);
        String word = input.nextLine();
        return word;
    }
    /**
     * Menue anzeigen und functionen einlesen
     */
    public static int readFunction() { 
        System.out.println ( 
            "\n: START_SERVER             -> "    + START_SERVER +   
            "\n: START_CLIENT             -> "    + START_CLIENT + 
            "\n: Programm Beenden         -> "    + END  );

        return readlnInt("\n\t Input: ");                    
    }
}
