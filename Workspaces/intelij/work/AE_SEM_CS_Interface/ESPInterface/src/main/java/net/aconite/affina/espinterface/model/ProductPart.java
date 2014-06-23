/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.model;

import com.platform7.pma.application.Application;
import com.platform7.pma.card.manifestproductpart.ManifestProductPart;
import com.platform7.pma.product.PMAProductPart;
import java.util.Iterator;
import java.util.List;
import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.helper.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class ProductPart
{
    /***/
    private List<Application> apps;
    /***/
    private PMAProductPart parent;
    private ManifestProductPart manPart;
    /***/
    private Application currentApplication;

    /**
     * 
     * @param apps
     * @param parent 
     */
    public ProductPart(List<Application> apps, PMAProductPart parent)
    {
        this.apps = apps;
        this.parent = parent;
    }
    public ProductPart(List<Application> apps, ManifestProductPart manPart)
    {
        this.apps = apps;
        this.manPart = manPart;
        this.parent = manPart.getPart();
    }
    /**
     * 
     * @param apps
     * @param parent
     * @param currApplication 
     */
    public ProductPart(List<Application> apps, PMAProductPart parent, Application currApplication)
    {
        this.apps = apps;
        this.parent = parent;
        currentApplication = currApplication;
    }
    
    public ProductPart(List<Application> apps, ManifestProductPart manPart, Application currApplication)
    {
        this.apps = apps;
        this.manPart = manPart;
        this.parent = manPart.getPart();
        currentApplication = currApplication;
    }

    /**
     * 
     * @return returns null if current Application is not found. 
     */
    public Application getCurrentApplication()
    {
        return currentApplication;
    }
    /**
     * 
     * @return 
     */
    public boolean isCurrentApplicationValid()
    {
        return DataUtil.isNull(getCurrentApplication());
    }
    /**
     * 
     * @return 
     */
    public List<Application> getApps()
    {
        return apps;
    }

    /**
     * 
     * @return 
     */
    public PMAProductPart getParent()
    {
        return parent;
    }
    /**
     * 
     * @return 
     */
    public boolean containsMultipleScriptableApps()
    {
        Iterator<Application> appIt = getApps().iterator();
        int x = 0;
        while(appIt.hasNext())
        {
            Application app = appIt.next();
            if("true".equalsIgnoreCase(app.getScriptable()))
            {
                x++;
            }
        }
        return x > 1;
    }
    /**
     * Returns the Application that matches argument application or
     * returns the Application that is scriptable
     * @return 
     */
    public Application getScriptableApp(Application application)
    {
        Iterator<Application> appIt = getApps().iterator();   
        Application existingApp = null;
        Application scriptableApp = null;
        while(appIt.hasNext())
        {
            Application app = appIt.next();
            boolean appIsSame = application != null && app.getId().equals(application.getId());
            if(appIsSame)
            {
                existingApp = app;
                break;
            }
            if("true".equalsIgnoreCase(app.getScriptable()))
            {
                scriptableApp = app;
            }
        }
        currentApplication = existingApp != null ? existingApp : scriptableApp;
        return currentApplication;
    }
    
    /**
     * Returns the Application that matches argument application and that has a matching panSequence configured or
     * returns the Application that is scriptable and has a matching panSequence configured
     * @param application
     * @param panSequence
     * @return 
     */
    public Application getScriptableApp(Application application, String panSequence)
    {
        Iterator<Application> appIt = getApps().iterator();   
        Application existingApp = null;
        Application scriptableApp = null;
        while(appIt.hasNext())
        {
            Application app = appIt.next();
            
            boolean isPSNConfigured = !DataUtil.isNull(app.getPlatformStore()) && 
                                    !DataUtil.isNull(app.getPlatformStore().getElement(EspConstant.PAN_SEQUENCE_ID));
            if (isPSNConfigured)
            {
                String configuredPSN = app.getPlatformStore().getElement(EspConstant.PAN_SEQUENCE_ID).
                        getValue().toString();
                boolean appIsSame = application != null && app.getId().equals(application.getId());
                if (appIsSame)
                {
                    if (configuredPSN.equals(panSequence))
                    {
                        existingApp = app;
                        break;
                    }
                }
                if ("true".equalsIgnoreCase(app.getScriptable()))
                {
                    if (configuredPSN.equals(panSequence))
                    {
                        scriptableApp = app;
                    }
                }
            }
        }
        currentApplication = existingApp != null ? existingApp : scriptableApp;
        return currentApplication;
    }
    
    /**
     * 
     * @return 
     */
    public Application getScriptableApp()
    {
        Iterator<Application> appIt = getApps().iterator();        
        while(appIt.hasNext())
        {
            Application app = appIt.next();            
            if("true".equalsIgnoreCase(app.getScriptable()))
            {
                return app;
            }
        }
        return null;
    }
    /**
     * 
     * @param application
     * @return 
     */
    public boolean containsApplication(Application application)
    {
        Iterator<Application> appIt = getApps().iterator();        
        while(appIt.hasNext())
        {
            Application app = appIt.next();
            
            if(application != null && app.getId().equals(application.getId()))
            {
                return true;
            }
        }
        return false;
    }
}
