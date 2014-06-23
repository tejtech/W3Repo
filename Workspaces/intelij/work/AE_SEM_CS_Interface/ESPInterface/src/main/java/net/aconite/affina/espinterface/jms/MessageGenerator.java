/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.jms.JmsHeaders;
import org.springframework.integration.support.MessageBuilder;

/**
 *
 * @author wakkir.muzammil
 */
public class MessageGenerator implements IMessageGenerator
{
    private static final Logger logger = LoggerFactory.getLogger(MessageGenerator.class.getName());

    public <T> Message<T> generateMessage(MessageHeaders headers, T sourceData)
    {
        logger.debug("message body : {}" , sourceData);

        return MessageBuilder.withPayload(sourceData)
                .copyHeaders(headers)
                .build();
    }
    
    public <T> Message<T> generateMessage(MessageHeaders headers, T sourceData,String jmsType)
    {
        logger.debug("message body : {}" , sourceData);

        return MessageBuilder.withPayload(sourceData)
                .copyHeaders(headers)
                .setHeader(JmsHeaders.TYPE, jmsType)
                .build();
    }
}
