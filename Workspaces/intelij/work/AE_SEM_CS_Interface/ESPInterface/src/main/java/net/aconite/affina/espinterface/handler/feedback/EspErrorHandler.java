/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.handler.feedback;

import java.util.Hashtable;
import net.acointe.affina.esp.AffinaEspUtils;
import net.aconite.affina.espinterface.constants.EspConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.*;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.jms.JmsHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.support.channel.ChannelResolutionException;
import org.springframework.integration.transformer.MessageTransformationException;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * @author wakkir.muzammil
 */
public class EspErrorHandler implements IEspFeedbackHandler
{
    private static final Logger logger = LoggerFactory.getLogger(EspErrorHandler.class.getName());
    
    private EspFeedbackHeader espFeedbackHeader;
        
    public EspErrorHandler(EspFeedbackHeader espFeedbackHeader)
    {
        this.espFeedbackHeader = espFeedbackHeader;
    }
    
    @ServiceActivator
    public void endProcess(Message<MessagingException> inMessage) 
    {
        Message outMessage = processErrorMessage(inMessage);
    }
    
    @ServiceActivator
    public Message process(Message<MessagingException> inMessage)
    {
        Message outMessage = processErrorMessage(inMessage);

        return outMessage;
    }
    
    /**
     * @param inMessage
     */
    
    private Message processErrorMessage(Message<MessagingException> inMessage)
    {
        MessageHeaders inHeaders = inMessage.getHeaders();
        MessagingException inPayload = inMessage.getPayload();

        EspPayload espPayload = buildErrorPayload(inPayload);
                
        Message outMessage = generateErrorMessage(inHeaders, espPayload);

        return outMessage;
    }

    private Message<String> generateErrorMessage(MessageHeaders headers, EspPayload espPayload)
    {        
        return MessageBuilder.withPayload(espPayload.getMessageWithHeader())
                .copyHeaders(headers)
                .setHeader(JmsHeaders.TYPE, headers.get(JmsHeaders.TYPE))
                .build();
    }

    private EspPayload buildErrorPayload(MessagingException inPayload)
    {        
        Hashtable<String, Object> props = new Hashtable<String, Object>();
        StringBuffer sb = new StringBuffer();       

        String errorMessage;
        if (inPayload instanceof MessageHandlingException)
        {
            errorMessage = inPayload.getCause().getMessage();
        }
        else if (inPayload instanceof MessageTransformationException)
        {
            errorMessage = inPayload.getCause().getMessage();
        }
        else if (inPayload instanceof MessageDeliveryException)
        {
            errorMessage = inPayload.toString();
        }
        else if (inPayload instanceof ChannelResolutionException)
        {
            errorMessage = inPayload.getMessage();
        }
        else
        {
            errorMessage = inPayload.getCause().getMessage();
        }
        //sb.append(errorMessage);
        //String request=inPayload.getCause().getMessage();//getFailedMessage().getPayload().toString();
        
        props.put(EspConstant.VT_ERROR_DESCRIPTION, AffinaEspUtils.getEmptyIfNull(errorMessage));
        props.put(EspConstant.VT_ERROR_CODE, AffinaEspUtils.getEmptyIfNull(""));

        sb.append(VelocityEngineUtils.mergeTemplateIntoString(espFeedbackHeader.getVelocityEngine(), "/applicationError.vm", espFeedbackHeader.getEspMessageEncoding(), props));


        logger.error(sb.toString(), inPayload.getCause());
        
        EspPayload espPayload=new EspPayload(espFeedbackHeader.generateMessageHeader(),sb.toString(),true);
        
        return espPayload;
    }
    
}
