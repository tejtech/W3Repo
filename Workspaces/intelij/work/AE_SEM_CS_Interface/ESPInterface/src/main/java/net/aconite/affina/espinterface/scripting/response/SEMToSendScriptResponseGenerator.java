/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.scripting.response;

import com.platform7.pma.request.emvscriptrequest.*;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import java.math.*;
import net.aconite.affina.espinterface.helper.*;
import net.aconite.affina.espinterface.model.AbstractModel;
import net.aconite.affina.espinterface.model.StageScriptDetail;
import net.aconite.affina.espinterface.xmlmapping.cs.RejectionType;
import net.aconite.affina.espinterface.xmlmapping.cs.SendScriptResponse;
import net.aconite.affina.espinterface.xmlmapping.cs.TrackingReferenceType;
import net.aconite.affina.espinterface.xmlmapping.sem.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class SEMToSendScriptResponseGenerator extends ResponseGenerator<SendScriptResponse, StageScriptDetail>
{

    /**
     * Default Constructor
     */
    public SEMToSendScriptResponseGenerator()
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
    public SendScriptResponse generateResponse(Result<StageScriptDetail> result)
    {
        SendScriptResponse sendScriptResponse  = generateGenericResponse(result);
        if (StatusType.ERROR.value().equals(result.getStatus()))        
        {
            sendScriptResponse = generateGenericResponse(result);
            RejectionType rt = new RejectionType();

            rt.setDateAndTime(DateHelper.getGeorgianDate());
            rt.setDescription(!DataUtil.isEmpty(result.getErrorData()) ? result.getErrorData() + ": " + result.getFriendlyMessage(): result.getFriendlyMessage());
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
    private SendScriptResponse generateGenericResponse(Result<StageScriptDetail> result)
    {
        SendScriptResponse sendScriptResponse = new SendScriptResponse();
        TrackingReferenceType trt = new TrackingReferenceType();
        trt.setId(result.getTrackingReference());
        StageScriptDetail model = result.getData();
        if(result.getData() != null)
        {
            ESPStageScriptDetail espSSDetail = model.getParent();//<ESPStageScriptDetail>getProperty(StageScriptDetail.FIELD_PARENT);
            if(espSSDetail != null)
            {
                String scope = model.getScope();
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
