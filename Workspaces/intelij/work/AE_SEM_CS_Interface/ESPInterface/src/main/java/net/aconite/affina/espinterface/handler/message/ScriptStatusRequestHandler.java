package net.aconite.affina.espinterface.handler.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.platform7.standardinfrastructure.utilities.Dovecote;
import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.exceptions.EspMessageTransformationException;
import net.aconite.affina.espinterface.exceptions.EspResponseValidationException;
import net.aconite.affina.espinterface.helper.Result;
import net.aconite.affina.espinterface.model.*;
import net.aconite.affina.espinterface.persistence.GenericPersistentDAO;
import net.aconite.affina.espinterface.persistence.Workable;
import net.aconite.affina.espinterface.scripting.generic.*;
import net.aconite.affina.espinterface.scripting.statusupdate.ScriptStatusUpdateDataHolder;
import net.aconite.affina.espinterface.scripting.statusupdate.ScriptStatusUpdateEvent;
import net.aconite.affina.espinterface.scripting.statusupdate.ScriptStatusUpdateEventHandler;
import net.aconite.affina.espinterface.scripting.statusupdate.ScriptUpdateProcessor;
import net.aconite.affina.espinterface.xmlmapping.sem.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.jms.JmsHeaders;
import org.springframework.integration.support.MessageBuilder;

public class ScriptStatusRequestHandler implements IEspMessageHandler
{

    private static final Logger logger = LoggerFactory.getLogger(ScriptStatusRequestHandler.class.getName());
    private List<ScriptEventListener<ScriptStatusUpdateDataHolder>> scriptListenerList = new ArrayList<ScriptEventListener<ScriptStatusUpdateDataHolder>>();
    private String espScope;

    // A Workable to perform the actual work within the non-container managed transaction
    private class SSRTransformWorker extends Workable<Message, List<Message>>
    {

        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ScriptStatusRequestHandler.transform",
                                                                 new Dovecote.ProtectedOperation<List<Message>>()
                {
                    public List<Message> execute() throws Exception
                    {
                        return transformImpl(getData());
                    }
                }));
            }
            catch (Throwable e)
            {
                throw new EspResponseValidationException("Exception caught during response processing", e);
            }
        }
    }

    @Transformer
    public Message transform(Message inMessage) throws EspMessageTransformationException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private List<Message>  transformImpl(Message inMessage) throws EspMessageTransformationException
    {        
        List<Message> outMessages = new ArrayList<Message>();
        MessageHeaders inHeaders = inMessage.getHeaders();
        ScriptStatusUpdate inPayload = (ScriptStatusUpdate) inMessage.getPayload();

        logger.debug("process : Incoming Message header: {}", inHeaders);
        logger.debug("process : Message payload: {}", inPayload);
        
        Result result = processScriptUpdate(inPayload);
        ScriptStatusResponse response = result.getResultData().getScriptStatusResponse();
        if (response.getStatus() == StatusType.STATUS_OK && response.getError() != null)
        {
            ScriptStatusResponse responseErr = new ScriptStatusResponse();
            responseErr.setError(response.getError());
            responseErr.getError().setData("ALARM" + response.getError().getData());
            responseErr.setTrackingReference(response.getTrackingReference()); 
            responseErr.setStatus(StatusType.ERROR); 
            Message outMessage = generateScriptStatusResponseMessage(inHeaders, responseErr,
                                                                                EspConstant.JMS_AFFINA_MESSAGE);
            outMessages.add(outMessage);
            response.setError(null);
            response.setStatus(StatusType.STATUS_OK);
            outMessage = generateScriptStatusResponseMessage(inHeaders, response, EspConstant.JMS_SEM_MESSAGE);
            outMessages.add(outMessage);
        }
        else
        {
            Message outMessage = generateScriptStatusResponseMessage(inHeaders, response, "");
            outMessages.add(outMessage);
        
        }
        return outMessages;
    }

    @Splitter
    public List<Message> split(Message inMessage) throws EspMessageTransformationException
    {
        SSRTransformWorker worker = new SSRTransformWorker();
        worker.setData(inMessage);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
        
    }

    /**
     *
     * @param su
     * <p/>
     * @return
     */
    private Result processScriptUpdate(ScriptStatusUpdate su)
    {
        Result scriptUpdateResult;

        ScriptProcessable<ScriptStatusUpdateDataHolder, AbstractModel> sProcessable = ScriptUpdateProcessor.getProcessable();
        ScriptStatusUpdateEventHandler eh =
                new ScriptStatusUpdateEventHandler(sProcessable);
        ScriptStatusUpdateDataHolder dh = new ScriptStatusUpdateDataHolder(su, getEspScope());
        ScriptEvent<ScriptStatusUpdateDataHolder> scriptEvent = new ScriptStatusUpdateEvent(dh,
                                                                                            ScriptUpdateType.STATUSUPDATE);
        scriptUpdateResult = eh.onScriptAlert(scriptEvent);
        return scriptUpdateResult;
    }

    /**
     * Use this if this method is invoked via any registered listeners.
     * <p/>
     * @param su
     * <p/>
     * @return
     */
    private Result processScriptUpdateListener(ScriptStatusUpdate su)
    {
        Iterator<ScriptEventListener<ScriptStatusUpdateDataHolder>> scriptEventListeners = scriptListenerList.iterator();
        Result scriptUpdateResult = null;

        while (scriptEventListeners.hasNext())
        {
            ScriptEventListener<ScriptStatusUpdateDataHolder> scriptEventListener = scriptEventListeners.next();
            ScriptStatusUpdateDataHolder dh = new ScriptStatusUpdateDataHolder(su, getEspScope());
            ScriptEvent<ScriptStatusUpdateDataHolder> scriptEvent = new ScriptStatusUpdateEvent(dh,
                                                                                                ScriptUpdateType.STATUSUPDATE);
            scriptUpdateResult = scriptEventListener.onScriptAlert(scriptEvent);
        }
        return scriptUpdateResult;
    }
    //==========================================================================

    private Message<ScriptStatusResponse> generateScriptStatusResponseMessage(MessageHeaders headers,
                                                                              ScriptStatusResponse sourceData,
                                                                              String destination)
    {

        logger.info("Created ScriptStatusResponse Message : {}", sourceData.getTrackingReference());

        return MessageBuilder.withPayload(sourceData)
                .copyHeaders(headers)
                .setHeader(JmsHeaders.TYPE, EspConstant.SCRIPT_STATUS_RESPONSE)
                .setHeader(EspConstant.JMS_MESSAGE_DEST, destination)
                .build();
    }
    private Message<ScriptStatusResponse> generateScriptStatusResponseMessage(MessageHeaders headers,
                                                                              ScriptStatusResponse sourceData
                                                                              )
    {

        logger.info("Created ScriptStatusResponse Message : {}", sourceData.getTrackingReference());

        return MessageBuilder.withPayload(sourceData)
                .copyHeaders(headers)
                .setHeader(JmsHeaders.TYPE, EspConstant.SCRIPT_STATUS_RESPONSE)
                //.setHeader(EspConstant.JMS_MESSAGE_DEST, destination)
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

    /**
     *
     * @return
     */
    public List<ScriptEventListener<ScriptStatusUpdateDataHolder>> getScriptListenerList()
    {
        return scriptListenerList;
    }

    /**
     *
     * @param scriptListenerList
     */
    public void addScriptListener(ScriptEventListener<ScriptStatusUpdateDataHolder> scriptListenerList)
    {
        this.scriptListenerList.add(scriptListenerList);
    }
}
