/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Server.Server;
import java.util.Scanner;

/**
 *
 * @author Tristan
 */
public class Main {
    
    private static Scanner input = new Scanner(System.in); 

    //-----------Constants--------------------// 
    private static final int START_SERVER           = 1;
    private static final int START_CLIENT           = 2;
    private static final int CALCULATE_PRIM         = 3;
    private static final int INT                    = 4;
    private static final int STRING                 = 5;
    private static final int STRUCT                 = 6;
    private static final int END                    = 0;
    
    public static void main(String[] args){
        start();
    }
    public static void start() {
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
                    port = readlnInt(input,"\nPort: ");
                    // TODO: Server starten mit Port
                    // startServer(port);
                    System.out.println( "\nServer is running...");
                    Server s = new Server();
                    s.run(port);
                    break;
                    
                    case START_CLIENT :
                    port = readlnInt(input,"\nPort: ");
                    ipAdress = readlnString(input,"\nIP-Adress: ");
                    // TODO: Client starten und mit Server verbinden
                    // startClient();
                    // connectToServer(ipAdress, port);
                    System.out.println( "\nClient is connected with server...");
                    break;
                    
                    case CALCULATE_PRIM :
                    nPrim = readlnInt(input,"\nAnzahl(n): ");
                    output = readlnInt(input,"\nOutput (INT-Array-->4 / STRING-->5 / STRUCT--> 6): ");
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
     * Liest eingegebene Int, String.
     */
    private static int readlnInt(Scanner input,String stringInput) {
        System.out.print(stringInput);
        String line = input.nextLine();
        return Integer.parseInt(line);
    }

    private static String readlnString(Scanner input, String stringInput) {
        System.out.print(stringInput);
        String word = input.nextLine();
        return word;
    }
    /**
     * Menue anzeigen und functionen einlesen
     */
    private static int readFunction() { 
        System.out.println ( 
            "\n: START_SERVER             -> "    + START_SERVER +   
            "\n: START_CLIENT             -> "    + START_CLIENT + 
            "\n: CALCULATE_PRIM           -> "    + CALCULATE_PRIM + 
            "\n: Programm Beenden         -> "    + END  );

        return readlnInt(input,"\n\t Input: ");                    
    }
    
}
