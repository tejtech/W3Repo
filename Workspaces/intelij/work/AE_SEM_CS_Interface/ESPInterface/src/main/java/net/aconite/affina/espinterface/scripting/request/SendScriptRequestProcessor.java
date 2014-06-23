/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.scripting.request;

import com.platform7.pma.request.emvscriptrequest.ESPStageScriptFilter;
import java.math.*;
import net.acointe.affina.esp.*;
import net.aconite.affina.espinterface.helper.*;
import net.aconite.affina.espinterface.model.*;
import net.aconite.affina.espinterface.persistence.*;
import net.aconite.affina.espinterface.scripting.generic.*;
import net.aconite.affina.espinterface.xmlmapping.cs.SendScriptRequest;

/**
 * This class processes requests for stage scripts received from external systems.
 * <p/>
 * @author thushara.pethiyagoda
 */
public class SendScriptRequestProcessor implements
        ScriptProcessable<ScriptDataHolder<SendScriptRequest>, ScriptableCard>
{

    /**
     * 
     */
    public static final String REQUEST_SOURCE = "CustomerService";
    /**
     * Field holding a Validator, used to validate script requests.
     */
    private Validator<ScriptDataHolder<SendScriptRequest>, ScriptableCard> validator;

    /**
     * Constructs a default SendScriptRequestProcessor
     */
    public SendScriptRequestProcessor()
    {
    }

    /**
     * Processes the Script Request and returns a Result indicating its success or failure.
     * <p/>
     * @param scriptData The message holding information about the Script Request.
     * <p/>
     * @return Result encapsulating the results of processing a script.
     */
    public Result<ScriptableCard> processScript(ScriptDataHolder<SendScriptRequest> scriptData)
    {

        Workable<ScriptDataHolder<SendScriptRequest>, Result<ScriptableCard>> workable = 
                                                                    new SendScriptRequestPersistentWorker(scriptData);
        GenericPersistentDAO.instance().addTransactionalWorker(workable);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit();
        return workable.getResult();
    }
}

/**
 *
 * @author thushara.pethiyagoda
 */
class SendScriptRequestPersistentWorker extends Workable<ScriptDataHolder<SendScriptRequest>, Result<ScriptableCard>>
{
    /**
     *
     * @param data
     */
    public SendScriptRequestPersistentWorker(ScriptDataHolder<SendScriptRequest> data)
    {
        super(data);
    }

    /**
     * @see (Workable)
     */
    @Override
    public void doWork()
    {
        ScriptDataHolder<SendScriptRequest> data = getData();
        Validator<ScriptDataHolder<SendScriptRequest>, ScriptableCard> validator = new SendScriptRequestValidator();
        Result<ScriptableCard> result = validator.validate(data);
        
        if (result.isSuccessFul())
        {
            Result<ScriptableCard> rtnResult = generateESPStageScriptFilter(result);            
            setResponse(rtnResult);
        }
        else
        {
            setResponse(result);
        }
    }

    /**
     * Generates an ESPStageScriptFilter to be persisted.
     * <p/>
     * @param result Result Object encapsulating data required to construct an ESPStageScriptFilter.
     * <p/>
     * @return ESPStageScriptFilter
     */
    private Result<ScriptableCard> generateESPStageScriptFilter(Result<ScriptableCard> result)
    {
        Persistent persistent = GenericPersistentDAO.instance();

        ESPStageScriptFilter stageScriptFilter = persistent.getRegisteredObject(ESPStageScriptFilter.class);
        ScriptableCard data = result.getData();
        java.util.Date today = new java.util.Date();
        java.sql.Timestamp timeStampToday = new java.sql.Timestamp(today.getTime());
        long dateCreated = timeStampToday.getTime();
        stageScriptFilter.setApplicationOID(data.getApplication().getApplication().getOID());
        stageScriptFilter.setEspBusinessFunctionOID(data.getBusinessFunction().getParent().getOID());
        stageScriptFilter.setScriptDescription(data.getBusinessFunction().getName());
        stageScriptFilter.setScopeOID(data.getSoftCard().getArea().getOID());
        stageScriptFilter.setCardId(data.getSoftCard().getCardId());
        stageScriptFilter.setSource(SendScriptRequestProcessor.REQUEST_SOURCE);
        stageScriptFilter.setTrackingId(result.getTrackingReference());
        stageScriptFilter.setFilterOrder(BigDecimal.valueOf(dateCreated));
        stageScriptFilter.setDateCreated(timeStampToday);
        stageScriptFilter.setStatus(AffinaEspUtils.STATUS_INITIAL);
        return Result.<ScriptableCard>getInstance(true, result.getTrackingReference(), result.getData());
    }
}