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
 *
 * @author Tristan
 */
@WebService()
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class PrimeNumbers {
    
    private String[] stringArray;
    private int[] intArray;
    
     
    @WebMethod
    public String getPrimeNumbersAsString(int n ){
        //String[] stringArray = intToStringArray(calcPrimes(n));
        String primeString = Arrays.toString(calcPrimes(n));
        return primeString;
    }
    @WebMethod
    public int[] getPrimeNumbersAsArray(int n){
        int[] intArray = toPrimitive(calcPrimes(n));
        return intArray;
    }
    @WebMethod
    public PrimeNumberCollection getPrimeNumbersAsStringArray1(int n){
        
        Integer[] primeNumberArray = calcPrimes(n);
        
        ArrayList<Integer> primeNumbersArrayList = new ArrayList<>();
        Collections.addAll(primeNumbersArrayList, primeNumberArray);
             
        PrimeNumberCollection pnc = new PrimeNumberCollection();
        pnc.primeNumbers = primeNumbersArrayList;
               
        return pnc;
    }
     
    
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
    
    private void intArrayOutput(int[] primeArray){
        System.out.println("Primzahlenausgabe als Int-Array");
        for (int i = 0; i < primeArray.length ; i++) {
            System.out.println(i+1 +")\t" +primeArray[i]);
        }
        System.out.println("\n");
    }
    
    private String[] intToStringArray(int[] primeArray){
        String[] stringArray = new String[primeArray.length];
        for (int i = 0; i < primeArray.length ; i++) {
            stringArray[i]= String.valueOf(primeArray[i]); 
        }
        return stringArray;
    }
    
    
    private void stringArrayOutput(String[] stringArray){
        System.out.println("Primzahlenausgabe als String-Array");
        for (int i = 0; i < stringArray.length ; i++) {   
            System.out.println(i+1 +")\t" +stringArray[i]);           
        }
        System.out.println("\n");
    }
    
    /*
    private void structOutput(String[] stringArray, int[] intArray){
          
        System.out.println("Primzahlenausgabe als Struktur");
        
        for (int i = 0; i < intArray.length; i++) {   
            System.out.println(i+1 +")\tString-Array: " +prim.stringArray[i]+ " \tInteger-Array: "
            + prim.intArray[i]);           
        }
        System.out.println("\n");
    }*/

}