/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.handler.feedback;

import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.xmlmapping.sem.CardSetupResponse;
import net.aconite.affina.espinterface.xmlmapping.sem.StageScriptResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.Hashtable;
import net.acointe.affina.esp.AffinaEspUtils;
import net.aconite.affina.espinterface.workflow.model.ConfigImportAlert;
import net.aconite.affina.espinterface.xmlmapping.sem.*;
import org.springframework.integration.MessagingException;
import org.springframework.integration.jms.JmsHeaders;

/**
 * @author wakkir.muzammil
 */
public class EspProgressHandler implements IEspFeedbackHandler
{
    private static final Logger logger = LoggerFactory.getLogger(EspProgressHandler.class.getName());

    private EspFeedbackHeader espFeedbackHeader;

    public EspProgressHandler(EspFeedbackHeader espFeedbackHeader)
    {
        this.espFeedbackHeader = espFeedbackHeader;
    }

    @ServiceActivator
    public Message process(Message<MessagingException> inMessage)
    {
         Message outMessage = processProgressMessage(inMessage);
         return outMessage;
    }

    @ServiceActivator
    public void endProcess(Message inMessage)
    {
        Message outMessage = processProgressMessage(inMessage);
    }


    private Message processProgressMessage(Message inMessage)
    {
        MessageHeaders inHeaders = inMessage.getHeaders();
        Object inPayload = inMessage.getPayload();

        logger.debug("process : Incoming Message header: {}", inHeaders);
        logger.debug("process : Message payload: {}", inPayload);

        EspPayload msgPayload = buildProgressPayload(inHeaders,inPayload);

        logger.debug("Progress Message: {}", msgPayload);

        Message outMessage = generateProgressMessage(inHeaders, msgPayload);

        return outMessage;
    }


    private Message<String> generateProgressMessage(MessageHeaders headers, EspPayload msgPayload)
    {
        String message="";
        boolean showValidationError=false;
        boolean showProgressMessage=false;
        if(msgPayload.isError())
        {
            message=msgPayload.getMessageInOneLineWithHeader();
            boolean showProgressForSpecialError = msgPayload.getMessage().indexOf("ALARM") > -1;
            showValidationError=(showProgressForSpecialError && 
                                espFeedbackHeader.isShowWarningMessages(String.valueOf(headers.get(JmsHeaders.TYPE)))) || 
                                espFeedbackHeader.isShowValidationError(String.valueOf(headers.get(JmsHeaders.TYPE)));
            message = message.replaceAll("ALARM", "");
            logger.error(msgPayload.getMessageBody().replaceAll("ALARM", ""));
        }
        else
        {
            message=msgPayload.getMessageWithHeader();
            showProgressMessage=espFeedbackHeader.isShowProgressMessage(String.valueOf(headers.get(JmsHeaders.TYPE)));
            logger.debug(msgPayload.getMessageBody());
        }

        return MessageBuilder.withPayload(message.replaceAll("ALARM", ""))
                .copyHeaders(headers)
                .setHeader(JmsHeaders.TYPE, headers.get(JmsHeaders.TYPE))
                .setHeader(EspConstant.MQ_ESP_ERROR, msgPayload.isError())
                .setHeader(EspConstant.MQ_SHOW_VALIDATION_ERROR, showValidationError)
                .setHeader(EspConstant.MQ_SHOW_PROGRESS_MESSAGE, showProgressMessage)
                .build();
    }

