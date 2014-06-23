/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao;

import net.aconite.affina.espinterface.dao.model.StageScriptFilterObject;
import com.platform7.pma.request.emvscriptrequest.ESPStageScriptFilter;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import com.platform7.standardinfrastructure.utilities.Dovecote;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import net.acointe.affina.esp.AffinaEspUtils;
import net.aconite.affina.espinterface.exceptions.EspDaoException;
import net.aconite.affina.espinterface.persistence.GenericPersistentDAO;
import net.aconite.affina.espinterface.persistence.Workable;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.webservice.restful.service.model.StageScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author wakkir.muzammil
 */
@Service("stageScriptFilterDao")
public class StageScriptFilterDao  implements IEspDao<ESPStageScriptFilter,ESPStageScriptFilter,StageScriptFilterObject,FilterCriteria,PagingCriteria>
{
    private static final Logger logger = LoggerFactory.getLogger(StageScriptFilterDao.class.getName());
    
    public ESPStageScriptFilter getById(BigDecimal oid)
    {
        StageScriptFilterDao.GetByIdWorker worker = new StageScriptFilterDao.GetByIdWorker();
        worker.setData(oid);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public ESPStageScriptFilter getByName(String name)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ESPStageScriptFilter getByTrackId(Integer scopeOID,String trackId)
    {          
        StageScriptFilterDao.GetByTrackIdWorker worker = new StageScriptFilterDao.GetByTrackIdWorker();
        worker.setData(new StageScriptFilterObject(scopeOID,trackId));
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public List<ESPStageScriptFilter> getList(FilterCriteria filterObject, PagingCriteria pagingObject)
    {
        StageScriptFilterDao.GetListWorker worker = new StageScriptFilterDao.GetListWorker();
        worker.setData(filterObject);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public BigDecimal getTotalCount(FilterCriteria filterObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ESPStageScriptFilter add(StageScriptFilterObject stageScriptObject) 
    {
        StageScriptFilterDao.AddWorker worker = new StageScriptFilterDao.AddWorker();
        worker.setData(stageScriptObject);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }

    public ESPStageScriptFilter delete(BigDecimal oid)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ESPStageScriptFilter update(StageScriptFilterObject stageScriptObject)
    {
        StageScriptFilterDao.UpdateWorker worker = new StageScriptFilterDao.UpdateWorker();
        worker.setData(stageScriptObject);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }

    /////////////////////////////////////////////////////////////////////////////
  
    //################# Start: getById Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetByIdWorker extends Workable<BigDecimal, ESPStageScriptFilter>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("StageScriptFilterDao.getById", new Dovecote.ProtectedOperation<ESPStageScriptFilter>()
                {
                    public ESPStageScriptFilter execute() throws Exception
                    {
                        return getByIdImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during StageScriptFilterDao.getById processing", e);
            }
        }
    }    

    private ESPStageScriptFilter getByIdImpl(BigDecimal oid)
    {
       return GenericPersistentDAO.instance().getStageScriptFiltersById(oid);        
    }

    //################# End: getById Suppliments ##############################
     //################# Start: getByName Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetByTrackIdWorker extends Workable<StageScriptFilterObject, ESPStageScriptFilter>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("StageScriptFilterDao.getByTrackId", new Dovecote.ProtectedOperation<ESPStageScriptFilter>()
                {
                    public ESPStageScriptFilter execute() throws Exception
                    {
                        return getByTrackIdImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during StageScriptFilterDao.getByTrackId processing", e);
            }
        }
    }    

    private ESPStageScriptFilter getByTrackIdImpl(StageScriptFilterObject stageScriptObject)
    { 
        return GenericPersistentDAO.instance().getStageScriptFiltersByTrackId(BigDecimal.valueOf(stageScriptObject.getScopeId()),stageScriptObject.getTrackId()); 
    }

    //################# End: getByName Suppliments ##############################
    //################# Start: getList Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetListWorker extends Workable<FilterCriteria, List<ESPStageScriptFilter>>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("StageScriptFilterDao.getList", new Dovecote.ProtectedOperation<List<ESPStageScriptFilter>>()
                {
                    public List<ESPStageScriptFilter> execute() throws Exception
                    {
                        return getListImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during StageScriptFilterDao.getList processing", e);
            }
        }
    }    

    private List<ESPStageScriptFilter> getListImpl(FilterCriteria filter)
    {   
        List<ESPStageScriptFilter> myList;
        //
        if(filter!=null && filter.getApplicationId()!=null && filter.getBusinessFunctionId()!=null)
        {
            myList= GenericPersistentDAO.instance().getStageScriptFiltersByCheckDuplicate(filter);
        }        
        else
        {
            myList= GenericPersistentDAO.instance().getStageScriptFiltersByStatusAndScope(filter);
        }
        
        return myList;
    }

    //################# End: getList Suppliments ##############################
    //################# Start: Adding Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class AddWorker extends Workable<StageScriptFilterObject, ESPStageScriptFilter>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("StageScriptFilterDao.add", new Dovecote.ProtectedOperation<ESPStageScriptFilter>()
                {
                    public ESPStageScriptFilter execute() throws Exception
                    {
                        return addImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during StageScriptFilterDao.add processing", e);
            }
        }
    }    

