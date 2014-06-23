/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao;

import com.platform7.pma.request.emvscriptrequest.ESPBusinessFunction;
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
@Service("businessFunctionDao")
public class BusinessFunctionDao implements IEspDao<ESPBusinessFunction,BigDecimal,ESPBusinessFunction,FilterCriteria,PagingCriteria>
{
    private static final Logger logger = LoggerFactory.getLogger(BusinessFunctionDao.class.getName());

    
    public ESPBusinessFunction getById(BigDecimal oid)
    {
        BusinessFunctionDao.GetByIdWorker worker = new BusinessFunctionDao.GetByIdWorker();
        worker.setData(oid);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }

    public ESPBusinessFunction getByName(String name)
    {
        BusinessFunctionDao.GetByNameWorker worker = new BusinessFunctionDao.GetByNameWorker();
        worker.setData(name);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public ESPBusinessFunction getByTrackId(Integer scopeOID,String trackId)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<ESPBusinessFunction> getList(FilterCriteria filterObject, PagingCriteria pagingObject)
    {
        BusinessFunctionDao.GetListWorker worker = new BusinessFunctionDao.GetListWorker();
        worker.setData(filterObject);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }

    public BigDecimal getTotalCount(FilterCriteria filterObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal add(ESPBusinessFunction ormObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal delete(BigDecimal oId)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal update(ESPBusinessFunction ormObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /////////////////////////////////////////////////////////////////////////////
    
    //################# Start: getById Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetByIdWorker extends Workable<BigDecimal, ESPBusinessFunction>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("BusinessFunctionDao.getById", new Dovecote.ProtectedOperation<ESPBusinessFunction>()
                {
                    public ESPBusinessFunction execute() throws Exception
                    {
                        return getByIdImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during BusinessFunctionDao.getById processing", e);
            }
        }
    }    

    private ESPBusinessFunction getByIdImpl(BigDecimal oid)
    {
       return GenericPersistentDAO.instance().getBusinessFunctionById(oid);        
    }

    //################# End: getById Suppliments ##############################
     //################# Start: getByName Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetByNameWorker extends Workable<String, ESPBusinessFunction>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("BusinessFunctionDao.getByName", new Dovecote.ProtectedOperation<ESPBusinessFunction>()
                {
                    public ESPBusinessFunction execute() throws Exception
                    {
                        return getByNameImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during BusinessFunctionDao.getByName processing", e);
            }
        }
    }    

    private ESPBusinessFunction getByNameImpl(String name)
    { 
        return GenericPersistentDAO.instance().getBusinessFunctionByName(name); 
    }

    //################# End: getByName Suppliments ##############################
    
    //################# Start: getList Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetListWorker extends Workable<FilterCriteria, List<ESPBusinessFunction>>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("BusinessFunctionDao.getList", new Dovecote.ProtectedOperation<List<ESPBusinessFunction>>()
                {
                    public List<ESPBusinessFunction> execute() throws Exception
                    {
                        return getListImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during BusinessFunctionDao.getList processing", e);
            }
        }
    }    

    private List<ESPBusinessFunction> getListImpl(FilterCriteria filterObject)
    {     
        return GenericPersistentDAO.instance().getBusinessFunctions(filterObject); 
    }

    //################# End: getList Suppliments ##############################
}
