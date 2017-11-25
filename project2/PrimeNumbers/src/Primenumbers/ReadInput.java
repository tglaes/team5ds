
import java.util.Scanner;

/**
 * Die folgenden Methoden lesen unsere eingegeben Werte ein.
 * 
 * @author (Juri, Meris) 
 */
public class ReadInput {

    /**
     * Liest eingegebene Int, String.
     */
    public static int readlnInt(Scanner input,String stringInput) {
        System.out.print(stringInput);
        String line = input.nextLine();
        return Integer.parseInt(line);
    }

    public static String readlnString(Scanner input, String stringInput) {
        System.out.print(stringInput);
        String word = input.nextLine();
        return word;
    }
}

