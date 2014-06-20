package net.aconite.affina.espinterface.webservice.restful.service;

import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;

public interface IEspDispatchService<T>
{        
    T getById(Integer id);

    Integer getTotalCount(FilterCriteria filter);

    QueryResult<T> getList(FilterCriteria filter,PagingCriteria paging);
    
    void save(T saveobj);

    boolean update(T updateobj);

    boolean delete(Integer id);
    
}
