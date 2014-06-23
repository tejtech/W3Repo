package net.aconite.affina.espinterface.handler.message;

import java.util.ArrayList;
import java.util.List;

import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.exceptions.EspMessageTransformationException;
import net.aconite.affina.espinterface.helper.Result;
import net.aconite.affina.espinterface.model.*;
import net.aconite.affina.espinterface.scripting.generic.ScriptDataHolder;
import net.aconite.affina.espinterface.scripting.generic.ScriptProcessable;
import net.aconite.affina.espinterface.scripting.request.*;
import net.aconite.affina.espinterface.scripting.response.*;
import net.aconite.affina.espinterface.xmlmapping.cs.SendScriptRequest;
import net.aconite.affina.espinterface.xmlmapping.cs.SendScriptResponse;
import net.aconite.affina.espinterface.xmlmapping.sem.StageScriptRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.jms.JmsHeaders;
import org.springframework.integration.support.MessageBuilder;


public class SendScriptRequestHandler implements IEspMessageHandler
{
    private static final Logger logger = LoggerFactory.getLogger(SendScriptRequestHandler.class.getName());
    
    private String espScope;
    

    @Splitter
    public List<Message> split(Message inMessage) throws EspMessageTransformationException
    {
        
        MessageHeaders inHeaders = inMessage.getHeaders();
        SendScriptRequest inPayload = (SendScriptRequest) inMessage.getPayload();

        logger.debug("process : Incoming Message header: {}", inHeaders);
        logger.debug("process : Message payload: {}", inPayload);
        
        ScriptProcessable<ScriptDataHolder<SendScriptRequest>, ScriptableCard> sendScriptProcessor = new SendScriptRequestProcessor();
        ScriptDataHolder<SendScriptRequest> dataHolder = new SendScriptRequestDataHolder(inPayload);
        
        Result<ScriptableCard> result = sendScriptProcessor.processScript(dataHolder);
        
        result.handleError();
        
        SendScriptResponse response = result.
                <SendScriptResponse>getResponse(ResponseGenerator.SEND_SCRIPT_RESPONSE_GENRATOR_ID);
        List<Message> msgs = new ArrayList<Message>();
        Message outMessage = generateSendScriptResponseMessage(inHeaders, response);         
        msgs.add(outMessage);
        return msgs;
    }
    
    @Transformer
    public Message transform(Message inMessage) throws EspMessageTransformationException
    {
        throw new UnsupportedOperationException("Not supported yet.");        
    }

        
    //==========================================================================

    private Message<StageScriptRequest> generateStageScriptRequestMessage(MessageHeaders headers, StageScriptRequest sourceData)
    {

        logger.info("Created StageScriptRequest Message : {}" , sourceData.getTrackingReference());

        return MessageBuilder.withPayload(sourceData)
                .copyHeaders(headers)
                .setHeader(JmsHeaders.TYPE, EspConstant.STAGE_SCRIPT_REQUEST)
                //.setHeader(EspConstant.MQ_MESSAGE_TYPE, EspConstant.SCRIPT_STATUS_RESPONSE)
                .build();
    }
    
    
    private Message<SendScriptResponse> generateSendScriptResponseMessage(MessageHeaders headers, SendScriptResponse sourceData)
    {

        logger.info("Created StageScriptRequest Message : {}" , sourceData.getTrackingReference().getId());

        return MessageBuilder.withPayload(sourceData)
                .copyHeaders(headers)
                .setHeader(JmsHeaders.TYPE, EspConstant.SEND_SCRIPT_RESPONSE)
                //.setHeader(EspConstant.MQ_MESSAGE_TYPE, EspConstant.SCRIPT_STATUS_RESPONSE)
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
     //==========================================================================
    
}
