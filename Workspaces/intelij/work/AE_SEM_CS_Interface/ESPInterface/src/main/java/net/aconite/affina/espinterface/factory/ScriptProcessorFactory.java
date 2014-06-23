/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.factory;

import net.aconite.affina.espinterface.helper.Result;
import net.aconite.affina.espinterface.model.*;
import net.aconite.affina.espinterface.persistence.Workable;
import net.aconite.affina.espinterface.scripting.generic.*;
import net.aconite.affina.espinterface.scripting.response.SendScriptResponseValidator;
import net.aconite.affina.espinterface.scripting.statusupdate.ScriptStatusUpdateDataHolder;
import net.aconite.affina.espinterface.scripting.statusupdate.ScriptStatusUpdateEventHandler;
import net.aconite.affina.espinterface.scripting.statusupdate.ScriptUpdateProcessor;
import net.aconite.affina.espinterface.xmlmapping.sem.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class ScriptProcessorFactory
{
    public static ScriptProcessable<ScriptStatusUpdateDataHolder, AbstractModel> createScriptProcessable()
    {
        return ScriptUpdateProcessor.getProcessable();
    }
    
    public static ScriptEventListener<ScriptStatusUpdateDataHolder> createScriptEventListener(ScriptProcessable<ScriptStatusUpdateDataHolder, AbstractModel> scriptProcessor)
    {
        return new ScriptStatusUpdateEventHandler(scriptProcessor);
    }
    
//    public static Validator<StageScriptResponse, StageScriptDetail> getSendScriptResponseValidator()
//    {
//        return SendScriptResponseValidator.getInstance();
//    }
    
    public static Workable<StageScriptResponse, Result<StageScriptDetail>> getSendScriptResponseValidatorWorkable()
    {
        return SendScriptResponseValidator.getInstance();
    }
}
