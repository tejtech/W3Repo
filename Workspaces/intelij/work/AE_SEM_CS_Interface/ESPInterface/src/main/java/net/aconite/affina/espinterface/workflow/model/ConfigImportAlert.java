/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.workflow.model;

/**
 *
 * @author wakkir.muzammil
 */
public class ConfigImportAlert
{
  String message;
  String errorCode;
  boolean isError;
  boolean isAnyScopeNotExist;
  boolean isCurrentScopeNotExist;

  
    public ConfigImportAlert()
    {
       
    }
    
    public ConfigImportAlert(String message, boolean isError)
    {
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

    public boolean isAnyScopeNotExist()
    {
        return isAnyScopeNotExist;
    }

    public void setIsAnyScopeNotExist(boolean isAnyScopeNotExist)
    {
        this.isAnyScopeNotExist = isAnyScopeNotExist;
    }

    public boolean isCurrentScopeNotExist()
    {
        return isCurrentScopeNotExist;
    }

    public void setIsCurrentScopeNotExist(boolean isCurrentScopeNotExist)
    {
        this.isCurrentScopeNotExist = isCurrentScopeNotExist;
    }

    

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    @Override
    public String toString()
    {
        return "ConfigImportAlert{" + "message=" + message + ", errorCode=" + errorCode + ", isError=" + isError + ", isAnyScopeNotExist=" + isAnyScopeNotExist + ", isCurrentScopeNotExist=" + isCurrentScopeNotExist + '}';
    }

    
    
    
      
}
