/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.webservice.restful.service.model;

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
    
    private String message;    
    private String trackId;    
    private long totalCount;
    private boolean isDuplicate;
    

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

    public BigDecimal getoID()
    {
        return oID;
    }

    public void setoID(BigDecimal oID)
    {
        this.oID = oID;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getTrackId()
    {
        return trackId;
    }

    public void setTrackId(String trackId)
    {
        this.trackId = trackId;
    }

    public long getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(long totalCount)
    {
        this.totalCount = totalCount;
    }

    public boolean getIsDuplicate()
    {
        return isDuplicate;
    }

    public void setIsDuplicate(boolean isDuplicate)
    {
        this.isDuplicate = isDuplicate;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\nOID          : ").append(oID);
        sb.append("\nName         : ").append(name);
        sb.append("\nExpiryDate   : ").append(expiryDate);   
        sb.append("\nBusFunName   : ").append(businessFunctionName);    
        sb.append("\nScopeName    : ").append(scopeName); 
        sb.append("\nBusFunOID    : ").append(businessFunctionOID);  
        sb.append("\nScopeOID     : ").append(scopeOID);    
        sb.append("\nSoftcardOID  : ").append(softcardOID); 
        sb.append("\nScrManAppOID : ").append(scriptableManifestAppOID); 
        sb.append("\nManAppOID    : ").append(manifestApplicationOID); 
        sb.append("\nMessage      : ").append(message); 
        sb.append("\nTrackId      : ").append(trackId); 
        sb.append("\nTotalCount   : ").append(totalCount); 
        sb.append("\nIsDuplicate  : ").append(isDuplicate); 
        sb.append("\n");
        return sb.toString(); 
        
    }
    
    

    
    
    
    
}
