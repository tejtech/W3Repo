/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.webservice.restful.service.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.apache.log4j.Logger;

/**
 *
 * @author wakkir.muzammil
 */
public class StageScript //implements Serializable
{
    private static Logger log = Logger.getLogger(StageScript.class);

    private BigDecimal oID;
    private String name;
    
    private String pan;
    private String panSequenceNumber;
    private Timestamp expiryDate;   
    
    private String  businessFunctionName;
    private String scopeName;
    
    private BigDecimal businessFunctionOID;   
    private BigDecimal scopeOID;
    
    private BigDecimal softcardOID;
    private BigDecimal scriptableManifestAppOID;
    private BigDecimal manifestApplicationOID;
    

    public StageScript()
    {
    }

    public BigDecimal getOID() 
    {
        return oID;
    }

    public void setOid(BigDecimal oID) 
    {
        this.oID = oID;
    }
    
    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getPan() 
    {
        return pan;
    }

    public void setPan(String pan) 
    {
        this.pan = pan;
    }

    public String getPanSequenceNumber() 
    {
        return panSequenceNumber;
    }

    public void setPanSequenceNumber(String panSequenceNumber) 
    {
        this.panSequenceNumber = panSequenceNumber;
    }

    public Timestamp getExpiryDate() 
    {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) 
    {
        this.expiryDate = expiryDate;
    }

    public String getBusinessFunctionName() 
    {
        return businessFunctionName;
    }

    public void setBusinessFunctionName(String businessFunctionName) 
    {
        this.businessFunctionName = businessFunctionName;
    }

    public String getScopeName() 
    {
        return scopeName;
    }

    public void setScopeName(String scopeName) 
    {
        this.scopeName = scopeName;
    }

    public BigDecimal getBusinessFunctionOID() 
    {
        return businessFunctionOID;
    }

    public void setBusinessFunctionOID(BigDecimal businessFunctionOID) 
    {
        this.businessFunctionOID = businessFunctionOID;
    }

    public BigDecimal getScopeOID() 
    {
        return scopeOID;
    }

    public void setScopeOID(BigDecimal scopeOID) 
    {
        this.scopeOID = scopeOID;
    }

    public BigDecimal getSoftcardOID() 
    {
        return softcardOID;
    }

    public void setSoftcardOID(BigDecimal softcardOID) 
    {
        this.softcardOID = softcardOID;
    }

    public BigDecimal getScriptableManifestAppOID() 
    {
        return scriptableManifestAppOID;
    }

    public void setScriptableManifestAppOID(BigDecimal scriptableManifestAppOID) 
    {
        this.scriptableManifestAppOID = scriptableManifestAppOID;
    }

    public BigDecimal getManifestApplicationOID() 
    {
        return manifestApplicationOID;
    }

    public void setManifestApplicationOID(BigDecimal manifestApplicationOID) 
    {
        this.manifestApplicationOID = manifestApplicationOID;
    }
    
}
