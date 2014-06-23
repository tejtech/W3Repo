/**
 * 
 */
package net.aconite.affina.espinterface.webservice.restful.common;

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author wakkir.muzammil
 *
 */
public class UIRecords<T>
{
    
    @JsonProperty
    private Integer totalCount;

    @JsonProperty
    private String recordName;

    @JsonProperty
    private List<T> records;

    /**
     * 
     */

    public UIRecords(Integer totalCount, String recordName, List<T> records)
    {
        this.recordName = recordName;
        this.totalCount = totalCount;
        this.records = records;
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

    /**
     * @return the recordName
     */
    public String getRecordName()
    {
        return recordName;
    }

    /**
     * @param recordName the recordName to set
     */
    public void setRecordName(String recordName)
    {
        this.recordName = recordName;
    }

    /**
     * @return the records
     */
    public List<T> getRecords()
    {
        return records;
    }

    /**
     * @param records the records to set
     */
    public void setRecords(List<T> records)
    {
        this.records = records;
    }
    
    
    
    

}
