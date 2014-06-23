package net.aconite.affina.espinterface.webservice.restful.service.impl;

import com.platform7.pma.application.Application;
import java.util.List;
import net.aconite.affina.espinterface.dao.ApplicationDao;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.webservice.restful.service.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("applicationService")
public class ApplicationService implements IApplicationService
{       
    @Autowired
    private ApplicationDao applicationDao;
    
    public Application getById(Integer id) 
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

    public QueryResult<Application> getList(FilterCriteria filter, PagingCriteria paging)
    {
        //List<PMAProduct> recordList = pmaProductDao.getListPMAProducts(filter,pagingCriteria);
        //Integer totalCount = pmaProductDao.getTotalCount(filter);
        
        List<Application> recordList =  applicationDao.getList(filter, paging);
        
        Integer totalCount;
        if(recordList!=null)
        {
           totalCount = recordList.size();
        }
        else
        {
            totalCount=0;
        }

        QueryResult<Application> queryResult=new QueryResult<Application>(recordList, totalCount);

        return queryResult;
    }

    public void save(Application saveobj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean update(Application updateobj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean delete(Integer id) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }   
    
}
