/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Client.Client;
import Server.Server;

/**
 *
 * @author Tristan
 */
public class Main {

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        Server s = null;
        Client c = null;
        int function = -1;
        int port = 0;

        try {
            function = Dialog.readFunction();
            switch (function) {
                case Dialog.START_SERVER:
                    s = new Server();
                    port = Dialog.readlnInt("\nPort: ");
                    System.out.println("\nServer is running under port " + port);
                    s.run(port);
                    break;

                case Dialog.START_CLIENT:
                    c = new Client();
                    c.start();
                    break;
                case Dialog.END:
                    if (s != null) {
                        s.stop();
                        System.out.println("\nServer stopped...");
                    }
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
