
package net.aconite.affina.espinterface.xmlmapping.sem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Identifies individual card in SEM
 * <p/>
 * <p>Java class for CardType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="CardType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="PAN" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="PANSequence" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ExpirationDate" use="required" type="{}JavaDateType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardType")
public class CardType
{

    @XmlAttribute(name = "PAN", required = true)
    protected String pan;
    @XmlAttribute(name = "PANSequence", required = true)
    protected String panSequence;
    @XmlAttribute(name = "expirationYear", required = true)
    protected String expirationYear;
    @XmlAttribute(name = "expirationMonth", required = true)
    protected String expirationMonth;

    /**
     * Gets the value of the pan property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPAN()
    {
        return pan;
    }

    /**
     * Sets the value of the pan property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPAN(String value)
    {
        this.pan = value;
    }

    /**
     * Gets the value of the panSequence property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPANSequence()
    {
        return panSequence;
    }

    /**
     * Sets the value of the panSequence property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPANSequence(String value)
    {
        this.panSequence = value;
    }

    /**
     * Gets the value of the expirationYear property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExpirationYear()
    {
        return expirationYear;
    }
    /**
     * Sets the value of the expirationYear property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExpirationYear(String value)
    {
        this.expirationYear = value;
    }
    
     /**
     * Gets the value of the expirationMonth property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExpirationMonth()
    {
        return expirationMonth;
    }

    /**
     * Sets the value of the expirationMonth property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExpirationMonth(String value)
    {
        this.expirationMonth = value;
    }

}
