package net.aconite.affina.espinterface.workflow.process;

import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.webservice.restful.service.IScopeService;
import net.aconite.affina.espinterface.workflow.model.ConfigImportAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.jms.JmsHeaders;
import org.springframework.integration.support.MessageBuilder;

/**
 * User: wakkir
 * Date: 08/03/14
 * Time: 03:01
 */
public class CheckImportProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(CheckImportProcessor.class.getName());
    
    private String espScope;
    
    @Autowired        
    private IScopeService scopeService;
    
    @ServiceActivator
    public Message process(Message inMessage)
    {
        // At info level, the data recorded shall be limited to the message type
        //   and its identifier (tracking reference or service instance).
        // At debug level, the complete message shall be recorded.

        MessageHeaders inHeaders = inMessage.getHeaders();
        Object inPayload = inMessage.getPayload();

        
        logger.debug("EndProcessor processing...{}",inPayload.toString());
        
        ConfigImportAlert outPayload=new ConfigImportAlert();
        String message="";
        
        if(scopeService.getTotalCount(null)>0)
        {            
            outPayload.setIsAnyScopeNotExist(false);
            
            if(scopeService.getByName(espScope)!=null)
            {
                outPayload.setIsCurrentScopeNotExist(false);
                outPayload.setMessage("Scope "+espScope+" exist"); 
            }
            else
            {
                message="'"+espScope+"' scope doesn't exist in current configuration";
                outPayload.setMessage(message);                
                outPayload.setIsError(true);
                outPayload.setIsCurrentScopeNotExist(true);
            }
        }   
        else
        {
            message="There are no configuration data found. This container is looking for '"+espScope+"' scope";
            outPayload.setMessage(message);            
            outPayload.setIsError(false);
            outPayload.setIsAnyScopeNotExist(true);
        }
        
        
        Message outMessage = generateMessage(inHeaders, outPayload);
        
        return outMessage;

    }
    
    
    private Message<ConfigImportAlert> generateMessage(MessageHeaders headers, ConfigImportAlert sourceData)
    {
        //logger.debug("Created ConfigImportAlert Message : "+sourceData);

        return MessageBuilder.withPayload(sourceData)
                .copyHeaders(headers)
                .setHeader(JmsHeaders.TYPE, EspConstant.CONFIG_IMPORT_ALERT)  
                .setHeader(EspConstant.MQ_NOTEXIST_ANY_SCOPES, sourceData.isAnyScopeNotExist())  
                .setHeader(EspConstant.MQ_NOTEXIST_CURRENT_SCOPE,sourceData.isCurrentScopeNotExist())
                .build();
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
