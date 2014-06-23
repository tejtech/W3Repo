/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.model;

import java.util.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class BusinessParameterList extends AbstractModel
{

    protected static final long serialVersionUID = -3127516993124225943L;
    /**
     * Holds a list of BusinessParameters.
     */
    private List<BusinessParameter> validBPList = new ArrayList<BusinessParameter>();
    private List<BusinessParameter> invalidBPList = new ArrayList<BusinessParameter>();    

    public BusinessParameterList()
    {
    }

    public List<BusinessParameter> getValidBPList()
    {
        return validBPList;
    }

    public List<BusinessParameter> getInvalidBPList()
    {
        return invalidBPList;
    }

    public String getInvalidDataDescription()
    {
        List<BusinessParameter> invalidData = getInvalidBPList();
        StringBuilder sb = new StringBuilder();
        if (invalidData != null && !invalidData.isEmpty())
        {
            Iterator<BusinessParameter> it = invalidData.iterator();
            while (it.hasNext())
            {                                           
                BusinessParameter bp = it.next();
                String alias = bp.getParent().getAlias();
                                
                if (sb != null && sb.toString().trim().length() > 0)
                {
                    sb.append(",");
                }                
                sb.append(alias);
            }
        }
        return sb.toString();
    }
    
    public boolean hasInvalidData()
    {
        return getInvalidBPList() != null && !getInvalidBPList().isEmpty();
    }
    
    public boolean hasvalidData()
    {
        return getValidBPList() != null && !getValidBPList().isEmpty();
    }
    
    public int getInvalidDataCount()
    {
        return getInvalidBPList().size();
    }
    
    public int getValidDataCount()
    {
        return getValidBPList().size();
    }
}
