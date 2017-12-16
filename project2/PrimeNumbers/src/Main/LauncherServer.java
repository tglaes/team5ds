
package Main;

import Server.Server;

/**
 * Klasse die einen Server erstellt.
 * 
 * @author Tristan Glaes,Meris Krupic,Jurie Golovencic,Vadim Khablov 
 * @version 14.12.2017
 */

public class LauncherServer {

    public static void main(String[] args) {
        startServer();
    }

    /**
    * Methode zum starten des Server
    *
    */
    
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
