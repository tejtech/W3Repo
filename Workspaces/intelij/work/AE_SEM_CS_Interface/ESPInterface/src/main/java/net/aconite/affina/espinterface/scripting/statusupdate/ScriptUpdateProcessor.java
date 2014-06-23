package net.aconite.affina.espinterface.scripting.statusupdate;

import com.platform7.pma.application.Application;
import com.platform7.pma.card.SoftCard;
import com.platform7.pma.request.emvscriptrequest.*;
import java.sql.Timestamp;
import java.util.*;

import com.platform7.standardinfrastructure.multiissuer.Scope;
import net.acointe.affina.esp.*;
import net.aconite.affina.espinterface.cardselection.CardGenerator;
import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.exceptions.*;
import net.aconite.affina.espinterface.exceptions.util.*;
import net.aconite.affina.espinterface.factory.*;
import net.aconite.affina.espinterface.helper.*;
import net.aconite.affina.espinterface.model.*;
import net.aconite.affina.espinterface.persistence.GenericPersistentDAO;
import net.aconite.affina.espinterface.persistence.Workable;
import net.aconite.affina.espinterface.scripting.generic.*;
import net.aconite.affina.espinterface.xmlmapping.sem.*;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.*;
import org.slf4j.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class ScriptUpdateProcessor
        extends Workable<ScriptStatusUpdate, Object>
        implements ScriptProcessable<ScriptStatusUpdateDataHolder, AbstractModel>
{

    private static final Logger logger = LoggerFactory.getLogger(ScriptUpdateProcessor.class);
    /**
     * Scriptable card-generator.
     */
    private CardGenerator cardGen;
    /**
     * To indicate whether validation is successful or not.
     */
    private boolean isScriptValidationSuccessful = true;
    /**
     * Used to indicate whether script order should be checked or not.
     */
    private boolean isCheckScriptOrder;
    private Application app;
    private Scope scope;
    /**
     * Scriptable card.
     */
    private ScriptableCard sc;
    private boolean hasTransactionDetails = false;
    private boolean hasDeviceDetails = false;
    private boolean hasDeletionDetails = false;
    private boolean hasDeliveryStatus = false;
    private boolean isNewScript = false;
    private String errorData;
    private boolean enableProcessOnlyValidDataItems = true;
    private boolean enableAlarmForMixedDataItems = false;
    /**
     * A helper Map to store script status life cycles and their processing order.
     */
    private static final Map<String, Integer> statusOrderMap = new HashMap<String, Integer>();

    static
    {
        statusOrderMap.put("STAGED", 1);
        statusOrderMap.put("SENT", 2);
        statusOrderMap.put("RESENT", 3);
        statusOrderMap.put("DELETED", 4);
        statusOrderMap.put("DELIVERED", 5);
    }

    /**
     * Constructor.
     */
    private ScriptUpdateProcessor()
    {
        cardGen = CardGeneratorFactory.getCardgenerator();//new SelectableCardGenerator();
    }

    /**
     * Returns a new instance of ScriptProcessable.
     * <p/>
     * @return
     */
    public static ScriptProcessable<ScriptStatusUpdateDataHolder, AbstractModel> getProcessable()
    {
        ScriptProcessable<ScriptStatusUpdateDataHolder, AbstractModel> sp = new ScriptUpdateProcessor();
        ((ScriptUpdateProcessor) sp).addWorker(sp);
        //peristent.addTransactionalWorker((Workable) sp);
        return sp;
    }

    public void addWorker(ScriptProcessable sp)
    {
        GenericPersistentDAO.instance().addTransactionalWorker((Workable) sp);
    }

    /**
     * 1. Select soft card based on pan,psn and expiry date. (Done) 2. Validate card if its null and valid/Active.(Done)
     * 3. Check to see if tracking ref already exists. And if does then update status or create one. 4. Store Data
     * <p/>
     * This method throws ScriptableCardNotFoundException if a card cannot be found that matches PAN, PSN and the
     * Expiration Date received via the update script which is a RuntimeException.
     * <p/>
     * @param scriptData ScriptStatusUpdateDataHolder which encapsulates the XML object ScriptStatusUpdate
     */
    public synchronized Result<AbstractModel> processScript(ScriptStatusUpdateDataHolder scriptData)
    {
        Result<AbstractModel> result;
        ScriptStatusUpdate data = scriptData.getScriptData();
        try
        {
            scope = GenericPersistentDAO.instance().getScope(scriptData.getScopeName());
            //Validate Script
            logger.info("Received scripting request: Tracking reference " + data.getTrackingReference());
            //If all is well do the transaction and commit.
            this.setData(data);
            GenericPersistentDAO.instance().<ScriptStatusUpdate>doTransactionalWorkAndCommit();
            result = Result.<AbstractModel>getInstance(isScriptValidationSuccessful, null,
                                                       "Script processing completed successfully", "1",
                                                       data.getTrackingReference());
            logger.info("Scripting successful: Tracking reference " + data.getTrackingReference());
        }
        catch (ScriptValidationException esvx)
        {
            logger.error("Unable to complete Script processing successfully: " + esvx.getErrReason());
            result = Result.<AbstractModel>getInstance(isScriptValidationSuccessful, esvx,
                                                       "Unable to complete Script processing successfully.",
                                                       esvx.getErrorCode(),
                                                       data == null ? "" : data.getTrackingReference());
            result.setErrorData(errorData);
        }
        catch (ScriptableCardNotFoundException esvx)
        {
            logger.error("Unable to complete Script processing successfully: " + esvx.getMessage());
            result = Result.<AbstractModel>getInstance(isScriptValidationSuccessful, esvx,
                                                       "Unable to complete Script processing successfully.",
                                                       esvx.getErrorCode(),
                                                       data == null ? "" : data.getTrackingReference());
            result.setErrorData(errorData);
        }
        catch (PersistentException pex)
        {
            logger.error("Persistent related error.", pex);
            result = Result.<AbstractModel>getInstance(false, pex,
                                                       "Unable to complete Script processing successfully due to persistent issues.",
                                                       pex.getErrorCode(),
                                                       data == null ? "" : data.getTrackingReference());
            result.setErrorData(errorData);
        }
        finally
        {
            //peristent = null;
        }
        return result;
    }

    /**
     * Performs the actual persistent related work.
     * <p/>
     */
    public void doWork()
    {
        validateScript(getData());
        storeScriptData(getData());
    }

    /**
     * Validates the Script represented by ScriptStatusUpdate. Validates the whether a Card exist with matching PAN, PSN
     * and Expiry date.
     * <p/>
     * This methods throws ScriptValidationException and ScriptableCardNotFoundException which are themselves
     * RuntimeExceptions.
     * <p/>
     * @param ssu ScriptStatusUpdate
     */
    private void validateScript(ScriptStatusUpdate ssu) throws ScriptValidationException
    {
        if (ssu == null)
        {
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_MESSAGE),
                                                getErrorMessage(MsgConstant.INVALID_MESSAGE_EMPTY_SCRIPT),
                                                getErrorCode(MsgConstant.INVALID_MESSAGE));
        }
        if (DataUtil.isNull(ssu.getScriptUpdateStatus()))
        {
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_SCRIPT_UPDATE_STATUS),
                                                getErrorMessage(MsgConstant.INVALID_SCRIPT_UPDATE_STATUS),
                                                getErrorCode(MsgConstant.INVALID_SCRIPT_UPDATE_STATUS));
        }
        if (DataUtil.isEmpty(ssu.getSource()))
        {
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_MESSAGE),
                                                getErrorMessage(MsgConstant.INVALID_MESSAGE_EMPTY_SOURCE),
                                                getErrorCode(MsgConstant.INVALID_MESSAGE));
        }
        if (DataUtil.isEmpty(ssu.getTarget()))
        {
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_MESSAGE),
                                                getErrorMessage(MsgConstant.INVALID_MESSAGE_EMPTY_TARGET),
                                                getErrorCode(MsgConstant.INVALID_MESSAGE));
        }
        if (DataUtil.isNull(ssu.getScriptSequenceNumber()))
        {
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_MESSAGE),
                                                getErrorMessage(MsgConstant.INVALID_MESSAGE_EMPTY_SEQ_NO),
                                                getErrorCode(MsgConstant.INVALID_MESSAGE));
        }
        if (DataUtil.isNull(ssu.getAutoRetryCount()))
        {
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_MESSAGE),
                                                getErrorMessage(MsgConstant.INVALID_MESSAGE_EMPTY_RETRY_COUNT),
                                                getErrorCode(MsgConstant.INVALID_MESSAGE));
        }

        if (DataUtil.isEmpty(ssu.getTrackingReference()))
        {
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_MESSAGE),
                                                getErrorMessage(MsgConstant.INVALID_MESSAGE_EMPTY_TRACKING_REF),
                                                getErrorCode(MsgConstant.INVALID_MESSAGE));
        }

        if (DataUtil.isNull(ssu.getCard()) || DataUtil.isEmpty(ssu.getCard().getPAN()))
        {
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_MESSAGE),
                                                getErrorMessage(MsgConstant.INVALID_MESSAGE_EMPTY_PAN),
                                                getErrorCode(MsgConstant.INVALID_MESSAGE));
        }

        if (DataUtil.isEmpty(ssu.getCard().getPANSequence()) || !DataUtil.isPSNValid(ssu.getCard().getPANSequence()))
        {
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_MESSAGE),
                                                getErrorMessage(MsgConstant.INVALID_MESSAGE_EMPTY_PSN),
                                                getErrorCode(MsgConstant.INVALID_MESSAGE));
        }

        long expDate = -1;
        try
        {
            expDate = generateExpiryDate(ssu);
        }
        catch (ScriptValidationException ex)
        {
            isScriptValidationSuccessful = false;
            throw ex;
        }

        if (!DateHelper.isDate(expDate))
        {
            setErrorData("Expiry Date");
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_DATA),
                                                getErrorMessage(MsgConstant.INVALID_DATA_INVALID_EXP_DATE_FORMAT),
                                                getErrorCode(MsgConstant.INVALID_DATA));
        }

        if (!DataUtil.isDate(ssu.getDatePublished()))
        {
            setErrorData("Date Published");
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_DATA),
                                                getErrorMessage(MsgConstant.INVALID_DATA_INVALID_PUBLISHED_DATE_FORMAT),
                                                getErrorCode(MsgConstant.INVALID_DATA));
        }

        if (DataUtil.isNull(ssu.getScriptOrder()))
        {
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_DATA),
                                                getErrorMessage(MsgConstant.INVALID_MESSAGE_EMPTY_SCRIPT_ORDER),
                                                getErrorCode(MsgConstant.INVALID_DATA));
        }
        boolean isStatusValid = isValidScriptUpdateStatus(ssu);
        if (!isStatusValid)
        {
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_SCRIPT_UPDATE_STATUS),
                                                getErrorMessage(MsgConstant.INVALID_SCRIPT_UPDATE_STATUS)
                    + ": " + ssu.getScriptUpdateStatus().toString(),
                                                getErrorCode(MsgConstant.INVALID_SCRIPT_UPDATE_STATUS));
        }
        validateDeletionDetails(ssu);
        validateDeviceDetails(ssu);
        validateTransactionDetails(ssu);
        validateDeliveryStatus(ssu);

        //Locate Card R.5-1
        long cadExpdate = generateExpiryDate(ssu);
        try
        {
            sc = cardGen.generateScriptableCard(ssu.getCard().getPAN(),
                                                ssu.getCard().getPANSequence(), cadExpdate, scope);
            if (sc != null)
            {
                app = sc.getApplication().getApplication();
            }
        }
        catch (ScriptValidationException ex)
        {
            isScriptValidationSuccessful = false;
            throw ex;
        }

        //If card cannot be located log error.
        if (sc == null)
        {
            isScriptValidationSuccessful = false;
            throw new ScriptableCardNotFoundException(getErrorMessage(MsgConstant.NO_MATCHING_CARD),
                                                      getErrorMessage(MsgConstant.NO_MATCHING_CARD),
                                                      getErrorCode(MsgConstant.NO_MATCHING_CARD));
        }

        ///If non parm script having the wrong business function.


        if (ssu.getBusinessFunction() == null || ssu.getBusinessFunction().getFunctionName().trim().length() == 0)
        {
            //setErrorData("Business Function");
            isScriptValidationSuccessful = false;
            throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_MESSAGE_EMPTY_BUSINESS_FUNCTION),
                                                getErrorMessage(MsgConstant.INVALID_MESSAGE_EMPTY_BUSINESS_FUNCTION),
                                                getErrorCode(MsgConstant.INVALID_MESSAGE));
        }

        //Here the assumption is if the bus. function is configured then it is a non param business function.
        ESPBusinessFunction bf = app.getESPBusinessFunctionWithAlias(ssu.getBusinessFunction().getFunctionName());
        if (!isParameterScript(ssu) || bf != null)
        {

            if (bf == null)
            {
                setErrorData(ssu.getBusinessFunction().getFunctionName());
                isScriptValidationSuccessful = false;
                ScriptValidationException svex = new ScriptValidationException(
                        getErrorMessage(MsgConstant.UNKNOWN_BUSINESS_FUNCTION),
                         DataUtil.getErrorMessage(MsgConstant.DATA_ITEM_MISSING_NON_PARAM_SCRIPT_BUSINESS_FUNCTION),
                        getErrorCode(MsgConstant.UNKNOWN_BUSINESS_FUNCTION));
                svex.setBusinessError(false);
                throw svex;
            }
            else
            {
                if (ssu.getScriptDataItem() != null && !ssu.getScriptDataItem().isEmpty())
                {
                    List<NVPType> dataItems = ssu.getScriptDataItem();
                    String[] sb = new String[dataItems.size()];
                    String diMsg = "";
                    for (int x = 0; x < dataItems.size(); x++)
                    {
                        if (diMsg.trim().length() > 0)
                        {
                            diMsg += ",";
                        }
                        NVPType di = dataItems.get(x);
                        sb[x] = di.getName();
                        diMsg += di.getName();
                    }
                    setErrorData(diMsg);
                    ScriptValidationException svex =
                            new ScriptValidationException(DataUtil.getErrorMessage(MsgConstant.INVALID_DATA_ITEM_SUPPLIED),
                               DataUtil.getErrorMessage(MsgConstant.INVALID_DATA_ITEM_SUPPLIED_FOR_NON_PARAM_BF, diMsg),
                               DataUtil.getErrorCode(MsgConstant.INVALID_DATA_ITEM_SUPPLIED));
                    svex.setBusinessError(false);
                    throw svex;
                }
            }
        }
        else
        {
            bf = app.getESPBusinessFunctionWithAlias("Parameter Script");
            if (bf == null)
            {
                isScriptValidationSuccessful = false;
                ScriptValidationException svex = new ScriptValidationException(getErrorMessage(
                        MsgConstant.UNKNOWN_BUSINESS_FUNCTION),
                                                                               getErrorMessage(
                        MsgConstant.DEFAULT_BF_FOR_PARAM_SCRIPT_MISSING),
                                                                               getErrorCode(
                        MsgConstant.UNKNOWN_BUSINESS_FUNCTION));
                svex.setBusinessError(false);
                throw svex;
            }
            else
            {
                Result<BusinessParameterList> result = getResultOfAllDefinedParametersInRequest(ssu.getScriptDataItem());
                boolean docontinue = result.isSuccessFul();
                List<NVPType> scriptData = ssu.getScriptDataItem();
                BusinessParameterList edata = result.getData();
                if (!docontinue)
                {
                    //should throw error if all items are not found or enableProcessOnlyValidDataItems = true
                    errorData = edata.getInvalidDataDescription();                    
                    {
                        isScriptValidationSuccessful = false;
                        throw new ScriptValidationException(getErrorMessage(MsgConstant.UNKNOWN_SCRIPT_DATA_ITEM),
                                                            DataUtil.getErrorMessage(
                                MsgConstant.SCRIPT_DATA_ITEM_NOT_RECOGNISED, errorData),
                                                            getErrorCode(MsgConstant.UNKNOWN_SCRIPT_DATA_ITEM));
                    }
                }
                else
                {
                    if (enableProcessOnlyValidDataItems && edata.hasInvalidData() 
                                                    && edata.getInvalidDataCount() != scriptData.size())
                    {
                        errorData = edata.getInvalidDataDescription(); 
                        logger.warn(getErrorMessage(MsgConstant.UNKNOWN_SCRIPT_DATA_ITEM) + " " + errorData);                        
                    }
                }
            }
        }
    }

    /**
     *
     * @param ssu < p/>
     * <p/>
     * @return
     */
    private long generateExpiryDate(ScriptStatusUpdate ssu)
    {
        long expDate = 0;
        try
        {
            expDate = AffinaEspUtils.getMonthendDateZeroTime(ssu.getCard().getExpirationYear(),
                                                             ssu.getCard().getExpirationMonth());
        }
        catch (AffinaEspUtilException ex)
        {
            setErrorData("Expiry Date");
            logger.error(ex.getMessage());
            throw new ScriptValidationException(DataUtil.getErrorMessage(MsgConstant.INVALID_DATA, "Expiry Date"),
                                                getErrorMessage(MsgConstant.INVALID_DATA_INVALID_EXP_DATE_FORMAT),
                                                getErrorCode(MsgConstant.INVALID_DATA));
        }
        return expDate;
    }

    /**
     *
     * @param errData
     */
    private void setErrorData(String errData)
    {
        errorData = errData;
    }

    /**
     * Updates script status.
     */
    private void updateScriptStatus(ESPScriptUpdateStatus data, ScriptStatusUpdate ssu)
    {
        data.setScriptLifecycleStatus(ssu.getScriptUpdateStatus().value());
        if (hasScriptDeliveryStatus(ssu))
        {
            data.setScriptDeliveryStatus(ssu.getScriptDeliveryStatus().getDeliveryStatus());
        }
        //Do store delete Details and others
    }

    /**
     * 1.(Ref3.2.3)Script order needs to be checked because since messages can come out of order, script-order with a
     * value lower than the script-order of a previous script can arrive later, which should be ignored as scripts with
     * a higher script-order takes precedence over one with a lower script-order.
     * <p/>
     * 2.Compare date, script description with the new arrival and they are the same as stored then discard it.
     * <p/>
     * Stores script data. If the script tracking reference already exists then the status is updated or else a new
     * record is created subject to above 1 & 2
     */
    private void storeScriptData(ScriptStatusUpdate ssu)
    {
        try
        {
            //Get the stored record that matches the TrackingReference.
            ESPScriptUpdateStatus statusUpdateData = getExistingStatusUpdateData(ssu.getTrackingReference(), scope);
            //1.Get the pan,psnexp date
            //2.See whether another ESPParameter exist with a similar PAN to the one fetched above via tracking ref.
            //3.if does not then fine and proceed. script order does not matter.
            //4.if it does then check the script order
            //Must get the soft card and test the PAN,PSN and EXP Date with the one arrived
            //And check the arived seq and if its greater than stored update else discard.
            Timestamp newDate = getScriptDate(ssu);
            ESPScriptUpdateStatus statusUpdateDataWithSameCardData = null;
            //Null statusUpdateData means that Script with this Tracking Ref does not exists yet. So create it new.
            if (statusUpdateData == null)
            {
                //If new tracking reference then its a new script.
                isNewScript = true;
                //We are here means its a new script new tracking ref. So we check for existence of card for the new TR
                statusUpdateDataWithSameCardData = getESPScriptUpdateStatusByCardData(ssu);
                if (statusUpdateDataWithSameCardData == null)
                {
                    //We are here means no card found for new TR so we check if card exists at all
                    //This will check for PAN/PSN/EXPDATE and NO TR as in the above case.
                    //statusUpdateDataWithSameCardData = getESPScriptUpdateStatusByCardDataAndNoTR(ssu);
                }
                //if new TR same SO then update. New TR different SO then create new.
                //statusUpdateDataWithSameCardData == null means TR is new and Card does not exist.
                if (statusUpdateDataWithSameCardData == null
                        || doUpdateScriptStatus(statusUpdateDataWithSameCardData.getScriptOrder(),
                                                ssu.getScriptOrder().longValue()))
                {
                    //We are here means new script with new TR does not have that card in DB
                    statusUpdateData = GenericPersistentDAO.instance().getRegisteredObject(ESPScriptUpdateStatus.class);
                    updateModel(statusUpdateData, ssu, true);
                    //Get ESP Param that matches PAN, PSN and Ex Date of the card in request (ssu above)
                    SoftCard softCard = sc.getSoftCard();
                    statusUpdateData.setSoftCard(softCard);
                }
            }
            else
            {
                if (!statusUpdateData.getESPApplicationDetail().getPan().equals(ssu.getCard().getPAN())
                        || !statusUpdateData.getESPApplicationDetail().getPanSequenceNumber().equals(
                        ssu.getCard().getPANSequence())
                        || statusUpdateData.getESPApplicationDetail().getExpiryDate().getTime() != generateExpiryDate(
                        ssu))
                {
                    isScriptValidationSuccessful = false;
                    throw new ScriptValidationException(getErrorMessage(MsgConstant.DUPLICATE_TRACKING_REFERENCE),
                                                        getErrorMessage(MsgConstant.DUPLICATE_TRACKING_REFERENCE),
                                                        getErrorCode(MsgConstant.DUPLICATE_TRACKING_REFERENCE));
                }
                else
                {
                }
            }

            /**
             * If the Record exists for the given Tracking ref then update Script State if
             * isCurrLifecycleStateEarlierOrRestagedScript returns true.
             */
            if (statusUpdateData != null)
            {
                boolean isCurrLifecycleStateEarlierOrIsRestagedScript =
                        isCurrLifecycleStateEarlierOrRestagedScript(statusUpdateData.getScriptLifecycleStatus(), ssu);
                //This means that we have a state (via script) later than currently stored in DB.
                if (isCurrLifecycleStateEarlierOrIsRestagedScript)
                {
                    updateScriptStatus(statusUpdateData, ssu);
                }
            }
            else
            {
                statusUpdateData = statusUpdateDataWithSameCardData;
            }
            //If it does not exist means, a previous update for this card does not exist,so no need to check script order.
            if (statusUpdateDataWithSameCardData != null)
            {
                isCheckScriptOrder = true;
            }
            //Another record for the card exists (indicated by isCheckScriptOrder above) and script order is heigher.
            boolean doUpdateScriptStatus = (statusUpdateData != null
                    && doUpdateScriptStatus(statusUpdateData.getScriptOrder(), ssu.getScriptOrder().longValue()))
                    || (isCheckScriptOrder
                    && doUpdateScriptStatus(statusUpdateDataWithSameCardData.getScriptOrder(),
                                            ssu.getScriptOrder().longValue()));
            if (doUpdateScriptStatus || ssu.getScriptUpdateStatus().equals(ScriptStatusUpdateType.DELETED))
            {
                //Update just the params..
                updateModel(statusUpdateData, ssu, false);
            }
        }
        catch (ScriptValidationException ex)
        {
            throw ex;
        }
    }

    /**
     * Updates the Database mapped script update date model with the relevant changes.
     * <p/>
     * @param scriptUpdateData The Model
     * @param ssu              The XML Data
     * @param createNew        Indicates whether its a new Model creation or and Update of an existing Model.
     */
    private void updateModel(ESPScriptUpdateStatus scriptUpdateData, ScriptStatusUpdate ssu,
                             boolean createNew)
    {
        Timestamp newDate = getScriptDate(ssu);
        if (createNew)
        {
            scriptUpdateData.setTrackingReference(ssu.getTrackingReference());
//            ESPApplicationDetail easpAppDetails = (ESPApplicationDetail) GenericPersistentDAO.instance().
//                                                                getRegisteredExistingObject(sc.getEaspAppDetails());
            ESPApplicationDetail easpAppDetails = sc.getEaspAppDetails();
            scriptUpdateData.setEspApplicationDetail(easpAppDetails);
            //scriptUpdateData.setSoftCard(sc.getSoftCard());
            scriptUpdateData.setSource(ssu.getSource());
            scriptUpdateData.setTarget(ssu.getTarget());
            scriptUpdateData.setScriptSequenceNumber(ssu.getScriptSequenceNumber().intValue());
            scriptUpdateData.setAutoRetryCount(ssu.getAutoRetryCount().intValue());
            scriptUpdateData.setStatus(ssu.getScriptUpdateStatus().name());
            scriptUpdateData.setDateCreated(new java.sql.Timestamp(new Date().getTime()));
            scriptUpdateData.setScope(scope);
            //scriptUpdateData.setPan(ssu.getCard().getPAN());
            //scriptUpdateData.setPsn(ssu.getCard().getPANSequence());
            //long expDate = generateExpiryDate(ssu);
            //scriptUpdateData.setExpiryDate(DateHelper.getTimestampUSFormat(expDate));
        }
        scriptUpdateData.setDatePublished(new java.sql.Timestamp(Long.parseLong(ssu.getDatePublished())));
        scriptUpdateData.setScriptDate(newDate);
        /**
         * **********SET TRANSACTION AND DELETEION DETAILS********************
         */
        if (!DataUtil.isNull(ssu.getBusinessFunction()) && !isParameterScript(ssu))
        {
            ESPBusinessFunction bf = app.getESPBusinessFunctionWithAlias(ssu.getBusinessFunction().getFunctionName());
            scriptUpdateData.setBusinessFunction(bf);
            scriptUpdateData.setScriptDescription(ssu.getBusinessFunction().getFunctionName());
        }
        else
        {
            ESPBusinessFunction bf = app.getESPBusinessFunctionWithAlias("Parameter Script");
            scriptUpdateData.setBusinessFunction(bf);
            scriptUpdateData.setScriptDescription(ssu.getBusinessFunction().getFunctionName());
        }

        if (!DataUtil.isNull(ssu.getScriptOrder()))
        {
            scriptUpdateData.setScriptOrder(ssu.getScriptOrder().longValue());
        }

        //For parameter based Scripts.
        try
        {
            changeParameterValues(scriptUpdateData, ssu);
        }
        catch (ScriptValidationException svex)
        {
            throw svex;
        }

        //This should simply be SCRIPT STATUS i.e setScriptStatus.
        if (!DataUtil.isNull(ssu.getScriptUpdateStatus()))
        {
            scriptUpdateData.setScriptLifecycleStatus(ssu.getScriptUpdateStatus().value());
        }

        if (hasScriptDeliveryStatus(ssu))
        {
            scriptUpdateData.setScriptDeliveryStatus(ssu.getScriptDeliveryStatus().getDeliveryStatus());
        }
    }

    /**
     * This method updates the EMV Parameter values fro a particular Business Function.
     * <p/>
     * @param ssu The XML Object received via the script.
     */
    private void changeParameterValues(ESPScriptUpdateStatus scriptUpdateData, ScriptStatusUpdate ssu)
    {
        if (isParameterScript(ssu))
        {
            boolean docontinue;
            List<NVPType> paramData = ssu.getScriptDataItem();
            Result<BusinessParameterList> result = getResultOfAllDefinedParametersInRequest(paramData);
            docontinue = result.isSuccessFul();
            if (docontinue)
            {
                //This is what we have configured.
                //ESPBusinessFunction bf = (ESPBusinessFunction) bfList.get(0);
                //Parameter Script
                BusinessParameterList paramDefinition = result.getData();//getESPBusinessParameters();
                List<BusinessParameter> bpList = paramDefinition.getValidBPList();//getProperty(AbstractModel.FIELD_BP_LIST);
                docontinue = !bpList.isEmpty();
                if (docontinue)
                {
                    Vector storeParams = scriptUpdateData.getScriptStatusParameters();
                    Iterator<NVPType> it = paramData.iterator();
                    updateEMVParamvalues(scriptUpdateData, storeParams, it, paramDefinition, isDeleted(ssu));
                }
                else
                {
                    logger.info("Unable to find EMV parameter: " + paramData.toString());
                }
            }
            else
            {
                isScriptValidationSuccessful = false;
                throw new ScriptValidationException(getErrorMessage(MsgConstant.UNKNOWN_SCRIPT_DATA_ITEM),
                                                    DataUtil.getErrorMessage(MsgConstant.SCRIPT_DATA_ITEM_NOT_RECOGNISED,
                                                                             extractParamterAliasesFromMessage(paramData)),
                                                    getErrorCode(MsgConstant.UNKNOWN_SCRIPT_DATA_ITEM));
            }
        }
    }

    /**
     *
     * @param mc < p/>
     * <p/>
     * @return
     */
    public static String getErrorMessage(MsgConstant mc)
    {
        return ErrorMessageBundle.getMessage(mc);
    }

    public static String getErrorCode(MsgConstant mc)
    {
        return ErrorMessageBundle.getMessageCode(mc);
    }

    /**
     * Updates EMV parameters.
     * <p/>
     * @param paramListFromRequest
     * @param storedparams
     * @param paramListFromRequest
     */
    private void updateEMVParamvalues(ESPScriptUpdateStatus scriptUpdateData,
                                      Vector storedparams,
                                      Iterator<NVPType> paramListFromRequest,
                                      BusinessParameterList configuredParamList, boolean isDeleted)
    {
        while (paramListFromRequest.hasNext())
        {
            NVPType requestParamItem = paramListFromRequest.next();
            List<BusinessParameter> bpList = configuredParamList.getValidBPList();//getProperty(AbstractModel.FIELD_BP_LIST);

            Iterator<BusinessParameter> definedParams = bpList.iterator();
            boolean isParamFound = false;
            while (definedParams.hasNext())
            {
                BusinessParameter definedInstance = definedParams.next();
                String value = definedInstance.getParent().getValueDefault();
                String paramAlias = definedInstance.getParent().getAlias();
                NVPType definedNVP = createNewType(paramAlias, value);
                boolean isParamInRequestDefined = definedNVP.getName().equals(requestParamItem.getName());
                //if defined params is in the request.
                if (isParamInRequestDefined)
                {
                    isParamFound = true;
                    if (storedparams == null || storedparams.isEmpty()
                            || !isRequestedParamStored(requestParamItem, storedparams))
                    {
                        //logger.info("Param in message " + requestParamItem.getName() + " "
                        // + "is not already stored. So continuing with storing it.");
                        ESPScriptStatusParameter pInstance = getBusinessParameterStatusInstance(true);
                        ESPBusinessParameter parentObject = definedInstance.getParent();
                        pInstance.setEspBusinessParameter(parentObject);
                        pInstance.setParameterValue(isDeleted ? null : requestParamItem.getValue());
                        pInstance.setScriptUpdateStatus(scriptUpdateData);
                        pInstance.setDateCreated(DateHelper.today());
                        scriptUpdateData.addScriptStatusParameter(pInstance);
                    }
                    else
                    {
                        Iterator storedIt = storedparams.iterator();
                        while (storedIt.hasNext())
                        {
                            ESPScriptStatusParameter storedInstance = (ESPScriptStatusParameter) storedIt.next();
                            //Convert it to NVPType so that we can compare easily.
                            NVPType storedNVP = createNewType(storedInstance.getEspBusinessParameter().getAlias(),
                                                              storedInstance.getParameterValue());
                            boolean storedParamIsDefined = storedNVP.getName().equals(definedNVP.getName());
                            //if both request and stored params are defined
                            if (storedParamIsDefined)
                            {
                                if (isDeleted)
                                {
                                    logger.info("Setting param value to default.");
                                    storedInstance.setParameterValue(null);
                                }
                                else
                                {
                                    storedInstance.setParameterValue(requestParamItem.getValue());
                                }
                            }
                        }
                    }
                }
            }
            if (!isParamFound)
            {
                //logger.info("Param in message " + requestParamItem.getName() + " is not defined. So ignoring");
            }
        }
    }

    /**
     *
     * @param paramList
     */
    private Result<BusinessParameterList> getResultOfAllDefinedParametersInRequest(List<NVPType> paramList)
    {
        List<String> paramnamesList = extractParameterAliasListFromMessage(paramList);
        List<NVPType> localParamList = new ArrayList<NVPType>(paramList);
        Vector params = getESPBusinessParameters(paramnamesList);
        boolean isSuccess = params != null && ((params.size() == paramList.size())
                || (enableProcessOnlyValidDataItems && params.size() != paramList.size()));
        BusinessParameterList bpList = new BusinessParameterList();
        if (isSuccess)
        {
            Iterator it = params.iterator();
            while (it.hasNext())
            {
                ESPBusinessParameter bp = (ESPBusinessParameter) it.next();
                BusinessParameter busparam = new BusinessParameter(bp);
                bpList.getValidBPList().add(busparam);
            }
        }

        if (params != null)
        {
            Iterator it = params.iterator();
            boolean found = false;
            while (it.hasNext())
            {
                ESPBusinessParameter bp = (ESPBusinessParameter) it.next();
                BusinessParameter busparam = new BusinessParameter(bp);
                Iterator<NVPType> pIt = localParamList.iterator();
                while (pIt.hasNext())
                {
                    NVPType nvp = pIt.next();
                    if (bp.getAlias().equals(nvp.getName()))
                    {
                        found = true;
                        pIt.remove();
                        break;  
                    }
                }                
            }
            if(!localParamList.isEmpty())
            {
                Iterator<NVPType> pIt = localParamList.iterator();
                while (pIt.hasNext())
                {
                    NVPType nvp = pIt.next();
                    ESPBusinessParameter invalidbp = new ESPBusinessParameter();
                    invalidbp.setName(nvp.getName());
                    invalidbp.setAlias(nvp.getName());
                    BusinessParameter invalidbusparam = new BusinessParameter(invalidbp);
                    bpList.getInvalidBPList().add(invalidbusparam);
                }
            }
        }
        else
        {
            Iterator<NVPType> pIt = paramList.iterator();
            while (pIt.hasNext())
            {
                NVPType nvp = pIt.next();
                ESPBusinessParameter bp = new ESPBusinessParameter();
                bp.setName(nvp.getName());
                bp.setAlias(nvp.getName());
                BusinessParameter busparam = new BusinessParameter(bp);
                bpList.getInvalidBPList().add(busparam);
                //bpList.add(bp);
            }
        }

        Result<BusinessParameterList> res = Result.<BusinessParameterList>getInstance(isSuccess, bpList);
        return res;
    }

    /**
     *
     * @param paramList < p/>
     * <p/>
     * @return
     */
    private List<String> extractParameterAliasListFromMessage(List<NVPType> paramList)
    {
        if (paramList.isEmpty())
        {
            return null;
        }
        List<String> paramnamesList = new ArrayList<String>();
        Iterator<NVPType> it = paramList.iterator();
        StringBuilder dataItems = new StringBuilder();
        while (it.hasNext())
        {
            NVPType nvpType = it.next();           
            paramnamesList.add(nvpType.getName());
        }
        return paramnamesList;
    }

    private String extractParamterAliasesFromMessage(List<NVPType> paramList)
    {
        StringBuilder sb = new StringBuilder();
        if (paramList.isEmpty())
        {
            return sb.toString();
        }
        Iterator<NVPType> it = paramList.iterator();
        while (it.hasNext())
        {
            if (sb.length() > 0)
            {
                sb.append(",");
            }
            NVPType nvpType = it.next();
            sb.append(nvpType.getName());
        }
        return sb.toString();
    }

    /**
     *
     * @param requestedParam
     * @param storedList     < p/>
     * <p/>
     * @return
     */
    private boolean isRequestedParamStored(NVPType requestedParam, Vector storedList)
    {
        Iterator it = storedList.iterator();
        boolean rParamExists = false;
        while (it.hasNext())
        {
            ESPScriptStatusParameter storedInstance = (ESPScriptStatusParameter) it.next();
            if (requestedParam.getName().equals(storedInstance.getEspBusinessParameter().getAlias()))
            {
                rParamExists = true;
                break;
            }
            else
            {
                rParamExists = false;
            }
        }
        return rParamExists;
    }

    /**
     *
     * @param name
     * @param value < p/>
     * <p/>
     * @return
     */
    private NVPType createNewType(String name, String value)
    {
        NVPType newNVP = new NVPType();
        newNVP.setName(name);
        newNVP.setValue(value);
        return newNVP;
    }

    /**
     *
     * @param createNew < p/>
     * <p/>
     * @return
     */
    private ESPScriptStatusParameter getBusinessParameterStatusInstance(boolean createNew)
    {
        ESPScriptStatusParameter parameterInstance = null;
        if (createNew)
        {
            parameterInstance = GenericPersistentDAO.instance().getRegisteredObject(ESPScriptStatusParameter.class);
        }
        return parameterInstance;

    }

    /**
     * R.8-1 Generate Script date based on Script life cycle statuses.
     * <p/>
     * @param ssu ScriptStatusUpdate
     * <p/>
     * @return Timestamp
     */
    private Timestamp getScriptDate(ScriptStatusUpdate ssu)
    {
        Timestamp dateTime = null;
        //Date For Staged scripts
        if (isStaged(ssu))
        {
            dateTime = new java.sql.Timestamp(Long.parseLong(ssu.getDatePublished()));//DateHelper.getTimestampUSFormat(DateHelper.fromStringTolong(ssu.getDatePublished()));
        }
        //For Deleted scripts
        if (isDeleted(ssu))
        {
            dateTime = new java.sql.Timestamp(Long.parseLong(ssu.getDeletionDetails().getDeletedDate())); //DateHelper.getTimestampUSFormat(DateHelper.fromStringTolong(
            //ssu.getDeletionDetails().getDeletedDate()));
        }
        //For Delivered Scripts
        if (isDelivered(ssu))
        {
            dateTime = new java.sql.Timestamp(Long.parseLong(ssu.getTransactionDetails().getTransactionDate()));
            //DateHelper.getTimestampUSFormat(DateHelper.fromStringTolong(
            //ssu.getTransactionDetails().getTransactionDate()));
        }
        //If no status yet
        if (dateTime == null)
        {
            Date d = new Date();
            dateTime = new java.sql.Timestamp(new Date().getTime());//DateHelper.getTimestampUSFormat(d);
        }
        return dateTime;
    }

    private ESPScriptUpdateStatus getExistingStatusUpdateData(String trackingRef, Scope scope)
    {
        Vector existingUpdateDataList = getESPScriptUpdateStatusByTrackingReference(trackingRef, scope);
        if (existingUpdateDataList.isEmpty())
        {
            return null;
        }
        ESPScriptUpdateStatus existingUpdateData = (ESPScriptUpdateStatus) existingUpdateDataList.get(0);
        return existingUpdateData;
    }

    /**
     * Non-parameter based scripts have the empty element <ScriptDataItem/>, whereas parameter based scripts have data
     * for this element.
     * <p/>
     * This method returns true if Script represented by ScriptStatusUpdate is a Parameter script or else returns false.
     * <p/>
     * @param data ScriptStatusUpdate XML Object
     * <p/>
     * @return True if data is a parameter script
     */
    private boolean isParameterScript(ScriptStatusUpdate data)
    {
        return data != null && data.getScriptDataItem() != null && !data.getScriptDataItem().isEmpty();
    }

    /**
     * Returns true if current script order is less than the newly arrived script order.
     * <p/>
     * @param currentScriptOrder Current Business function recorded.
     * @param arrivedScriptOrder Newly arrived status via SriptStatusUpdate message alert.
     * <p/>
     * @return true as described above.
     */
    private boolean doUpdateScriptStatus(long currentScriptOrder, long arrivedScriptOrder)
    {
        return (currentScriptOrder < arrivedScriptOrder);
    }

    /**
     * Checks to see if the currently stored script status is in an earlier life cycle state to the newly arrived state.
     * Also it checks to see if the new script is re-staged, which amounts to the current state being earlier than new.
     * <p/>
     * @param curStatus Status stored
     * @param curStatus New status as part of the request.
     * @param ssu       < p/>
     * <p/>
     * @return true or false as above.
     */
    private boolean isCurrLifecycleStateEarlierOrRestagedScript(String curStatus, ScriptStatusUpdate ssu)
    {
        int currStatusOrder = statusOrderMap.get(curStatus);
        int newStatusOrder = statusOrderMap.get(ssu.getScriptUpdateStatus().name());
        boolean isCurrStateEarlier = currStatusOrder < newStatusOrder;
        return isCurrStateEarlier || isScriptRestaged(ssu);
    }

    /**
     *
     * @param ssu < p/>
     * <p/>
     * @return
     */
    private boolean isScriptRestaged(ScriptStatusUpdate ssu)
    {
        return hasScriptDeliveryStatus(ssu)
                && ssu.getScriptDeliveryStatus().
                getDeliveryStatus().equals(SEMScriptDeliveryStatus.DELIVERY_FAILED_SCRIPT_RESTAGED.toString());
    }

    /**
     *
     * @param ssu < p/>
     * <p/>
     * @return
     */
    private boolean isStaged(ScriptStatusUpdate ssu)
    {
        return ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.STAGED;
    }

    /**
     *
     * @param ssu < p/>
     * <p/>
     * @return
     */
    private boolean isDeleted(ScriptStatusUpdate ssu)
    {
        return ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.DELETED;
    }

    /**
     *
     * @param ssu < p/>
     * <p/>
     * @return
     */
    private boolean isDelivered(ScriptStatusUpdate ssu)
    {
        return ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.DELIVERED;
    }

    /**
     *
     * @param ssu < p/>
     * <p/>
     * @return
     */
    private boolean hasScriptDeliveryStatus(ScriptStatusUpdate ssu)
    {
        boolean isDeliveryScript = ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.DELIVERED
                || ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.SENT
                || ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.RESENT;
        return isDeliveryScript && !DataUtil.isNull(ssu.getScriptDeliveryStatus());
    }

    /**
     *
     * @param ssu
     */
    private void validateTransactionDetails(ScriptStatusUpdate ssu)
    {
        if (ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.SENT
                || ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.RESENT
                || ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.DELIVERED)
        {
            if (DataUtil.isNull(ssu.getTransactionDetails()))
            {
                setErrorData("Transaction details");
                isScriptValidationSuccessful = false;
                throw new ScriptValidationException(DataUtil.getErrorMessage(MsgConstant.DATA_ITEM_MISSING),
                                                    DataUtil.getErrorMessage(MsgConstant.MISSING_TRANSACTION_DETAILS),
                                                    getErrorCode(MsgConstant.DATA_ITEM_MISSING));
            }
            else
            {
                if (!DataUtil.isDate(ssu.getTransactionDetails().getTransactionDate()))
                {
                    setErrorData("Transaction date");
                    isScriptValidationSuccessful = false;
                    throw new ScriptValidationException(DataUtil.getErrorMessage(MsgConstant.INVALID_DATA),
                                                        DataUtil.getErrorMessage(MsgConstant.INVALID_TRANSACTION_DATE),
                                                        getErrorCode(MsgConstant.INVALID_DATA));
                }
                if (DataUtil.isEmpty(ssu.getTransactionDetails().getAtc()))
                {
                    setErrorData("ATC");
                    isScriptValidationSuccessful = false;
                    throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_DATA),
                                                        getErrorMessage(MsgConstant.INVALID_TRANSACTION_ATC),
                                                        getErrorCode(MsgConstant.INVALID_DATA));
                }
                if (DataUtil.isNull(ssu.getTransactionDetails().getScriptBytes()))
                {
                    setErrorData("Script bytes");
                    isScriptValidationSuccessful = false;
                    throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_DATA),
                                                        getErrorMessage(MsgConstant.INVALID_TRANSACTION_SCRIPT_BYTES),
                                                        getErrorCode(MsgConstant.INVALID_DATA));
                }
            }
        }
        else
        {
            if (!DataUtil.isNull(ssu.getTransactionDetails()))
            {
                setErrorData("Transaction details");
                isScriptValidationSuccessful = false;
                throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_DATA_ITEM_SUPPLIED),
                                                    getErrorMessage(MsgConstant.TRANSACTION_DETAILS_DATA_NOT_EXPECTED),
                                                    getErrorCode(MsgConstant.INVALID_DATA_ITEM_SUPPLIED));
            }
        }
        hasTransactionDetails = true;
    }

    /**
     *
     * @param ssu
     */
    private void validateDeviceDetails(ScriptStatusUpdate ssu)
    {
        if (ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.SENT
                || ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.RESENT
                || ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.DELIVERED)
        {
            if (DataUtil.isNull(ssu.getDeviceDetails()))
            {
                setErrorData("Device Details");
                isScriptValidationSuccessful = false;
                throw new ScriptValidationException(getErrorMessage(MsgConstant.DATA_ITEM_MISSING),
                                                    DataUtil.getErrorMessage(MsgConstant.INVALID_DEVICE_DETAILS_SUPPLIED),
                                                    getErrorCode(MsgConstant.DATA_ITEM_MISSING));
            }
            else
            {
                //This is optional
                if (DataUtil.isEmpty(ssu.getDeviceDetails().getDeviceCapabilities()))
                {
//                    setErrorData("Device Capabilities");
//                    isScriptValidationSuccessful = false;
//                    throw new ScriptValidationException(
//                            getErrorMessage(MsgConstant.INVALID_DATA),
//                                                        getErrorMessage(MsgConstant.INVALID_DEVICE_CAPABILITIES_SUPPLIED),
//                                                        getErrorCode(MsgConstant.INVALID_DATA));
                }
                if (DataUtil.isNull(ssu.getDeviceDetails().getDeviceType()))
                {
                    setErrorData("Device Type");
                    isScriptValidationSuccessful = false;
                    throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_DATA),
                                                        DataUtil.getErrorMessage(
                            MsgConstant.INVALID_DEVICE_TYPE_SUPPLIED),
                                                        getErrorCode(MsgConstant.INVALID_DATA));
                }
            }
        }
        else
        {
            if (!DataUtil.isNull(ssu.getDeviceDetails()))
            {
                setErrorData("Device Details");
                isScriptValidationSuccessful = false;
                throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_DATA_ITEM_SUPPLIED),
                                                    getErrorMessage(MsgConstant.DEVICE_DETAILS_DATA_NOT_EXPECTED),
                                                    getErrorCode(MsgConstant.INVALID_DATA_ITEM_SUPPLIED));
            }
        }
        hasDeviceDetails = true;
    }

    private void validateDeliveryStatus(ScriptStatusUpdate ssu)
    {
        if (ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.SENT
                || ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.RESENT
                || ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.DELIVERED)
        {
            if (DataUtil.isNull(ssu.getScriptDeliveryStatus()))
            {
                isScriptValidationSuccessful = false;
                setErrorData("Script Delivery status");
                throw new ScriptValidationException(getErrorMessage(MsgConstant.DATA_ITEM_MISSING),
                                                    getErrorMessage(MsgConstant.MISSING_SCRIPT_DELIVERY_STATUS),
                                                    getErrorCode(MsgConstant.DATA_ITEM_MISSING));
            }
            else
            {
                if (DataUtil.isEmpty(ssu.getScriptDeliveryStatus().getDeliveryStatus()))
                {
                    setErrorData("Script Delivery status");
                    isScriptValidationSuccessful = false;
                    throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_DATA),
                                                        getErrorMessage(MsgConstant.INVALID_SCRIPT_DELIVERY_STATUS_SUPPLIED),
                                                        getErrorCode(MsgConstant.INVALID_DATA));
                }
                String delStatus = ssu.getScriptDeliveryStatus().getDeliveryStatus();
                if (!SEMScriptDeliveryStatus.DELIVERY_FAILED.isIn(delStatus)
                        && !SEMScriptDeliveryStatus.DELIVERY_FAILED_SCRIPT_RESTAGED.isIn(delStatus)
                        && !SEMScriptDeliveryStatus.DELIVERY_SUCCEEDED.isIn(delStatus)
                        && !SEMScriptDeliveryStatus.UNKNOWN.isIn(delStatus))
                {
                    setErrorData("Script Delivery status");
                    isScriptValidationSuccessful = false;
                    throw new ScriptValidationException(getErrorMessage(
                            MsgConstant.INVALID_DATA),
                                                        getErrorMessage(
                            MsgConstant.INVALID_SCRIPT_DELIVERY_STATUS_SUPPLIED),
                                                        getErrorCode(MsgConstant.INVALID_DATA));
                }
            }
        }
        else
        {
            if (!DataUtil.isNull(ssu.getScriptDeliveryStatus()))
            {
                setErrorData("Script Delivery status");
                isScriptValidationSuccessful = false;
                throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_DATA_ITEM_SUPPLIED),
                                                    getErrorMessage(MsgConstant.SCRIPT_DELIVERY_STATUS_DATA_NOT_EXPECTED),
                                                    getErrorCode(MsgConstant.INVALID_DATA_ITEM_SUPPLIED));
            }
        }
        hasDeliveryStatus = true;
    }

    private void validateDeletionDetails(ScriptStatusUpdate ssu)
    {
        if (ssu.getScriptUpdateStatus() == ScriptStatusUpdateType.DELETED)
        {
            if (DataUtil.isNull(ssu.getDeletionDetails()))
            {
                setErrorData("Deletion details");
                isScriptValidationSuccessful = false;
                throw new ScriptValidationException(getErrorMessage(MsgConstant.DATA_ITEM_MISSING),
                                                    getErrorMessage(MsgConstant.MISSING_DELETION_DETAILS),
                                                    getErrorCode(MsgConstant.DATA_ITEM_MISSING));
            }
            else
            {
                if (!DataUtil.isDate(ssu.getDeletionDetails().getDeletedDate()))
                {
                    setErrorData("Deletion date");
                    isScriptValidationSuccessful = false;
                    throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_DATA),
                                                        getErrorMessage(MsgConstant.INVALID_DELETION_DATE),
                                                        getErrorCode(MsgConstant.INVALID_DATA));
                }
            }
        }
        else
        {
            if (!DataUtil.isNull(ssu.getDeletionDetails()))
            {
                setErrorData("Deletion details");
                isScriptValidationSuccessful = false;
                throw new ScriptValidationException(getErrorMessage(MsgConstant.INVALID_DATA_ITEM_SUPPLIED),
                                                    getErrorMessage(
                        MsgConstant.SCRIPT_DELETION_DETAILS_DATA_NOT_EXPECTED),
                                                    getErrorCode(MsgConstant.INVALID_DATA_ITEM_SUPPLIED));
            }
        }
        hasDeletionDetails = true;
    }

    private boolean isValidScriptUpdateStatus(ScriptStatusUpdate ssu)
    {
        ScriptStatusUpdateType ssut = ssu.getScriptUpdateStatus();
        boolean isValidStatus = SEMScriptStatus.DELETED.isIn(ssut.toString())
                || SEMScriptStatus.DELIVERED.isIn(ssut.toString())
                || SEMScriptStatus.SENT.isIn(ssut.toString())
                || SEMScriptStatus.RESENT.isIn(ssut.toString())
                || SEMScriptStatus.STAGED.isIn(ssut.toString());
        return isValidStatus;
    }

    public Vector getESPScriptUpdateStatusByTrackingReference(String trackingRef, Scope scope)
    {
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression expTR = builder.get("trackingReference").equal(trackingRef);
        Expression expScope = builder.get("scopeOID").equal(scope.getPrimaryKey());
        Expression expAll = expTR.and(expScope);
        String[] partialAttributes = null;
        return GenericPersistentDAO.instance().executeReadQuery(expAll, ESPScriptUpdateStatus.class, null,
                                                                partialAttributes);
    }

    /**
     *
     * @param ssu < p/>
     * <p/>
     * @return
     */
    public ESPScriptUpdateStatus getESPScriptUpdateStatusByCardData(ScriptStatusUpdate ssu)
    {
        //Get max script order
        ReportQuery rq = new ReportQuery();
        rq.setReferenceClass(ESPScriptUpdateStatus.class);
        rq.addMaximum("scriptOrder");

//        Scope scope = GenericPersistentDAO.instance().getScope()

        ExpressionBuilder builder = new ExpressionBuilder();
        Expression expPAN = builder.get("espApplicationDetail").get("pan").equal(ssu.getCard().getPAN());
        Expression expPSN = builder.get("espApplicationDetail").get("panSequenceNumber").equal(
                ssu.getCard().getPANSequence());
        Expression expTR = builder.get("trackingReference").equal(ssu.getTrackingReference());
        long expDate = generateExpiryDate(ssu);
        Expression expEXPD = builder.get("espApplicationDetail").get("expiryDate").equal(DateHelper.getTimestampUSFormat(
                expDate));
        Expression expScope = builder.get("espApplicationDetail").get("scopeOID").equal(scope.getPrimaryKey());
        Expression expScopeSU = builder.get("scopeOID").equal(scope.getPrimaryKey());

        Expression expAll = expPAN.and(expPSN).and(expEXPD).and(expTR).and(expScope).and(expScopeSU);
        rq.setSelectionCriteria(expAll);
        rq.addGrouping(builder.get("espApplicationDetail").get("pan"));
        rq.addGrouping(builder.get("espApplicationDetail").get("panSequenceNumber"));
        rq.addGrouping(builder.get("espApplicationDetail").get("expiryDate"));
        rq.addGrouping(builder.get("trackingReference"));

        Vector v = (Vector) GenericPersistentDAO.instance().executeReportQuery(rq);
        //Get ESPScriptUpdateStatus with max sxript order.
        if (v != null && !v.isEmpty())
        {
            ReportQueryResult result = (ReportQueryResult) v.get(0);
            if (result.isEmpty())
            {
                return null;
            }
            builder = new ExpressionBuilder();
            expAll = expAll.and(builder.get("scriptOrder").equal(result.get("scriptOrder")));
            String[] partialAttributes = null;
            Vector essu = GenericPersistentDAO.instance().executeReadQuery(expAll, ESPScriptUpdateStatus.class, null,
                                                                           partialAttributes);
            if (essu.isEmpty())
            {
                return null;
            }
            return (ESPScriptUpdateStatus) essu.get(0);
        }
        return null;
    }

    public ESPScriptUpdateStatus getESPScriptUpdateStatusByCardDataAndNoTR(ScriptStatusUpdate ssu)
    {
        //Get max script order
        ReportQuery rq = new ReportQuery();
        rq.setReferenceClass(ESPScriptUpdateStatus.class);
        rq.addMaximum("scriptOrder");

        ExpressionBuilder builder = new ExpressionBuilder();
        Expression expPAN = builder.get("espApplicationDetail").get("pan").equal(ssu.getCard().getPAN());
        Expression expPSN = builder.get("espApplicationDetail").get("panSequenceNumber").equal(
                ssu.getCard().getPANSequence());

        long expDate = generateExpiryDate(ssu);
        Expression expEXPD = builder.get("espApplicationDetail").get("expiryDate").equal(DateHelper.getTimestampUSFormat(
                expDate));
        Expression expScope = builder.get("espApplicationDetail").get("scopeOID").equal(scope.getPrimaryKey());
        Expression expScopeSU = builder.get("scopeOID").equal(scope.getPrimaryKey());

        Expression expAll = expPAN.and(expPSN).and(expEXPD).and(expScope).and(expScopeSU);
        rq.setSelectionCriteria(expAll);
        rq.addGrouping(builder.get("espApplicationDetail").get("pan"));
        rq.addGrouping(builder.get("espApplicationDetail").get("panSequenceNumber"));
        rq.addGrouping(builder.get("espApplicationDetail").get("expiryDate"));

        Vector v = (Vector) GenericPersistentDAO.instance().executeReportQuery(rq);
        //Get ESPScriptUpdateStatus with max sxript order.
        if (v != null && !v.isEmpty())
        {
            ReportQueryResult result = (ReportQueryResult) v.get(0);
            if (result.isEmpty())
            {
                return null;
            }
            builder = new ExpressionBuilder();
            expAll = expAll.and(builder.get("scriptOrder").equal(result.get("scriptOrder")));
            String[] partialAttributes = null;
            Vector essu = GenericPersistentDAO.instance().executeReadQuery(expAll, ESPScriptUpdateStatus.class, null,
                                                                           partialAttributes);
            if (essu.isEmpty())
            {
                return null;
            }
            return (ESPScriptUpdateStatus) essu.get(0);
        }
        return null;
    }

    public ESPScriptUpdateStatus getScriptUpdateStatusByCardDataWithMatchingScriptOrderAndNoTR(ScriptStatusUpdate ssu)
    {
        //Get max script order
        ReportQuery rq = new ReportQuery();
        rq.setReferenceClass(ESPScriptUpdateStatus.class);
        rq.addMaximum("scriptOrder");

        ExpressionBuilder builder = new ExpressionBuilder();
        Expression expPAN = builder.get("espApplicationDetail").get("pan").equal(ssu.getCard().getPAN());
        Expression expPSN = builder.get("espApplicationDetail").get("panSequenceNumber").equal(
                ssu.getCard().getPANSequence());

        long expDate = generateExpiryDate(ssu);
        Expression expEXPD = builder.get("espApplicationDetail").get("expiryDate").equal(DateHelper.getTimestampUSFormat(
                expDate));
        Expression expScope = builder.get("espApplicationDetail").get("scopeOID").equal(scope.getPrimaryKey());
        Expression expScopeSU = builder.get("scopeOID").equal(scope.getPrimaryKey());
        Expression expScriptOrder = builder.get("scriptOrder").equal(ssu.getScriptOrder());

        Expression expAll = expPAN.and(expPSN).and(expEXPD).and(expScope).and(expScopeSU).and(expScriptOrder);
        rq.setSelectionCriteria(expAll);
        rq.addGrouping(builder.get("espApplicationDetail").get("pan"));
        rq.addGrouping(builder.get("espApplicationDetail").get("panSequenceNumber"));
        rq.addGrouping(builder.get("espApplicationDetail").get("expiryDate"));


        Vector v = (Vector) GenericPersistentDAO.instance().executeReportQuery(rq);
        //Get ESPScriptUpdateStatus with max sxript order.
        if (v != null && !v.isEmpty())
        {
            ReportQueryResult result = (ReportQueryResult) v.get(0);
            if (result.isEmpty())
            {
                return null;
            }
            builder = new ExpressionBuilder();
            expAll = expAll.and(builder.get("scriptOrder").equal(result.get("scriptOrder")));
            String[] partialAttributes = null;
            Vector essu = GenericPersistentDAO.instance().executeReadQuery(expAll, ESPScriptUpdateStatus.class, null,
                                                                           partialAttributes);
            if (essu.isEmpty())
            {
                return null;
            }
            return (ESPScriptUpdateStatus) essu.get(0);
        }
        return null;
    }

    /**
     *
     * @param paramAliasList List of parameters to search for.
     * <p/>
     * @return
     */
    public Vector getESPBusinessParameters(List<String> paramAliasList)
    {
        Application app = this.app;
        Vector<ESPBusinessParameter> v = new Vector<ESPBusinessParameter>();
        List<String> duplicateList = new ArrayList<String>();
        StringBuilder duplicateStr = new StringBuilder();
        for (int i = 0; i < app.getBusinessParams().size(); i++)
        {
            ESPBusinessParameter param = (ESPBusinessParameter) app.getBusinessParams().get(i);

            if (paramAliasList.contains(param.getAlias()))
            {
                v.add(param);
            }
        }
        Iterator<String> paramAliasListIt = paramAliasList.iterator();
        while(paramAliasListIt.hasNext())
        {
            String paramAlias = paramAliasListIt.next();
            for (int i = 0; i < app.getBusinessParams().size(); i++)
            {
                ESPBusinessParameter param = (ESPBusinessParameter) app.getBusinessParams().get(i);                
                if(paramAlias.equals(param.getAlias()))
                {  
                    if(duplicateList.contains(param.getAlias()))
                    {
                        if(duplicateStr.toString().trim().length() > 0)
                        {
                            duplicateStr.append(",");
                        }
                        duplicateStr.append(paramAlias);
                    }                    
                    duplicateList.add(paramAlias);
                }   
            }
        }
        if(duplicateStr.toString().trim().length() > 0)
        {
            isScriptValidationSuccessful = false;
            errorData = duplicateStr.toString();
            throw new ScriptValidationException(getErrorMessage(MsgConstant.DUPLICATE_DATA_ITEM_FOUND),
                                                DataUtil.getErrorMessage(MsgConstant.DUPLICATE_DATA_ITEM_FOUND, errorData),
                                                getErrorCode(MsgConstant.DUPLICATE_DATA_ITEM_FOUND));
        }
        if (v.isEmpty())
        {
            return null;
        }

        return v;
    }
}
