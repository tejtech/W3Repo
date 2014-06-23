/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.validators;

import com.platform7.pma.request.emvscriptrequest.ESPCardSetup;

import java.util.List;
import net.aconite.affina.espinterface.builder.MessageContent;
import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.dao.CardSetupDetailDao;
import net.aconite.affina.espinterface.exceptions.EspMessageTransformationException;
import net.aconite.affina.espinterface.exceptions.EspResponseValidationException;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.xmlmapping.sem.ErrorType;
import net.aconite.affina.espinterface.xmlmapping.sem.StatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author wakkir.muzammil
 */
public class ResponseMessageValidator implements IResponseValidator
{
    private CardSetupDetailDao cardSetupDetailDao=new CardSetupDetailDao();
    
    private static final Logger logger = LoggerFactory.getLogger(ResponseMessageValidator.class.getName());

    public MessageContent validate(final MessageContent messageContent) throws EspResponseValidationException
    {
        String type=messageContent.getType();
        String semTrackId=messageContent.getTrackingReference();
        StatusType statusType=messageContent.getStatus();
        ErrorType errorType=messageContent.getError();
        MessageContent response = messageContent;

        if (statusType!=null && StatusType.STATUS_OK.value().equalsIgnoreCase(statusType.value()))
        {
            response=validateData(messageContent);
        }
        else if (statusType!=null && StatusType.ERROR.value().equalsIgnoreCase(statusType.value()))
        {
            response=validateData(messageContent);
            if(response.isValid())
            {
                messageContent.setIsValid(false);
                messageContent.getError().setDescription(errorType.getDescription());
                messageContent.getError().setErrorCode(errorType.getErrorCode());
                messageContent.getError().setData(errorType.getData());
            }
        }
        else
        {
            printResponse(type,semTrackId,statusType.value(),errorType);

            messageContent.setIsValid(false);
            messageContent.setError(new ErrorType());
            messageContent.getError().setDescription("Invalid status code received");
            messageContent.getError().setErrorCode("");
        }
        return response;
    }

    private MessageContent validateData(MessageContent messageContent)
    {
        String type=messageContent.getType();
        String semTrackId=messageContent.getTrackingReference();
        StatusType statusType=messageContent.getStatus();
        ErrorType errorType=messageContent.getError();

        List<ESPCardSetup> espCardSetups = null;

        try
        {            
            if(EspConstant.CARD_SETUP_RESPONSE.equals(type))
            {
                FilterCriteria filterObject=new FilterCriteria();
                filterObject.setScopeName( messageContent.getScopeName());
                filterObject.setSemTrackId(semTrackId);
                espCardSetups=cardSetupDetailDao.getList(filterObject, null);                
                
            }
            else if(EspConstant.STAGE_SCRIPT_RESPONSE.equals(type))
            {
                //do db validation here
            }
        }
        catch (Exception ex)
        {
            throw new EspMessageTransformationException(ex.getMessage(), ex);
        }

        if (espCardSetups == null || espCardSetups.isEmpty())
        {
            printResponse(type,semTrackId,statusType.value(),errorType);

            messageContent.setIsValid(false);
            messageContent.setError(new ErrorType());
            messageContent.getError().setDescription("Tracking reference in cardSetup response cannot be validated in Affina");
            messageContent.getError().setErrorCode("");
            messageContent.getError().setData("");
        }
        else
        {
             messageContent.setIsValid(true);
             ESPCardSetup espCardSetup=cardSetupDetailDao.delete(espCardSetups.get(0).getPrimaryKey());       
             logger.debug("CardSetup record removed : {}",espCardSetup);
        }

        return messageContent;
    }


    private void printResponse(String type,String trackId,String statusType,ErrorType error)
    {
        StringBuffer sb=new StringBuffer();
        sb.append("\nType : "+type);
        sb.append("\nTrackId : "+trackId);
        if(statusType!=null)
        {
            sb.append("\nStatus : "+statusType);
        }
        if(error!=null)
        {
            sb.append("\nError : "+error.getDescription());
            sb.append("\nCode : "+error.getErrorCode());
            sb.append("\nData : "+error.getData());
        }
        sb.append("\n");
        if(error!=null)
        {
            logger.error("Message with errors : {}", sb.toString());
        }
        else
        {
            logger.debug("Message without errors : {}", sb.toString());
        }
    }

}

