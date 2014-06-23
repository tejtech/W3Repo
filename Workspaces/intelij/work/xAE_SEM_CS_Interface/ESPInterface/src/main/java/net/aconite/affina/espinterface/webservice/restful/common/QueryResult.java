/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.webservice.restful.common;

import java.util.List;

/**
 *
 * @author wakkir.muzammil
 */
public class QueryResult<T> 
{

    private List<T> resultsList;
    private Integer totalCount;
    
    public QueryResult() 
    {
        super();        
    }
    
    public QueryResult(List<T> resultsList, Integer totalCount) 
    {
        super();
        this.resultsList = resultsList;
        this.totalCount = totalCount;
    }
    
    /**
     * @return the resultsList
     */
    public List<T> getResultsList() 
    {
        return resultsList;
    }
    /**
     * @param resultsList the resultsList to set
     */
    public void setResultsList(List<T> resultsList) 
    {
        this.resultsList = resultsList;
    }
    /**
     * @return the totalCount
     */
    public Integer getTotalCount() 
    {
        return totalCount;
    }
    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(Integer totalCount) 
    {
        this.totalCount = totalCount;
    }
    

}
