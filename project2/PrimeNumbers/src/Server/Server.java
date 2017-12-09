/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import javax.xml.ws.Endpoint;

/**
 *
 * @author Tristan
 */

public class Server {
    
    private Endpoint endpoint;
    
    public void run(int port){
        // Diese Operation ist Non-Blocking!!
        endpoint = Endpoint.publish("http://localhost:" + port + "/primenumbers", new PrimeNumbers());
    } 
    
    public void stop(){
        endpoint.stop();
    }
}
