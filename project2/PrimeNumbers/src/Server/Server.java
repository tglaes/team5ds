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
 *
 * @author Tristan
 */
public class Server {

    private Endpoint endpoint;

    public void run(int port) throws IOException {
        
        //endpoint = Endpoint.create(new PrimeNumbers());
        //endpoint.setExecutor(Executors.newFixedThreadPool(10));
        endpoint = Endpoint.publish("http://localhost:" + port + "/primenumbers", new PrimeNumbers());
        System.out.println("Press enter to stop the server.");
        System.in.read();
        endpoint.stop();
    }
}
