/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Diese Klasse dient zur berechnung der Primzahlen und stellt Methoden
 * zur Rueckgabe der Primzahlen mit verschiedenen Datentypen.
 *
 * @author Tristan Glaes,Meris Krupic,Jurie Golovencic,Vadim Khablov 
 * @version 14.12.2017
 * 
 */
@WebService()
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class PrimeNumbers {
    
    /**
     * Gibt Primzahlen als StringArray zurueck.
     * @param n
     * @return Ausgabe der Primzahlen als String.
     */
    @WebMethod
    public String getPrimeNumbersAsString(int n ){
        //String[] stringArray = intToStringArray(calcPrimes(n));
        String primeString = Arrays.toString(calcPrimes(n));
        return primeString;
    }
    
    /**
     * Gibt die Primzahlen als IntArray zurueck.
     * @param n
     * @return IntArray mit Primzahlen
     */
    @WebMethod
    public int[] getPrimeNumbersAsArray(int n){
        int[] intArray = toPrimitive(calcPrimes(n));
        return intArray;
    }
    
    /**
     * Gibt die Primzahlen als eine eine Struktur von der PrimeNumberCollection 
     * zurueck.
     * @param n
     * @return 
     */
    @WebMethod
    public PrimeNumberCollection getPrimeNumbersAsStringArray1(int n){
        
        Integer[] primeNumberArray = calcPrimes(n);
        
        ArrayList<Integer> primeNumbersArrayList = new ArrayList<>();
        Collections.addAll(primeNumbersArrayList, primeNumberArray);
             
        PrimeNumberCollection pnc = new PrimeNumberCollection();
        pnc.primeNumbers = primeNumbersArrayList;
               
        return pnc;
    }
     
    /**
     * Methode zur berechnung der Primzahlen 
     * 
     * @param n Anzahl der zurueckgegebenen Primzahlen
     * @return Primzahlen
     */
    private Integer[] calcPrimes(int n) {
        Integer[] primeNumbers = new Integer[n];
        int sumPrimes = 0;
        for (int i = 2; sumPrimes < n ; i++) {
            boolean isPrime = true;
            for (int j = 2; j < i && isPrime; j++) {
                if ((i % j) == 0) {
                    isPrime = false;
                }              
            }           
            if (isPrime) {
                sumPrimes++;
                primeNumbers[sumPrimes-1] = i;
            }
        }
        return primeNumbers;  
    }
    
    /**
     * Methode zur Umwandlung eines IntegerArray in ein IntArray.
     * 
     * @param array 
     * @return Gibt ein IntArray zurueck.
     */
    public static int[] toPrimitive(Integer[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            int[] EMPTY_INT_ARRAY = null;
            return EMPTY_INT_ARRAY;
        }
        final int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].intValue();
        }
        return result;
    }
}
