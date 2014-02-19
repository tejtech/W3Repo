/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.exceptions;

/**
 * @author wakkir.muzammil
 */
public class EspMessageValidationException extends EspInterfaceException
{
    private static final long serialVersionUID = 4233390399647614871L;


    public EspMessageValidationException()
    {
        super();
    }

    public EspMessageValidationException(String message)
    {
        super(message);
    }

    public EspMessageValidationException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public EspMessageValidationException(String message,String errCode)
    {
        super(message,errCode);
    }

    public EspMessageValidationException(String message, Throwable cause,String errCode)
    {
        super(message, cause,errCode);
    }

    public EspMessageValidationException(Throwable cause)
    {
        super(cause);
    }
}
