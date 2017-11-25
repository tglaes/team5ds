
import java.util.Scanner;


/**
 *
 * @author Juri & Meris
 */


public class Dialog {
    private Scanner input = new Scanner(System.in); 

    //-----------Constants--------------------// 
    private static final int START_SERVER           = 1;
    private static final int START_CLIENT           = 2;
    private static final int CALCULATE_PRIM         = 3;
    private static final int INT                    = 4;
    private static final int STRING                 = 5;
    private static final int STRUCT                 = 6;
    private static final int END                    = 0;


    /**
     * Hauptschleife der Klasse BerechungDialog !
     * Diese ermöglicht das Einlesen der eingegeben Werte 
     * und gibt die Ergebnisse aus.
     */

    public void start() {
        int function = -1;
        int port = 0;
        int nPrim= 0;
        String ipAdress = "";
        int output = 0;
        
        while (function != END) {
            try {
                function = readFunction(); 
                switch (function)
                {
                    case START_SERVER :
                    port = ReadInput.readlnInt(input,"\nPort: ");
                    // TODO: Server starten mit Port
                    // startServer(port);
                    System.out.println( "\nServer is running...");
                    break;
                    
                    case START_CLIENT :
                    port = ReadInput.readlnInt(input,"\nPort: ");
                    ipAdress = ReadInput.readlnString(input,"\nIP-Adress: ");
                    // TODO: Client starten und mit Server verbinden
                    // startClient();
                    // connectToServer(ipAdress, port);
                    System.out.println( "\nClient is connected with server...");
                    break;
                    
                    case CALCULATE_PRIM :
                    nPrim = ReadInput.readlnInt(input,"\nAnzahl(n): ");
                    output = ReadInput.readlnInt(input,"\nOutput (INT-Array-->4 / STRING-->5 / STRUCT--> 6): ");
                    if(output == 4){
                        //intArrayOutput(nPrim);
                    } else if(output == 5) {
                        //stringArrayOutput(nPrim);
                    } else if(output == 6) {
                        //structOutput(nPrim);
                    }
                    break;
                                       
                    case END :
                    // TODO Server und Client Verbindung "beenden"
                    // closeSocket();
                    System.out.println("\nEND");
                    break;

                    default :
                    System.out.println("\nWrong input");
                    break;
                } 

            }
            catch (AssertionError e) {
                System.out.println(e);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Menue anzeigen und functionen einlesen
     */
    private int readFunction() { 
        System.out.println ( 
            "\n: START_SERVER             -> "    + START_SERVER +   
            "\n: START_CLIENT             -> "    + START_CLIENT + 
            "\n: CALCULATE_PRIM           -> "    + CALCULATE_PRIM + 
            "\n: Programm Beenden         -> "    + END  );

        return ReadInput.readlnInt(input,"\n\t Input: ");                    
    }

    /**
     * Eine Main Methode um BerechnungDialag zu starten
     */

    public static void main(String[] args) {
        new Dialog().start();
    }
}
