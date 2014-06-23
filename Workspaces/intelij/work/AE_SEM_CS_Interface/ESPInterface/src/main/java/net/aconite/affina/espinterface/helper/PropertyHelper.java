/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.helper;

import net.aconite.affina.espinterface.handler.feedback.EspFeedbackHeader;
import org.springframework.stereotype.Service;

/**
 *
 * @author thushara.pethiyagoda
 */
@Service("propertyHelper")
public class PropertyHelper
{
    private String espScope;
    

    public PropertyHelper()
    {
        
    }   
    
    
    public void setEspScope(String scope)
    {
        espScope = scope;
    }
    
    public String getEspScope()
    {
        return espScope;
    }
}
    
