/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Main.Dialog;
import java.util.List;

/**
 *
 * @author Tristan Glaes
 */
public class Client {

    private int numberOfPrimes = 0;
    private int function = 0;
    private PrimeNumbersService pns;
    
    public Client() {
        pns = new PrimeNumbersService();
    }

    public void start() {
        //TODO: Den Client nocheinmal nach einer Eingabe fragen oder beenden.
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
        function = Dialog.readlnInt("\nOutput (INT-Array-->1 / STRING-->2 / STRUCT--> 3): ");
    }
    
    private void getNumberOfPrimes() {
        numberOfPrimes = Dialog.readlnInt("\nAnzahl(n): ");
    }
    
}
