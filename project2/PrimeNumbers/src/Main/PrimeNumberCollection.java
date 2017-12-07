/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;


/**
 *
 * @author Tristan Glaes
 */
public class PrimeNumberCollection {
    
    @XmlElement
    private ArrayList<Integer> primeNumbers = new ArrayList<>();
    
    public PrimeNumberCollection(ArrayList<Integer> primeNumbers){
        this.primeNumbers = primeNumbers;
    }
    
    public ArrayList<Integer> getPrimeNumbers(){
        return this.primeNumbers;
    }
}
