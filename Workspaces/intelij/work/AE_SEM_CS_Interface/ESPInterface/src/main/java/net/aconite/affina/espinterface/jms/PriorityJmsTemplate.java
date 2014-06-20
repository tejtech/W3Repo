/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.jms;

//https://jira.springsource.org/browse/INT-3259

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author wakkir.muzammil
 */
public class PriorityJmsTemplate extends JmsTemplate 
{
	private static final Logger logger = LoggerFactory.getLogger(PriorityJmsTemplate.class.getName());

	@Override
	protected void doSend(MessageProducer producer, Message message) throws JMSException 
        {
		if (isExplicitQosEnabled()) 
                {
			int priority;
			try 
                        {
				priority = message.getJMSPriority();
			}
			catch (javax.jms.JMSException e) 
                        {
				logger.warn("message.getJMSPriority() did not work.", e);
				priority = getPriority();
			}
			producer.send(message, getDeliveryMode(), priority, getTimeToLive());
		}
		else 
                {
			producer.send(message);
		}
	}

}