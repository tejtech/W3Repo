/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao;

import com.platform7.pma.request.emvscriptrequest.ESPStageScriptDetail;
import com.platform7.standardinfrastructure.utilities.Dovecote;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import net.acointe.affina.esp.AffinaEspUtilException;
import net.acointe.affina.esp.AffinaEspUtils;
import net.aconite.affina.espinterface.exceptions.EspDaoException;
import net.aconite.affina.espinterface.persistence.GenericPersistentDAO;
import net.aconite.affina.espinterface.persistence.Workable;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.dao.model.StageScriptChunker;
import net.aconite.affina.espinterface.helper.*;
import net.aconite.affina.espinterface.model.*;
import net.aconite.affina.espinterface.persistence.Persistent;
import net.aconite.affina.espinterface.xmlmapping.sem.CardType;
import net.aconite.affina.espinterface.xmlmapping.sem.StageScriptRequest;
import net.aconite.affina.espinterface.xmlmapping.sem.StageScriptRequest.Action;
import net.aconite.affina.espinterface.xmlmapping.sem.StageScriptRequest.BusinessFunction;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author wakkir.muzammil
 */
@Service("stageScriptDetailDao")
public class StageScriptDetailDao  implements IEspDao<StageScriptChunker,StageScriptRequest,StageScriptChunker,FilterCriteria,PagingCriteria>
{
    private static final Logger logger = LoggerFactory.getLogger(StageScriptFilterDao.class.getName());
    

    public StageScriptChunker getById(BigDecimal oId)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public StageScriptChunker getByName(String name)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public StageScriptChunker getByTrackId(Integer scopeOID,String trackId)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<StageScriptChunker> getList(FilterCriteria filterObject, PagingCriteria pagingObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public BigDecimal getTotalCount(FilterCriteria filterObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public StageScriptRequest add(StageScriptChunker addObject) 
    {
        StageScriptDetailDao.AddWorker worker = new StageScriptDetailDao.AddWorker();
        worker.setData(addObject);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }

    public StageScriptRequest update(StageScriptChunker ormObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public StageScriptRequest delete(BigDecimal oId)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    
    
    //################# Start: Adding Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class AddWorker extends Workable<StageScriptChunker, StageScriptRequest>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("StageScriptDetailDao.add", new Dovecote.ProtectedOperation<StageScriptRequest>()
                {
                    public StageScriptRequest execute() throws Exception
                    {
                        return addImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during StageScriptDetailDao.add processing", e);
            }
        }
    }    

    private StageScriptRequest addImpl(StageScriptChunker addObject)throws AffinaEspUtilException
    {
        long scriptOrder=new Date().getTime();
        
        //String filterTrackingId,String appId,String psn,String appType,String appVersion,long scriptOrder
        String trackId=AffinaEspUtils.generateStageScriptTrackingId(addObject.getFilterTrackId(),addObject.getAppId(),addObject.getPsn(),addObject.getAppType(),addObject.getAppVersion(),scriptOrder);
        
        ESPStageScriptDetail stageScriptDetail = GenericPersistentDAO.instance().getRegisteredObject(ESPStageScriptDetail.class);
        
        
        stageScriptDetail.setTrackingReference(trackId);
        stageScriptDetail.setScriptableManifestApplicationOID(addObject.getScriptableManifestOID());
        stageScriptDetail.setEspStageScriptFilterOID(addObject.getStageScriptFilterOID());
        stageScriptDetail.setScopeOID(addObject.getScopeOID());
        stageScriptDetail.setScriptOrder(BigDecimal.valueOf(scriptOrder));
        stageScriptDetail.setDateCreated(new Timestamp(new Date().getTime()));
        stageScriptDetail.setStatus(AffinaEspUtils.STATUS_INITIAL);
                        
        
        StageScriptRequest request=getStageScriptRequest(addObject,trackId);
        
        return request;
    }

    //################# End: Adding Suppliments ##############################
    
    
    private StageScriptRequest getStageScriptRequest(StageScriptChunker addObject,String trackId) throws AffinaEspUtilException
    {
        StageScriptRequest request=new StageScriptRequest();
        request.setTrackingReference(trackId);
        CardType cardType=new CardType();
        cardType.setPAN(addObject.getPan());
        cardType.setPANSequence(addObject.getPsn()); 
        cardType.setExpirationYear(AffinaEspUtils.getYearInYYYY(addObject.getExpDate()));
        cardType.setExpirationMonth(AffinaEspUtils.getMonthInMM(addObject.getExpDate()));       
        request.setCard(cardType);
        
        BusinessFunction busFun=new BusinessFunction();
        busFun.setFunctionName(addObject.getBusFundAlias());
        request.setBusinessFunction(busFun);
        
        Action action =new Action();
        if(addObject.getStageEndDate()!=null)
        {
            action.setEndDate(String.valueOf(addObject.getStageEndDate().getTime()));
        }
        if(addObject.getStageStartDate()!=null)
        {
            action.setStartDate(String.valueOf(addObject.getStageStartDate().getTime()));
        }
        if(addObject.getStageMaxcount()!=null)
        {
            action.setRestageAutomatically(BigInteger.valueOf(addObject.getStageMaxcount().longValue()));
        }
        request.setAction(action);
        
        return request;
    }
    
}
