/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author Tristan Glaes
 */
@XmlRootElement
public class PrimeNumberCollection {
    
    @XmlElement
    public ArrayList<Integer> primeNumbers = new ArrayList<>();  
}
