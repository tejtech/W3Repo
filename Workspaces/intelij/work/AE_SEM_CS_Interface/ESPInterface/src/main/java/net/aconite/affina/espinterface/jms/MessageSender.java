/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.jms;

import com.platform7.standardinfrastructure.appconfig.AppConfig;
import net.aconite.common.util.MessageSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wakkir.muzammil
 */
public class MessageSender 
{
    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class.getName());

    private static MessageSenderService stgReqSender = (MessageSenderService)AppConfig.getBean("stgReqSender");
    
    public static void sendStageScriptAlertMessage(String trackId) 
    {        
        logger.debug(" sendStageScriptMessage()");
        String stageScriptMsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><StageScriptAlert><TrackingReference>"+trackId+"</TrackingReference></StageScriptAlert>";
        logger.debug(" StageScript Msg : {}" , stageScriptMsg);
        stgReqSender.sendMessage(stageScriptMsg, "StageScriptAlert");
        logger.debug(" message sent");


    }
    
}
