package net.aconite.affina.espinterface.handler.message;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.aconite.affina.espinterface.builder.IMessageBuilder;
import net.aconite.affina.espinterface.builder.MessageBuilderFactory;
import net.aconite.affina.espinterface.builder.MessageContent;
import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.exceptions.EspMessageBuilderException;
import net.aconite.affina.espinterface.exceptions.EspMessageTransformationException;
import net.aconite.affina.espinterface.helper.Result;
import net.aconite.affina.espinterface.scripting.generic.*;
import net.aconite.affina.espinterface.scripting.statusupdate.ScriptStatusUpdateDataHolder;
import net.aconite.affina.espinterface.scripting.statusupdate.ScriptStatusUpdateEvent;
import net.aconite.affina.espinterface.scripting.statusupdate.ScriptStatusUpdateEventHandler;
import net.aconite.affina.espinterface.scripting.statusupdate.ScriptUpdateProcessor;
import net.aconite.affina.espinterface.xmlmapping.sem.ScriptStatusResponse;
import net.aconite.affina.espinterface.xmlmapping.sem.ScriptStatusUpdate;
import net.aconite.affina.espinterface.xmlmapping.sem.StatusType;
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
    @Transformer
    public Message transform(Message inMessage) throws EspMessageTransformationException
    {
        // At info level, the data recorded shall be limited to the message type
        //   and its identifier (tracking reference or service instance).
        // At debug level, the complete message shall be recorded.

        MessageHeaders inHeaders = inMessage.getHeaders();
        ScriptStatusUpdate inPayload = (ScriptStatusUpdate) inMessage.getPayload();

        logger.debug("process : Incoming Message header: ", inHeaders);
        logger.debug("process : Message payload: ", inPayload); 
        
        //Expiration time is in Hex from SEM and need to convert into java long milliseconds.
        //BigInteger timeFromHex2Bigint = new BigInteger(inPayload.getCard().getExpirationDate(), 16);        
        //inPayload.getCard().setExpirationDate(timeFromHex2Bigint.toString());
        
        Result result = processScriptUpdate(inPayload);
        ScriptStatusResponse response = result.getResultData().getScriptStatusResponse();  
        Message outMessage = generateScriptStatusResponseMessage(inHeaders, response);
        return outMessage;
    }

    @Splitter
    public List<Message> split(Message inMessage) throws EspMessageTransformationException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * 
     * @param su
     * @return 
     */
    private Result processScriptUpdate(ScriptStatusUpdate su)
    {        
        Result scriptUpdateResult;
        
        ScriptProcessable<ScriptStatusUpdateDataHolder> sProcessable = ScriptUpdateProcessor.getProcessable();
        ScriptStatusUpdateEventHandler eh = 
                new ScriptStatusUpdateEventHandler(sProcessable);
        ScriptStatusUpdateDataHolder dh = new ScriptStatusUpdateDataHolder(su, getEspScope());
        ScriptEvent<ScriptStatusUpdateDataHolder> scriptEvent = new ScriptStatusUpdateEvent(dh, ScriptUpdateType.STATUSUPDATE);
        scriptUpdateResult = eh.onScriptAlert(scriptEvent);
        return scriptUpdateResult;
    }
    /**
     * Use this if this method is invoked via any registered listeners.
     * @param su
     * @return 
     */
    private Result processScriptUpdateListener(ScriptStatusUpdate su)
    {
        Iterator<ScriptEventListener<ScriptStatusUpdateDataHolder>> scriptEventListeners = scriptListenerList.iterator();
        Result scriptUpdateResult = null;
        
        while(scriptEventListeners.hasNext())
        {
            ScriptEventListener<ScriptStatusUpdateDataHolder> scriptEventListener = scriptEventListeners.next();
            ScriptStatusUpdateDataHolder dh = new ScriptStatusUpdateDataHolder(su, getEspScope());
            ScriptEvent<ScriptStatusUpdateDataHolder> scriptEvent = new ScriptStatusUpdateEvent(dh, ScriptUpdateType.STATUSUPDATE);
            scriptUpdateResult = scriptEventListener.onScriptAlert(scriptEvent);
        }
        return scriptUpdateResult;
    }
    //==========================================================================

    private Message<ScriptStatusResponse> generateScriptStatusResponseMessage(MessageHeaders headers, ScriptStatusResponse sourceData)
    {

        logger.info("Created ScriptStatusResponse Message : " + sourceData.getTrackingReference());

        return MessageBuilder.withPayload(sourceData)
                .copyHeaders(headers)
                .setHeader(JmsHeaders.TYPE, EspConstant.SCRIPT_STATUS_RESPONSE)
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
