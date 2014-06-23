/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.scripting.response;

import com.platform7.pma.request.emvscriptrequest.*;
import com.platform7.standardinfrastructure.appconfig.*;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import java.math.BigDecimal;
import java.util.*;
import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.exceptions.ScriptValidationException;
import net.aconite.affina.espinterface.helper.*;
import net.aconite.affina.espinterface.model.StageScriptDetail;
import net.aconite.affina.espinterface.persistence.*;
import net.aconite.affina.espinterface.scripting.generic.Validator;
import net.aconite.affina.espinterface.scripting.request.*;
import net.aconite.affina.espinterface.xmlmapping.sem.*;
import org.eclipse.persistence.expressions.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class SendScriptResponseValidator extends Workable<StageScriptResponse, Result<StageScriptDetail>> 
                                            implements Validator<StageScriptResponse, StageScriptDetail> 
{
    /**
     * Properties.
     */    
    private PropertyHelper propertyHelper;
    
    /** Validator instance */
    private final static Workable<StageScriptResponse, Result<StageScriptDetail>> workableValidator = new SendScriptResponseValidator();

    /**
     * Constructs a no arg SendScriptResponseValidator
     */
    private SendScriptResponseValidator()
    {
        
    }
    
    /**
     * Creates an instance of a Validator
     * @return Validator
     */
    public static Workable<StageScriptResponse, Result<StageScriptDetail>> getInstance()
    {
        return workableValidator;
    }
    
    
    
    @Override
    public void doWork()
    {
        StageScriptResponse arg = getData();
        Result<StageScriptDetail> result = validate(arg);
        setResponse(result);
    }

    /**
     * Performs Validation
     * @param arg StageScriptResponse
     * <
     * p/>
     * @return Result
     */
    public Result<StageScriptDetail> validate(StageScriptResponse arg)
    {
        ESPStageScriptFilter filter;
        propertyHelper = (PropertyHelper)AppConfig.getBean("propertyHelper");
        String scopeName = propertyHelper.getEspScope();

        if (DataUtil.isEmpty(arg.getTrackingReference()))
        {
            ScriptValidationException svex = DataUtil.
                    createScriptValidationException(MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_EMPTY_TRF,
                                                    MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_EMPTY_TRF,
                                                    MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_DATA);
            return Result.getInstance(false, svex, arg.getTrackingReference(), null, scopeName);
        }

        if (DataUtil.isNull(arg.getStatus()))
        {
            ScriptValidationException svex = DataUtil.
                    createScriptValidationException(MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_INVALID_STATUS,
                                                    MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_INVALID_STATUS,
                                                    MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_DATA);
            return Result.getInstance(false, svex, arg.getTrackingReference(), null, scopeName);
        }

        if (arg.getStatus() == StatusType.ERROR)
        {
            if (DataUtil.isNull(arg.getError()))
            {
                ScriptValidationException svex = DataUtil.
                        createScriptValidationException(MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_INVALID_ERROR_STATUS,
                                                        MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_INVALID_ERROR_STATUS,
                                                        MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_DATA);
                return Result.getInstance(false, svex, arg.getTrackingReference(), null, scopeName);
            }

            if (DataUtil.isNull(arg.getError().getDescription()))
            {
                ScriptValidationException svex = DataUtil.
                        createScriptValidationException(
                        MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_EMPTY_STATUS_DESCRIPTION,
                        MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_EMPTY_STATUS_DESCRIPTION,
                        MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_DATA);
                return Result.getInstance(false, svex, arg.getTrackingReference(), null, scopeName);
            }

            if (DataUtil.isNull(arg.getError().getErrorCode()))
            {
                ScriptValidationException svex = DataUtil.
                        createScriptValidationException(MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_EMPTY_STATUS_CODE,
                                                        MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_EMPTY_STATUS_CODE,
                                                        MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_DATA);
                return Result.getInstance(false, svex, arg.getTrackingReference(), null, scopeName);
            }
        }
        
        if (arg.getStatus() == StatusType.STATUS_OK)
        {
            if (!DataUtil.isNull(arg.getError()))
            {
                ScriptValidationException svex = DataUtil.
                        createScriptValidationException(MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_INVALID_OK_STATUS,
                                                        MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_INVALID_OK_STATUS,
                                                        MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_DATA);
                return Result.getInstance(false, svex, null, StatusType.ERROR.value(), 
                                                      "", "", "", arg.getTrackingReference());
            }
        }

        Scope scope = DBHelper.getScopeByName(scopeName);
        if (scope == null)
        {
            ScriptValidationException svex = DataUtil.
                    createScriptValidationException(MsgConstant.INVALID_DATA_NON_EXISTENT_SCOPE,
                                                    MsgConstant.INVALID_DATA_NON_EXISTENT_SCOPE,
                                                    MsgConstant.INVALID_DATA_NON_EXISTENT_SCOPE);
            return Result.getInstance(false, svex, arg.getTrackingReference(), null, scopeName);
        }

        ErrorType et = arg.getError();
        String errMsg = "";
        String errCode = "";
        String errData = "";
        if (et != null)
        {
            errMsg = et.getDescription();
            errCode = et.getErrorCode();
            errData = et.getData();
        }


        Result<StageScriptDetail> result = getESPStageScriptDetail(scope.getPrimaryKey(), arg);
        if (!result.isSuccessFul())
        {
            return result;
        }
        StageScriptDetail stageScriptData = result.getData();
        ESPStageScriptDetail espStageScriptDetails = stageScriptData.getParent();                
        filter = DBHelper.getFilterByID(espStageScriptDetails.getEspStageScriptFilterOID());
        if (filter == null)
        {
            ScriptValidationException svex = DataUtil.
                    createScriptValidationException(MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_INVALID_TRACKING_REF,
                                                    MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_INVALID_TRACKING_REF,
                                                    MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_DATA);
            return Result.<StageScriptDetail>getInstance(false, svex, result.getData(), arg.getStatus().value(), 
                                                      errMsg, errCode, errData, filter.getTrackingId());
        }
        boolean doContinue = SendScriptRequestProcessor.REQUEST_SOURCE.equalsIgnoreCase(filter.getSource());
        return Result.<StageScriptDetail>getInstance(doContinue, result.getData(), arg.getStatus().value(), 
                                                      errMsg, errCode, errData, filter.getTrackingId());

    }

    /**
     *
     * @param trf
     * @param scopeName < p/>
     * <p/>
     * @return
     */
    private Result<StageScriptDetail> getESPStageScriptDetail(BigDecimal scopeId,
                                                              StageScriptResponse arg)
    {
        Persistent persistent = GenericPersistentDAO.instance();
        ExpressionBuilder builder = new ExpressionBuilder();

        Expression scopeExp = builder.get("scopeOID").equal(scopeId);
        Expression trfExp = builder.get("trackingReference").equal(arg.getTrackingReference());
        Expression exap = trfExp.and(scopeExp);
        Vector res = persistent.executeReadQuery(exap, ESPStageScriptDetail.class, null, (String[]) null);
        if (res.isEmpty())
        {
            ScriptValidationException svex = DataUtil.
                    createScriptValidationException(MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_INVALID_TRACKING_REF,
                                                    MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_INVALID_TRACKING_REF,
                                                    MsgConstant.INVALID_STAGE_SCRIPT_RESPONSE_DATA, 
                                                    arg.getTrackingReference());
            return Result.getInstance(false, svex, null, arg.getStatus().value(), 
                                                      "", "", "", arg.getTrackingReference());
        }
        ESPStageScriptDetail espSSDetail = (ESPStageScriptDetail) res.get(0);
        StageScriptDetail ssd = new StageScriptDetail(espSSDetail, espSSDetail.getScope().getName());
        return Result.getInstance(true, ssd);
    } 
    
}
