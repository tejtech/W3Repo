/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao;

import com.platform7.pma.product.PMAProduct;
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

@Service("productDao")
public class ProductDao  implements IEspDao<PMAProduct,BigDecimal,PMAProduct,FilterCriteria,PagingCriteria>
{
    private static final Logger logger = LoggerFactory.getLogger(ProductDao.class.getName());
    

    public PMAProduct getById(BigDecimal oId)
    {
        ProductDao.GetByIdWorker worker = new ProductDao.GetByIdWorker();
        worker.setData(oId);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public PMAProduct getByName(String name)
    {
        ProductDao.GetByNameWorker worker = new ProductDao.GetByNameWorker();
        worker.setData(name);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public PMAProduct getByTrackId(Integer scopeOID,String trackId)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<PMAProduct> getList(FilterCriteria filterObject, PagingCriteria pagingObject)
    {
        ProductDao.GetListWorker worker = new ProductDao.GetListWorker();
        worker.setData(filterObject);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public BigDecimal getTotalCount(FilterCriteria filterObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public BigDecimal add(PMAProduct ormObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public BigDecimal delete(BigDecimal oId)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal update(PMAProduct ormObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /////////////////////////////////////////////////////////////////////////////
    
    //################# Start: getById Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetByIdWorker extends Workable<BigDecimal, PMAProduct>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ProductDao.getById", new Dovecote.ProtectedOperation<PMAProduct>()
                {
                    public PMAProduct execute() throws Exception
                    {
                        return getByIdImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during ProductDao.getById processing", e);
            }
        }
    }    

    private PMAProduct getByIdImpl(BigDecimal oid)
    {
       return GenericPersistentDAO.instance().getProductById(oid);        
    }

    //################# End: getById Suppliments ##############################
     //################# Start: getByName Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetByNameWorker extends Workable<String, PMAProduct>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ProductDao.getByName", new Dovecote.ProtectedOperation<PMAProduct>()
                {
                    public PMAProduct execute() throws Exception
                    {
                        return getByNameImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during ProductDao.getByName processing", e);
            }
        }
    }    

    private PMAProduct getByNameImpl(String name)
    { 
        return GenericPersistentDAO.instance().getProductByName(name); 
    }

    //################# End: getByName Suppliments ##############################
    
    //################# Start: getList Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetListWorker extends Workable<FilterCriteria, List<PMAProduct>>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ProductDao.getList", new Dovecote.ProtectedOperation<List<PMAProduct>>()
                {
                    public List<PMAProduct> execute() throws Exception
                    {
                        return getListImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during ProductDao.getList processing", e);
            }
        }
    }    

    private List<PMAProduct> getListImpl(FilterCriteria filterObject)
    {     
        return GenericPersistentDAO.instance().getProducts(filterObject); 
    }

    //################# End: getList Suppliments ##############################
    
}
