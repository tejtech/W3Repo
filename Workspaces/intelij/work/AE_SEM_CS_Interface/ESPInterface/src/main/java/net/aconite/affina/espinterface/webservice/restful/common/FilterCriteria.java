/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.webservice.restful.common;

import java.sql.Timestamp;

/**
 *
 * @author wakkir.muzammil
 */
public class FilterCriteria 
{

    private Integer id;
    private String  name;
    private String  trackId;
    private String  semTrackId;
    private String  scopeName;
    private Integer scopeId;
    private Integer productId;
    private Integer productPartId;
    private Integer applicationId;
    private Integer bin;
    private String  cardId;
    private Integer businessFunctionId;
    private String  source;
    private String  status;
    private String pan;
    private String psn;
    private Long expiryDate;

    public Integer getId() 
    {
        return id;
    }

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getTrackId()
    {
        return trackId;
    }

    public void setTrackId(String trackId)
    {
        this.trackId = trackId;
    }
    
     public String getSemTrackId()
    {
        return semTrackId;
    }

    public void setSemTrackId(String semTrackId)
    {
        this.semTrackId = semTrackId;
    }

    public String getScopeName()
    {
        return scopeName;
    }

    public void setScopeName(String scopeName)
    {
        this.scopeName = scopeName;
    }

    
    public Integer getScopeId() 
    {
        return scopeId;
    }

    public void setScopeId(Integer scopeId) 
    {
        this.scopeId = scopeId;
    }

    public Integer getProductId() 
    {
        return productId;
    }

    public void setProductId(Integer productId) 
    {
        this.productId = productId;
    }

    public Integer getProductPartId() 
    {
        return productPartId;
    }

    public void setProductPartId(Integer productPartId) 
    {
        this.productPartId = productPartId;
    }

    public Integer getApplicationId() 
    {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) 
    {
        this.applicationId = applicationId;
    }

    public Integer getBin() 
    {
        return bin;
    }

    public void setBin(Integer bin) 
    {
        this.bin = bin;
    }

    public String getCardId() 
    {
        return cardId;
    }

    public void setCardId(String cardId) 
    {
        if(cardId!=null)
        {
            this.cardId = cardId.toUpperCase();
        }
        else
        {
            this.cardId = cardId;
        }
    }
    
    public Integer getBusinessFunctionId() 
    {
        return businessFunctionId;
    }

    public void setBusinessFunctionId(Integer businessFunctionId) 
    {
        this.businessFunctionId = businessFunctionId;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getPan()
    {
        return pan;
    }

    public void setPan(String pan)
    {
        this.pan = pan;
    }

    public String getPsn()
    {
        return psn;
    }

    public void setPsn(String psn)
    {
        this.psn = psn;
    }

    public Long getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(Long expiryDate)
    {
        this.expiryDate = expiryDate;
    }
    
    
    
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\nId                 : ").append(id);
        sb.append("\nName               : ").append(name);
        sb.append("\nTrackId            : ").append(trackId);
        sb.append("\nSemTrackId         : ").append(semTrackId);
        sb.append("\nScopeId            : ").append(scopeId);
        sb.append("\nScopeName          : ").append(scopeName);
        sb.append("\nProductId          : ").append(productId);
        sb.append("\nProductPartId      : ").append(productPartId);
        sb.append("\nApplicationId      : ").append(applicationId);
        sb.append("\nBin                : ").append(bin);
        sb.append("\nCardId             : ").append(cardId);
        sb.append("\nBusinessFunctionId : ").append(businessFunctionId);
        sb.append("\nSource             : ").append(source);
        sb.append("\nStatus             : ").append(status);
        sb.append("\nPSN                : ").append(psn);
        sb.append("\nExpiryDate         : ").append(expiryDate);
        sb.append("\n");
        return sb.toString();
    }       

    
}
