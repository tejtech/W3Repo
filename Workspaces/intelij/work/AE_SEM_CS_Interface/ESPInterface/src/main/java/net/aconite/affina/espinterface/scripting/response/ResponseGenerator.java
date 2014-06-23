/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.scripting.response;

import java.util.HashMap;
import java.util.Map;
import net.aconite.affina.espinterface.helper.Result;
import net.aconite.affina.espinterface.model.*;

/**
 * An abstract class used to generate a particular response.
 * Implementing class must override generateResponse() to provide the necessary functionality.
 * @author thushara.pethiyagoda
 */
public abstract class ResponseGenerator<X, G extends AbstractModel>
{
    /** Id for identifying StgeScript response generator strategy. */
    public static final String STAGE_SCRIPT_RESPONSE_GENRATOR_ID = "StageScriptResponse";
    /**ID Identifying the Send Script Response generator. */
    public static final String SEND_SCRIPT_RESPONSE_GENRATOR_ID = "sendScriptResponse";
    public static final String SEM_TO_SEND_SCRIPT_RESPONSE_GENRATOR_ID = "SEMTosendScriptResponse";
    /** A Map holding a key mapped to an implementing class of a ResponseGenerator. */
    public static final Map<String, String>responseGenerationStrategiesMap = new HashMap<String, String>();
    
    static
    {        
        //responseGenerationStrategiesMap.put("sendScriptResponse", "net.aconite.affina.espinterface.factory.ResponseGeneratorFactory");
        responseGenerationStrategiesMap.put(SEND_SCRIPT_RESPONSE_GENRATOR_ID, 
                                            "net.aconite.affina.espinterface.scripting.response.SendScriptResponseGenerator");  
        responseGenerationStrategiesMap.put(SEM_TO_SEND_SCRIPT_RESPONSE_GENRATOR_ID, 
                                            "net.aconite.affina.espinterface.scripting.response.SEMToSendScriptResponseGenerator");  
        responseGenerationStrategiesMap.put(STAGE_SCRIPT_RESPONSE_GENRATOR_ID, 
                                            "net.aconite.affina.espinterface.scripting.response.StageScriptResponseGenerator");  

    }
    /**
     * Default constructor.
     */
    public ResponseGenerator()
    {
    }    
    /**
     * X is the Type of response to be returned (e.g StageScriptResponse).
     * Abstract method to generate the relevant response.
     * @param result A Result object that contains data required to generate a response.
     * @return Any type that contains Response data defined as per the particular context.
     */
    public abstract X generateResponse(Result<G> result);
}