    private ESPStageScriptFilter addImpl(StageScriptFilterObject stageScriptObject)
    {
        long filterOrder=new Date().getTime();
        String trackId=AffinaEspUtils.generateFilterTrackId(stageScriptObject.getApplicationId(),stageScriptObject.getBusinessFunctionId(),filterOrder);
        
        ESPStageScriptFilter stageScriptFilter = GenericPersistentDAO.instance().getRegisteredObject(ESPStageScriptFilter.class);
        
        stageScriptFilter.setApplicationOID(BigDecimal.valueOf(stageScriptObject.getApplicationId()));
        stageScriptFilter.setEspBusinessFunctionOID(BigDecimal.valueOf(stageScriptObject.getBusinessFunctionId()));
        stageScriptFilter.setScopeOID(BigDecimal.valueOf(stageScriptObject.getScopeId()));
        stageScriptFilter.setSource(stageScriptObject.getSource());
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
            stageScriptFilter.setCardId(stageScriptObject.getCardId().toUpperCase());
        } 
                
        //MessageSender.sendStageScriptAlertMessage(trackId); 
        
        return stageScriptFilter;
    }

    //################# End: Adding Suppliments ##############################
    
    //################# Start: Update Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class UpdateWorker extends Workable<StageScriptFilterObject, ESPStageScriptFilter>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("StageScriptFilterDao.update", new Dovecote.ProtectedOperation<ESPStageScriptFilter>()
                {
                    public ESPStageScriptFilter execute() throws Exception
                    {
                        return updateImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during StageScriptFilterDao.add processing", e);
            }
        }
    }    

    private ESPStageScriptFilter updateImpl(StageScriptFilterObject stageScriptObject)
    {
        ESPStageScriptFilter stageScriptFilter;
        
        if(stageScriptObject.getScopeId()!=null)
        {
            stageScriptFilter = GenericPersistentDAO.instance().getStageScriptFiltersByTrackId(BigDecimal.valueOf(stageScriptObject.getScopeId()),stageScriptObject.getTrackId());
        }
        else
        {
            Scope scope=GenericPersistentDAO.instance().getScope(stageScriptObject.getScopeName());
            stageScriptFilter = GenericPersistentDAO.instance().getStageScriptFiltersByTrackId(scope.getPrimaryKey(),stageScriptObject.getTrackId());
        }
        
        stageScriptFilter.setStatus(stageScriptObject.getStatus());                   
        
        return stageScriptFilter;
    }

    //################# End: Update Suppliments ##############################
    
    
}
