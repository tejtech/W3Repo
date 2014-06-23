/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.model;

import com.platform7.pma.application.Application;
import com.platform7.pma.card.manifestproduct.ManifestProduct;
import com.platform7.pma.product.PMAProduct;
import java.util.*;
import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.helper.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class ScriptableProduct
{

    private List<ProductPart> parts;
    private PMAProduct parent;
    private ManifestProduct manProd;

    public ScriptableProduct(List<ProductPart> parts, PMAProduct parent)
    {
        this.parts = parts;
        this.parent = parent;
    }
    
    public ScriptableProduct(List<ProductPart> parts, ManifestProduct manProd)
    {
        this.parts = parts;
        this.manProd = manProd;
        this.parent = this.manProd.getProduct();
    }

    public List<ProductPart> getParts()
    {
        return parts;
    }

    public PMAProduct getParent()
    {
        return parent;
    }

    public void addPart(ProductPart part)
    {
        parts.add(part);
    }

    /**
     * Go through the parts and get the part with the Application that has a matching PSN.
     * <p/>
     * @param psn
     * <p/>
     * @return
     */
    public ProductPart getPartByConfiguredPSN(String psn)
    {
        if (parts.isEmpty())
        {
            return null;
        }
        Iterator<ProductPart> partIt = parts.iterator();
        while (partIt.hasNext())
        {
            ProductPart part = partIt.next();
            List<Application> apps = part.getApps();
            Iterator<Application> apIt = apps.iterator();
            while (apIt.hasNext())
            {
                Application app = apIt.next();
                boolean isPSNConfigured = !DataUtil.isNull(app.getPlatformStore()) && !DataUtil.isNull(app.getPlatformStore().
                        getElement(EspConstant.PAN_SEQUENCE_ID));
                if (isPSNConfigured)
                {
                    String configuredPSN = app.getPlatformStore().getElement(EspConstant.PAN_SEQUENCE_ID).
                            getValue().toString();
                    if (configuredPSN.equals(psn))
                    {
                        return part;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 
     * @param psn
     * @param application
     * @return 
     */
    public ProductPart getPartByConfiguredPSN(String psn, Application application)
    {
        if (parts.isEmpty())
        {
            return null;
        }
        Iterator<ProductPart> partIt = parts.iterator();        
        while (partIt.hasNext())
        {
            ProductPart part = partIt.next();
            if (part.getScriptableApp(application, psn) != null)
            {
                return part;
            }
        }
        return null;
    }

    /**
     * Returns the part that has a scriptable Application
     * @return 
     */
    public ProductPart getScriptablePart()
    {
        if (parts.isEmpty())
        {
            return null;
        }
        Iterator<ProductPart> partIt = parts.iterator();
        while (partIt.hasNext())
        {
            ProductPart part = partIt.next();
            if (part.getScriptableApp() != null)
            {
                return part;
            }
        }
        return null;
    }

    /**
     * Returns the scriptable Part that has a matching application as the argument or if one is not found
     * then returns the one that has the scriptable flag set to true or else null.
     * @param application
     * @return 
     */
    public ProductPart getScriptablePart(Application application)
    {
        if (parts.isEmpty())
        {
            return null;
        }
        Iterator<ProductPart> partIt = parts.iterator();        
        while (partIt.hasNext())
        {
            ProductPart part = partIt.next();
            if (part.getScriptableApp(application) != null)
            {
                return part;
            }
        }
        return null;
    }
}
