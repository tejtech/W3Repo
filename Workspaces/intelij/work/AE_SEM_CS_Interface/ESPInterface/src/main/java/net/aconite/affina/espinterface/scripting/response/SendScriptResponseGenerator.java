/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.scripting.response;

import com.platform7.pma.card.SoftCard;
import java.math.*;
import net.aconite.affina.espinterface.helper.*;
import net.aconite.affina.espinterface.model.*;
import net.aconite.affina.espinterface.xmlmapping.cs.RejectionType;
import net.aconite.affina.espinterface.xmlmapping.cs.SendScriptResponse;
import net.aconite.affina.espinterface.xmlmapping.cs.TrackingReferenceType;

/**
 *
 * @author thushara.pethiyagoda
 */
public class SendScriptResponseGenerator extends ResponseGenerator<SendScriptResponse, ScriptableCard>
{

    /**
     * Default Constructor
     */
    public SendScriptResponseGenerator()
    {
    }

    /**
     * Generates Response for send script request.
     * <p/>
     * @param <T>
     * @param <R>
     * @param result
     * <p/>
     * @return
     */
    @Override
    public SendScriptResponse generateResponse(Result<ScriptableCard> result)
    {
        SendScriptResponse sendScriptResponse  = generateGenericResponse(result);;
        if (!result.isSuccessFul())        
        {
            sendScriptResponse = generateGenericResponse(result);
            RejectionType rt = new RejectionType();

            rt.setDateAndTime(DateHelper.getGeorgianDate());
            rt.setDescription(result.getErrorMessage());
            rt.setErrorCode(BigInteger.valueOf(Long.parseLong(result.getErrorCode())));

            sendScriptResponse.setRejection(rt);
        }
        return sendScriptResponse;
    }

    /**
     * Generates Response, if scope cannot be extracted then set it to blank.
     * @param result
     * @return 
     */
    private SendScriptResponse generateGenericResponse(Result<ScriptableCard> result)
    {
        SendScriptResponse sendScriptResponse = new SendScriptResponse();
        TrackingReferenceType trt = new TrackingReferenceType();
        trt.setId(result.getTrackingReference());
        ScriptableCard model = result.getData();
        if(result.getData() != null)
        {
            SoftCard sc = model.getSoftCard();
            if(sc != null)
            {
                String scope = sc.getArea().getScope().getName();
                trt.setScope(scope);
            }
            else
            {
                trt.setScope("");
            }
        }
        sendScriptResponse.setTrackingReference(trt);
        
        return sendScriptResponse;
    }
    
}
