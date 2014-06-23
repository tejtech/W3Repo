/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.scripting.query;

import com.platform7.pma.request.emvscriptrequest.*;
import java.util.*;
import net.aconite.affina.espinterface.helper.*;
import net.aconite.affina.espinterface.persistence.*;
import org.eclipse.persistence.expressions.*;

/**
 * Helper class to fetch script related data.
 * @author thushara.pethiyagoda
 */
public class QueryScriptUpdateStatus
{
    /**
     *
     */
    /**
     * Fetches the ScriptUpdateStatus data.
     * @param pan PAN
     * @param psn Pan Sequence number
     * @param expDate Expiry date
     * @param appType Application type
     * @param appVersion Application version
     * @return Vector of ScriptUpdateStatus
     */
    public static Vector getScriptUpdateStatus(final String pan, final String psn,
                                                              final long expDate, final String appType,
                                                              final String appVersion)
    {
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression expPAN = builder.get("pan").equal(pan);
        Expression expPSN = builder.get("psn").equal(psn);
        Expression expEXPD = builder.get("expiryDate").equal(DateHelper.getTimestampUSFormat(expDate));
        Expression expAppType = builder.get("businessFunction").get("application").get("applicationType").equal(appType);
        Expression expAppVersion = builder.get("businessFunction").get("application").get("applicationVersion").equal(appType);
        Expression expAll = expPAN.and(expPSN).and(expEXPD);
        if(appType.trim().length() > 0)
        {
            expAll = expAll.and(expAppType);
        }
        if(appVersion.trim().length() > 0)
        {
            expAll = expAll.and(expAppVersion);
        }
        Vector v = GenericPersistentDAO.instance().
                executeReadQuery(expAll, ESPScriptUpdateStatus.class, null, (String[]) null);

        return v;
    }
}
