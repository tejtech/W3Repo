package net.aconite.affina.espinterface.handler.message;

import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.exceptions.EspMessageTransformationException;
import net.aconite.affina.espinterface.xmlmapping.sem.CardSetupResponse;
import net.aconite.affina.espinterface.xmlmapping.sem.StatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.jms.JmsHeaders;
import org.springframework.integration.support.MessageBuilder;

import java.util.List;
import net.aconite.affina.espinterface.builder.MessageContent;
import net.aconite.affina.espinterface.validators.ResponseMessageValidator;
import net.aconite.affina.espinterface.xmlmapping.sem.ErrorType;


public class CardSetupResponseHandler implements IEspMessageHandler
{
    private static final Logger logger = LoggerFactory.getLogger(CardSetupResponseHandler.class.getName());

    private String espScope;

   
    @Transformer
    public Message transform(Message inMessage) throws EspMessageTransformationException
    {
        // At info level, the data recorded shall be limited to the message type
        //   and its identifier (tracking reference or service instance).
        // At debug level, the complete message shall be recorded.

        MessageHeaders inHeaders = inMessage.getHeaders();
        CardSetupResponse inPayload = (CardSetupResponse) inMessage.getPayload();

        logger.debug("process : Incoming Message header: {}", inHeaders);
        logger.debug("process : Message payload: {}", inPayload);

        String trackId = inPayload.getTrackingReference();
        StatusType statusType = inPayload.getStatus();
        ErrorType errorType = inPayload.getError();

        String inTrackId = inPayload.getTrackingReference();
        MessageContent messageContent = new MessageContent(EspConstant.CARD_SETUP_RESPONSE, inTrackId,statusType,errorType);
        messageContent.setScopeName(getEspScope());


        CardSetupResponse response = new CardSetupResponse();
        response.setTrackingReference(trackId);

        MessageContent validatedContent=new ResponseMessageValidator().validate(messageContent);

        if(validatedContent.isValid())
        {
            response.setStatus(StatusType.STATUS_OK);
        }
        else
        {
            response.setStatus(StatusType.ERROR);
            response.setError(new ErrorType());
            response.setError(validatedContent.getError());
        }

        Message outMessage = generateCardSetupResponseMessage(inHeaders, response);

        return outMessage;
    }
    

    @Splitter
    public List<Message> split(Message inMessage) throws EspMessageTransformationException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //==========================================================================

    private Message<CardSetupResponse> generateCardSetupResponseMessage(MessageHeaders headers, CardSetupResponse sourceData)
    {

        return MessageBuilder.withPayload(sourceData)
                .copyHeaders(headers)
                .setHeader(JmsHeaders.TYPE, EspConstant.CARD_SETUP_RESPONSE)
                //.setHeader(EspConstant.MQ_MESSAGE_TYPE, EspConstant.CARD_SETUP_RESPONSE)
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
