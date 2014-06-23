/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.exceptions;

import org.springframework.integration.Message;
import org.springframework.integration.MessageHandlingException;

/**
 * @author wakkir.muzammil
 */
public class EspMessageHandlingException extends MessageHandlingException
{
    private static final long serialVersionUID = 4233390399647614871L;
    
    private String errorCode="0";

    public EspMessageHandlingException(Message message)
    {
        super(message);
    }

    public EspMessageHandlingException(Message message, Throwable cause)
    {
        super(message, cause);
    }
    
    public EspMessageHandlingException(Message message,String errorCode)
    {
        super(message);
        this.errorCode=errorCode;
    }

    public EspMessageHandlingException(Message message, Throwable cause,String errorCode)
    {
        super(message, cause);
        this.errorCode=errorCode;
    }
}
