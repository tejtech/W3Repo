/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.constants;

/**
 *
 * @author thushara.pethiyagoda
 */
public enum SEMScriptDeliveryStatus
{
    DELIVERY_SUCCEEDED("DELIVERY SUCCEEDED"),
    DELIVERY_FAILED_SCRIPT_RESTAGED("DELIVERY FAILED SCRIPT RESTAGED"), 
    DELIVERY_FAILED("DELIVERY FAILED"),
    UNKNOWN("UNKNOWN");
    
    SEMScriptDeliveryStatus(String delStatus)
    {
        status = delStatus;
    }
    
    private String status;
    public boolean isIn(String val)
    {
        return getStatus().equals(val);
    }
    
    public String getStatus()
    {
        return status;
    }
}
