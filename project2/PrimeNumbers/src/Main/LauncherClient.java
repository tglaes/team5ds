
package Main;

import Client.Client;

/**
 * Klasse die einen Client erstellt welcher mit dem Server verbunden ist.
 * 
 *@author Tristan Glaes,Meris Krupic,Jurie Golovencic,Vadim Khablov 
 *@version 14.12.2017
 */

public class LauncherClient {

    public static void main(String[] args) {
        startClient();
    }

    /**
     * Methode zum starten des Client
     * 
    */
    public static void startClient() {
        Client c = null;
        int function = -1;
        int port = 8000;

        while(function!= 0){
        try {
            if(c == null){
                function = ClientDialog.readFunction();
            }else if(c != null){
                function = ClientDialog.readClientFunction();
            }
            switch (function) {
                case ClientDialog.START_CLIENT:
                    if(c == null){
                    c = new Client();
                    }
                    c.start();
                    break;
                case ClientDialog.END:
                     System.out.println("\nProgram closed!" );
                    break;
                default:
                    System.out.println("\nWrong input");
                    break;
            }

        } catch (AssertionError e) {
            System.out.println(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    }
}
