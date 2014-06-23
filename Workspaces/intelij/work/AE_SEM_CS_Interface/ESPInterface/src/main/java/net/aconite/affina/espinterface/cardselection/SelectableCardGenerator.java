/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.cardselection;

import com.platform7.pma.application.Application;
import com.platform7.pma.card.*;
import com.platform7.pma.card.CardStatus;
import com.platform7.pma.card.manifestapplication.ManifestApplication;
import com.platform7.pma.card.manifestproductpart.*;
import com.platform7.pma.product.*;
import com.platform7.pma.request.emvscriptrequest.*;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import net.acointe.affina.esp.*;
import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.exceptions.*;
import net.aconite.affina.espinterface.helper.*;
import net.aconite.affina.espinterface.model.*;
import net.aconite.affina.espinterface.persistence.GenericPersistentDAO;
import net.aconite.affina.espinterface.scripting.generic.ResultGenerator;
import net.aconite.affina.espinterface.scripting.generic.ResultGeneratorContext;
import net.aconite.affina.espinterface.scripting.statusupdate.*;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.*;
import org.slf4j.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class SelectableCardGenerator implements CardGenerator<ScriptableCard>
{

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ScriptUpdateProcessor.class);
    /**
     * A mapping to indicate a numeric value to text representation of card status.
     */
    private final static Map<Integer, String> cardStatusMap = new HashMap<Integer, String>();
    /**
     * Instance of this object.
     */
    private static final CardGenerator<ScriptableCard> cardGen = new SelectableCardGenerator();

    /**
     * Static block initialising cardStatusMap.
     */
    static
    {
        cardStatusMap.put(CardStatus.ACTIVE,
                          "net.aconite.affina.espinterface.cardselection.ValidCardStatusResultGenerator");
        cardStatusMap.put(CardStatus.PENDING,
                          "net.aconite.affina.espinterface.cardselection.ValidCardStatusResultGenerator");
        cardStatusMap.put(CardStatus.CANCELLED,
                          "net.aconite.affina.espinterface.cardselection.CancelledCardStatusResultGenerator");
        cardStatusMap.put(CardStatus.CANCELLED_PURGED,
                          "net.aconite.affina.espinterface.cardselection.CancelledCardStatusResultGenerator");
        cardStatusMap.put(CardStatus.EXPIRED_PURGED,
                          "net.aconite.affina.espinterface.cardselection.ExpiredCardStatusResultGenerator");
        cardStatusMap.put(CardStatus.EXPIRED,
                          "net.aconite.affina.espinterface.cardselection.ExpiredCardStatusResultGenerator");
        cardStatusMap.put(CardStatus.SUSPENDED,
                          "net.aconite.affina.espinterface.cardselection.SuspendedCardStatusResultGenerator");
        cardStatusMap.put(CardStatus.SUSPENDED_PURGED,
                          "net.aconite.affina.espinterface.cardselection.SuspendedCardStatusResultGenerator");
        /**
         * Used for status of zero.
         */
        cardStatusMap.put(CardStatus.UNKNOWN,
                          "net.aconite.affina.espinterface.cardselection.UnknownCardStatusResultGenerator");
        /**
         * null strategy is used in the case where the Card is not found.
         */
        cardStatusMap.put(null, "net.aconite.affina.espinterface.cardselection.CardNotFoundResultGenerator");
        /**
         * -1 indicates an unknown status scenario for statuses other than 0.
         */
        cardStatusMap.put(-1, "net.aconite.affina.espinterface.cardselection.UnknownCardStatusResultGenerator");
    }

    /**
     * Constructs a default SelectableCardGenerator.
     */
    private SelectableCardGenerator()
    {
    }

    /**
     *
     * @return
     */
    public static CardGenerator<ScriptableCard> getInstance()
    {
        return cardGen;
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
    public ScriptableCard generateScriptableCard(final String pan, final String psn, final long expirationDate,
                                                 Scope scope)
    {
        SoftCard softCard = generateSoftCard(pan, psn, expirationDate, scope);
        if (softCard == null)
        {
            softCard = generateSoftCard(pan, expirationDate, scope);
        }
        boolean isScriptableApp = false;
        boolean isMatchingPsnFound = false;
        boolean isPopulateScriptDataOnDemand = false;
        ScriptableCard card = null;
        ScriptableApplication scriptableApp = null;
        if (softCard != null)
        {
            if (softCard.getCardStatus() != CardStatus.ACTIVE)
            {
                throw new ScriptValidationException(ScriptUpdateProcessor.getErrorMessage(
                        MsgConstant.SUSPENED_OR_CANCELLED_CARD),
                                                    ScriptUpdateProcessor.getErrorMessage(
                        MsgConstant.SUSPENED_OR_CANCELLED_CARD), ScriptUpdateProcessor.getErrorCode(
                        MsgConstant.SUSPENED_OR_CANCELLED_CARD));
            }
            SoftCard sc = softCard;
            long accountId = sc.get_t1Account().getPrimaryKey().longValue();
            long expnDate = sc.getValidTo().getTime();
            //Check for Manifest APPs
            //Iterator<ManifestApplication> manAppIter = sc.getCardManifest().getManifestApplications();
            Application app = null, application = null;
            ScriptableManifestApplication scriptableManApp = null;
            ESPApplicationDetail espAppDetails = extractScriptableManAppDetailRecord(pan, psn, expirationDate, scope);            
            if (espAppDetails != null)
            {
                //This means a previously true application was found and we have had script updates.
                //We still needs to check that this App is still available withing Product.
                scriptableManApp = new ScriptableManifestApplication(espAppDetails.getManifestApplication());
                application = scriptableManApp.getApplication();
                isScriptableApp = true;                
            }

            ScriptableProduct scriptableProduct = getScriptableProduct(sc);
            ProductPart prodpart = scriptableProduct.getPartByConfiguredPSN(psn, application);
            if(prodpart == null)
            {   
                isMatchingPsnFound = psn.equalsIgnoreCase(softCard.getPANSequenceNumber());
                if(isMatchingPsnFound)
                {
                    prodpart = scriptableProduct.getScriptablePart(application);
                }
            }
            
            if (prodpart != null)
            {
                if (prodpart.containsMultipleScriptableApps())
                {
                    throw new ScriptValidationException("Card contains multiple scriptable applications",
                                                        "Card contains multiple scriptable applications", "-15000");
                }
                application = prodpart.getCurrentApplication();
                if (!DataUtil.isNull(application))
                {
                    Iterator<ManifestApplication> manAppIter = sc.getCardManifest().getManifestApplications();
                    scriptableManApp = getValidatedScriptableApp(manAppIter, application);
                    if (scriptableManApp != null)
                    {
                        app = scriptableManApp.getApplication();
                        scriptableApp = new ScriptableApplication(app);
                        isScriptableApp = isScriptableApp || scriptableApp.isScriptable();
                        isMatchingPsnFound = isMatchingPsnFound || scriptableApp.isPanSequenceConfigured();                        
                        isPopulateScriptDataOnDemand = (espAppDetails == null);
                    }
                }
            }
            else
            {
                isScriptableApp = false;
            }
            
            if (!isScriptableApp || !isMatchingPsnFound)
            {
                logger.error("PAN and Matching PSN found = " + isMatchingPsnFound
                        + ", app is scriptable = " + isScriptableApp);
                return null;
            }

            if (isPopulateScriptDataOnDemand)
            {
                espAppDetails = new ESPApplicationDetail();
                espAppDetails.setApplication(app);
                espAppDetails.setPan(pan);
                espAppDetails.setPanSequenceNumber(psn);
                espAppDetails.setScope(scope);
                espAppDetails.setExpiryDate(new Timestamp(expirationDate));
                espAppDetails.setDateCreated(DateHelper.today());
                espAppDetails.setManifestApplication(scriptableManApp.getManApp());
            }            
            card = new ScriptableCard(accountId, sc.getPlasticNumber(), sc.getTextualName(), sc.getCardStatus(),
                                      psn, sc.getCardId(), expnDate, sc, scriptableApp, espAppDetails);
        }
        return card;
    }

    /**
     *
     * @param manAppIter < p/>
     * <p/>
     * @return
     */
    public ScriptableManifestApplication getValidatedScriptableApp(Iterator<ManifestApplication> manAppIter,
                                                                   Application app)
    {
        while (manAppIter.hasNext())
        {
            ManifestApplication manApp = manAppIter.next();
            Application application = manApp.getApplication();

            if (app.getId().equals(application.getId()))
            {
                ScriptableManifestApplication smanApp = new ScriptableManifestApplication(application, manApp);
                return smanApp;
            }

        }
        return null;
    }

    /**
     * Extracts the PMAProduct and the parts attached to this card.
     * <p/>
     * @param softCard < p/>
     * <p/>
     * @return
     */
    private ScriptableProduct getScriptableProduct(SoftCard softCard)
    {
        List<ProductPart> prodparts = new ArrayList<ProductPart>();
        PMAProduct pmaProd = DBHelper.getPMAProductByManifestProduct(softCard.getManifestProduct());
        ScriptableProduct sproduct = new ScriptableProduct(prodparts, pmaProd);
        Iterator<ManifestProductPart> partsIt = softCard.getManifestProduct().getParts();
        //Vector pmaProdparts = DBHelper.getPMAProductParts(partsIt);
        //Iterator it = pmaProdparts.iterator();
        while (partsIt.hasNext())
        {
            ManifestProductPart part = (ManifestProductPart) partsIt.next();
            List<Application> apps = part.getScriptableApplications();//getApplications();
            if(apps != null)
            {
                ProductPart prodParts = new ProductPart(apps, part);
                sproduct.addPart(prodParts);
            }
        }
        return sproduct;
    }

    /**
     * Returns a SoftCard matching PAN, PSN and Expiration Date. If no match is found then returns null.
     * <p/>
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
        Expression selection = DBHelper.getCommonExpressionForSoftCardSelection(pan, psn, expirationDate, scope);
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
     * Gets a List of soft cards matching the given arguments.
     * <p/>
     * @param pan            PAN
     * @param psn            Pan sequence number
     * @param expirationDate Card Expiration Date
     * @param scope          System Scope
     * <p/>
     * @return Vector of available SoftCards.
     */
    public Vector generateSoftCards(String pan, String psn, long expirationDate, Scope scope)
    {
        ReadAllQuery rq = new ReadAllQuery();
        rq.setReferenceClass(SoftCard.class);
        Expression selection = DBHelper.getCommonExpressionForSoftCardSelection(pan, psn, expirationDate, scope);
        rq.setSelectionCriteria(selection);
        rq.conformResultsInUnitOfWork();
        rq.bindAllParameters();
        Vector softCards = GenericPersistentDAO.instance().executeQuery(rq);
        return softCards;
    }

    /**
     * Returns a SoftCard matching PAN and Expiration Date. If no match is found then returns null.
     * <p/>
     * @param pan
     * @param expirationDate < p/>
     * <p/>
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
     * @param expdate < p/>
     * <p/>
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
        Vector cardSetupList = GenericPersistentDAO.instance().executeReadQuery(selection, ESPCardSetup.class, null,
                                                                                (String[]) null);
        if (cardSetupList.isEmpty())
        {
            return null;
        }
        ESPCardSetup cardSetup = (ESPCardSetup) cardSetupList.get(0);
        return cardSetup;
    }

    /**
     *
     * @param pan
     * @param psn
     * @param expdate
     * @param scope   < p/>
     * <p/>
     * @return
     */
    public ESPApplicationDetail extractScriptableManAppDetailRecord(String pan, String psn, long expdate, Scope scope)
    {
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression expPan = builder.get("pan").equal(pan);
        Expression expPsn = builder.get("panSequenceNumber").equal(psn);
        Expression expExpDate = builder.get("expiryDate").equal(expdate);
        Expression expScopeAppDetails = builder.get("scopeOID").equal(scope.getPrimaryKey());
        Expression selection = expPan.and(expExpDate).and(expPsn).and(expScopeAppDetails);
        Vector cardSetupList = GenericPersistentDAO.instance().executeReadQuery(selection,
                                                                                ESPApplicationDetail.class, null,
                                                                                (String[]) null);
        if (cardSetupList.isEmpty())
        {
            return null;
        }
        ESPApplicationDetail cardSetup = (ESPApplicationDetail) cardSetupList.get(0);
        return cardSetup;
    }

    /**
     * Generates a soft card instance for a given cardId and scope. Null is returned if no card is found.
     * <p/>
     * @param cardId A hex string representing the cardId.
     * <p/>
     * @return SoftCard if a record is found or null.
     */
    private SoftCard generateSoftCard(String cardId, Scope scope)
    {
        ReadAllQuery rq = new ReadAllQuery();
        rq.setReferenceClass(SoftCard.class);
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression expCardId = builder.get("cardId").equal(cardId);
        Expression cardScope = builder.get("area").get("scope").equal(scope);
        Expression selection = expCardId.and(cardScope);
        Vector softCards = GenericPersistentDAO.instance().
                executeReadQuery(selection, SoftCard.class, null, (String[]) null);
        if (softCards.isEmpty())
        {
            return null;
        }
        return (SoftCard) softCards.get(0);
    }

    /**
     * Returns a Result composed of and AbstractModel as follows: 1. Card exists, has not expired, has pending or active
     * state and has business application. 2. Business application has AppVersion and AppType configured. 3. Business
     * function exists 4. Application is scriptable.
     * <p/>
     * @param cardId
     * @param scope  < p/>
     * <p/>
     * @return
     */
    public Result<ScriptableCard> generateValidCard(String cardId, Scope scope, String trackingReference)
    {
        SoftCard sc = generateSoftCard(cardId, scope);
        ScriptableCard scriptableCard = new ScriptableCard(sc, null);
        Result<ScriptableCard> result;
        String resultGenClass;

        //Card id is not found.
        if (sc == null)
        {
            resultGenClass = cardStatusMap.get(null);
        }
        else
        {
            //2.Expired, Cancelled, Suspended and Unknown
            resultGenClass = cardStatusMap.get(sc.getCurrentRuntimeCardStatus());
            if (resultGenClass == null)
            {
                resultGenClass = cardStatusMap.get(-1);
            }
        }
        ResultBoundData rbd = new ResultBoundData(cardId, scope.getName(), "", "", trackingReference,
                                                  "", null, "", "", "", "");
        ResultGeneratorContext resultGenContext = new ResultGeneratorContext(resultGenClass);
        result = resultGenContext.<ResultBoundData, ScriptableCard>generateResult(rbd, scriptableCard);
        return result;
    }

    /**
     * Creates an instance of ResultGenerator reflectively.
     * <p/>
     * @param <T>
     * @param className < p/>
     * <p/>
     * @return
     */
    public static ResultGenerator createInstance(String className)
    {
        try
        {
            return (ResultGenerator) (Class.forName(className).newInstance());
        }
        catch (Exception ex)
        {
            logger.error("Unable to create a ResultGenerator", ex);
            return null;
        }
    }
}

