/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.model;

import com.platform7.pma.application.Application;

/**
 *
 * @author thushara.pethiyagoda
 */
public class ScriptableApplication
{
    private Application application;
    
    public ScriptableApplication(Application app)
    {
        application = app;
    }

    public Application getApplication()
    {
        return application;
    }
}
