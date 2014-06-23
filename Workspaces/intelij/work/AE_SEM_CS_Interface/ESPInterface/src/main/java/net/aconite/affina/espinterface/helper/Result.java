/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.helper;

import net.aconite.affina.espinterface.exceptions.ScriptProcessingRuntimeException;
import net.aconite.affina.espinterface.model.AbstractModel;
import net.aconite.affina.espinterface.scripting.response.ResponseGenerator;
import org.slf4j.*;

/**
 * Class to encapsulate generic task results which stores a message, success flag and any exceptions. A generic Type T
 * is defined to hold an object that can hold any helpful information that can be passed across.
 * <p/>
 * @author thushara.pethiyagoda
 */
public class Result<T extends AbstractModel>
{

    private static final Logger logger = LoggerFactory.getLogger(Result.class);
    /**
     * Field to indicate if this Result isSuccessful
     */
    private boolean isSuccessFul;
    /**
     * Exception attached to this Result
     */
    private ScriptProcessingRuntimeException exception;
    /**
     * A message linked to this Result.
     */
    private String msg;
    /**
     * Tracking reference attached to this Result.
     */
    private String trackingRef;
    /**
     * Error code attached to this Result in the case of an Error.
     */
    private String errorCode = "0";
    /**
     * And object of specified type holding information a caller can extract.
     */
    private T dataObject;
    /**
     * Any data as part of an error scenario.
     */
    private String errorData;
    /**
     * Status flag of the Result (OK or ERROR)
     */
    private String status;    
    
    private boolean enableAlarm = false;
    
    private String alarmMsg;

    private Result()
    {
    }

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

    /**
     *
     * @param success
     * @param ex
     * @param friendlyMessage
     * @param errCode
     * @param trackingId
     * @param additionalData
     */
    private Result(boolean success, ScriptProcessingRuntimeException ex,
                   String friendlyMessage, String errCode,
                   String trackingId, T additionalData)
    {
        isSuccessFul = success;
        exception = ex;
        msg = friendlyMessage;
        trackingRef = trackingId;
        errorCode = errCode;
        dataObject = additionalData;
    }

    private Result(boolean success, String statusType,
                   String friendlyMessage, String errCode, String errData,
                   String trackingId, T additionalData)
    {
        isSuccessFul = success;
        status = statusType;
        msg = friendlyMessage;
        trackingRef = trackingId;
        errorCode = errCode;
        dataObject = additionalData;
        errorData = errData;

    }

    /**
     *
     * @param success
     * @param trackingId
     * @param data
     */
    private Result(boolean success, String trackingId, T additionalData)
    {
        isSuccessFul = success;
        dataObject = additionalData;
        trackingRef = trackingId;
    }

    public Result(boolean success, ScriptProcessingRuntimeException exp,
                  String trackingId, String statusType)
    {
        isSuccessFul = success;
        trackingRef = trackingId;
        status = statusType;
        exception = exp;
    }

    /**
     *
     * @param success
     * @param data
     */
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
     * <p/>
     * @return
     */
    public static <T5 extends AbstractModel> Result<T5> getInstance(boolean success,
                                                                    ScriptProcessingRuntimeException ex,
                                                                    String friendlyMessage, String errCode,
                                                                    String trackingId)
    {
        return new Result<T5>(success, ex, friendlyMessage, errCode, trackingId);
    }

    /**
     *
     * @param <T6>
     * @param success
     * @param additionalData
     * @param status
     * @param friendlyMessage
     * @param errCode
     * @param trackingId
     * <p/>
     * @return
     */
    public static <T6 extends AbstractModel> Result<T6> getInstance(boolean success, T6 additionalData,
                                                                    String status, String friendlyMessage,
                                                                    String errCode, String errData, String trackingId)
    {
        return new Result<T6>(success, status, friendlyMessage, errCode, errData, trackingId, additionalData);
    }

    /**
     *
     * @param <T7>
     * @param success
     * @param ex
     * @param additionalData
     * @param status
     * @param friendlyMessage
     * @param errCode
     * @param errData
     * @param trackingId
     * <p/>
     * @return
     */
    public static <T7 extends AbstractModel> Result<T7> getInstance(boolean success, ScriptProcessingRuntimeException ex,
                                                                    T7 additionalData,
                                                                    String status, String friendlyMessage,
                                                                    String errCode, String errData, String trackingId)
    {
        Result<T7> result = getInstance(success, additionalData, status, friendlyMessage, errCode, errData, trackingId);
        result.exception = ex;
        return result;
    }

    /**
     *
     * @param <T>
     * @param success
     * @param ex
     * @param friendlyMessage
     * @param errCode
     * @param trackingId
     * @param additionalData
     * <p/>
     * @return
     */
    public static <T4 extends AbstractModel> Result<T4> getInstance(boolean success,
                                                                    ScriptProcessingRuntimeException ex,
                                                                    String friendlyMessage,
                                                                    String errCode, String trackingId,
                                                                    T4 additionalData)
    {
        return new Result<T4>(success, ex, friendlyMessage, errCode, trackingId, additionalData);
    }

    /**
     *
     * @param <T>
     * @param success
     * @param additionalData
     * <p/>
     * @return
     */
    public static <T1 extends AbstractModel> Result<T1> getInstance(boolean success, T1 additionalData)
    {
        return new Result<T1>(success, additionalData);
    }

    /**
     *
     * @param <T1>
     * @param success
     * @param trackingId
     * @param additionalData
     * <p/>
     * @return
     */
    public static <T1 extends AbstractModel> Result<T1> getInstance(boolean success, String trackingId,
                                                                    T1 additionalData)
    {
        return new Result<T1>(success, trackingId, additionalData);
    }

