/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.model;

import com.platform7.pma.application.Application;
import com.platform7.pma.card.manifestapplication.ManifestApplication;

/**
 *
 * @author thushara.pethiyagoda
 */
public class ScriptableManifestApplication
{
    private Application application;
    private ManifestApplication manApp;

    public ScriptableManifestApplication(Application application, ManifestApplication manApp)
    {
        this.application = application;
        this.manApp = manApp;
    }
    
    public ScriptableManifestApplication(ManifestApplication manApp)
    {
        this.application = manApp.getApplication();
        this.manApp = manApp;
    }

    public Application getApplication()
    {
        return application;
    }

    public ManifestApplication getManApp()
    {
        return manApp;
    } 
}
