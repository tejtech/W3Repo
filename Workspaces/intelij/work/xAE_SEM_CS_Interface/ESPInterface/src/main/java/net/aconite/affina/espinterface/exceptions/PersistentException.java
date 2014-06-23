/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.exceptions;

/**
 *
 * @author thushara.pethiyagoda
 */
public class PersistentException extends ScriptProcessingRuntimeException
{
    static final long serialVersionUID = -3387516993124225943L;    
    private Exception exception;
    
    public PersistentException(String msg, String eReason, String errCode,  Exception ex)
    {
        super(msg, eReason, errCode);        
        exception = ex;
    }
    public PersistentException(String msg, Exception ex)
    {
        this(msg, "", "0", ex);        
    }
    public PersistentException(String msg)
    {
        super(msg,"","1");        
    }
    public Exception getException()
    {
        return exception;
    }   
}
