/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.exceptions;

/**
 *
 * @author thushara.pethiyagoda
 */
public class ScriptProcessException extends Exception
{

    static final long serialVersionUID = -3387516993124229943L;
    private String errorCode;
    private String errReason;

    public ScriptProcessException()
    {
        super();
    }

    public ScriptProcessException(String message)
    {
        super(message);
    }

    public ScriptProcessException(String message, String errCode)
    {
        super(message);
        errorCode = errCode;
    }

    public ScriptProcessException(String message, String errCode, String eReason)
    {
        this(message, errCode);
        errReason = eReason;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public String getErrReason()
    {
        return errReason;
    }
}
