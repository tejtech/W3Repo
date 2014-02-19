/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.model;

import com.platform7.pma.application.Application;
import com.platform7.pma.card.SoftCard;
import com.platform7.pma.request.emvscriptrequest.*;
import java.util.Iterator;

/**
 *
 * @author thushara.pethiyagoda
 */
public class ScriptableCard
{

    /**
     * Application that this card can contain.
     */
    Iterator<ScriptableProduct> scriptableProducts;
    /**
     * Account linked to this card.
     */
    private long accountId;
    /**
     * PAN of this card.
     */
    private String plasticNumber;
    /**
     * Name on the card.
     */
    private String textualName;
    /**
     * Current status of the card.
     */
    private int status;
    /**
     * Pan sequence number.
     */
    private String psn;
    /**
     * Card identification number.
     */
    private String cardId;
    /**
     * Card expiration date.
     */
    private long expirationDate;
    /**
     * 
     */
    private boolean isValid = false;
    private SoftCard softCard;
    private ScriptableApplication application;
    private ESPApplicationDetail easpAppDetails;

    /**
     *
     * @param scriptableApplications
     * @param accountId
     * @param plasticNumber
     * @param textualName
     * @param status
     * @param psn
     * @param cardId
     * @param expirationDate
     */
    public ScriptableCard(long accountId, String plasticNumber,
                          String textualName, int status, String psn,
                          String cardId, long expirationDate,
                          SoftCard softCard, ScriptableApplication scriptableApplication,
                          ESPApplicationDetail appData)
    {
        this.accountId = accountId;
        this.plasticNumber = plasticNumber;
        this.textualName = textualName;
        this.status = status;
        this.psn = psn;
        this.cardId = cardId;
        this.expirationDate = expirationDate;
        this.isValid = true;
        this.softCard = softCard;
        application = scriptableApplication;
        easpAppDetails = appData;
    }

    public Iterator<ScriptableProduct> getScriptableProducts()
    {
        return scriptableProducts;
    }

    public ESPApplicationDetail getEaspAppDetails()
    {
        return easpAppDetails;
    }

    public long getAccountId()
    {
        return accountId;
    }

    public String getPlasticNumber()
    {
        return plasticNumber;
    }

    public String getTextualName()
    {
        return textualName;
    }

    public int getStatus()
    {
        return status;
    }

    public String getPsn()
    {
        return psn;
    }

    public String getCardId()
    {
        return cardId;
    }

    public long getExpirationDate()
    {
        return expirationDate;
    }

    public SoftCard getSoftCard()
    {
        return softCard;
    }

    public ScriptableApplication getApplication()
    {
        return application;
    }

    /**
     * To indicate whether this instance is valid. If this instance is created by invoking the no args constructor then
     * this would constitute to an invalid instance.
     * <p/>
     * @return
     */
    public boolean isIsValid()
    {
        return isValid;
    }
}
