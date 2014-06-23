package net.aconite.affina.espinterface.webservice.restful.service;

import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.webservice.restful.service.model.StageScript;

public interface IStageScriptService
{   
    long submit(FilterCriteria filter, Integer maxRestageCount, Long stageScriptStartDate,Long stageScriptEndDate);
    
    StageScript getById(Integer id);

    Integer getTotalCount(FilterCriteria filter);

    QueryResult<StageScript> getList(FilterCriteria filter,PagingCriteria paging);
    
    void save(StageScript saveobj);

    boolean update(StageScript updateobj);

    boolean delete(Integer id);
    
}
