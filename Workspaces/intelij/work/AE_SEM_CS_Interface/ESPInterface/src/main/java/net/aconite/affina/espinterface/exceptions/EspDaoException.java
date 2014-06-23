/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.exceptions;

/**
 * @author wakkir.muzammil
 */
public class EspDaoException extends EspMessageTransformationException
{
    private static final long serialVersionUID = 4233390399647614871L;


    public EspDaoException(String message)
    {
        super(message);
    }

    public EspDaoException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public EspDaoException(String message,String errCode)
    {
        super(message,errCode);
    }

    public EspDaoException(String message, Throwable cause,String errCode)
    {
        super(message, cause,errCode);
    }
    
}
