package net.aconite.affina.espinterface.workflow.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.ServiceActivator;

/**
 * User: wakkir
 * Date: 08/03/14
 * Time: 03:01
 */
public class WorkflowEndProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(WorkflowEndProcessor.class.getName());
    
    private String espScope;
    
    @ServiceActivator
    public void process(Message inMessage)
    {
        // At info level, the data recorded shall be limited to the message type
        //   and its identifier (tracking reference or service instance).
        // At debug level, the complete message shall be recorded.

        MessageHeaders inHeaders = inMessage.getHeaders();
        Object inPayload = inMessage.getPayload();

        
        logger.debug("EndProcessor processing...{}",inPayload.toString());



    }
    
    public String getEspScope()
    {
        return espScope;
    }

    public void setEspScope(String espScope)
    {
        this.espScope = espScope;
    }
}
