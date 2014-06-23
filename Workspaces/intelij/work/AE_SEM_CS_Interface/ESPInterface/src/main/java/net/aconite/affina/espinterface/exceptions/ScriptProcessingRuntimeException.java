/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.exceptions;

/**
 *
 * @author thushara.pethiyagoda
 */
public class ScriptProcessingRuntimeException extends RuntimeException
{

    static final long serialVersionUID = -3387516993124229943L;
    private String errorCode;
    private String errReason;
    public final static int ERROR_APPLICATION = 1;
    public final static int ERROR_VALIDATION = 2;
    private int errorType;
    protected ScriptProcessingRuntimeException ex;
    private boolean isBusinessError = true;
    

    public ScriptProcessingRuntimeException()
    {
        super();
    }
    
    public ScriptProcessingRuntimeException(ScriptProcessingRuntimeException exception)
    {
        this(exception.getMessage(), exception.errorCode, exception.errorCode);
        ex = exception ;
    }

    public ScriptProcessingRuntimeException(String message)
    {
        super(message);
    }

    public ScriptProcessingRuntimeException(String message, String errCode)
    {
        super(message);
        errorCode = errCode;
    }

    public ScriptProcessingRuntimeException(String message, String eReason, String errCode)
    {
        this(message, errCode);
        errReason = eReason;
    }
    
    public ScriptProcessingRuntimeException(String message, String eReason, String errCode, int errType)
    {
        this(message, errCode);
        errReason = eReason;
        errorType = errType;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public String getErrReason()
    {
        return errReason;
    }  
    
    public int getErrorType()
    {
        return errorType;
    }   

    public boolean isBusinessError()
    {
        return isBusinessError;
    }

    public void setBusinessError(boolean isBusinessError)
    {
        this.isBusinessError = isBusinessError;
    } 
}