    /**
     *
     * @param <T2>
     * @param success
     * @param exp
     * @param trackingId
     * @param additionalData
     * <p/>
     * @return
     */
    public static <T2 extends AbstractModel> Result<T2> getInstance(boolean success,
                                                                    ScriptProcessingRuntimeException exp,
                                                                    String trackingId, T2 additionalData)
    {
        return new Result<T2>(success, exp, "", exp.getErrorCode(), trackingId, additionalData);
    }

    /**
     * 
     * @param <T>
     * @param result
     * @return 
     */
    public <T extends AbstractModel> Result<T> getCopy(Result<T> result)
    {
        Result<T> r = new Result<T>();
        r.isSuccessFul = result.isSuccessFul;
        r.exception = result.exception;
        r.msg = result.msg;
        r.trackingRef = result.trackingRef;
        r.errorCode = result.errorCode;
        r.dataObject = result.dataObject;
        r.errorData = result.errorData;
        r.status = result.status;        
        return r;
    }

    /**
     *
     * @return
     */
    public T getData()
    {
        return dataObject;
    }

    /**
     * Retruns true or false indicating success or failure of the operation.
     * <p/>
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
    
    public void setEnableAlarm(boolean doAlarm, String msg, String errCode, String errData)
    {
        enableAlarm = doAlarm;
        alarmMsg = msg;
        this.errorCode = errCode;
        this.errorData = errData;
    }
    
    public boolean getEnableAlarm()
    {
        return enableAlarm;
    }

    /**
     * Returns the Exception if any that is part of this result. Can be null if this is a successful result.
     * <p/>
     * @return Exception if any related to this Result.
     */
    public ScriptProcessingRuntimeException getException()
    {
        return exception;
    }

    /**
     * Throws the exception if there is one.
     * <p/>
     * @param logException True if stack trace to be logged, else only the message will be logged.
     */
    private void throwException(boolean logException)
    {
        if (hasException())
        {
            if (logException)
            {
                logger.error(getException().getMessage(), getException());
            }
            else
            {
                logger.error(getException().getMessage());
            }
            if (hasApplicationException())
            {
                throw getException();
            }
        }
    }

    /**
     *
     * @param logStackTrace
     */
    public void handleError()
    {
        if (hasApplicationException())
        {
            throwException(true);
        }
        if (hasValidationException())
        {
            logError();
        }
    }

    /**
     *
     */
    private void logError()
    {
        if (hasException())
        {
            logger.error(getException().getMessage());
        }
    }

    /**
     *
     * @return
     */
    public boolean hasException()
    {
        return !DataUtil.isNull(getException());
    }

    /**
     *
     * @return
     */
    public boolean hasValidationException()
    {
        return !DataUtil.isNull(getException()) && getException().
                getErrorType() == ScriptProcessingRuntimeException.ERROR_VALIDATION;
    }

    /**
     *
     * @return
     */
    public boolean hasApplicationException()
    {
        return !DataUtil.isNull(getException()) && getException().
                getErrorType() == ScriptProcessingRuntimeException.ERROR_APPLICATION;
    }

    /**
     * Returns a meaningful message defined for this result. This may come from a properties file or a programmer
     * defined message attached to a certain business function.
     * <p/>
     * @return
     */
    public String getFriendlyMessage()
    {
        return msg == null ? "" : msg;
    }

    /**
     * The message attached to an Exception. This could be if this result is been generated as a result of an exception
     * thrown by a previous method then this would return that message found within that exception.
     * <p/>
     * @return
     */
    public String getErrorMessage()
    {
        return getException() != null ? getException().getMessage() : "";
    }

    /**
     * returns any specific data that should encapsulate this Result.
     * <p/>
     * @return T the object representing any result related data.
     */
    public ResponseData getResultData()
    {
        ResponseData scriptStatusResponse;
        if((getException() != null && !getException().isBusinessError()) || getEnableAlarm())
        {
            isSuccessFul = true; 
            if(getEnableAlarm())
            {
                scriptStatusResponse = new ResponseData(trackingRef, isSuccessFul(), getErrorCode(),
                                                    alarmMsg, errorData == null ? "" : errorData, false);
            }
            else
            {
                scriptStatusResponse = new ResponseData(trackingRef, isSuccessFul(), getErrorCode(),
                                                    getErrorMessage() + " " + getException().getErrReason(),
                                                    errorData == null ? "" : errorData,
                                                    getException().isBusinessError());
            }
        }
        else
        {
            scriptStatusResponse = new ResponseData(trackingRef, isSuccessFul(), getErrorCode(),
                                                    getErrorMessage(), errorData == null ? "" : errorData);
        }
        return scriptStatusResponse;
    }

    /**
     * Status flag attached to this Result
     * <p/>
     * @return String;
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Generates an appropriate Response based on the context identified by parameter responseId
     * <p/>
     * @param <X>        The type to be returned.
     * @param responseId The identification for the ResponseGenerator strategy.
     * <p/>
     * @return
     */
    @SuppressWarnings ("unchecked")
    public <X> X getResponse(String responseId)
    {
        String strategyClass = ResponseGenerator.responseGenerationStrategiesMap.get(responseId);
        ResponseGenerator<X, T> rg = null;
        try
        {
            rg = (ResponseGenerator<X, T>) Class.forName(strategyClass).newInstance();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return rg.generateResponse(this);
    }

    /**
     *
     * @param errData
     */
    public void setErrorData(String errData)
    {
        errorData = errData;
    }

    /**
     * Any Error data that can be attached to the result.
     * <p/>
     * @return
     */
    public String getErrorData()
    {
        return errorData;
    }

    /**
     *
     * @return
     */
    public String getTrackingReference()
    {
        return trackingRef;
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
        if (getException() != null)
        {
            sb.append(getException().getMessage());
        }
        return sb.toString();
    }
}
