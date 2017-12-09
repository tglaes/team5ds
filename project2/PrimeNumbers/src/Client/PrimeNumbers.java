
package Client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "PrimeNumbers", targetNamespace = "http://Server/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface PrimeNumbers {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://Server/PrimeNumbers/getPrimeNumbersAsStringRequest", output = "http://Server/PrimeNumbers/getPrimeNumbersAsStringResponse")
    public String getPrimeNumbersAsString(
        @WebParam(name = "arg0", partName = "arg0")
        int arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns Client.IntArray
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://Server/PrimeNumbers/getPrimeNumbersAsArrayRequest", output = "http://Server/PrimeNumbers/getPrimeNumbersAsArrayResponse")
    public IntArray getPrimeNumbersAsArray(
        @WebParam(name = "arg0", partName = "arg0")
        int arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns Client.PrimeNumberCollection
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://Server/PrimeNumbers/getPrimeNumbersAsStringArray1Request", output = "http://Server/PrimeNumbers/getPrimeNumbersAsStringArray1Response")
    public PrimeNumberCollection getPrimeNumbersAsStringArray1(
        @WebParam(name = "arg0", partName = "arg0")
        int arg0);

}