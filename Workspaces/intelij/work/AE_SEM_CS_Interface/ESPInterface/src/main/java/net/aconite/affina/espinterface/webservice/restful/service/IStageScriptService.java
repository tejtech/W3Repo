package net.aconite.affina.espinterface.webservice.restful.service;

import com.platform7.pma.request.emvscriptrequest.ESPStageScriptFilter;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.webservice.restful.service.model.StageScript;

public interface IStageScriptService
{   
    StageScript submit(FilterCriteria filter, Integer maxRestageCount, Long stageScriptStartDate,Long stageScriptEndDate);
    
    StageScript getById(Integer id);

    Integer getTotalCount(FilterCriteria filter);

    QueryResult<StageScript> getList(FilterCriteria filter,PagingCriteria paging);
    
    Boolean save(StageScript saveobj);

    Boolean update(StageScript updateobj);

    Boolean delete(Integer id);
    
}
