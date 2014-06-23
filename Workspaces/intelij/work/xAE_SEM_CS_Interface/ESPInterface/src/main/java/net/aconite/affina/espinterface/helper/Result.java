/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.helper;

import net.aconite.affina.espinterface.exceptions.ScriptProcessingRuntimeException;

/**
 * Class to encapsulate generic task results which stores a message, success flag and any exceptions.
 * 
 * @author thushara.pethiyagoda
 */
public class Result<T>
{
    private boolean isSuccessFul;
    private ScriptProcessingRuntimeException exception;
    private String msg;
    private String trackingRef;
    private String errorCode;
    private T dataObject;
    private String errorData;
    
    /**
     * 
     * @param success
     * @param ex
     * @param additionalMsg 
     */
    private Result(boolean success, ScriptProcessingRuntimeException ex, 
                                    String friendlyMessage, String errCode, String trackingId)
    {
        isSuccessFul = success;
        exception = ex;
        msg = friendlyMessage;
        trackingRef = trackingId;        
        errorCode = errCode;
    }
    
    private Result(boolean success, ScriptProcessingRuntimeException ex, 
                                    String friendlyMessage, String errCode, 
                                    String trackingId, T errorData)
    {
        isSuccessFul = success;
        exception = ex;
        msg = friendlyMessage;
        trackingRef = trackingId;        
        errorCode = errCode;
        dataObject = errorData;
    }
    
    private Result(boolean success, T data)
    {
        isSuccessFul = success;
        dataObject = data;
    }
    
    /**
     * 
     * @param success
     * @param ex
     * @param friendlyMessage
     * @return 
     */
    public static Result getInstance(boolean success, ScriptProcessingRuntimeException ex, 
                                                      String friendlyMessage, String errCode, String trackingId)
    {
        return new Result(success, ex, friendlyMessage, errCode,trackingId);
    }
    public static <T>Result<T> getInstance(boolean success, ScriptProcessingRuntimeException ex, 
                                                      String friendlyMessage, 
                                                      String errCode, String trackingId, T data)
    {
        return new Result<T>(success, ex, friendlyMessage, errCode,trackingId, data);
    }
    
    public static <T> Result<T> getInstance(boolean success, T data)
    {
        return new Result<T>(success, data);
    }  
    
    public T getData()
    {
        return dataObject;
    }
    
    /**
     * Retruns true or false indicating success or failure of the operation.
     * @return 
     */
    public boolean isSuccessFul()
    {
        return isSuccessFul;
    }
    /**
     * 
     * @return 
     */
    public String getErrorCode()
    {
        return errorCode;
    }
    /**
     * Returns the Exception if any that is part of this result.
     * Can be null if this is a successful result.
     * @return Exception if any related to this Result.
     */
    public ScriptProcessingRuntimeException getException()
    {
        return exception;
    }
    /**
     * Returns a meaningful message defined for this result.
     * This may come from a properties file or a programmer defined message attached to a certain business function.
     * @return 
     */
    public String getFriendlyMessage()
    {
        return  msg == null ? "" : msg;
    }
         
    /**
     * The message attached to an Exception.
     * This could be if this result is been generated as a result of an exception thrown by a previous method then
     * this would return that message found within that exception.
     * 
     * @return 
     */
    public String getExceptionMessage()
    {
        return getException() != null ? getException().getMessage() : "";
    }

    /**
     * returns any specific data that should encapsulate this Result.
     * @return T the object representing any result related data.
     */
    public ResponseData getResultData()
    {
        ResponseData scriptStatusResponse = new ResponseData(trackingRef, isSuccessFul(), getErrorCode(), 
        		getExceptionMessage(), errorData == null ? "" : errorData);
        
        return scriptStatusResponse;
    }
    
    public void setErrorData(String errData)
    {
        errorData = errData;
    }
    /**
     * 
     * @return 
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(isSuccessFul());
        sb.append(getFriendlyMessage());
        if(getException() != null)
        {
            sb.append(getException().getMessage());
        }
        return sb.toString();
    }            
}
