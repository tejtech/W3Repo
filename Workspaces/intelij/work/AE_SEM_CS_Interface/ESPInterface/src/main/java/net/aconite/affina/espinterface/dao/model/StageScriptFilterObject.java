/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao.model;

import java.math.BigDecimal;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;

/**
 *
 * @author wakkir.muzammil
 */
public class StageScriptFilterObject extends FilterCriteria
{
    protected static final long serialVersionUID = -3327516113124225943L;
    private Integer maxRestageCount; 
    private Long stageScriptStartDate; 
    private Long stageScriptEndDate;
    private String trackId;
    private BigDecimal oid;
    private String scopeName;
    
    public StageScriptFilterObject(Integer scopeId,String trackId)
    {        
        this.trackId=trackId;
        setScopeId(scopeId);
    }
    
    public StageScriptFilterObject(String scopeName,String trackId,String status)
    {        
        this.scopeName=scopeName;
        this.trackId=trackId;
        setStatus(status);
    }

    public StageScriptFilterObject(FilterCriteria filter, Integer maxRestageCount, Long stageScriptStartDate, Long stageScriptEndDate)
    {            
        setScopeId(filter.getScopeId());
        setProductId(filter.getProductId());
        setProductPartId(filter.getProductPartId());
        setApplicationId(filter.getApplicationId());
        setBin(filter.getBin());  
        setCardId(filter.getCardId());
        setBusinessFunctionId(filter.getBusinessFunctionId());
        setSource(filter.getSource());
        setStatus(filter.getStatus());
        this.maxRestageCount=maxRestageCount;
        this.stageScriptStartDate=stageScriptStartDate;
        this.stageScriptEndDate=stageScriptEndDate;        
    } 

    public Integer getMaxRestageCount() 
    {
        return maxRestageCount;
    }

    public void setMaxRestageCount(Integer maxRestageCount) 
    {
        this.maxRestageCount = maxRestageCount;
    }

    public Long getStageScriptStartDate() 
    {
        return stageScriptStartDate;
    }

    public void setStageScriptStartDate(Long stageScriptStartDate) 
    {
        this.stageScriptStartDate = stageScriptStartDate;
    }

    public Long getStageScriptEndDate() 
    {
        return stageScriptEndDate;
    }

    public void setStageScriptEndDate(Long stageScriptEndDate) 
    {
        this.stageScriptEndDate = stageScriptEndDate;
    }

    public String getTrackId()
    {
        return trackId;
    }

    public void setTrackId(String trackId)
    {
        this.trackId = trackId;
    }

    public BigDecimal getOid()
    {
        return oid;
    }

    public void setOid(BigDecimal oid)
    {
        this.oid = oid;
    }

    public String getScopeName()
    {
        return scopeName;
    }

    public void setScopeName(String scopeName)
    {
        this.scopeName = scopeName;
    }
    
    
    
    
}
