/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

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
    @WebMethod
    public String getPrimeNumbersAsString(int n ){
        return null;
    }
    
    @WebMethod
    public int[] getPrimeNumbersAsArray(int n){
        return null;
    }
    
    @WebMethod
    public String[] getPrimeNumbersAsStringArray1(int n){
        return null;
    }
}
