/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.cardselection;

import com.platform7.pma.application.Application;
import com.platform7.pma.card.*;
import com.platform7.pma.card.CardStatus;
import com.platform7.pma.card.manifestapplication.ManifestApplication;
import com.platform7.pma.request.emvscriptrequest.*;
import java.util.Iterator;
import java.util.Vector;

import com.platform7.standardinfrastructure.multiissuer.Scope;
import net.acointe.affina.esp.*;
import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.exceptions.*;
import net.aconite.affina.espinterface.model.ScriptableApplication;
import net.aconite.affina.espinterface.model.ScriptableCard;
import net.aconite.affina.espinterface.persistence.GenericPersistentDAO;
import net.aconite.affina.espinterface.scripting.statusupdate.*;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.*;
import org.slf4j.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class SelectableCardGenerator implements CardGenerator
{
    private static final Logger logger = LoggerFactory.getLogger(ScriptUpdateProcessor.class);

    public SelectableCardGenerator()
    {
    }
    /**
     * R.5-1 Fetches an Active SoftCard matching the input parameters and returns a cut down version called
     * ScriptableCard specific to Scripting operations. Returns null if one cannot be found.
     * <p/>
     * @param pan            Personal identification number
     * @param psn            pan sequence number
     * @param expirationDate Card valid to date.
     * <p/>
     * @return ScriptableCard as explained above
     */
    public ScriptableCard generateScriptableCard(final String pan, final String psn, final long expirationDate, Scope scope)
    {
        SoftCard softCard = generateSoftCard(pan, expirationDate, scope);
        boolean isScriptableApp = false;
        boolean isMatchingPsnFound = false;
        ScriptableCard card = null;
        ScriptableApplication scriptableApp = null;
        if (softCard != null)
        {
            if(softCard.getCardStatus() != CardStatus.ACTIVE)
            {
                throw new ScriptValidationException(ScriptUpdateProcessor.getErrorMessage(MsgConstant.SUSPENED_OR_CANCELLED_CARD),
                                                    ScriptUpdateProcessor.getErrorMessage(MsgConstant.SUSPENED_OR_CANCELLED_CARD)
                                                    ,ScriptUpdateProcessor.getErrorCode(MsgConstant.SUSPENED_OR_CANCELLED_CARD));
            }
            SoftCard sc = softCard;
            //Check for Manifest APPs.

            Iterator manAppIter = sc.getCardManifest().getManifestApplications();
            Application app;
            ESPCardSetup cs;
            ESPApplicationDetail espAppDetails = null;
            while (manAppIter.hasNext())
            {
                ManifestApplication manApp = (ManifestApplication)manAppIter.next();
                app = manApp.getApplication();
                boolean isPSNConfigured = app.getPlatformStore() != null
                        && app.getPlatformStore().getElement(EspConstant.PAN_SEQUENCE_ID) != null;
                isScriptableApp = "true".equalsIgnoreCase(app.getScriptable());
                if (isPSNConfigured)
                {
                    String psnFromApp = (String) app.getPlatformStore().getElement(EspConstant.PAN_SEQUENCE_ID).getValue();
                    if (psnFromApp.equalsIgnoreCase(psn))
                    {
                        scriptableApp = new ScriptableApplication(app);
                        isMatchingPsnFound = psnFromApp.equalsIgnoreCase(psn);
                        cs = extractCardSetupRecord(pan, psn, expirationDate, scope);
                        espAppDetails = cs.getESPApplicationDetail();
                        //logger.info("Matching PSN found configured in to the Application.");
                        break;
                    }
                }
            }
            if (!isMatchingPsnFound)
            {
                //logger.info("Matching PSN not found to be configured in to the Application. So checking SoftCard.");
                isMatchingPsnFound = psn.equalsIgnoreCase(softCard.getPANSequenceNumber());
                cs = extractCardSetupRecord(pan, psn, expirationDate, scope);
                if(cs != null)
                {
                    espAppDetails = cs.getESPApplicationDetail();
                    if(espAppDetails!=null)
                    {
                        app = espAppDetails.getApplication();
                        isScriptableApp = "true".equalsIgnoreCase(app.getScriptable());
                        scriptableApp = new ScriptableApplication(app);
                    }
                }
            }
            if (!isScriptableApp || !isMatchingPsnFound)
            {
                logger.error("PAN = " + pan + ", Matching PSN found = " + isMatchingPsnFound +
                        ", app is scriptable = " + isScriptableApp);
                return null;
            }
            //logger.info("Matching PSN found and the app is scriptable.");
            long accountId = sc.get_t1Account().getPrimaryKey().longValue();
            long expnDate = sc.getValidTo().getTime();
            card = new ScriptableCard(accountId, sc.getPlasticNumber(), sc.getTextualName(), sc.getCardStatus(),
                                      psn, sc.getCardId(), expnDate, sc, scriptableApp, espAppDetails);
        }
        return card;
    }

    /**
     * Returns a SoftCard matching PAN, PSN and Expiration Date. If no match is found then returns null.
     *
     * This method throws ScriptValidationException a RuntimeException
     * <p/>
     * @param pan            PAN
     * @param psn            Pan sequence number
     * @param expirationDate Card Expiration Date
     * <p/>
     * @return SoftCard as explained above
     */
    public SoftCard generateSoftCard(final String pan, final String psn, final long expirationDate, Scope scope)
    {
        ReadAllQuery rq = new ReadAllQuery();
        rq.setReferenceClass(SoftCard.class);
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression expPan = builder.get("plasticNumber").equal(pan);
        Expression expPsn = builder.get("panSequenceNumber").equal(psn);
        long expDate;
        try
        {
            expDate = AffinaEspUtils.getMonthEndDateZeroTime(expirationDate);
        }
        catch(AffinaEspUtilException ex)
        {
            throw new ScriptValidationException(ex.getMessage());
        }

        Expression expExpDate = builder.get("validTo").equal(expDate);
        Expression cardScope = builder.get("area").get("scope").equal(scope);
        Expression selection = expPan.and(expExpDate).and(cardScope);
        if (psn != null)
        {
            selection = selection.and(expPsn);
        }
        rq.setSelectionCriteria(selection);
        rq.conformResultsInUnitOfWork();
        rq.bindAllParameters();
        Vector softCards = GenericPersistentDAO.instance().executeQuery(rq);
        SoftCard sc = null;
        if (!softCards.isEmpty())
        {
            sc = (SoftCard) softCards.get(0);
        }
        return sc;
    }

    /**
     * Returns a SoftCard matching PAN and Expiration Date. If no match is found then returns null.
     * @param pan
     * @param expirationDate
     * @return SoftCard
     */
    public SoftCard generateSoftCard(String pan, long expirationDate, Scope scope)
    {
        return generateSoftCard(pan, null, expirationDate, scope);
    }

    /**
     *
     * @param pan
     * @param psn
     * @param expdate
     * @return
     */
    public ESPCardSetup extractCardSetupRecord(String pan, String psn, long expdate, Scope scope)
    {
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression expPan = builder.get("espApplicationDetail").get("pan").equal(pan);
        Expression expPsn = builder.get("espApplicationDetail").get("panSequenceNumber").equal(psn);
        Expression expExpDate = builder.get("espApplicationDetail").get("expiryDate").equal(expdate);
        Expression expScopeAppDetails = builder.get("espApplicationDetail").get("scopeOID").equal(scope.getPrimaryKey());
        Expression expScopeCardSetup = builder.get("scopeOID").equal(scope.getPrimaryKey());
        Expression selection = expPan.and(expExpDate).and(expPsn).and(expScopeAppDetails).and(expScopeCardSetup);
        Vector cardSetupList = GenericPersistentDAO.instance().executeReadQuery(selection, ESPCardSetup.class, null, (String[]) null);
        if(cardSetupList.isEmpty())
        {
            return null;
        }
        ESPCardSetup cardSetup = (ESPCardSetup) cardSetupList.get(0);
        return cardSetup;
    }
}
