/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.scripting.response;

import net.aconite.affina.espinterface.helper.*;
import net.aconite.affina.espinterface.model.StageScriptDetail;
import net.aconite.affina.espinterface.xmlmapping.sem.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class StageScriptResponseGenerator extends ResponseGenerator<StageScriptResponse, StageScriptDetail>
{

    /**
     * Default Constructor
     */
    public StageScriptResponseGenerator()
    {
    }

    /**
     * Generates Response for send script request.
     * <p/>
     * @param <T>
     * @param <R>
     * @param result
     * <
     * p/>
     * @return
     */
    @Override
    public StageScriptResponse generateResponse(Result<StageScriptDetail> result)
    {
        StageScriptResponse stageScriptResponse = generateGenericResponse(result);
        String errMsg;
        String errCode;
        String errData;
        if (result.getStatus().compareTo(StatusType.ERROR.value()) == 0)
        {
            stageScriptResponse = generateGenericResponse(result);
            if (result.hasException())
            {
                errMsg = result.getException().getMessage();
                errCode = result.getException().getErrorCode();
                errData = result.getErrorData();
            }
            else
            {
                errMsg = result.getFriendlyMessage();
                errCode = result.getErrorCode();
                errData = result.getErrorData();
            }
            ErrorType et = new ErrorType();
            et.setDescription(errMsg);
            et.setErrorCode(errCode);
            et.setData(errData);

            stageScriptResponse.setError(et);
        }
        return stageScriptResponse;
    }

    /**
     * Generates Response, if scope cannot be extracted then set it to blank.
     * <p/>
     * @param result
     * <p/>
     * @return
     */
    private StageScriptResponse generateGenericResponse(Result<StageScriptDetail> result)
    {
        StageScriptResponse stageScriptResponse = new StageScriptResponse();
        stageScriptResponse.setTrackingReference(result.getTrackingReference());
        stageScriptResponse.setStatus(StatusType.fromValue(result.getStatus()));
        return stageScriptResponse;
    }
}
