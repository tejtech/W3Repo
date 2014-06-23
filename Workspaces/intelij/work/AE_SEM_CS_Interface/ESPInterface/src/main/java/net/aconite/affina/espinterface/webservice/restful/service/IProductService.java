package net.aconite.affina.espinterface.webservice.restful.service;

import com.platform7.pma.product.PMAProduct;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;

public interface IProductService
{        
    PMAProduct getById(Integer id);

    Integer getTotalCount(FilterCriteria filter);

    QueryResult<PMAProduct> getList(FilterCriteria filter,PagingCriteria paging);
    
    void save(PMAProduct saveobj);

    boolean update(PMAProduct updateobj);

    boolean delete(Integer id);
    
}
