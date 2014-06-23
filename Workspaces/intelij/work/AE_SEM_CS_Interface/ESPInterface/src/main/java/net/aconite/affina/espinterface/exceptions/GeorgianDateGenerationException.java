/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.exceptions;

import net.aconite.affina.espinterface.exceptions.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class GeorgianDateGenerationException extends ScriptProcessingRuntimeException
{

    static final long serialVersionUID = -3387516993124229942L;

    public GeorgianDateGenerationException()
    {
        super();
    }

    public GeorgianDateGenerationException(String message)
    {
        super(message);
    }

    public GeorgianDateGenerationException(String message, String errCode)
    {
        super(message, errCode);
    }

    public GeorgianDateGenerationException(String message, String eReason , String errCode)
    {
        super(message, eReason, errCode);

    }
}
