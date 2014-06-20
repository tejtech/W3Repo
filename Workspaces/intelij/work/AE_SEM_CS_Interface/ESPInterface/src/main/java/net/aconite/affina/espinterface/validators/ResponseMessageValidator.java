/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.validators;

import com.platform7.pma.request.emvscriptrequest.ESPCardSetup;

import com.platform7.standardinfrastructure.multiissuer.Scope;
import net.aconite.affina.espinterface.builder.MessageContent;
import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.exceptions.EspMessageTransformationException;
import net.aconite.affina.espinterface.exceptions.EspResponseValidationException;
import net.aconite.affina.espinterface.persistence.GenericPersistentDAO;
import net.aconite.affina.espinterface.xmlmapping.sem.ErrorType;
import net.aconite.affina.espinterface.xmlmapping.sem.StatusType;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author wakkir.muzammil
 */
public class ResponseMessageValidator implements IResponseValidator
{
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
            messageContent.setIsValid(false);
            messageContent.getError().setDescription(errorType.getDescription());
            messageContent.getError().setErrorCode(errorType.getErrorCode());
            messageContent.getError().setData(errorType.getData());
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

        ESPCardSetup cardDetails = null;

        try
        {
            UnitOfWork uow = GenericPersistentDAO.instance().getUnitOfWork();
            Scope scope = Scope.locate(uow, messageContent.getScopeName());

            if(EspConstant.CARD_SETUP_RESPONSE.equals(type))
            {
                cardDetails = ESPCardSetup.locateByScopeAndTRN(uow, scope, semTrackId);
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

        if (cardDetails == null)
        {
            printResponse(type,semTrackId,statusType.value(),errorType);

            messageContent.setIsValid(false);
            messageContent.setError(new ErrorType());
            messageContent.getError().setDescription("Invalid Tracking reference received ");
            messageContent.getError().setErrorCode("");
            messageContent.getError().setData("");
        }
        else
        {
             messageContent.setIsValid(true);
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
            logger.error("Message with errors : "+ sb.toString());
        }
        else
        {
            logger.debug("Message without errors : "+ sb.toString());
        }
    }

}

