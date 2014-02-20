/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.exceptions;

/**
 * @author wakkir.muzammil
 */
public class EspResponseValidationException extends EspMessageTransformationException
{
    private static final long serialVersionUID = 4233390399647614871L;


    public EspResponseValidationException(String message)
    {
        super(message);
    }

    public EspResponseValidationException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public EspResponseValidationException(String message,String errCode)
    {
        super(message,errCode);
    }

    public EspResponseValidationException(String message, Throwable cause,String errCode)
    {
        super(message, cause,errCode);
    }
    
}
