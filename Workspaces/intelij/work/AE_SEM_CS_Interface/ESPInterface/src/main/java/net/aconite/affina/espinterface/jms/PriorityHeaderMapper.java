/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.jms;
//https://jira.springsource.org/browse/INT-3259

import java.util.Map;
import net.aconite.affina.espinterface.constants.EspConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.jms.DefaultJmsHeaderMapper;

/**
 *
 * @author wakkir.muzammil
 */
public class PriorityHeaderMapper extends DefaultJmsHeaderMapper 
{
    private static final Logger logger = LoggerFactory.getLogger(PriorityHeaderMapper.class.getName());


    @Override
    public Map<String, Object> toHeaders(javax.jms.Message jmsMessage) 
    {
        Map<String, Object> headers = super.toHeaders(jmsMessage);
        try 
        {
         headers.put(EspConstant.JMS_Priority, String.valueOf(jmsMessage.getJMSPriority()) );
        }
        catch (javax.jms.JMSException e) 
        {
         logger.warn("jmsMessage.getJMSPriority() did not work.", e);
        }
        return headers;  
    }
    
    @Override
    public void fromHeaders(MessageHeaders headers, javax.jms.Message jmsMessage) 
    {
            super.fromHeaders(headers, jmsMessage);
            try 
            {
                String jmsPriority= String.valueOf(headers.get(EspConstant.JMS_Priority));
                logger.debug("jmsPriority:|{}|",jmsPriority);
                if(jmsPriority !=null && jmsPriority.trim().length()>0 && !EspConstant.NULL.equalsIgnoreCase(jmsPriority.trim()))
                {
                    int priority = Integer.parseInt(jmsPriority); // missing catch for numeric exception
                    jmsMessage.setJMSPriority(priority);
                }
                else
                {
                    logger.warn("valid jms priority is not found in the jms message");
                }
            } 
            catch (NumberFormatException e) 
            {
                    logger.warn("jms message must be integer.", e);
            }
            catch (javax.jms.JMSException e) 
            {
                    logger.warn("jmsMessage.setJMSPriority() did not work.", e);
            }
    }

}