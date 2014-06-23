package net.aconite.affina.espinterface.handler.message;

import com.platform7.standardinfrastructure.utilities.Dovecote;
import java.util.ArrayList;
import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.exceptions.EspMessageTransformationException;
import net.aconite.affina.espinterface.exceptions.EspResponseValidationException;
import net.aconite.affina.espinterface.persistence.GenericPersistentDAO;
import net.aconite.affina.espinterface.persistence.Workable;
import net.aconite.affina.espinterface.xmlmapping.sem.StageScriptResponse;
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
import net.aconite.affina.espinterface.factory.*;
import net.aconite.affina.espinterface.helper.Result;
import net.aconite.affina.espinterface.model.*;
import net.aconite.affina.espinterface.scripting.generic.*;
import net.aconite.affina.espinterface.scripting.response.*;
import net.aconite.affina.espinterface.xmlmapping.cs.SendScriptResponse;
import net.aconite.affina.espinterface.xmlmapping.sem.ErrorType;


public class StageScriptResponseHandler implements IEspMessageHandler
{
    private static final Logger logger = LoggerFactory.getLogger(StageScriptResponseHandler.class.getName());
    private Workable<StageScriptResponse, Result<StageScriptDetail>> worker = ScriptProcessorFactory.getSendScriptResponseValidatorWorkable();
    private String espScope;

    // A Workable to perform the actual work within the non-container managed transaction
    private class SSRTransformWorker extends Workable<Message, Message>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("CardSetupAlertHandler.split", new Dovecote.ProtectedOperation<Message>()
                {
                    public Message execute() throws Exception
                    {
                        return transformImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspResponseValidationException("Exception caught during response processing", e);
            }
        }
    }

    @Transformer
    public Message transform(Message inMessage)
    {
        SSRTransformWorker worker = new SSRTransformWorker();
        worker.setData(inMessage);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }

    private Message transformImpl(Message inMessage)
    {
        // At info level, the data recorded shall be limited to the message type
        //   and its identifier (tracking reference or service instance).
        // At debug level, the complete message shall be recorded.

       

        return null;


    }

    @Splitter
    public List<Message> split(Message inMessage) throws EspMessageTransformationException
    {
        List<Message> msgs = new ArrayList<Message>();
        MessageHeaders inHeaders = inMessage.getHeaders();
        StageScriptResponse inPayload = (StageScriptResponse) inMessage.getPayload();

        logger.debug("process : Incoming Message header: {}", inHeaders);
        logger.debug("process : Message payload: {}", inPayload);

        String trackId = inPayload.getTrackingReference();
        StatusType statusType = inPayload.getStatus();
        ErrorType errorType = inPayload.getError();

        String inTrackId = inPayload.getTrackingReference();
        MessageContent messageContent = new MessageContent(EspConstant.STAGE_SCRIPT_RESPONSE, 
                                    inTrackId,statusType,errorType);
        messageContent.setScopeName(getEspScope());
        StageScriptResponse response = new StageScriptResponse();
        response.setTrackingReference(trackId);     
        
        worker.setData(inPayload);        
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        Result<StageScriptDetail> result = worker.getResult();
        result.handleError();        
        //isSuccessful here means that StageScriptResponse is as a result of A SendScriptRequest.
        //So only for such scripts a SendScriptResponse will be sent.
        //False means either a validation error or not a response for a SendScriptRequest.
        if(result.isSuccessFul())
        {            
            SendScriptResponse sendScriptResponse = result.
                                    <SendScriptResponse>getResponse(ResponseGenerator.
                                                                    SEM_TO_SEND_SCRIPT_RESPONSE_GENRATOR_ID);
            Message sendScriptResponseMessage = generateStageScriptResponseMessage(inHeaders, sendScriptResponse);
            msgs.add(sendScriptResponseMessage); 
        }
        //StageScriptResponse will be shown in the progress watcher.        
        StageScriptResponse stageScriptResponse = result.
                            <StageScriptResponse>getResponse(ResponseGenerator.
                                                            STAGE_SCRIPT_RESPONSE_GENRATOR_ID);
        Message srageScriptResponseMessage = generateStageScriptResponseMessage(inHeaders, stageScriptResponse);
        msgs.add(srageScriptResponseMessage);
        
        
        return msgs;
    }

    //==========================================================================

    private Message<StageScriptResponse> generateStageScriptResponseMessage(MessageHeaders headers, 
                                                                                StageScriptResponse sourceData)
    {
        return MessageBuilder.withPayload(sourceData)
                .copyHeaders(headers)
                .setHeader(JmsHeaders.TYPE, EspConstant.STAGE_SCRIPT_RESPONSE)
                //.setHeader(EspConstant.MQ_MESSAGE_TYPE, EspConstant.STAGE_SCRIPT_RESPONSE)
                .build();
    }
    
    private Message<SendScriptResponse> generateStageScriptResponseMessage(MessageHeaders headers, 
                                                                                SendScriptResponse sourceData)
    {
        return MessageBuilder.withPayload(sourceData)
                .copyHeaders(headers)
                .setHeader(JmsHeaders.TYPE, EspConstant.SEND_SCRIPT_RESPONSE)
                //.setHeader(EspConstant.MQ_MESSAGE_TYPE, EspConstant.STAGE_SCRIPT_RESPONSE)
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
