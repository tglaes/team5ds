/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.util.concurrent.Executors;
import javax.xml.ws.Endpoint;

/**
 * Klasse dient zum Starten des Server mit uebergabe des Ports.
 * 
 * @author Tristan Glaes,Meris Krupic,Jurie Golovencic,Vadim Khablov 
 * @version 14.12.2017
 */
public class Server {

    private Endpoint endpoint;

    /**
     * Methode dient zum Starten des Servers.
     * 
     * @param port hardcoded 8000
     * @throws IOException 
     */
    public void run(int port) throws IOException {
        
        //endpoint = Endpoint.create(new PrimeNumbers());
        //endpoint.setExecutor(Executors.newFixedThreadPool(10));
        endpoint = Endpoint.publish("http://localhost:" + port + "/primenumbers", new PrimeNumbers());
        System.out.println("Press enter to stop the server.");
        System.in.read();
        endpoint.stop();
    }
}
