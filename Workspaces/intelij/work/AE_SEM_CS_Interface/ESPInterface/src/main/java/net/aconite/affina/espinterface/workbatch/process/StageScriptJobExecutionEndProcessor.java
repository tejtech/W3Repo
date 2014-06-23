package net.aconite.affina.espinterface.workbatch.process;

import com.platform7.standardinfrastructure.appconfig.AppConfig;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.dao.ScopeDao;
import net.aconite.affina.espinterface.dao.StageScriptFilterDao;
import net.aconite.affina.espinterface.dao.model.StageScriptFilterObject;
import net.aconite.affina.espinterface.webservice.restful.service.IScopeService;
import net.aconite.affina.espinterface.xmlmapping.affina.StageScriptAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;

/**
 * User: wakkir
 * Date: 08/03/14
 * Time: 00:50
 */
public class StageScriptJobExecutionEndProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(StageScriptJobExecutionEndProcessor.class.getName());
    
    @Autowired
    private StageScriptFilterDao stageScriptFilterDao;
    
    private String espScope;       
   
    @ServiceActivator
    public void process(Message inMessage)
    {
        // At info level, the data recorded shall be limited to the message type
        //   and its identifier (tracking reference or service instance).
        // At debug level, the complete message shall be recorded.

        MessageHeaders inHeaders = inMessage.getHeaders();
        StageScriptAlert inPayload = (StageScriptAlert) inMessage.getPayload();

        logger.debug("process : Incoming Message header: {}", inHeaders);
        logger.debug("process : Message payload: {}", inPayload);
        String filterTrackId = inPayload.getTrackingReference();
        
        
        /////////////////////////////
        
        JobLauncher jobLauncher = (JobLauncher) AppConfig.getBean("espJobLauncher");
        Job job = (Job) AppConfig.getBean("stageScriptRequestChunkJob");

        try
        {
            JobParametersBuilder builder=new JobParametersBuilder();
            //builder.addString("age", "30");
            //builder.addString("sessionId", String.valueOf(new Date().getTime()));
            builder.addString(EspConstant.WF_CONTEXT_SCOPE_NAME,espScope);
            builder.addString(EspConstant.WF_CONTEXT_FILTER_TRACKID,filterTrackId);
            JobParameters param = builder.toJobParameters();
            
            //JobParameters param = new JobParametersBuilder().addString("name", "user_c").toJobParameters();
            JobExecution execution=null;
            
            if(execution!=null && execution.isRunning())
            {
                logger.info("Job is running....");
            } 
            else
            {
                execution = jobLauncher.run(job, param);
            }
            
            if(execution!=null)
            {
                logger.debug("Exit Status : {}" , execution.getStatus());
                logger.debug("Exit Status : {}" , execution.getAllFailureExceptions());
                if(BatchStatus.COMPLETED.name().equals(execution.getStatus().name()))
                {                         
                    stageScriptFilterDao.update(new StageScriptFilterObject(espScope,filterTrackId,execution.getStatus().name()));
                }
                
            }

        }
        catch(JobInstanceAlreadyCompleteException ex)
        {
            logger.warn(ex.getMessage());
        }
        catch (Exception ex)
        {
           logger.error("ExecuteStageScriptDetail : Error occurred",ex);

        }
        
        /////////////////////////////

    }
    
    
    public String getEspScope()
    {
        return espScope;
    }

    public void setEspScope(String espScope)
    {
        this.espScope = espScope;
    }

}
