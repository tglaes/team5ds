/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Client.Client;
import Server.Server;

//TODO: Getrennter Dialog fuer Server und fuer Client

public class Main {

    public static void main(String[] args) {
        start();
    }

    
    
    public static void start() {
        Server s = null;
        Client c = null;
        int function = -1;
        int port = 8000;

        while(function!= 0){
        try {
            function = ClientDialog.readFunction();
            switch (function) {
                case ClientDialog.START_SERVER:
                    s = new Server();
                    //port = ClientDialog.readlnInt("\nPort: ");
                    System.out.println("\nServer is running under port 8000" );
                    s.run(port);
                    break;

                case ClientDialog.START_CLIENT:
                    c = new Client();
                    c.start();
                    break;
                case ClientDialog.END:
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
