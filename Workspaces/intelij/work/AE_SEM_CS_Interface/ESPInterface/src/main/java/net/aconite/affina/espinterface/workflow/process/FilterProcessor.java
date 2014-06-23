package net.aconite.affina.espinterface.workflow.process;

import com.platform7.pma.request.emvscriptrequest.ESPStageScriptFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.Splitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.acointe.affina.esp.AffinaEspUtils;
import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.jms.IMessageGenerator;
import net.aconite.affina.espinterface.jms.MessageGenerator;
import net.aconite.affina.espinterface.workflow.service.IWorkflow;
import net.aconite.affina.espinterface.xmlmapping.affina.StageScriptAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: wakkir Date: 08/03/14 Time: 00:50
 */
public class FilterProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(FilterProcessor.class.getName());

    @Autowired
    IWorkflow espStandardWorkflow;
    
    //@Autowired
    //ScopeHandler scopeHandler;

    private String espScope;
    
    //@Autowired
    IMessageGenerator  messageGenerator = new MessageGenerator();
    
    private final String WORKFLOW_ACTION="FilterProcessing_Action";

    /**
     *
     * @param inMessage
     * @return
     */
    @Splitter
    public List<Message> process(Message inMessage)
    {
        // At info level, the data recorded shall be limited to the message type
        //   and its identifier (tracking reference or service instance).
        // At debug level, the complete message shall be recorded.

        MessageHeaders inHeaders = inMessage.getHeaders();
        Object inPayload = inMessage.getPayload();

        //logger.debug(">********************************************************>");
        logger.debug("JobSelector processing...");

        HashMap<String, Object> myMap = new HashMap<String, Object>();
        myMap.put(EspConstant.WF_CONTEXT_SCOPE_NAME, espScope);
        myMap.put(EspConstant.WF_CONTEXT_FILTER_STATUS, AffinaEspUtils.STATUS_INITIAL);
        
                

        List<Object> objectList = espStandardWorkflow.processWorkflow(WORKFLOW_ACTION, myMap);

        //Message outMessage=generateMessage(inHeaders, obj);

        List<Message> outMessages = new ArrayList<Message>();
        for (Object workflowActionResponse : objectList)
        {
            List actionResponses = (List) workflowActionResponse;

            for (Object actionResponse : actionResponses)
            {
                if (actionResponse instanceof ESPStageScriptFilter)
                {
                    Message<ESPStageScriptFilter> outMessage = messageGenerator.<ESPStageScriptFilter>generateMessage(inHeaders, (ESPStageScriptFilter) actionResponse);
                    outMessages.add(outMessage);
                }
                
                if (actionResponse instanceof StageScriptAlert)
                {
                    Message<StageScriptAlert> outMessage = messageGenerator.<StageScriptAlert>generateMessage(inHeaders, (StageScriptAlert) actionResponse,EspConstant.STAGE_SCRIPT_ALERT);
                    outMessages.add(outMessage);
                }
            }
        }

        logger.debug("<********************************************************<");
        return outMessages;
    }

    
    
    /*
     * private Message<StageScriptAlert> generateMessage(MessageHeaders headers, StageScriptAlert sourceData)
    {
        logger.debug("message body : " + sourceData);

        return MessageBuilder.withPayload(sourceData)
                .copyHeaders(headers)
                .build();
    }*/

    public String getEspScope()
    {
        return espScope;
    }

    public void setEspScope(String espScope)
    {
        this.espScope = espScope;
    }
    
    
    
}
