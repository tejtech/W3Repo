/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao;

import com.platform7.pma.request.emvscriptrequest.ESPApplicationDetail;
import com.platform7.pma.request.emvscriptrequest.ESPCardSetup;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import com.platform7.standardinfrastructure.utilities.Dovecote;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
@Service("scriptableManifestAppsDao")
public class ScriptableManifestAppsDao  implements IEspDao<ESPApplicationDetail,ESPApplicationDetail,CardSetupDetailObject,FilterCriteria,PagingCriteria>
{
    private static final Logger logger = LoggerFactory.getLogger(ScriptableManifestAppsDao.class.getName());
    
    public ESPApplicationDetail getById(BigDecimal oid)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ESPApplicationDetail getByName(String name)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ESPApplicationDetail getByTrackId(Integer scopeOID,String trackId)
    {          
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<ESPApplicationDetail> getList(FilterCriteria filterObject, PagingCriteria pagingObject)
    {
        ScriptableManifestAppsDao.GetListWorker worker = new ScriptableManifestAppsDao.GetListWorker();
        worker.setData(filterObject);
        GenericPersistentDAO.instance().doTransactionalWorkAndCommit(worker);
        return worker.getResult();
    }
    
    public BigDecimal getTotalCount(FilterCriteria filterObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ESPApplicationDetail add(CardSetupDetailObject stageScriptObject) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ESPApplicationDetail delete(BigDecimal oid)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ESPApplicationDetail update(CardSetupDetailObject stageScriptObject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /////////////////////////////////////////////////////////////////////////////
  
  //################# Start: getList Suppliments ##############################
    
    // A Workable to perform the actual work within the non-container managed transaction
    private class GetListWorker extends Workable<FilterCriteria, List<ESPApplicationDetail>>
    {
        public void doWork()
        {
            try
            {
                setResponse(Dovecote.instance().performOperation("ScriptableManifestAppsDao.getList", new Dovecote.ProtectedOperation<List<ESPApplicationDetail>>()
                {
                    public List<ESPApplicationDetail> execute() throws Exception
                    {
                        return getListImpl(getData());
                    }
                }));
            }
            catch(Throwable e)
            {
                throw new EspDaoException("Exception caught during ScriptableManifestAppsDao.getList processing", e);
            }
        }
    }    

    private List<ESPApplicationDetail> getListImpl(FilterCriteria filter)
    {   
        List<ESPApplicationDetail> myList=null;
        //
        if(filter!=null && filter.getPan()!=null && filter.getPsn()!=null  && filter.getExpiryDate()!=null)
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
               myList= GenericPersistentDAO.instance().getESPApplicationDetails(scope, filter.getPan(),filter.getPsn(),filter.getExpiryDate());
           }
        }          
        
        return myList;
    }

    //################# End: getList Suppliments ##############################
   
    
}
