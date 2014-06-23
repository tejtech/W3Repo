/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao;

import com.platform7.standardinfrastructure.multiissuer.Scope;
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
@Service("scopeDao")
public class ScopeDao  implements IEspDao<Scope,BigDecimal,Scope,FilterCriteria,PagingCriteria>
{
    private static final Logger logger = LoggerFactory.getLogger(ScopeDao.class.getName());
    

    public Scope getById(BigDecimal oId)
    {
        ScopeDao.GetByIdWorker worker = new ScopeDao.GetByIdWorker();
        worker.setData(oId);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public Scope getByName(String name)
    {
        ScopeDao.GetByNameWorker worker = new ScopeDao.GetByNameWorker();
        worker.setData(name);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public Scope getByTrackId(Integer scopeOID,String trackId)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Scope> getList(FilterCriteria filterObject, PagingCriteria pagingObject)
    {
        ScopeDao.GetListWorker worker = new ScopeDao.GetListWorker();
        worker.setData(filterObject);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public BigDecimal getTotalCount(FilterCriteria filterObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal add(Scope ormObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal delete(BigDecimal oId)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal update(Scope ormObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    

    
    
    /////////////////////////////////////////////////////////////////////////////
    
    //################# Start: getById Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetByIdWorker extends Workable<BigDecimal, Scope>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ScopeDao.getById", new Dovecote.ProtectedOperation<Scope>()
                {
                    public Scope execute() throws Exception
                    {
                        return getByIdImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during ScopeDao.getById processing", e);
            }
        }
    }    

    private Scope getByIdImpl(BigDecimal oid)
    {
  
        return GenericPersistentDAO.instance().getScopeById(oid);   
    }

    //################# End: getById Suppliments ##############################
     //################# Start: getByName Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetByNameWorker extends Workable<String, Scope>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ScopeDao.getByName", new Dovecote.ProtectedOperation<Scope>()
                {
                    public Scope execute() throws Exception
                    {
                        return getByNameImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during ScopeDao.getByName processing", e);
            }
        }
    }    

    private Scope getByNameImpl(String name)
    {

        return GenericPersistentDAO.instance().getScopeByName(name); 
    }

    //################# End: getByName Suppliments ##############################
    
    //################# Start: getList Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetListWorker extends Workable<FilterCriteria, List<Scope>>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ScopeDao.getList", new Dovecote.ProtectedOperation<List<Scope>>()
                {
                    public List<Scope> execute() throws Exception
                    {
                        return getListImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during ScopeDao.getList processing", e);
            }
        }
    }    

    private List<Scope> getListImpl(FilterCriteria filterObject)
    {
     
        return GenericPersistentDAO.instance().getScopes(filterObject);     
    }

    //################# End: getList Suppliments ##############################
    
}
