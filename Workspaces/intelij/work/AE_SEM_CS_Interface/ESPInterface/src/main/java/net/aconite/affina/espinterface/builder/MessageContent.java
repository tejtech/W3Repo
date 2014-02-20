/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.builder;

import net.aconite.affina.espinterface.xmlmapping.sem.StatusType;

import java.io.Serializable;
import net.aconite.affina.espinterface.xmlmapping.sem.ErrorType;

/**
 * @author wakkir.muzammil
 */
public class MessageContent implements Serializable
{
    private static final long serialVersionUID = 7526472295622776147L;  // unique id
    
    private String scopeName;    
    private String type;
    private String trackingReference;
    private StatusType status;
    private ErrorType error;
    //private ErrorType validation;
    private boolean isValid;

    
    //private String errorData;
    //private String errorDescription;
    //private String errorCode;

    public MessageContent(String type, String trackingReference)
    {
        this.type = type;
        this.trackingReference = trackingReference;
    }


    public MessageContent(String type, String trackingReference, StatusType status)
    {
        this.type = type;
        this.trackingReference = trackingReference;
        this.status = status;
    }

    public MessageContent(String type, String trackingReference, StatusType status,ErrorType error)//String errorData, String errorDescription, String errorCode)
    {
        this.type = type;
        this.trackingReference = trackingReference;
        this.status = status;
        this.error=error;
        //this.errorData = errorData;
        //this.errorDescription = errorDescription;
        //this.errorCode = errorCode;
    }


    @Override
    public String toString()
    {
        StringBuffer sb=new StringBuffer();
        sb.append("MessageContent{ ");
        sb.append("ScopeName=" + scopeName);
        sb.append(", ");
        sb.append("Type=" + type );
        sb.append(", ");
        sb.append("TrackingReference=" + trackingReference);
        sb.append(", ");
        sb.append("Status=" + status);
        if(getError()!=null)
        {
            sb.append(", ");
            sb.append("ErrorData=" + getError().getData());
            sb.append(", ");
            sb.append("ErrorDescription=" + getError().getDescription());
            sb.append(", ");
            sb.append("ErrorCode=" + getError().getErrorCode());
        }
        sb.append("}");
                
        return sb.toString();
    }
    
    public String getScopeName() 
    {
        return scopeName;
    }

    public void setScopeName(String scopeName) 
    {
        this.scopeName = scopeName;
    }
    
    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getTrackingReference()
    {
        return trackingReference;
    }

    public void setTrackingReference(String trackingReference)
    {
        this.trackingReference = trackingReference;
    }

    public StatusType getStatus()
    {
        return status;
    }

    public void setStatus(StatusType status)
    {
        this.status = status;
    }

    public ErrorType getError() 
    {
        return error;
    }

    public void setError(ErrorType error) 
    {
        this.error = error;
    }

   
    public boolean isValid() 
    {
        return isValid;
    }

    public void setIsValid(boolean isValid) 
    {
        this.isValid = isValid;
    }

}
