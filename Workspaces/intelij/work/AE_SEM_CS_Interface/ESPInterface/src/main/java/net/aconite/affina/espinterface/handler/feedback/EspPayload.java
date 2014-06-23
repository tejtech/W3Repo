/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.handler.feedback;

import java.io.Serializable;


/**
 *
 * @author wakkir.muzammil
 */
public class EspPayload implements Serializable
{
    private static final long serialVersionUID = 7525572295622776147L;  // unique id
    
    private String header;    
    private String message; 
    private boolean isError;
    
    public EspPayload(String header,String message, boolean isError) 
    {
        this.header = header;
        this.message = message;
        this.isError = isError;
    }

    public String getMessage() 
    {
        return message;
    }

    public void setMessage(String message) 
    {
        this.message = message;
    }

    public boolean isError() 
    {
        return isError;
    }

    public void setIsError(boolean isError) 
    {
        this.isError = isError;
    }
   
    public String getHeader() 
    {
        return header;
    }

    public void setHeader(String header) 
    {
        this.header = header;
    }
    
    public String getMessageBody() 
    {
        return message;
    }
    
    public String getMessageWithHeader() 
    {
        return header+" "+message;
    }
    
    public String getMessageInOneLine() 
    {
        return message.replace("\n", "");
    }
    
    public String getMessageInOneLineWithHeader() 
    {
        return getMessageWithHeader().replace("\n", "");
    }
    
    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\nHeader  : ").append(header);
        sb.append("\nMessage : ").append(message);
        sb.append("\nIsError : ").append(isError);
        sb.append("\n");
        return sb.toString();
    }
    
    
    
}