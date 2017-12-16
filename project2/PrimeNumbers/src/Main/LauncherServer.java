/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Server.Server;

//TODO: Getrennter Dialog fuer Server und fuer Client
public class LauncherServer {

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        Server s = null;
        int function = -1;
        int port = 8000;

        while (function != 0) {
            try {
                function = ServerDialog.readFunction();
                switch (function) {
                    case ServerDialog.START_SERVER:
                        if (s == null) {
                            s = new Server();
                            System.out.println("\nServer is running under port 8000");
                            s.run(port);
                        } else {
                            System.out.println("\nServer is already running!");
                        }
                        break;
                    case ServerDialog.END:
                        if (s != null) {
                            s.stop();
                        }
                        System.out.println("\nServer closed successfully.");
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
