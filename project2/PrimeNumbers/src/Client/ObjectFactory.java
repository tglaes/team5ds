
package Client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the Client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PrimeNumberCollection_QNAME = new QName("http://Server/", "primeNumberCollection");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: Client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PrimeNumberCollection }
     * 
     */
    public PrimeNumberCollection createPrimeNumberCollection() {
        return new PrimeNumberCollection();
    }

    /**
     * Create an instance of {@link IntArray }
     * 
     */
    public IntArray createIntArray() {
        return new IntArray();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrimeNumberCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Server/", name = "primeNumberCollection")
    public JAXBElement<PrimeNumberCollection> createPrimeNumberCollection(PrimeNumberCollection value) {
        return new JAXBElement<PrimeNumberCollection>(_PrimeNumberCollection_QNAME, PrimeNumberCollection.class, null, value);
    }

}
