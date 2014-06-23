/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.model;

import com.platform7.pma.request.emvscriptrequest.ESPBusinessParameter;

/**
 *
 * @author thushara.pethiyagoda
 */
public class BusinessParameter extends AbstractModel
{
    /**Serial version id.*/
    protected static final long serialVersionUID = -3227516993124225943L;  
    /** Param name.*/
    private String name;
    /** param value.*/
    private String alias;
    /** Parent of this object. */
    private ESPBusinessParameter parent;
    /**
     * 
     * @param nameValue
     * @param aliasValue 
     */
    public BusinessParameter(String nameValue, String aliasValue)
    {
        name = nameValue;
        alias = aliasValue;
    }
    /**
     * 
     * @param parentObj 
     */
    public BusinessParameter(ESPBusinessParameter parentObj)
    {
        parent = parentObj;
        name = parent.getName();
        alias = parent.getAlias();        
    }
    
    public ESPBusinessParameter getParent()
    {
        return parent;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setValue(String value)
    {
        this.alias = value;
    }
    
    
}
