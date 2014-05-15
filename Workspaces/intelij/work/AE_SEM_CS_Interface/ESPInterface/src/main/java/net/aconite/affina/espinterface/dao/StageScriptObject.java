/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao;

import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;

/**
 *
 * @author wakkir.muzammil
 */
public class StageScriptObject extends FilterCriteria
{
    
    private Integer maxRestageCount; 
    private Long stageScriptStartDate; 
    private Long stageScriptEndDate;

    public StageScriptObject(FilterCriteria filter, Integer maxRestageCount, Long stageScriptStartDate, Long stageScriptEndDate)
    {            
        setScopeId(filter.getScopeId());
        setProductId(filter.getProductId());
        setProductPartId(filter.getProductPartId());
        setApplicationId(filter.getApplicationId());
        setBin(filter.getBin());  
        setCardId(filter.getCardId());
        setBusinessFunctionId(filter.getBusinessFunctionId());
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
    
    
    
}
