package net.aconite.affina.espinterface.webservice.restful.service;

import com.platform7.pma.application.Application;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;

public interface IApplicationService
{        
    Application getById(Integer id);

    Integer getTotalCount(FilterCriteria filter);

    QueryResult<Application> getList(FilterCriteria filter,PagingCriteria paging);
    
    void save(Application saveobj);

    boolean update(Application updateobj);

    boolean delete(Integer id);
    
}
