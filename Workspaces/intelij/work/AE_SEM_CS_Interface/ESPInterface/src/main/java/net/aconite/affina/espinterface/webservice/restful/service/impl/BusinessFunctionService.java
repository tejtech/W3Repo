package net.aconite.affina.espinterface.webservice.restful.service.impl;

import com.platform7.pma.request.emvscriptrequest.ESPBusinessFunction;
import java.util.List;
import net.aconite.affina.espinterface.dao.BusinessFunctionDao;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.webservice.restful.service.IBusinessFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("businessFunctionService")
public class BusinessFunctionService implements IBusinessFunctionService
{       
    @Autowired
    private BusinessFunctionDao businessFunctionDao;
    
    public ESPBusinessFunction getById(Integer id) 
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

    public QueryResult<ESPBusinessFunction> getList(FilterCriteria filter, PagingCriteria paging)
    {
        //List<PMAProduct> recordList = pmaProductDao.getListPMAProducts(filter,pagingCriteria);
        //Integer totalCount = pmaProductDao.getTotalCount(filter);
        
        List<ESPBusinessFunction> recordList =  businessFunctionDao.getList(filter, paging);
        
        Integer totalCount;
        if(recordList!=null)
        {
           totalCount = recordList.size();
        }
        else
        {
            totalCount=0;
        }

        QueryResult<ESPBusinessFunction> queryResult=new QueryResult<ESPBusinessFunction>(recordList, totalCount);

        return queryResult;
    }

    public void save(ESPBusinessFunction saveobj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean update(ESPBusinessFunction updateobj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean delete(Integer id) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }   
    
}
