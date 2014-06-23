/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.handler.feedback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.MessagingException;

/**
 * @author wakkir.muzammil
 */
public class EspDestinationHandler implements IEspFeedbackHandler
{
    private static final Logger logger = LoggerFactory.getLogger(EspDestinationHandler.class.getName());

    private EspFeedbackHeader espFeedbackHeader;
   
    public EspDestinationHandler(EspFeedbackHeader espFeedbackHeader)
    {
        this.espFeedbackHeader = espFeedbackHeader;
    }
        
    @ServiceActivator
    public void endProcess(Message inMessage)
    {        
        Message outMessage = processProgressMessage(inMessage);
    }   
    
    @ServiceActivator
    public Message process(Message inMessage) 
    {
        Message outMessage = processProgressMessage(inMessage);
        return outMessage;
    }
    
    private Message processProgressMessage(Message inMessage)
    {
        MessageHeaders inHeaders = inMessage.getHeaders();
        Object inPayload = inMessage.getPayload();

        logger.debug("process : Incoming Message header: {}", inHeaders);
        logger.debug("process : Message payload: {}", inPayload);

        Message outMessage = inMessage;

        return outMessage;
    }
  
}
