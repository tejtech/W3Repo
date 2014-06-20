package net.aconite.affina.espinterface.webservice.restful.service.impl;

import com.platform7.standardinfrastructure.multiissuer.Scope;
import java.util.List;
import net.aconite.affina.espinterface.persistence.GenericPersistentDAO;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.webservice.restful.service.IScopeService;
import org.springframework.stereotype.Service;

@Service("scopeService")
public class ScopeService implements IScopeService
{       
    //@Autowired
    //private PMAProductDao pmaProductDao;
    
    public Scope getById(Integer id) 
    {
        //PMAProduct person = pmaProductDao.getPMAProduct(id);
        //PMAProduct person = new PMAProduct();		
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Integer getTotalCount(FilterCriteria filter) 
    {        
        //Integer totalCount = pmaProductDao.getTotalCount(filter);
        throw new UnsupportedOperationException("Not supported yet.");
        
    }
   
    public QueryResult<Scope> getList(FilterCriteria filter, PagingCriteria paging) 
    {
        //List<PMAProduct> recordList = pmaProductDao.getListPMAProducts(filter,paging);
        //Integer totalCount = pmaProductDao.getTotalCount(filter);

        List<Scope> recordList =  GenericPersistentDAO.instance().getScopes(filter);//,paging);
        
        Integer totalCount;
        if(recordList!=null)
        {
           totalCount = recordList.size();
        }
        else
        {
            totalCount=0;
        }

        QueryResult<Scope> queryResult=new QueryResult<Scope>(recordList, totalCount);

        return queryResult;
    }

    public void save(Scope saveobj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean update(Scope updateobj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean delete(Integer id) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }  

    
}
