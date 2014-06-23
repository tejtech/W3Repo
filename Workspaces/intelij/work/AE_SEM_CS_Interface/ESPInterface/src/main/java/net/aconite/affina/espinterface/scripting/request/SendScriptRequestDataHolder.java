/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.scripting.request;

import net.aconite.affina.espinterface.scripting.generic.*;
import net.aconite.affina.espinterface.xmlmapping.cs.SendScriptRequest;

/**
 * Holds the request message for sending a script request.
 * @author thushara.pethiyagoda
 */
public class SendScriptRequestDataHolder extends ScriptDataHolder<SendScriptRequest>
{

    /**
     * Constructs a ScriptRequestDataHolder for a SendScriptRequest 
     * @param ssupd 
     */
    public SendScriptRequestDataHolder(SendScriptRequest ssupd)
    {
        super(ssupd);
    }

    /**
     * Constructs a ScriptRequestDataHolder for a SendScriptRequest within a particular scope
     * @param ssupd
     * @param scopeName 
     */
    public SendScriptRequestDataHolder(SendScriptRequest ssupd, String scopeName)
    {
        super(ssupd, scopeName);
    }
}
