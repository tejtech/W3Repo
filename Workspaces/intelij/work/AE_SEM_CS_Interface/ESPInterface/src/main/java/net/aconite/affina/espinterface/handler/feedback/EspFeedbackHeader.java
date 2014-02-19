/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.handler.feedback;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.helper.DateHelper;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 *
 * @author wakkir.muzammil
 */
public class EspFeedbackHeader 
{
    private static final Logger logger = LoggerFactory.getLogger(EspErrorHandler.class.getName());

    private VelocityEngine velocityEngine;
    
    private String espScope;
    private String espMessageEncoding;
    private String espAppModuleName;
    private String espAppServiceName;
    private String espAppServiceSuffix;
    
    private Properties showProgressMessages;
    private Properties showValidationErrors; 
    
    public EspFeedbackHeader(VelocityEngine velocityEngine)
    {
        this.velocityEngine = velocityEngine;
    }
    
    public String generateMessageHeader()
    {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(EspConstant.VT_CURRENT_DATETIME, DateHelper.getFormattedCurrentDate());
        props.put(EspConstant.VT_MODULE_NAME, getEspAppModuleName());
        props.put(EspConstant.VT_SERVICE_NAME, getEspAppServiceName());
        props.put(EspConstant.VT_APPSERVER_SUFFIX, getEspAppServiceSuffix());
        
        StringBuffer sb = new StringBuffer();
        sb.append(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/messageHeader.vm", getEspMessageEncoding(), props));
        
        return  sb.toString();

    }
    
    public String getEspAppModuleName() 
    {
        return espAppModuleName;
    }

    public void setEspAppModuleName(String espAppModuleName) 
    {
        this.espAppModuleName = espAppModuleName;
    }

    public String getEspAppServiceName() 
    {
        return espAppServiceName;
    }

    public void setEspAppServiceName(String espAppServiceName) 
    {
        this.espAppServiceName = espAppServiceName;
    }

    public String getEspAppServiceSuffix() 
    {
        return espAppServiceSuffix;
    }

    public void setEspAppServiceSuffix(String espAppServiceSuffix) 
    {
        this.espAppServiceSuffix = espAppServiceSuffix;
    }

    public VelocityEngine getVelocityEngine() 
    {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) 
    {
        this.velocityEngine = velocityEngine;
    }
    
    public String getEspScope() 
    {
        return espScope;
    }

    public void setEspScope(String espScope) 
    {
        this.espScope = espScope;
    }

    public String getEspMessageEncoding() 
    {
        return espMessageEncoding;
    }

    public void setEspMessageEncoding(String espMessageEncoding) 
    {
        this.espMessageEncoding = espMessageEncoding;
    }
    
    public Properties getShowProgressMessages() 
    {
        return showProgressMessages;
    }
    
    public void setShowProgressMessages(Properties showProgressMessages) 
    {
        this.showProgressMessages = showProgressMessages;
    }

    public Properties getShowValidationErrors() 
    {
        return showValidationErrors;
    }
    
    public void setShowValidationErrors(Properties showValidationErrors) 
    {
        this.showValidationErrors = showValidationErrors;
    }
    
    public boolean isShowProgressMessage(String key) 
    {
        boolean isShow=false;
        
        if(showProgressMessages!=null && !showProgressMessages.isEmpty())
        {
            isShow=(EspConstant.TRUE.equalsIgnoreCase(showProgressMessages.getProperty(key)));
        }
        return isShow;
    }
    
    public boolean isShowValidationError(String key) 
    {
        boolean isShow=false;
        
        if(showValidationErrors!=null && !showValidationErrors.isEmpty())
        {
            isShow=(EspConstant.TRUE.equalsIgnoreCase(showValidationErrors.getProperty(key)));
        }
        return isShow;
    }
}
