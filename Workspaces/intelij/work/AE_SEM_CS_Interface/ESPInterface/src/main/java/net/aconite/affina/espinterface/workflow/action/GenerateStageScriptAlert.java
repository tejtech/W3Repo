package net.aconite.affina.espinterface.workflow.action;


import com.platform7.pma.request.emvscriptrequest.ESPStageScriptFilter;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import java.util.ArrayList;
import java.util.List;
import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.dao.ScopeDao;
import net.aconite.affina.espinterface.dao.StageScriptFilterDao;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.workflow.service.IContext;
import net.aconite.affina.espinterface.workflow.service.IWorkflowAction;
import net.aconite.affina.espinterface.xmlmapping.affina.StageScriptAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: wakkir
 * Date: 08/03/14
 * Time: 00:07
 */
public class GenerateStageScriptAlert implements IWorkflowAction<StageScriptAlert>
{
    private static final Logger logger = LoggerFactory.getLogger(GenerateStageScriptAlert.class.getName());

    @Autowired
    private StageScriptFilterDao stageScriptFilterDao;
    @Autowired
    private ScopeDao scopeDao;
        
    @Override
    public List<StageScriptAlert> doAction(IContext context) throws Exception
    {  
        String scopeName=String.valueOf(context.getAttribute(EspConstant.WF_CONTEXT_SCOPE_NAME));
        String filterStatus=String.valueOf(context.getAttribute(EspConstant.WF_CONTEXT_FILTER_STATUS));
        
        Scope scope = scopeDao.getByName(scopeName);
        
        logger.debug("SendStageScriptAlert => {scopeName:{}, scopeOID:{},filterStatus:{}}",scopeName,scope.getPrimaryKey(),filterStatus);
        
        FilterCriteria filter=new FilterCriteria();
        filter.setScopeId((scope.getPrimaryKey()).intValue());
        filter.setStatus(filterStatus);
        
        List<ESPStageScriptFilter> espStageScriptFilters = stageScriptFilterDao.getList(filter, null);
        
        
        List<StageScriptAlert> alerts = new ArrayList<StageScriptAlert>();

        if(espStageScriptFilters != null)
        {
            for(ESPStageScriptFilter espStageScriptFilter : espStageScriptFilters)
            {                
                StageScriptAlert stageScriptAlert=new StageScriptAlert();
                stageScriptAlert.setTrackingReference(espStageScriptFilter.getTrackingId());
                alerts.add(stageScriptAlert);
            }
        }

        logger.debug("List<StageScriptAlert> SIZE : {}",alerts.size());
        
        return alerts;

    }
}
