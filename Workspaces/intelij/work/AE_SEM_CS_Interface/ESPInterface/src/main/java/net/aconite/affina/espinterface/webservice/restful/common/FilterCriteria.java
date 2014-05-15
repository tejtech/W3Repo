/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.webservice.restful.common;

/**
 *
 * @author wakkir.muzammil
 */
public class FilterCriteria 
{

    private Integer id;
    private String  name;
    private Integer scopeId;
    private Integer productId;
    private Integer productPartId;
    private Integer applicationId;
    private Integer bin;
    private String  cardId;
    private Integer businessFunctionId;

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
        this.cardId = cardId;
    }

    
    
    public Integer getBusinessFunctionId() 
    {
        return businessFunctionId;
    }

    public void setBusinessFunctionId(Integer businessFunctionId) 
    {
        this.businessFunctionId = businessFunctionId;
    }
    

    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\nId                 : ").append(id);
        sb.append("\nName               : ").append(name);
        sb.append("\nScopeId            : ").append(scopeId);
        sb.append("\nProductId          : ").append(productId);
        sb.append("\nProductPartId      : ").append(productPartId);
        sb.append("\nApplicationId      : ").append(applicationId);
        sb.append("\nBin                : ").append(bin);
        sb.append("\nCardId             : ").append(cardId);
        sb.append("\nBusinessFunctionId : ").append(businessFunctionId);
        sb.append("\n");
        return sb.toString();
    }       

    
}
