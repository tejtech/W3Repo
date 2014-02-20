/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.validators;

import com.platform7.pma.request.emvscriptrequest.ESPCardSetup;
import java.util.Vector;
import net.aconite.affina.espinterface.builder.MessageContent;
import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.exceptions.EspMessageTransformationException;
import net.aconite.affina.espinterface.exceptions.EspResponseValidationException;
import net.aconite.affina.espinterface.persistence.GenericPersistentDAO;
import net.aconite.affina.espinterface.persistence.Persistent;
import net.aconite.affina.espinterface.xmlmapping.sem.ErrorType;
import net.aconite.affina.espinterface.xmlmapping.sem.StatusType;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author wakkir.muzammil
 */
public class ResponseMessageValidator  implements IResponseValidator
{
    private static final Logger logger = LoggerFactory.getLogger(ResponseMessageValidator.class.getName());


    public MessageContent validate(MessageContent messageContent) throws EspResponseValidationException
    {
        String type=messageContent.getType();
        String semTrackId=messageContent.getTrackingReference();
        StatusType statusType=messageContent.getStatus();
        ErrorType errorType=messageContent.getError();

        if (statusType!=null && StatusType.STATUS_OK.value().equalsIgnoreCase(statusType.value()))
        {
            messageContent=validateData(messageContent);
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
        return messageContent;
    }
    
    private MessageContent validateData(MessageContent messageContent)
    {
        String type=messageContent.getType();
        String semTrackId=messageContent.getTrackingReference();
        StatusType statusType=messageContent.getStatus();
        ErrorType errorType=messageContent.getError();
        
        Vector myList=null;
        try
        {
            Persistent myDAO = GenericPersistentDAO.getPersistent();
            
            ExpressionBuilder expBuilder=new ExpressionBuilder();
            Expression appDetailExp = expBuilder.get(EspConstant.DB_APP_DETAILS).get(EspConstant.DB_SCOPE).get(EspConstant.DB_SCOPE_NAME).equal(messageContent.getScopeName());
            
            if(EspConstant.CARD_SETUP_RESPONSE.equals(type))
            {
                Expression trackIdExp = expBuilder.get(EspConstant.DB_ESP_TRACKING_ID).equal(semTrackId);
                Expression scopeExp = expBuilder.get(EspConstant.DB_SCOPE).get(EspConstant.DB_SCOPE_NAME).equal(messageContent.getScopeName());
                Expression compoundExp=trackIdExp.and(scopeExp.and(appDetailExp));
                myList = myDAO.executeReadQuery(compoundExp,ESPCardSetup.class, null, (String[])null);                
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

        if (myList == null || myList.isEmpty())
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

