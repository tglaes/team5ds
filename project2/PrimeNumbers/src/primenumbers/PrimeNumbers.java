
package Primenumbers;

/**
 * 
 * @author Juri & Meris
 */
public class PrimeNumbers {

    private String[] stringArray;
    private int[] intArray;

    
    private PrimeNumbers(String[] stringArray, int[] intArray) {
        this.stringArray = stringArray;
        this.intArray = intArray;
    }
     
    
    private static int[] calcPrimes(int n) {
        int[] primeNumbers = new int[n];
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
    
    
    private static void intArrayOutput(int[] primeArray){
        System.out.println("Primzahlenausgabe als Int-Array");
        for (int i = 0; i < primeArray.length ; i++) {
            System.out.println(i+1 +")\t" +primeArray[i]);
        }
        System.out.println("\n");
    }
    
    private static String[] intToStringArray(int[] primeArray){
        String[] stringArray = new String[primeArray.length];
        for (int i = 0; i < primeArray.length ; i++) {
            stringArray[i]= String.valueOf(primeArray[i]); 
        }
        return stringArray;
    }
    
    
    private static void stringArrayOutput(String[] stringArray){
        System.out.println("Primzahlenausgabe als String-Array");
        for (int i = 0; i < stringArray.length ; i++) {   
            System.out.println(i+1 +")\t" +stringArray[i]);           
        }
        System.out.println("\n");
    }
    
    
    private static void structOutput(String[] stringArray, int[] intArray){
        PrimeNumbers prim = new PrimeNumbers(stringArray, intArray);
        
        System.out.println("Primzahlenausgabe als Struktur");
        
        for (int i = 0; i < intArray.length; i++) {   
            System.out.println(i+1 +")\tString-Array: " +prim.stringArray[i]+ " \tInteger-Array: "
            + prim.intArray[i]);           
        }
        System.out.println("\n");
    }
    

    public static void main(String[] args) {    
        intArrayOutput(calcPrimes(10));
        stringArrayOutput(intToStringArray(calcPrimes(10)));
        structOutput(intToStringArray(calcPrimes(10)), calcPrimes(10));
    }
}