/**
 *
 * @author thushara.pethiyagoda
 */
class CardNotFoundResultGenerator implements ResultGenerator
{

    /**
     * Constructs an ExpiredCardResultGenerator.
     * <p/>
     * @param trackingRef
     */
    public CardNotFoundResultGenerator()
    {
    }

    /**
     * @see ResultGenerator
     */
    public <T extends ResultBoundData, R extends AbstractModel> Result<R> generateResult(T arg1, R arg2)
    {
        ScriptProcessingRuntimeException exp = new ScriptableCardNotFoundException(
                DataUtil.getErrorMessage(MsgConstant.card_does_not_exist,
                                         arg1.getProperty(ResultBoundData.FIELD_CARD_ID)),
                DataUtil.getErrorCode(MsgConstant.card_does_not_exist));
        Result<R> result = Result.getInstance(false, exp, "", DataUtil.getErrorCode(MsgConstant.card_does_not_exist),
                                              arg1.getProperty(ResultBoundData.FIELD_TRACKING_REF), arg2);
        return result;
    }
}

/**
 *
 * @author thushara.pethiyagoda
 */
class ExpiredCardStatusResultGenerator implements ResultGenerator
{

    /**
     * Constructs an ExpiredCardResultGenerator.
     * <p/>
     * @param trackingRef
     */
    public ExpiredCardStatusResultGenerator()
    {
    }

