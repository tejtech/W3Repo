package net.aconite.affina.espinterface.webservice.restful.service;

import com.platform7.pma.application.Application;
import com.platform7.pma.request.emvscriptrequest.ESPBusinessFunction;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;

public interface IBusinessFunctionService
{        
    ESPBusinessFunction getById(Integer id);

    Integer getTotalCount(FilterCriteria filter);

    QueryResult<ESPBusinessFunction> getList(FilterCriteria filter,PagingCriteria paging);
    
    void save(ESPBusinessFunction saveobj);

    boolean update(ESPBusinessFunction updateobj);

    boolean delete(Integer id);
    
}
