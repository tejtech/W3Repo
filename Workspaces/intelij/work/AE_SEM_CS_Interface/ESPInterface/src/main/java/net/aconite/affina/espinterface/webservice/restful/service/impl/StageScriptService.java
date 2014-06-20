package net.aconite.affina.espinterface.webservice.restful.service.impl;

import com.platform7.standardinfrastructure.appconfig.AppConfig;
import net.aconite.affina.espinterface.dao.StageScriptDao;
import net.aconite.affina.espinterface.dao.StageScriptObject;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.webservice.restful.service.IStageScriptService;
import net.aconite.affina.espinterface.webservice.restful.service.model.StageScript;
import net.aconite.common.util.MessageSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("stageScriptService")
public class StageScriptService implements IStageScriptService
{   
    private static final Logger logger = LoggerFactory.getLogger(StageScriptService.class.getName());

    //@Autowired
    //private PMAProductDao pmaProductDao;
        
    public long submit(FilterCriteria filter, Integer maxRestageCount, Long stageScriptStartDate, Long stageScriptEndDate) 
    {
        StageScriptDao stageScriptDao=new StageScriptDao();
        
        long refernceId=stageScriptDao.add(new StageScriptObject(filter,maxRestageCount,stageScriptStartDate,stageScriptEndDate));
                
        
        return refernceId;
    }
    
    public StageScript getById(Integer id) 
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

    public QueryResult<StageScript> getList(FilterCriteria filter, PagingCriteria paging) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }    

    public void save(StageScript saveobj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean update(StageScript updateobj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean delete(Integer id) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }   
    
}
