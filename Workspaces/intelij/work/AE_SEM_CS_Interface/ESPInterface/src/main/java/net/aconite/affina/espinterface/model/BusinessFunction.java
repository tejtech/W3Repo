/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.model;

import com.platform7.pma.request.emvscriptrequest.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class BusinessFunction extends AbstractModel
{    
    protected static final long serialVersionUID = -3325516993124225943L;  
    private ESPBusinessFunction parent;
    private String name ;
    
    /**
     * 
     * @param parentObj 
     */
    public BusinessFunction(ESPBusinessFunction parentObj, String funcName)
    {
        parent = parentObj;          
        funcName = name;
    }
    
    public ESPBusinessFunction getParent()
    {
        return parent;
    }
    
    public String getName()
    {
        return name;
    }
}
