/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.scripting.request;

import com.platform7.pma.application.Application;
import com.platform7.pma.card.SoftCard;
import com.platform7.pma.card.manifestproductpart.ManifestProductPart;
import com.platform7.pma.multiissuer.DataContext;
import com.platform7.pma.product.*;
import com.platform7.pma.request.emvscriptrequest.*;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import java.math.*;
import java.util.*;
import net.aconite.affina.espinterface.cardselection.CardGenerator;
import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.exceptions.*;
import net.aconite.affina.espinterface.factory.*;
import net.aconite.affina.espinterface.helper.*;
import net.aconite.affina.espinterface.model.*;
import net.aconite.affina.espinterface.persistence.*;
import net.aconite.affina.espinterface.scripting.generic.ResultGenerator;
import net.aconite.affina.espinterface.scripting.generic.ScriptDataHolder;
import net.aconite.affina.espinterface.scripting.generic.Validator;
import net.aconite.affina.espinterface.xmlmapping.cs.SendScriptRequest;
import org.eclipse.persistence.expressions.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class SendScriptRequestValidator implements Validator<ScriptDataHolder<SendScriptRequest>, ScriptableCard>
{

    /**
     * Field for CardGenerator
     */
    private CardGenerator<ScriptableCard> cardGen = CardGeneratorFactory.getCardgenerator();

    public SendScriptRequestValidator()
    {
    }

    /**
     * Validates the generated card.
     * <p/>
     * @see Validator
     * @return Result
     */
    public Result<ScriptableCard> validate(ScriptDataHolder<SendScriptRequest> arg)
    {
        SendScriptRequest ssr = arg.getScriptData();
        Result<ScriptableCard> result;
        if (DataUtil.isNull(ssr))
        {
            return DataUtil.generateErrorResult(MsgConstant.INVALID_MESSAGE,
                                                MsgConstant.INVALID_MESSAGE_EMPTY_SCRIPT,
                                                MsgConstant.INVALID_MESSAGE);
        }

        if (DataUtil.isEmpty(ssr.getTrackingRef()))
        {
            return DataUtil.generateErrorResult(MsgConstant.INVALID_MESSAGE_EMPTY_TRACKING_REF,
                                                MsgConstant.INVALID_MESSAGE_EMPTY_TRACKING_REF,
                                                MsgConstant.INVALID_MESSAGE);
        }

        if (DataUtil.isEmpty(ssr.getCardId()))
        {
            return DataUtil.generateErrorResult(MsgConstant.INVALID_MESSAGE_EMPTY_CARD_ID,
                                                MsgConstant.INVALID_MESSAGE_EMPTY_CARD_ID,
                                                MsgConstant.INVALID_MESSAGE_EMPTY_CARD_ID);
        }
        else
        {
            String cardId = ssr.getCardId();

            boolean isHexString = cardId.matches("\\p{XDigit}+");
            if (!isHexString)
            {
                return DataUtil.createScriptValidationResult(MsgConstant.CARD_ID_HAS_INVALID_FORMAT,
                                                             MsgConstant.CARD_ID_HAS_INVALID_FORMAT,
                                                             MsgConstant.CARD_ID_HAS_INVALID_FORMAT,
                                                             ssr.getTrackingRef(), cardId);
            }
        }
        if (DataUtil.isNull(ssr.getDataContext()))
        {
            return DataUtil.createScriptValidationResult(MsgConstant.INVALID_MESSAGE_EMPTY_DATA_CONTEXT,
                                                         MsgConstant.INVALID_MESSAGE_EMPTY_DATA_CONTEXT,
                                                         MsgConstant.invalid_data_context_scope_name_datacontextname,
                                                         ssr.getTrackingRef());
        }

        if (DataUtil.isEmpty(ssr.getDataContext().getId()))
        {
            return DataUtil.createScriptValidationResult(MsgConstant.INVALID_MESSAGE_EMPTY_DATA_CONTEXT_ID,
                                                         MsgConstant.INVALID_MESSAGE_EMPTY_DATA_CONTEXT_ID,
                                                         MsgConstant.invalid_data_context_scope_name_datacontextname,
                                                         ssr.getTrackingRef(), ssr.getDataContext().getScope(),
                                                         ssr.getDataContext().getId());
        }

        if (DataUtil.isEmpty(ssr.getDataContext().getScope()))
        {
            return DataUtil.createScriptValidationResult(MsgConstant.INVALID_MESSAGE_EMPTY_DATA_CONTEXT_SCOPE,
                                                         MsgConstant.INVALID_MESSAGE_EMPTY_DATA_CONTEXT_SCOPE,
                                                         MsgConstant.invalid_data_context_scope_name_datacontextname,
                                                         ssr.getTrackingRef(), ssr.getDataContext().getScope(),
                                                         ssr.getDataContext().getId());
        }

        if (DataUtil.isNull(ssr.getScriptFunction()))
        {
            return DataUtil.createScriptValidationResult(MsgConstant.INVALID_MESSAGE_EMPTY_SCRIPT_FUNCTION,
                                                         MsgConstant.INVALID_MESSAGE_EMPTY_SCRIPT_FUNCTION,
                                                         MsgConstant.INVALID_MESSAGE_EMPTY_SCRIPT_FUNCTION,
                                                         ssr.getTrackingRef());
        }
        if (DataUtil.isEmpty(ssr.getScriptFunction().getFunction()))
        {
            return DataUtil.createScriptValidationResult(MsgConstant.INVALID_MESSAGE_EMPTY_SCRIPT_FUNCTION,
                                                         MsgConstant.INVALID_MESSAGE_EMPTY_SCRIPT_FUNCTION,
                                                         MsgConstant.INVALID_MESSAGE_EMPTY_SCRIPT_FUNCTION,
                                                         ssr.getTrackingRef());
        }

        if (!DataUtil.isNull(ssr.getRestage()))
        {
            int reStageCount = ssr.getRestage().intValue();
            if (reStageCount > 245 && reStageCount < -1)
            {
                return DataUtil.generateErrorResult(MsgConstant.INVALID_MESSAGE,
                                                    MsgConstant.INVALID_MESSAGE_EMPTY_RESTAGE_COUNT,
                                                    MsgConstant.INVALID_MESSAGE);
            }
        }

        if (!DataUtil.isNull(ssr.getScriptEndDate()))
        {
        }


        if (DataUtil.isNull(ssr.getBusinessApplication()))
        {
            return DataUtil.generateErrorResult(MsgConstant.INVALID_MESSAGE_EMPTY_BUS_APP,
                                                MsgConstant.INVALID_MESSAGE_EMPTY_BUS_APP,
                                                MsgConstant.INVALID_MESSAGE_EMPTY_BUS_APP);
        }
        if (DataUtil.isEmpty(ssr.getBusinessApplication().getId()))
        {
            return DataUtil.createScriptValidationResult(MsgConstant.INVALID_MESSAGE_EMPTY_BUS_APP_NAME,
                                                         MsgConstant.INVALID_MESSAGE_EMPTY_BUS_APP_NAME,
                                                         MsgConstant.INVALID_MESSAGE_EMPTY_BUS_APP_NAME,
                                                         ssr.getTrackingRef());
        }

        Scope scope = DBHelper.getScopeByName(ssr.getDataContext().getScope());
        if (scope == null)
        {
            return DataUtil.generateErrorResult(MsgConstant.INVALID_DATA_NON_EXISTENT_SCOPE,
                                                MsgConstant.INVALID_DATA_NON_EXISTENT_SCOPE,
                                                MsgConstant.INVALID_DATA);
        }

        DataContext dc = DBHelper.getDataContextById(ssr.getDataContext().getId(), scope);
        if (dc == null)
        {
            return DataUtil.createScriptValidationResult(MsgConstant.invalid_data_context_scope_name_datacontextname,
                                                         MsgConstant.invalid_data_context_scope_name_datacontextname,
                                                         MsgConstant.invalid_data_context_scope_name_datacontextname,
                                                         ssr.getTrackingRef(), ssr.getDataContext().getScope(),
                                                         ssr.getDataContext().getId());

        }

        result = cardGen.generateValidCard(ssr.getCardId(), scope, ssr.getTrackingRef());
        if (!result.isSuccessFul())
        {
            return result;
        }
        result = validateBusinessApplicationExists(ssr.getBusinessApplication().getId(), result);
        if (!result.isSuccessFul())
        {
            return result;
        }

        result = validateBusinessFunctinoExists(ssr.getScriptFunction().getFunction(), result);

        result = getDuplicateRecordResult(result);
        if (!result.isSuccessFul())
        {
            return result;
        }

        return result;
    }

    /**
     * Validates if the business function exists.
     * <p/>
     * @return
     */
    private Result<ScriptableCard> validateBusinessApplicationExists(String businessAppId,
                                                                     Result<ScriptableCard> resultArg)
    {
        boolean hasApp = false;
        ScriptValidationException exp = DataUtil.createScriptValidationException(
                MsgConstant.business_application_is_not_associated_with_a_scriptable_application,
                MsgConstant.business_application_is_not_associated_with_a_scriptable_application,
                MsgConstant.business_application_is_not_associated_with_a_scriptable_application,
                businessAppId);
        ScriptableCard card = resultArg.getData();
        Result<ScriptableCard> result = Result.<ScriptableCard>getInstance(hasApp, exp, resultArg.getTrackingReference(),
                                                                           card);
        SoftCard softCard = card.getSoftCard();//<SoftCard>getProperty(AbstractModel.FIELD_SOFT_CARD);
        Iterator<ManifestProductPart>  parts = softCard.getManifestProduct().getParts();
        //ManifestProductPart part = softCard.getManifestProduct().findManifestPart(businessAppId);
        PMAProductPart part = findPMAProductPart(parts, businessAppId);   
        PMAProduct prod = getProduct(softCard.getManifestProduct().getProductId());
        if (part == null)
        {
            exp = DataUtil.createScriptValidationException(
                    MsgConstant.business_application_is_not_present_for_product_at_version,
                    MsgConstant.business_application_is_not_present_for_product_at_version,
                    MsgConstant.business_application_is_not_present_for_product_at_version,
                    businessAppId, prod.getId(),prod.getVersion());
            result = Result.<ScriptableCard>getInstance(hasApp, exp, resultArg.getTrackingReference(), card);
            return result;
        }

        List allApps = part.getApplications();
        List<Application> apps = getScriptableApps(allApps);        
        hasApp = apps != null && !apps.isEmpty();
        //if matching app is found test for scriptable and existence of appType and appVersion
        if (hasApp)
        {
            if (apps.size() > 1)
            {
                exp = DataUtil.createScriptValidationException(
                        MsgConstant.BUS_APP_HAS_MULTIPLE_SCRIPTABLE_APPS,
                        MsgConstant.BUS_APP_HAS_MULTIPLE_SCRIPTABLE_APPS,
                        MsgConstant.business_application_is_not_associated_with_a_scriptable_application,
                        businessAppId);
                result = Result.<ScriptableCard>getInstance(false, exp, resultArg.getTrackingReference(), card);
                return result;
            }
            Application app = apps.get(0);
            boolean isScriptable = "true".equalsIgnoreCase(app.getScriptable());
            boolean isAppTypeDfined = app.getApplicationType() != null
                    && app.getApplicationType().trim().length() > 0;
            boolean isAppversionDefined = app.getApplicationVersion() != null
                    && app.getApplicationVersion().trim().length() > 0;

            if (!isAppversionDefined)
            {
                exp = DataUtil.createScriptValidationException(
                        MsgConstant.REASON_NO_APP_VERSION,
                        MsgConstant.REASON_NO_APP_VERSION,
                        MsgConstant.business_application_is_not_associated_with_a_scriptable_application,
                        businessAppId);
                result = Result.<ScriptableCard>getInstance(false, exp, resultArg.getTrackingReference(), card);
                return result;
            }
            if (!isAppTypeDfined)
            {
                exp = DataUtil.createScriptValidationException(
                        MsgConstant.REASON_NO_APP_TYPE,
                        MsgConstant.REASON_NO_APP_TYPE,
                        MsgConstant.business_application_is_not_associated_with_a_scriptable_application,
                        businessAppId);
                result = Result.<ScriptableCard>getInstance(false, exp, resultArg.getTrackingReference(), card);
                return result;
            }
            if (app.getValidTo().getTime() < DateHelper.today().getTime()
                    || app.getValidFrom().getTime() > DateHelper.today().getTime())
            {
                exp = DataUtil.createScriptValidationException(
                        MsgConstant.business_application_at_version_is_not_within_its_validity_period,
                        MsgConstant.business_application_at_version_is_not_within_its_validity_period,
                        MsgConstant.business_application_at_version_is_not_within_its_validity_period,
                        businessAppId, app.getVersion());
                result = Result.<ScriptableCard>getInstance(false, exp, resultArg.getTrackingReference(),
                                                            card);
                return result;
            }

            if (isScriptable && isAppTypeDfined && isAppversionDefined)
            {
                ScriptableApplication sApp = new ScriptableApplication(app);
                ScriptableCard scriptableCard = new ScriptableCard(softCard, sApp);
                result = Result.<ScriptableCard>getInstance(hasApp,
                                                            resultArg.getTrackingReference(),
                                                            scriptableCard);
            }
        }

        return result;
    }
    /**
     * 
     * @param apps
     * @return 
     */
    private List<Application> getScriptableApps(List apps)
    {
        List<Application> scriptableApps = new ArrayList<Application>();
        if(apps.isEmpty())
        {
            return null;
        }
        Iterator allApps = apps.iterator();
        while(allApps.hasNext())
        {
            List app = (List) allApps.next();
            Iterator appIter = app.iterator();
            while(appIter.hasNext())
            {
                Application application = (Application) appIter.next();
                if("true".equalsIgnoreCase(application.getScriptable()))
                {
                    scriptableApps.add(application);
                }
            }
        }
        return scriptableApps;
    }
    
    private PMAProduct getProduct(BigDecimal mapProdId)
    {
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression exp = builder.get("primaryKey").equal(mapProdId);
        Vector prod = GenericPersistentDAO.instance().
                                                executeReadQuery(exp, PMAProduct.class, null, (String[]) null);
        if(prod == null)
        {
            return null;
        }        
        return (PMAProduct)prod.get(0);
    }

    /**
     * Validates business function.
     * <p/>
     * @return Result
     */
    private Result<ScriptableCard> validateBusinessFunctinoExists(String businessFcuntion,
                                                                  Result<ScriptableCard> resultArg)
    {
        ScriptValidationException exp = DataUtil.createScriptValidationException(
                MsgConstant.function_is_not_present,
                MsgConstant.function_is_not_present,
                MsgConstant.function_is_not_present,
                businessFcuntion);
        ScriptableCard model = resultArg.getData();
        Result<ScriptableCard> result = Result.getInstance(false, exp, resultArg.getTrackingReference(), model);
        ESPBusinessFunction bf = new ESPBusinessFunction();
        bf.setName(businessFcuntion);

        ScriptableApplication scriptableApp = model.getApplication();//<ScriptableApplication>getProperty(
        //AbstractModel.FIELD_SCRIPTABLE_APPLICATION);
        Application app = scriptableApp.getApplication();

        ExpressionBuilder builder = new ExpressionBuilder();
        Expression nameExp = builder.get("name").equal(businessFcuntion);//should be function name
        Expression appExp = builder.get("application").equal(app);
        Expression selectExp = nameExp.and(appExp);
        Vector v = GenericPersistentDAO.instance().executeReadQuery(selectExp, ESPBusinessFunction.class,
                                                                    null, (String[]) null);
        if (v.isEmpty())
        {
            return result;
        }
        bf = (ESPBusinessFunction) v.get(0);

        BusinessFunction bFunction = new BusinessFunction(bf, businessFcuntion);

        SoftCard softCard = model.getSoftCard();//<SoftCard>getProperty(AbstractModel.FIELD_SOFT_CARD);
        ScriptableCard scriptableCard = new ScriptableCard(softCard, scriptableApp, bFunction);
        result = Result.getInstance(resultArg.isSuccessFul(), resultArg.getTrackingReference(), scriptableCard);

        return result;
    }

    /**
     * 
     * @param manifestPartOid
     * @return 
     */
    public PMAProductPart findPMAProductPart(Iterator<ManifestProductPart> manparts, String parId)
    {
        List<BigDecimal> manPartIdList = new ArrayList<BigDecimal>();
        while(manparts.hasNext())
        {
            ManifestProductPart partItem = manparts.next();
            manPartIdList.add(partItem.getOID());            
        }   
        
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression exp = builder.get("primaryKey").in(manPartIdList);
        Vector parts = GenericPersistentDAO.instance().
                                                executeReadQuery(exp, PMAProductPart.class, null, (String[]) null);
        if(parts.isEmpty())
        {
            return null;
        }
        Iterator it = parts.iterator();
        while(it.hasNext())
        {
            PMAProductPart part = (PMAProductPart) it.next();
            if(part.getId().equals(parId))
            {
                return part;
            }
        }        
        return null;
    }

    /**
     *
     * @param resultArg < p/>
     * <p/>
     * @return
     */
    private Result<ScriptableCard> getDuplicateRecordResult(Result<ScriptableCard> resultArg)
    {
        Persistent persistent = GenericPersistentDAO.instance();
        Result<ScriptableCard> result = resultArg;
        ExpressionBuilder builder = new ExpressionBuilder();
        ScriptableCard card = result.getData();
        Scope scope = card.getSoftCard().getArea().getScope();
        Expression trfExp = builder.get("trackingId").equal(resultArg.getTrackingReference());
        Expression scopeExp = builder.get("scopeOID").equal(scope.getPrimaryKey());
        Expression selectionExp = trfExp.and(scopeExp);
        Vector v = persistent.executeReadQuery(selectionExp, ESPStageScriptFilter.class, null, (String[]) null);
        if (!v.isEmpty())
        {
            ScriptValidationException vex = DataUtil.createScriptValidationException(
                    MsgConstant.tracking_ref_already_exists,
                    MsgConstant.tracking_ref_already_exists,
                    MsgConstant.tracking_ref_already_exists, resultArg.getTrackingReference(), scope.getName());
            result = Result.<ScriptableCard>getInstance(false, vex, resultArg.getTrackingReference(),
                                                        resultArg.getData());
        }
        return result;
    }
}

class InvalidMessageResult implements ResultGenerator
{

    public <T extends ResultBoundData, R extends AbstractModel> Result<R> generateResult(T arg1, R arg2)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}