    /**
     * @see ResultGenerator
     */
    public <T extends ResultBoundData, R extends AbstractModel> Result<R> generateResult(T arg1, R arg2)
    {
        ScriptProcessingRuntimeException exp = new ScriptableCardNotFoundException(
                DataUtil.getErrorMessage(MsgConstant.card_status_expired,
                                         arg1.getProperty(ResultBoundData.FIELD_CARD_ID)),
                DataUtil.getErrorCode(MsgConstant.card_status_expired));
        Result<R> result = Result.getInstance(false, exp, "",
                                              DataUtil.getErrorCode(MsgConstant.card_status_expired),
                                              arg1.getProperty(ResultBoundData.FIELD_TRACKING_REF), arg2);
        return result;
    }
}

/**
 *
 * @author thushara.pethiyagoda
 */
class ValidCardStatusResultGenerator implements ResultGenerator
{

    /**
     * Constructs an SuspendedCardResultGenerator.
     * <p/>
     * @param trackingRef
     */
    public ValidCardStatusResultGenerator()
    {
    }

    /**
     * @see ResultGenerator
     */
    public <T extends ResultBoundData, R extends AbstractModel> Result<R> generateResult(T arg1, R arg2)
    {
        Result<R> result = Result.getInstance(true, null, "Valid Card found", "0",
                                              arg1.getProperty(ResultBoundData.FIELD_TRACKING_REF), arg2);
        return result;
    }
}

