package net.aconite.affina.espinterface.handler.message;


import net.aconite.affina.espinterface.builder.IMessageBuilder;
import net.aconite.affina.espinterface.builder.MessageBuilderFactory;
import net.aconite.affina.espinterface.builder.MessageContent;
import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.exceptions.EspMessageBuilderException;
import net.aconite.affina.espinterface.exceptions.EspMessageTransformationException;
import net.aconite.affina.espinterface.xmlmapping.affina.CardSetupAlert;
import net.aconite.affina.espinterface.xmlmapping.sem.CardSetupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.jms.JmsHeaders;
import org.springframework.integration.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;


public class CardSetupAlertHandler implements IEspMessageHandler
{
    private static final Logger logger = LoggerFactory.getLogger(CardSetupAlertHandler.class.getName());

    private String espScope;

    @Splitter
    public List<Message> split(Message inMessage) throws EspMessageTransformationException
    {
        // At info level, the data recorded shall be limited to the message type
        //  and its identifier (tracking reference or service instance).
        // At debug level, the complete message shall be recorded.

        MessageHeaders inHeaders = inMessage.getHeaders();
        CardSetupAlert inPayload = (CardSetupAlert) inMessage.getPayload();

        logger.debug("process : Incoming Message header: {}", inHeaders);
        logger.debug("process : Message payload: {}", inPayload);
        String inTrackId = inPayload.getTrackingReference();
        MessageContent messageContent = new MessageContent(EspConstant.CARD_SETUP_ALERT, inTrackId);
        messageContent.setScopeName(getEspScope());

        MessageBuilderFactory msgBuilderFactory = new MessageBuilderFactory();
        IMessageBuilder builder = msgBuilderFactory.getBuilder(EspConstant.CARD_SETUP_REQUEST);

        List<CardSetupRequest> requests;
        try
        {
            requests = builder.buildCSList(messageContent);
        }
        catch (EspMessageBuilderException ex)
        {
            throw new EspMessageTransformationException(ex.getMessage(), ex);
        }

        List<Message> outMessages = new ArrayList<Message>();
        for (CardSetupRequest request : requests)
        {
            Message outMessage = generateCardSetupRequestMessage(inHeaders, request);
            outMessages.add(outMessage);
        }

        return outMessages;
    }
    
    @Transformer
    public Message transform(Message inMessage) throws EspMessageTransformationException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //==========================================================================

    private Message<CardSetupRequest> generateCardSetupRequestMessage(MessageHeaders headers, CardSetupRequest sourceData)
    {
        logger.info("Created CardSetupRequest Message : {}" , sourceData.getTrackingReference());

        return MessageBuilder.withPayload(sourceData)
                .copyHeaders(headers)
                .setHeader(JmsHeaders.TYPE, EspConstant.CARD_SETUP_REQUEST)
                //.setHeader(EspConstant.MQ_MESSAGE_TYPE, EspConstant.CARD_SETUP_REQUEST)
                .build();
    }

    //==========================================================================

    public String getEspScope()
    {
        return espScope;
    }

    public void setEspScope(String espScope)
    {
        this.espScope = espScope;
    }
}
