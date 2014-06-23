/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao;

import com.platform7.pma.application.Application;
import com.platform7.standardinfrastructure.utilities.Dovecote;
import java.math.BigDecimal;
import java.util.List;
import net.aconite.affina.espinterface.exceptions.EspDaoException;
import net.aconite.affina.espinterface.persistence.GenericPersistentDAO;
import net.aconite.affina.espinterface.persistence.Workable;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author wakkir.muzammil
 */
@Service("applicationDao")
public class ApplicationDao implements IEspDao<Application,BigDecimal,Application,FilterCriteria,PagingCriteria>
{
   private static final Logger logger = LoggerFactory.getLogger(ApplicationDao.class.getName());

    public Application getById(BigDecimal oId)
    {
        ApplicationDao.GetByIdWorker worker = new ApplicationDao.GetByIdWorker();
        worker.setData(oId);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }

    public Application getByName(String name)
    {
        ApplicationDao.GetByNameWorker worker = new ApplicationDao.GetByNameWorker();
        worker.setData(name);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public Application getByTrackId(Integer scopeOID,String trackId)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Application> getList(FilterCriteria filterObject, PagingCriteria pagingObject)
    {
        ApplicationDao.GetListWorker worker = new ApplicationDao.GetListWorker();
        worker.setData(filterObject);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }

    public BigDecimal getTotalCount(FilterCriteria filterObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal add(Application ormObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal delete(BigDecimal oId)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal update(Application ormObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    
    /////////////////////////////////////////////////////////////////////////////
    
    //################# Start: getById Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetByIdWorker extends Workable<BigDecimal, Application>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ApplicationDao.getById", new Dovecote.ProtectedOperation<Application>()
                {
                    public Application execute() throws Exception
                    {
                        return getByIdImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during ApplicationDao.getById processing", e);
            }
        }
    }    

    private Application getByIdImpl(BigDecimal oid)
    {
       return GenericPersistentDAO.instance().getApplicationById(oid);        
    }

    //################# End: getById Suppliments ##############################
     //################# Start: getByName Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetByNameWorker extends Workable<String, Application>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ApplicationDao.getByName", new Dovecote.ProtectedOperation<Application>()
                {
                    public Application execute() throws Exception
                    {
                        return getByNameImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during ApplicationDao.getByName processing", e);
            }
        }
    }    

    private Application getByNameImpl(String name)
    { 
        return GenericPersistentDAO.instance().getApplicationByName(name); 
    }

    //################# End: getByName Suppliments ##############################
    
    //################# Start: getList Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetListWorker extends Workable<FilterCriteria, List<Application>>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ApplicationDao.getList", new Dovecote.ProtectedOperation<List<Application>>()
                {
                    public List<Application> execute() throws Exception
                    {
                        return getListImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during ApplicationDao.getList processing", e);
            }
        }
    }    

    private List<Application> getListImpl(FilterCriteria filterObject)
    {     
        return GenericPersistentDAO.instance().getApplications(filterObject); 
    }

    //################# End: getList Suppliments ##############################
    
}