/**
 *
 * @author thushara.pethiyagoda
 */
class SuspendedCardStatusResultGenerator implements ResultGenerator
{

    /**
     * Constructs an SuspendedCardResultGenerator.
     * <p/>
     * @param trackingRef
     */
    public SuspendedCardStatusResultGenerator()
    {
    }

    /**
     * @see ResultGenerator
     */
    public <T extends ResultBoundData, R extends AbstractModel> Result<R> generateResult(T arg1, R arg2)
    {
        ScriptProcessingRuntimeException exp = new ScriptableCardNotFoundException(
                DataUtil.getErrorMessage(MsgConstant.card_status_suspended, arg1.getProperty(
                ResultBoundData.FIELD_CARD_ID)),
                DataUtil.getErrorCode(MsgConstant.card_status_suspended));
        Result<R> result = Result.getInstance(false, exp, "",
                                              DataUtil.getErrorCode(MsgConstant.card_status_suspended),
                                              arg1.getProperty(ResultBoundData.FIELD_TRACKING_REF),
                                              arg2);
        return result;
    }
}

/**
 *
 * @author thushara.pethiyagoda
 */
class CancelledCardStatusResultGenerator implements ResultGenerator
{

    /**
     * Constructs an CancelledCardResultGenerator for a given tracking reference.
     * <p/>
     * @param trackingRef
     */
    public CancelledCardStatusResultGenerator()
    {
    }

