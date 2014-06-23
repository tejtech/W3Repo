/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.exceptions;

/**
 *
 * @author thushara.pethiyagoda
 */
public class ReflectiveInstantiationException extends ScriptProcessingRuntimeException
{

    static final long serialVersionUID = -3387516993122229942L;
    public ReflectiveInstantiationException()
    {
        super();
    }

    public ReflectiveInstantiationException(String message)
    {
        super(message);
    }

    public ReflectiveInstantiationException(ScriptProcessingRuntimeException exception)
    {
        super(exception);
    }

    public ReflectiveInstantiationException(String message, String errCode)
    {
        super(message, errCode);
    }

    public ReflectiveInstantiationException(String message, String eReason, String errCode)
    {
        super(message, eReason, errCode);

    }
}
