/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.exceptions;

/**
 * @author wakkir.muzammil
 */
public class EspInterfaceException extends Exception
{
    private static final long serialVersionUID = 4233390499647614871L;
    private String errorCode="0";

    public EspInterfaceException()
    {
        super();
    }

    public EspInterfaceException(String message)
    {
        super(message);
    }
    
    public EspInterfaceException(String message,String errorCode)
    {
        super(message);
        this.errorCode=errorCode;
    }

    public EspInterfaceException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public EspInterfaceException(String message, Throwable cause,String errorCode)
    {
        super(message, cause);
        this.errorCode=errorCode;
    }

    public EspInterfaceException(Throwable cause)
    {
        super(cause);
    }

}