    private EspPayload buildProgressPayload(MessageHeaders headers,Object inPayload)
    {
        Hashtable<String, Object> props = new Hashtable<String, Object>();
        StringBuilder sb = new StringBuilder();

        boolean isError=false;

        if (inPayload instanceof StageScriptResponse)
        {
            StageScriptResponse res = (StageScriptResponse) inPayload;
            props.put(EspConstant.VT_RESPONSE_TYPE, EspConstant.STAGE_SCRIPT_RESPONSE);
            props.put(EspConstant.VT_TRACKING_REFERENCE, AffinaEspUtils.getEmptyIfNull(res.getTrackingReference()));
            props.put(EspConstant.VT_STATUS, AffinaEspUtils.getEmptyIfNull(res.getStatus().name()));
            if (res.getError() != null)
            {
                props.put(EspConstant.VT_ERROR_DATA, AffinaEspUtils.getEmptyIfNull(res.getError().getData()));
                props.put(EspConstant.VT_ERROR_DESCRIPTION, AffinaEspUtils.getEmptyIfNull(res.getError().getDescription()));
                props.put(EspConstant.VT_ERROR_CODE, AffinaEspUtils.getEmptyIfNull(res.getError().getErrorCode()));
                isError=true;
            }
            else
            {
                isError=false;
                logger.info("Received stage script response with tracking reference {}",res.getTrackingReference());
            }
            sb.append(VelocityEngineUtils.mergeTemplateIntoString(espFeedbackHeader.getVelocityEngine(), "/stageScriptResponse.vm", espFeedbackHeader.getEspMessageEncoding(), props));
        }
        else if (inPayload instanceof CardSetupResponse)
        {
            CardSetupResponse res = (CardSetupResponse) inPayload;
            props.put(EspConstant.VT_RESPONSE_TYPE, EspConstant.CARD_SETUP_RESPONSE);
            props.put(EspConstant.VT_TRACKING_REFERENCE, AffinaEspUtils.getEmptyIfNull(res.getTrackingReference()));
            props.put(EspConstant.VT_STATUS, AffinaEspUtils.getEmptyIfNull(res.getStatus().name()));
            if (res.getError() != null)
            {
                props.put(EspConstant.VT_ERROR_DATA, AffinaEspUtils.getEmptyIfNull(res.getError().getData()));
                props.put(EspConstant.VT_ERROR_DESCRIPTION, AffinaEspUtils.getEmptyIfNull(res.getError().getDescription()));
                props.put(EspConstant.VT_ERROR_CODE, AffinaEspUtils.getEmptyIfNull(res.getError().getErrorCode()));
                isError=true;
            }
            else
            {
                isError=false;
                logger.info("Received card setup response with tracking reference {}",res.getTrackingReference());
            }
            sb.append(VelocityEngineUtils.mergeTemplateIntoString(espFeedbackHeader.getVelocityEngine(), "/cardSetupResponse.vm", espFeedbackHeader.getEspMessageEncoding(), props));

        }
        else if (inPayload instanceof ScriptStatusResponse)
        {
            ScriptStatusResponse res = (ScriptStatusResponse) inPayload;
            props.put(EspConstant.VT_RESPONSE_TYPE, EspConstant.SCRIPT_STATUS_RESPONSE);
            props.put(EspConstant.VT_TRACKING_REFERENCE, AffinaEspUtils.getEmptyIfNull(res.getTrackingReference()));
            props.put(EspConstant.VT_STATUS, AffinaEspUtils.getEmptyIfNull(res.getStatus().name()));
            if (res.getStatus() == StatusType.ERROR && res.getError() != null)
            {
                    props.put(EspConstant.VT_ERROR_DATA, AffinaEspUtils.getEmptyIfNull(res.getError().getData()));
                    props.put(EspConstant.VT_ERROR_DESCRIPTION, AffinaEspUtils.getEmptyIfNull(res.getError().getDescription()));
                    props.put(EspConstant.VT_ERROR_CODE, AffinaEspUtils.getEmptyIfNull(res.getError().getErrorCode()));
                    isError=true;
                }
            else
            {
                isError=false;
                logger.info("Sent script update status response with tracking reference {}",res.getTrackingReference());
            }
            sb.append(VelocityEngineUtils.mergeTemplateIntoString(espFeedbackHeader.getVelocityEngine(), "/scriptStatusResponse.vm",espFeedbackHeader.getEspMessageEncoding(), props));
        }
        else if (inPayload instanceof CardSetupRequest)
        {
            CardSetupRequest req = (CardSetupRequest) inPayload;
            props.put(EspConstant.VT_RESPONSE_TYPE, EspConstant.CARD_SETUP_REQUEST);
            props.put(EspConstant.VT_TRACKING_REFERENCE, AffinaEspUtils.getEmptyIfNull(req.getTrackingReference()));
            props.put(EspConstant.VT_STATUS, "Cardsetup request sent to SEM");
            logger.info("Sent card setup request with tracking reference {}",req.getTrackingReference());
            sb.append(VelocityEngineUtils.mergeTemplateIntoString(espFeedbackHeader.getVelocityEngine(), "/cardSetupRequest.vm", espFeedbackHeader.getEspMessageEncoding(), props));

        }
        else if (inPayload instanceof StageScriptRequest)
        {
            StageScriptRequest req = (StageScriptRequest) inPayload;
            props.put(EspConstant.VT_RESPONSE_TYPE, EspConstant.SCRIPT_STATUS_RESPONSE);
            props.put(EspConstant.VT_TRACKING_REFERENCE, AffinaEspUtils.getEmptyIfNull(req.getTrackingReference()));
            props.put(EspConstant.VT_STATUS, "StageScript request sent to SEM");
            logger.info("Sent stage script request with tracking reference {}",req.getTrackingReference());
            sb.append(VelocityEngineUtils.mergeTemplateIntoString(espFeedbackHeader.getVelocityEngine(), "/stageScriptRequest.vm", espFeedbackHeader.getEspMessageEncoding(), props));
        }
        else if (inPayload instanceof ConfigImportAlert)
        {
            ConfigImportAlert payload = (ConfigImportAlert) inPayload;
            props.put(EspConstant.VT_ALERT_TYPE, EspConstant.CONFIG_IMPORT_ALERT);
            
            if (payload.isError())
            {   
                props.put(EspConstant.VT_STATUS,EspConstant.ERROR);
                props.put(EspConstant.VT_ERROR_DESCRIPTION, AffinaEspUtils.getEmptyIfNull(payload.getMessage())); 
                props.put(EspConstant.VT_ERROR_CODE, AffinaEspUtils.getEmptyIfNull(payload.getErrorCode()));
                isError=true;
            }
            else
            {                
                props.put(EspConstant.VT_WARNING_DESCRIPTION, AffinaEspUtils.getEmptyIfNull(payload.getMessage())); 
                isError=false;
                logger.info("Received config import alert: {}",payload.getMessage());
            }
            sb.append(VelocityEngineUtils.mergeTemplateIntoString(espFeedbackHeader.getVelocityEngine(), "/configImportAlert.vm", espFeedbackHeader.getEspMessageEncoding(), props));

        }
        else
        {
            sb.append(inPayload.toString());
        }

        EspPayload espPayload=new EspPayload(espFeedbackHeader.generateMessageHeader(),sb.toString(),isError);

        return espPayload;
    }

}
