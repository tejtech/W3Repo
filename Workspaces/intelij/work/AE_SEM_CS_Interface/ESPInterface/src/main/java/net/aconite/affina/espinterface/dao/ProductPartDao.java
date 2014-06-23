/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao;

import com.platform7.pma.product.PMAProductPart;
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
@Service("productPartDao")
public class ProductPartDao  implements IEspDao<PMAProductPart,BigDecimal,PMAProductPart,FilterCriteria,PagingCriteria>
{
    private static final Logger logger = LoggerFactory.getLogger(ProductPartDao.class.getName());
    

    public PMAProductPart getById(BigDecimal oId)
    {
        ProductPartDao.GetByIdWorker worker = new ProductPartDao.GetByIdWorker();
        worker.setData(oId);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public PMAProductPart getByName(String name)
    {
        ProductPartDao.GetByNameWorker worker = new ProductPartDao.GetByNameWorker();
        worker.setData(name);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public PMAProductPart getByTrackId(Integer scopeOID,String trackId)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<PMAProductPart> getList(FilterCriteria filterObject, PagingCriteria pagingObject)
    {
        ProductPartDao.GetListWorker worker = new ProductPartDao.GetListWorker();
        worker.setData(filterObject);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public BigDecimal getTotalCount(FilterCriteria filterObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public BigDecimal add(PMAProductPart ormObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public BigDecimal delete(BigDecimal oId)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal update(PMAProductPart ormObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    

    /////////////////////////////////////////////////////////////////////////////
    
    //################# Start: getById Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetByIdWorker extends Workable<BigDecimal, PMAProductPart>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ProductPartDao.getById", new Dovecote.ProtectedOperation<PMAProductPart>()
                {
                    public PMAProductPart execute() throws Exception
                    {
                        return getByIdImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during ProductPartDao.getById processing", e);
            }
        }
    }    

    private PMAProductPart getByIdImpl(BigDecimal oid)
    {
       return GenericPersistentDAO.instance().getProductPartById(oid);        
    }

    //################# End: getById Suppliments ##############################
     //################# Start: getByName Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetByNameWorker extends Workable<String, PMAProductPart>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ProductPartDao.getByName", new Dovecote.ProtectedOperation<PMAProductPart>()
                {
                    public PMAProductPart execute() throws Exception
                    {
                        return getByNameImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during ProductPartDao.getByName processing", e);
            }
        }
    }    

    private PMAProductPart getByNameImpl(String name)
    { 
        return GenericPersistentDAO.instance().getProductPartByName(name); 
    }

    //################# End: getByName Suppliments ##############################
    
    //################# Start: getList Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetListWorker extends Workable<FilterCriteria, List<PMAProductPart>>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ProductPartDao.getList", new Dovecote.ProtectedOperation<List<PMAProductPart>>()
                {
                    public List<PMAProductPart> execute() throws Exception
                    {
                        return getListImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during ProductPartDao.getList processing", e);
            }
        }
    }    

    private List<PMAProductPart> getListImpl(FilterCriteria filterObject)
    {     
        return GenericPersistentDAO.instance().getProductParts(filterObject); 
    }

    //################# End: getList Suppliments ##############################
    
}
