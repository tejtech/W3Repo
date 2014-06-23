/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.handler.message;

import java.util.*;
import net.acointe.affina.esp.*;
import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.handler.feedback.*;
import net.aconite.affina.espinterface.xmlmapping.sem.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.MessagingException;
import org.springframework.integration.jms.*;
import org.springframework.integration.support.*;
import org.springframework.ui.velocity.*;

/**
 * @author wakkir.muzammil
 */
public class EspSSUAlarmMessageHandler implements IEspFeedbackHandler
{

    private static final Logger logger = LoggerFactory.getLogger(EspSSUAlarmMessageHandler.class.getName());
    private EspFeedbackHeader espFeedbackHeader;

    public EspSSUAlarmMessageHandler(EspFeedbackHeader espFeedbackHeader)
    {
        this.espFeedbackHeader = espFeedbackHeader;
    }

    @ServiceActivator
    public Message process(Message inMessage)
    {
        Message outMessage = processProgressMessage(inMessage);
        return outMessage;
    }

    private Message processProgressMessage(Message inMessage)
    {
        Hashtable<String, Object> props = new Hashtable<String, Object>();
        boolean isError = false;
        StringBuilder sb = new StringBuilder();
        MessageHeaders inHeaders = inMessage.getHeaders();
        Object inPayload = inMessage.getPayload();

        ScriptStatusResponse res = (ScriptStatusResponse) inPayload;
        logger.debug("process : Incoming Message header: {}", inHeaders);
        logger.debug("process : Message payload: {}", inPayload);

        if (res.getStatus() == StatusType.STATUS_OK && res.getError() != null)
        {
            res.setStatus(StatusType.ERROR);
            res.getError().setData("ALARM");
        }        
        return inMessage;
    }

    public void endProcess(Message<MessagingException> message)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
