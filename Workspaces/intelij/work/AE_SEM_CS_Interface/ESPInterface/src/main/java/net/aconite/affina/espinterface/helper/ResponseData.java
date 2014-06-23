/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.helper;

import net.aconite.affina.espinterface.xmlmapping.sem.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class ResponseData
{
    private ScriptStatusResponse scriptStatusResponse = new ScriptStatusResponse();
    /** 
     * @param trackingRef
     * @param error
     * @param status 
     */
    public ResponseData(String trackingRef, boolean isSuccessFul, String errCode, String errDescription, String errData)
    {        
        scriptStatusResponse.setTrackingReference(trackingRef);
        StatusType status = isSuccessFul ? StatusType.STATUS_OK : StatusType.ERROR;
        scriptStatusResponse.setStatus(status);
        ErrorType error = new ErrorType();
        if(!isSuccessFul)
        {   
            error.setData(errData);
            error.setDescription(errDescription);
            error.setErrorCode(errCode);       
            scriptStatusResponse.setError(error);
        }        
    }
    
    public ResponseData(String trackingRef, boolean isSuccessFul,
                                            String errCode, String errDescription, String errData,
                                            boolean isBusinessError)
    {        
        scriptStatusResponse.setTrackingReference(trackingRef);
        StatusType status = isSuccessFul ? StatusType.STATUS_OK : StatusType.ERROR;
        scriptStatusResponse.setStatus(status);
        ErrorType error = new ErrorType();           
        error.setData(errData);
        error.setDescription(errDescription);
        error.setErrorCode(errCode);       
        scriptStatusResponse.setError(error);               
    }
    /**
     * 
     * @return 
     */
    public ScriptStatusResponse getScriptStatusResponse()
    {
        return scriptStatusResponse;
    }
}
