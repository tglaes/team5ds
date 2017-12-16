
package Main;

import static Main.ClientDialog.END;
import java.util.Scanner;

/**
 * Hilfsklasse zum erstellen des Dialogs fuer den Server
 * 
 *@author Tristan Glaes,Meris Krupic,Jurie Golovencic,Vadim Khablov 
 *@version 14.12.2017
 */
public class ServerDialog {
    
    public static final int START_SERVER           = 1;
    public static final int END                    = 0; 
    private static Scanner input = new Scanner(System.in);
    
    /**
     * Liest eingegebene String und gibt diesen als int zurueck.
     */
    public static int readlnInt(String stringInput) {
        System.out.print(stringInput);
        String line = input.nextLine();
        return Integer.parseInt(line);
    }
    
    /**
    * Liest String ein.
    */
    public static String readlnString(String stringInput) {
        System.out.print(stringInput);
        String word = input.nextLine();
        return word;
    }
    /**
     * Menue anzeigen und Funktionen einlesen
     */
    public static int readFunction() { 
        System.out.println ( 
            "\n: START_SERVER             -> "    + START_SERVER +
            "\n: Programm Beenden         -> "    + END  );

        return readlnInt("\n\t Input: ");                    
    }
}
