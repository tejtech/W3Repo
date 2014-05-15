package net.aconite.affina.espinterface.webservice.restful.service;

import com.platform7.pma.product.PMAProductPart;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;

public interface IProductPartService
{        
    PMAProductPart getById(Integer id);

    Integer getTotalCount(FilterCriteria filter);

    QueryResult<PMAProductPart> getList(FilterCriteria filter,PagingCriteria paging);
    
    void save(PMAProductPart saveobj);

    boolean update(PMAProductPart updateobj);

    boolean delete(Integer id);
    
}
