/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.helper;

import com.platform7.pma.application.*;
import com.platform7.pma.card.*;
import com.platform7.pma.card.manifestproduct.ManifestProduct;
import com.platform7.pma.card.manifestproductpart.*;
import com.platform7.pma.multiissuer.DataContext;
import com.platform7.pma.product.*;
import com.platform7.pma.request.emvscriptrequest.*;
import com.platform7.standardinfrastructure.multiissuer.*;
import java.math.*;
import java.util.*;
import net.acointe.affina.esp.*;
import net.aconite.affina.espinterface.exceptions.*;
import net.aconite.affina.espinterface.persistence.*;
import org.eclipse.persistence.expressions.*;
import org.eclipse.persistence.queries.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class DBHelper
{

    /**
     *
     * @param name
     * <p/>
     * @return
     */
    public static String getScopeNameByName(String name)
    {
        Scope scope = getScopeByName(name);
        if (scope == null)
        {
            return null;
        }
        return scope.getName();
    }

    /**
     *
     * @param name
     * <p/>
     * @return
     */
    public static Scope getScopeByName(String name)
    {
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression exp = builder.get("name").equal(name);

        Vector v = GenericPersistentDAO.instance().
                executeReadQuery(exp, Scope.class, null, (String[]) null);
        if (v == null || v.isEmpty())
        {
            return null;
        }
        Scope scope = (Scope) v.get(0);
        return scope;
    }

    /**
     * Extracts DataContext by name.
     * <p/>
     * @param name
     * <p/>
     * @return
     */
    public static DataContext getDataContextById(String name, Scope scope)
    {
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression exp = builder.get("name").equal(name);
        Expression expScope = builder.get("productScope").equal(scope);
        Expression expSelect = exp.and(expScope);
        Vector v = GenericPersistentDAO.instance().
                executeReadQuery(expSelect, DataContext.class, null, (String[]) null);
        if (v == null || v.isEmpty())
        {
            return null;
        }
        DataContext dc = (DataContext) v.get(0);
        return dc;
    }

    /**
     *
     * @param id < p/>
     * <p/>
     * @return
     */
    public static ESPStageScriptFilter getFilterByID(BigDecimal id)
    {
        Persistent persistent = GenericPersistentDAO.instance();
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression primaryKeyExp = builder.get("primaryKey").equal(id);

        Vector res = persistent.executeReadQuery(primaryKeyExp, ESPStageScriptFilter.class,
                                                 null, (String[]) null);
        if (res.isEmpty())
        {
            return null;
        }
        ESPStageScriptFilter filter = (ESPStageScriptFilter) res.get(0);
        return filter;

    }

    /**
     *
     * @param trf
     * @param scopeId < p/>
     * <p/>
     * @return
     */
    public static ESPStageScriptFilter getFilterByTrackingReference(String trf, BigDecimal scopeId)
    {
        Persistent persistent = GenericPersistentDAO.instance();
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression trfExp = builder.get("trackingId").equal(trf);
        Expression scopeExp = builder.get("scopeOID").equal(scopeId);
        Expression selectExp = trfExp.and(scopeExp);

        Vector res = persistent.executeReadQuery(selectExp, ESPStageScriptFilter.class,
                                                 null, (String[]) null);
        if (res.isEmpty())
        {
            return null;
        }
        ESPStageScriptFilter filter = (ESPStageScriptFilter) res.get(0);
        return filter;

    }

    /**
     * This method generates an Expression based on argument list. A special case here is if psn is null then this will
     * be ignored.
     * <p/>
     * @param pan            Plastic number
     * @param psn            Pan Sequence number
     * @param expirationDate Expiration date of the card
     * @param scope          Scope of the system
     * <p/>
     * @return Expression.
     */
    public static Expression getCommonExpressionForSoftCardSelection(final String pan, final String psn,
                                                                     final long expirationDate, Scope scope)
    {
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression expPan = builder.get("plasticNumber").equal(pan);
        Expression expPsn = builder.get("panSequenceNumber").equal(psn);

        Expression expExpDate = builder.get("validTo").equal(expirationDate);
        Expression cardScope = builder.get("area").get("scope").equal(scope);
        Expression selection = expPan.and(expExpDate).and(cardScope);
        if (psn != null)
        {
            selection = selection.and(expPsn);
        }
        return selection;
    }

    /**
     *
     * @param manifestPartOid
     * <p/>
     * @return
     */
    public static PMAProductPart findPMAProductPart(Iterator<ManifestProductPart> manparts, String parId)
    {
        List<BigDecimal> manPartIdList = new ArrayList<BigDecimal>();
        while (manparts.hasNext())
        {
            ManifestProductPart partItem = manparts.next();
            manPartIdList.add(partItem.getOID());
        }

        ExpressionBuilder builder = new ExpressionBuilder();
        Expression exp = builder.get("primaryKey").in(manPartIdList);
        Vector parts = GenericPersistentDAO.instance().
                executeReadQuery(exp, PMAProductPart.class, null, (String[]) null);
        if (parts.isEmpty())
        {
            return null;
        }
        Iterator it = parts.iterator();
        while (it.hasNext())
        {
            PMAProductPart part = (PMAProductPart) it.next();
            if (part.getId().equals(parId))
            {
                return part;
            }
        }
        return null;
    }

    public static PMAProduct getPMAProductByManifestProduct(ManifestProduct manProduct)
    {
        BigDecimal oid = manProduct.getProductId();
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression exp = builder.get("primaryKey").equal(oid);
        Vector prods = GenericPersistentDAO.instance().
                executeReadQuery(exp, PMAProduct.class, null, (String[]) null);
        if (prods.isEmpty())
        {
            return null;
        }
        PMAProduct prod = (PMAProduct) prods.get(0);
        return prod;
    }
    public static PMAProductPart getPartByID(BigDecimal oid)
    {
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression exp = builder.get("primaryKey").equal(oid);
        Vector parts = GenericPersistentDAO.instance().
                executeReadQuery(exp, PMAProductPart.class, null, (String[]) null);
        if (parts.isEmpty())
        {
            return null;
        }
        PMAProductPart part = (PMAProductPart) parts.get(0);
        return part;
    }

    public static Vector getPMAProductParts(Iterator<ManifestProductPart> manparts)
    {
        List<BigDecimal> manPartIdList = new ArrayList<BigDecimal>();
        while (manparts.hasNext())
        {
            ManifestProductPart partItem = manparts.next();
            manPartIdList.add(partItem.getOID());
        }

        ExpressionBuilder builder = new ExpressionBuilder();
        Expression exp = builder.get("primaryKey").in(manPartIdList);
        Vector parts = GenericPersistentDAO.instance().
                executeReadQuery(exp, PMAProductPart.class, null, (String[]) null);
        if (parts.isEmpty())
        {
            return null;
        }
        return parts;
    }

    /**
     *
     * @param apps
     * <p/>
     * @return
     */
    public static List<Application> getScriptableApps(List apps)
    {
        List<Application> scriptableApps = new ArrayList<Application>();
        if (apps.isEmpty())
        {
            return null;
        }
        Iterator allApps = apps.iterator();
        while (allApps.hasNext())
        {
            List app = (List) allApps.next();
            Iterator appIter = app.iterator();
            while (appIter.hasNext())
            {
                Application application = (Application) appIter.next();
                if ("true".equalsIgnoreCase(application.getScriptable()))
                {
                    scriptableApps.add(application);
                }
            }
        }
        return scriptableApps;
    }
    
    public static List<Application> getTypeSafeApps(List apps)
    {
        List<Application> scriptableApps = new ArrayList<Application>();
        if (apps.isEmpty())
        {
            return null;
        }
        Iterator allApps = apps.iterator();
        while (allApps.hasNext())
        {
            List app = (List) allApps.next();
            Iterator appIter = app.iterator();
            while (appIter.hasNext())
            {
                Application application = (Application) appIter.next();                
                scriptableApps.add(application);                
            }
        }
        return scriptableApps;
    }

    public static Vector getESPScriptUpdateStatusByTrackingReference(String trackingRef, Scope scope)
    {
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression expTR = builder.get("trackingReference").equal(trackingRef);
        Expression expScope = builder.get("scopeOID").equal(scope.getPrimaryKey());
        Expression expAll = expTR.and(expScope);
        String[] partialAttributes = null;
        return GenericPersistentDAO.instance().executeReadQuery(expAll, ESPScriptUpdateStatus.class, null,
                                                                partialAttributes);
    }
}
