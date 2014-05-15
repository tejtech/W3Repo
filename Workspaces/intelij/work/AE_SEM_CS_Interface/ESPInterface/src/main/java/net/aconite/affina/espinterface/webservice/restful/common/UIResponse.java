/**
 * 
 */
package net.aconite.affina.espinterface.webservice.restful.common;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author wakkir.muzammil
 *
 */

public class UIResponse
{
    @JsonProperty
    private boolean success;

    @JsonProperty
    private String message;


    public UIResponse(boolean success, String message)
    {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