    /**
     * @see ResultGenerator
     */
    public <T extends ResultBoundData, R extends AbstractModel> Result<R> generateResult(T arg1, R arg2)
    {
        ScriptProcessingRuntimeException exp = new ScriptableCardNotFoundException(
                DataUtil.getErrorMessage(MsgConstant.card_status_cancelled,
                                         arg1.getProperty(ResultBoundData.FIELD_CARD_ID)),
                DataUtil.getErrorCode(MsgConstant.card_status_cancelled));
        Result<R> result = Result.getInstance(false, exp, "",
                                              DataUtil.getErrorCode(MsgConstant.card_status_cancelled),
                                              arg1.getProperty(ResultBoundData.FIELD_TRACKING_REF),
                                              arg2);
        return result;
    }
}

/**
 *
 * @author thushara.pethiyagoda
 */
class UnknownCardStatusResultGenerator implements ResultGenerator
{

    /**
     * Constructs an CancelledCardResultGenerator for a given tracking reference.
     * <p/>
     * @param trackingRef
     */
    public UnknownCardStatusResultGenerator()
    {
    }

    /**
     * @see ResultGenerator
     */
    public <T extends ResultBoundData, R extends AbstractModel> Result<R> generateResult(T arg1, R arg2)
    {
        ScriptProcessingRuntimeException exp = new ScriptableCardNotFoundException(
                DataUtil.getErrorMessage(MsgConstant.invalid_card_status),
                DataUtil.getErrorCode(MsgConstant.invalid_card_status));
        Result<R> result = Result.getInstance(false, exp, "",
                                              DataUtil.getErrorCode(MsgConstant.invalid_card_status),
                                              arg1.getProperty(ResultBoundData.FIELD_TRACKING_REF), arg2);
        return result;
    }
}