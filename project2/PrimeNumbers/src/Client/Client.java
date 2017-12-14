/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Main.ClientDialog;
import java.util.List;

/**
 * Klasse welche Webmethoden vom Server aufruft und Primzahlen Ergebnisse
 * zurueck liefert.
 * 
 *@author Tristan Glaes,Meris Krupic,Jurie Golovencic,Vadim Khablov 
 *@version 14.12.2017
 */
public class Client {

    private int numberOfPrimes = 0;
    private int function = 0;
    private PrimeNumbersService pns;
    
    /**
     * Konstruktor der uns ein PrimeNumbersService Objekt erstellt 
     * ueber das wir auf die Webmethoden zugreifen koennen.
     */
    public Client() {
        pns = new PrimeNumbersService();
    }

    /**
     * Methode zur Ausgabe der gewaehlten Ausgabeart(Array,String,Struktur).
     */
    public void start() {
        getNumberOfPrimes();
        getFunction();
        switch (function) {
            case 1:
                List<Integer> array = pns.getPrimeNumbersPort().getPrimeNumbersAsArray(numberOfPrimes).getItem();
                System.out.println(array);
                break;
           
            case 2:
                String primes = pns.getPrimeNumbersPort().getPrimeNumbersAsString(numberOfPrimes);
                System.out.println(primes);
                break;
                
            case 3:          
                PrimeNumberCollection pnc = pns.getPrimeNumbersPort().getPrimeNumbersAsStringArray1(numberOfPrimes);
                System.out.println(pnc.primeNumbers);
                //
                break;
            default:
                break;
        }
    }
    
    private void getFunction(){
        function = ClientDialog.readlnInt("\nOutput (INT-Array-->1 / STRING-->2 / STRUCT--> 3): ");
    }
    
    private void getNumberOfPrimes() {
        numberOfPrimes = ClientDialog.readlnInt("\nAnzahl(n): ");
    }
    
}
