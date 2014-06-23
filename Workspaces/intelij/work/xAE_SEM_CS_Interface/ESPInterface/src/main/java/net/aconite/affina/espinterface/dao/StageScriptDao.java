/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao;

import com.platform7.pma.request.emvscriptrequest.ESPStageScriptFilter;
import com.platform7.standardinfrastructure.utilities.Dovecote;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import net.acointe.affina.esp.AffinaEspUtils;
import net.aconite.affina.espinterface.exceptions.EspResponseValidationException;
import net.aconite.affina.espinterface.jms.MessageSender;
import net.aconite.affina.espinterface.persistence.GenericPersistentDAO;
import net.aconite.affina.espinterface.persistence.Workable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wakkir.muzammil
 */
public class StageScriptDao 
{
    private static final Logger logger = LoggerFactory.getLogger(StageScriptDao.class.getName());

    // A Workable to perform the actual work within the non-container managed transaction
    private class StageScriptWorker extends Workable<StageScriptObject, Long>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("StageScriptDao.add", new Dovecote.ProtectedOperation<Long>()
                {
                    public Long execute() throws Exception
                    {
                        return addImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspResponseValidationException("Exception caught during response processing", e);
            }
        }
    }
    
    public Long add(StageScriptObject stageScriptObject) 
    {
        StageScriptDao.StageScriptWorker worker = new StageScriptDao.StageScriptWorker();
        worker.setData(stageScriptObject);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }

    private Long addImpl(StageScriptObject stageScriptObject)
    {
        long filterOrder=new Date().getTime();
        String trackId=AffinaEspUtils.generateFilterTrackId(stageScriptObject.getApplicationId(),stageScriptObject.getBusinessFunctionId(),filterOrder);
        
        ESPStageScriptFilter stageScriptFilter = GenericPersistentDAO.instance().getRegisteredObject(ESPStageScriptFilter.class);
        
        stageScriptFilter.setApplicationOID(BigDecimal.valueOf(stageScriptObject.getApplicationId()));
        stageScriptFilter.setEspBusinessFunctionOID(BigDecimal.valueOf(stageScriptObject.getBusinessFunctionId()));
        stageScriptFilter.setTrackingId(trackId);
        stageScriptFilter.setFilterOrder(BigDecimal.valueOf(filterOrder));  
        stageScriptFilter.setDateCreated(new Timestamp(new Date().getTime()));
        stageScriptFilter.setStatus(AffinaEspUtils.STATUS_INITIAL);
        
        if(stageScriptObject.getStageScriptStartDate()!=null)
        {
            stageScriptFilter.setStageStartDate(new Timestamp(stageScriptObject.getStageScriptStartDate()));
        }
        if(stageScriptObject.getStageScriptEndDate()!=null)
        {
            stageScriptFilter.setStageEndDate(new Timestamp(stageScriptObject.getStageScriptEndDate()));        
        }
        if(stageScriptObject.getMaxRestageCount()!=null)
        {
            stageScriptFilter.setMaxStageCount(BigDecimal.valueOf(stageScriptObject.getMaxRestageCount()));
        }
        if(stageScriptObject.getBin()!=null)
        {
            stageScriptFilter.setBin(BigDecimal.valueOf(stageScriptObject.getBin()));
        }        
        if(stageScriptObject.getCardId()!=null)
        {
            stageScriptFilter.setCardId(stageScriptObject.getCardId());
        }  
        
        
        MessageSender.sendStageScriptFilterMessage(String.valueOf(filterOrder)); 

        return filterOrder;
    }

    
}
