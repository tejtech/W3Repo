/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.model;

import com.platform7.pma.application.Application;
import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.helper.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class ScriptableApplication
{
    /** */
    private Application application;
    
    /**
     * 
     * @param app 
     */
    public ScriptableApplication(Application app)
    {
        application = app;
    }

    /**
     * 
     * @return 
     */
    public Application getApplication()
    {
        return application;
    }
    /**
     * 
     * @return 
     */
    public boolean isPanSequenceConfigured()
    {
        boolean isPSNConfigured = !DataUtil.isNull(application.getPlatformStore()) && 
                                    !DataUtil.isNull(application.getPlatformStore().
                                                            getElement(EspConstant.PAN_SEQUENCE_ID));
        return isPSNConfigured;
    }
    /**
     * 
     * @return 
     */
    public boolean isScriptable()
    {
        return DataUtil.isEqualIgnoreCaseToTrue(application.getScriptable());
    }
}
