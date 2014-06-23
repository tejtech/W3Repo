package net.aconite.affina.espinterface.webservice.restful.service.impl;

import com.platform7.pma.request.emvscriptrequest.ESPStageScriptFilter;
import java.util.List;
import net.aconite.affina.espinterface.dao.StageScriptFilterDao;
import net.aconite.affina.espinterface.dao.model.StageScriptFilterObject;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.webservice.restful.service.IStageScriptService;
import net.aconite.affina.espinterface.webservice.restful.service.model.StageScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("stageScriptService")
public class StageScriptService implements IStageScriptService
{   
    private static final Logger logger = LoggerFactory.getLogger(StageScriptService.class.getName());

    @Autowired
    private StageScriptFilterDao stageScriptFilterDao;
        
    public StageScript submit(FilterCriteria filter, Integer maxRestageCount, Long stageScriptStartDate, Long stageScriptEndDate) 
    {                
        
        StageScript stageScript=new StageScript();
        
        List<ESPStageScriptFilter> espStageScriptFilters = stageScriptFilterDao.getList(filter, null);
        
        if(espStageScriptFilters!=null && espStageScriptFilters.size()>0)
        {
            stageScript.setIsDuplicate(true);
        }
        else
        {
            ESPStageScriptFilter stageScriptFilter=stageScriptFilterDao.add(new StageScriptFilterObject(filter,maxRestageCount,stageScriptStartDate,stageScriptEndDate));
            if(stageScriptFilter!=null)
            {
                stageScript.setTrackId(stageScriptFilter.getTrackingId());
            }
        }         
        return stageScript;
    }

    public StageScript getById(Integer id)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Integer getTotalCount(FilterCriteria filter)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public QueryResult<StageScript> getList(FilterCriteria filter, PagingCriteria paging)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Boolean save(StageScript saveobj)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Boolean update(StageScript updateobj)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Boolean delete(Integer id)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }     
    
}
