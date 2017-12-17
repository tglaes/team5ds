
package Main;

import java.util.Scanner;

/**
 * Hilfsklasse die den Dialog fuer den Client aufbaut.
 * 
 *@author Tristan Glaes,Meris Krupic,Jurie Golovencic,Vadim Khablov 
 *@version 14.12.2017
 */
public class ClientDialog {
    public static final int START_CLIENT           = 1;
    public static final int END                    = 0; 
    private static Scanner input = new Scanner(System.in); 
    
    /**
     * Liest eingegebene String und wandelt in int um.
     */
    public static int readlnInt(String stringInput) {
        System.out.print(stringInput);
        String line = input.nextLine();
        return Integer.parseInt(line);
    }
    
    /**
     * Liest eingegebene String ein.
     */
    public static String readlnString(String stringInput) {
        System.out.print(stringInput);
        String word = input.nextLine();
        return word;
    }
    
    /**
     * Menue anzeigen und Funktionen einlesen fuer die aussere Anzeige
     */
    public static int readFunction() { 
        System.out.println (  
            "\n: START_CLIENT             -> "    + START_CLIENT + 
            "\n: Programm Beenden         -> "    + END  );

        return readlnInt("\n\t Input: ");                    
    }
    
    /**
     * Menue anzeigen und Funktionen einlesen fuer die innere Anzeige
     */
    public static int readClientFunction() { 
        System.out.println (  
            "\n: Weitere Berechnungen?    -> "    + START_CLIENT + 
            "\n: Programm Beenden         -> "    + END  );

        return readlnInt("\n\t Input: ");                    
    }
    
}
