package net.aconite.affina.espinterface.webservice.restful.service.impl;

import com.platform7.pma.product.PMAProduct;
import java.util.List;
import net.aconite.affina.espinterface.dao.ProductDao;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.webservice.restful.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productService")
public class ProductService implements IProductService
{       
    @Autowired
    private ProductDao productDao;
    
    public PMAProduct getById(Integer id) 
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

    public QueryResult<PMAProduct> getList(FilterCriteria filter, PagingCriteria paging) 
    {
        //List<PMAProduct> recordList = pmaProductDao.getListPMAProducts(filter,pagingCriteria);
        //Integer totalCount = pmaProductDao.getTotalCount(filter);
        
        List<PMAProduct> recordList =  productDao.getList(filter, paging);
        
        Integer totalCount;
        if(recordList!=null)
        {
           totalCount = recordList.size();
        }
        else
        {
            totalCount=0;
        }

        QueryResult<PMAProduct> queryResult=new QueryResult<PMAProduct>(recordList, totalCount);

        return queryResult;
    }

    public void save(PMAProduct saveobj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean update(PMAProduct updateobj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean delete(Integer id) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }   
}
