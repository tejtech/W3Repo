/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.exceptions;

/**
 * @author wakkir.muzammil
 */
public class EspRestServiceValidationException extends Exception
{
    private static final long serialVersionUID = 4433390499647614871L;
    private String errorCode="0";

    public EspRestServiceValidationException()
    {
        super();
    }

    public EspRestServiceValidationException(String message)
    {
        super(message);
    }
    
    public EspRestServiceValidationException(String message,String errorCode)
    {
        super(message);
        this.errorCode=errorCode;
    }

    public EspRestServiceValidationException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public EspRestServiceValidationException(String message, Throwable cause,String errorCode)
    {
        super(message, cause);
        this.errorCode=errorCode;
    }

    public EspRestServiceValidationException(Throwable cause)
    {
        super(cause);
    }

    
}
