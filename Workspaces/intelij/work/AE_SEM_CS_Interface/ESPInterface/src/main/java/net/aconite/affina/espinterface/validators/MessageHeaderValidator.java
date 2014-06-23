/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.validators;

import java.util.List;
import net.aconite.affina.espinterface.exceptions.EspMessageValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.jms.JmsHeaders;


/**
 * @author wakkir.muzammil
 */
public class MessageHeaderValidator  implements IJMSValidator
{
    private static final Logger logger = LoggerFactory.getLogger(MessageHeaderValidator.class.getName());

    private List<String> jmsTypes;



    @ServiceActivator
    public Message validateMessageHeader(Message inMessage) throws EspMessageValidationException
    {

        MessageHeaders inHeaders = inMessage.getHeaders();
        Object inPayload = inMessage.getPayload();

        if(logger.isDebugEnabled())
        {
            logger.debug("DecryptMessage : Incoming Message header: {}", inHeaders);
            logger.debug("DecryptMessage : Message payload: {}", inPayload);
            logger.debug(toString());
        }

        logger.info("JMSType read from MQ message: {}", inHeaders.get(JmsHeaders.TYPE));

        if(jmsTypes!=null && !jmsTypes.isEmpty())
        {
           if(!jmsTypes.contains(inHeaders.get(JmsHeaders.TYPE)))
           {
               logger.debug("received invalid jms_type : {}", inHeaders.get(JmsHeaders.TYPE));
               throw new EspMessageValidationException("Invalid JMS Type received {"+JmsHeaders.TYPE+" : "+inHeaders.get(JmsHeaders.TYPE)+"}");
           }
        }

        return inMessage;
    }

    public List<String> getJmsTypes()
    {
        return jmsTypes;
    }

    public void setJmsTypes(List<String> jmsTypes)
    {
        this.jmsTypes = jmsTypes;
    }


}

