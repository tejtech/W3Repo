/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao;

import com.platform7.pma.request.emvscriptrequest.ESPCardSetup;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import com.platform7.standardinfrastructure.utilities.Dovecote;
import java.math.BigDecimal;
import java.util.List;
import net.aconite.affina.espinterface.dao.model.CardSetupDetailObject;
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
@Service("cardSetupDetailDao")
public class CardSetupDetailDao  implements IEspDao<ESPCardSetup,ESPCardSetup,CardSetupDetailObject,FilterCriteria,PagingCriteria>
{
    private static final Logger logger = LoggerFactory.getLogger(CardSetupDetailDao.class.getName());
    
    public ESPCardSetup getById(BigDecimal oid)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ESPCardSetup getByName(String name)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ESPCardSetup getByTrackId(Integer scopeOID,String trackId)
    {          
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<ESPCardSetup> getList(FilterCriteria filterObject, PagingCriteria pagingObject)
    {
        CardSetupDetailDao.GetListWorker worker = new CardSetupDetailDao.GetListWorker();
        worker.setData(filterObject);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public BigDecimal getTotalCount(FilterCriteria filterObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ESPCardSetup add(CardSetupDetailObject stageScriptObject) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ESPCardSetup delete(BigDecimal oid)
    {
        CardSetupDetailDao.DeleteWorker worker = new CardSetupDetailDao.DeleteWorker();
        worker.setData(oid);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }

    public ESPCardSetup update(CardSetupDetailObject stageScriptObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /////////////////////////////////////////////////////////////////////////////
  
   
    //################# Start: getList Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetListWorker extends Workable<FilterCriteria, List<ESPCardSetup>>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("CardSetupDetailDao.getList", new Dovecote.ProtectedOperation<List<ESPCardSetup>>()
                {
                    public List<ESPCardSetup> execute() throws Exception
                    {
                        return getListImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during CardSetupDetailDao.getList processing", e);
            }
        }
    }    

    private List<ESPCardSetup> getListImpl(FilterCriteria filter)
    {   
        List<ESPCardSetup> myList=null;
        //
                
        if(filter!=null)
        {
            
           Scope scope=null;
                    
           if(filter.getScopeName()!=null)
           {
               scope=GenericPersistentDAO.instance().getScopeByName(filter.getScopeName());
           }
           else if(filter.getScopeId()!=null)
           {
               scope=GenericPersistentDAO.instance().getScopeById(BigDecimal.valueOf(filter.getScopeId()));
           }
           
           if(scope!=null)
           {
               if(filter.getTrackId()!=null && filter.getTrackId().trim().length()>0)
               {
                    myList= GenericPersistentDAO.instance().getCardSetupDetails(scope,filter.getTrackId());
               }
               else if(filter.getSemTrackId()!=null && filter.getSemTrackId().trim().length()>0)
               {
                    myList= GenericPersistentDAO.instance().getCardSetupDetailsBySemTrn(scope,filter.getSemTrackId());
               }
           }            
        }  
               
        return myList;
    }

    //################# End: getList Suppliments ##############################
    
    //################# Start: delete Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class DeleteWorker extends Workable<BigDecimal, ESPCardSetup>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("CardSetupDetailDao.delete", new Dovecote.ProtectedOperation<ESPCardSetup>()
                {
                    public ESPCardSetup execute() throws Exception
                    {
                        return deleteImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during CardSetupDetailDao.delete processing", e);
            }
        }
    }    

    private ESPCardSetup deleteImpl(BigDecimal oid)
    {   
        ESPCardSetup esp=null;
        if(oid!=null)
        {
             esp= GenericPersistentDAO.instance().getCardSetupDetailById(oid);

             return GenericPersistentDAO.instance().deleteObject(esp);
        }            
        
        return null;
    }

    //################# End: delete Suppliments ##############################
   
    
    
}
