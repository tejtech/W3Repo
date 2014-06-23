package net.aconite.affina.espinterface.webservice.restful.service;

import com.platform7.standardinfrastructure.multiissuer.Scope;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;

public interface IScopeService
{        
    Scope getById(Integer id);
    
    Scope getByName(String name); 

    Integer getTotalCount(FilterCriteria filter);

    QueryResult<Scope> getList(FilterCriteria filter,PagingCriteria paging);
    
    void save(Scope saveobj);

    boolean update(Scope updateobj);

    boolean delete(Integer id);
    
}
