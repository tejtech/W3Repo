package net.aconite.affina.espinterface.webservice.restful.service.impl;

import com.platform7.pma.product.PMAProductPart;
import java.util.List;
import net.aconite.affina.espinterface.dao.ProductPartDao;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.webservice.restful.service.IProductPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productPartService")
public class ProductPartService implements IProductPartService
{       
    @Autowired
    private ProductPartDao productPartDao;
    
    public PMAProductPart getById(Integer id) 
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

    public QueryResult<PMAProductPart> getList(FilterCriteria filter, PagingCriteria paging) 
    {
        //List<PMAProduct> recordList = pmaProductDao.getListPMAProducts(filter,pagingCriteria);
        //Integer totalCount = pmaProductDao.getTotalCount(filter);
        
        List<PMAProductPart> recordList =  productPartDao.getList(filter, paging);
        
        Integer totalCount;
        if(recordList!=null)
        {
           totalCount = recordList.size();
        }
        else
        {
            totalCount=0;
        }

        QueryResult<PMAProductPart> queryResult=new QueryResult<PMAProductPart>(recordList, totalCount);

        return queryResult;
    }

    public void save(PMAProductPart saveobj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean update(PMAProductPart updateobj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean delete(Integer id) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }   
}
