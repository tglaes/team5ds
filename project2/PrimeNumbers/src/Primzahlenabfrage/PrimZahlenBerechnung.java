
package Primzahlenabfrage;

/**
 *
 * @author Juri & Meris
 */
public class PrimZahlenBerechnung {

    private String[] stringArrayStruct;
    private int[] intArrayStruct;

    public PrimZahlenBerechnung(String[] stringArrayStruct, int[] intArrayStruct) {
        this.stringArrayStruct = stringArrayStruct;
        this.intArrayStruct = intArrayStruct;
    }
     
    
    private static int[] primBerechnen(int n) {
        int[] primzahlen = new int[n];
        int anzahlPrim = 0;
        for (int i = 2; anzahlPrim < n ; i++) {
            boolean isPrimzahl = true;
            for (int j = 2; j < i && isPrimzahl; j++) {
                if ((i % j) == 0) {
                    isPrimzahl = false;
                }              
            }
            
            if (isPrimzahl) {
                anzahlPrim++;
                primzahlen[anzahlPrim-1] = i;
                //System.out.println(anzahlPrim +")\t" + i + " ist eine Primzahl!");
            }
        }
        return primzahlen;  
    }
    
    private static void intArrayAusgabe(int[] primzahlen){
        System.out.println("Primzahlenausgabe als Int-Array");
        for (int i = 0; i < primzahlen.length ; i++) {
            System.out.println(i+1 +")\t" +primzahlen[i]);
        }
        System.out.println("\n");
    }
    
    private static String[] stringArrayBerechnen(int[] primzahlen){
        String[] stringArray = new String[primzahlen.length];
        for (int i = 0; i < primzahlen.length ; i++) {
            stringArray[i]= String.valueOf(primzahlen[i]); 
        }
        return stringArray;
    }
    
    private static void stringArrayAusgabe(String[] stringArray){
        System.out.println("Primzahlenausgabe als String-Array");
        for (int i = 0; i < stringArray.length ; i++) {   
            System.out.println(i+1 +")\t" +stringArray[i]);           
        }
        System.out.println("\n");
    }
    
    
    private static void structAusgabe(String[] stringArrayStruct, int[] intArrayStruct){
        PrimZahlenBerechnung prim = new PrimZahlenBerechnung(stringArrayStruct, intArrayStruct);
        
        System.out.println("Primzahlenausgabe als Struktur");
        
        for (int i = 0; i < intArrayStruct.length; i++) {   
            System.out.println(i+1 +")\tString-Array: " +prim.stringArrayStruct[i]+ " \tInteger-Array: "
            + prim.intArrayStruct[i]);           
        }
        System.out.println("\n");
    }
    

    public static void main(String[] args) {
        
        intArrayAusgabe(primBerechnen(10));
        stringArrayAusgabe(stringArrayBerechnen(primBerechnen(10)));
        structAusgabe(stringArrayBerechnen(primBerechnen(10)), primBerechnen(10));
    }
}

