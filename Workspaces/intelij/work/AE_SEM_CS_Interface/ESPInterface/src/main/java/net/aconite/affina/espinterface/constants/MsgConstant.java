/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.constants;

/**
 *
 * @author thushara.pethiyagoda
 * <p/>
 * Defines the constants representing the error codes and messages. This corresponds to the key value pairs defined in
 * the resource bundle.
 * <p/>
 * Note that these constants are named so that a common validator can cater different messages to similar error types
 * based on the context in which they are used.
 * <p/>
 * The name of the context appears as the first word in the constant. e.g DEVICELIST_EXISTS, where DEVICELIST is the
 * context.
 * <p/>
 * <p/>
 */
public enum MsgConstant
{

    DULPICATE_CARD("err.duplicate.card.msg", "err.duplicate.card.code"),
    INVALID_MESSAGE("err.invalid.message.msg", "err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_SCRIPT("err.invalid.message.empty.script.msg", "err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_SCRIPT_UPDATE_STATUS("err.invalid.message.empty.script.update.status.msg",
                                               "err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_SOURCE("err.invalid.message.empty.script.empty.source.msg", "err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_TARGET("err.invalid.message.empty.script.empty.target.msg", "err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_SEQ_NO("err.invalid.message.empty.script.empty.sequence.msg", "err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_RETRY_COUNT("err.invalid.message.empty.script.empty.retry.count.msg",
                                      "err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_TRACKING_REF("err.invalid.message.empty.script.empty.tracking.ref.msg",
                                       "err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_PAN("err.invalid.message.empty.script.empty.pan.msg", "err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_PSN("err.invalid.message.empty.script.empty.psn.msg", "err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_EXPIRY_DATE("err.invalid.message.empty.script.empty.exp.date.msg", "err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_PUBLISHED_DATE("err.invalid.message.empty.script.empty.published.date.msg",
                                         "err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_BUSINESS_FUNCTION("err.invalid.message.empty.script.empty.bf.msg", "err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_SCRIPT_ORDER("err.invalid.message.empty.script.order.msg", "err.invalid.message.code"),
    INVALID_MESSAGE_INVALID_SCRIPT_STATUS("err.invalid.message.invalid.script.status.msg", "err.invalid.message.code"),
    DUPLICATE_TRACKING_REFERENCE("err.duplicate.tracking.reference.msg", "err.duplicate.tracking.reference.code"),
    INVALID_SCRIPT_UPDATE_STATUS("err.invalid.script.update.status.msg", "err.invalid.script.update.status.code"),
    UNKNOWN_BUSINESS_FUNCTION("err.unknown.business.function.msg", "err.unknown.business.function.code"),
    UNKNOWN_SCRIPT_DATA_ITEM("err.unknown.script.data.item.msg", "err.unknown.script.data.item.code"),
    SCRIPT_DATA_ITEM_NOT_RECOGNISED("err.unknown.script.data.item.reason.msg", "err.unknown.script.data.item.code"),
    NO_MATCHING_BUSINESS_FUNCTION("err.no.matching.business.function.msg", "err.no.matching.business.function.code"),
    
    DUPLICATE_DATA_ITEM_FOUND("err.invalid.data.duplicate.item.supplied.msg","err.invalid.data.item.supplied.code"),
    
    INVALID_DATA("err.invalid.data.msg", "err.invalid.data.code"),
    INVALID_DATA_INVALID_EXP_DATE_FORMAT("err.invalid.data.invalid.exp.date.msg", "err.invalid.data.code"),
    INVALID_DATA_INVALID_PUBLISHED_DATE_FORMAT("err.invalid.data.invalid.published.date.msg", "err.invalid.data.code"),
    INVALID_DATA_ITEM_SUPPLIED("err.invalid.data.item.supplied.msg", "err.invalid.data.item.supplied.code"),
    INVALID_DATA_ITEM_SUPPLIED_FOR_NON_PARAM_BF("err.invalid.data.item.supplied.for.np.bf.msg", "err.invalid.data.item.supplied.for.np.bf.code"),
    
    INVALID_SCRIPT_DELIVERY_STATUS_SUPPLIED("err.invalid.data.item.invalid.script.delivery.status.supplied.msg", "err.invalid.data.code"),
    INVALID_TRANSACTION_DETAILS("err.invalid.data.item.invalid.transaction.details.supplied.msg", "err.invalid.data.code"),
    INVALID_DEVICE_DETAILS_SUPPLIED("err.invalid.device.details.supplied.msg", "err.invalid.data.code"),
    INVALID_DEVICE_CAPABILITIES_SUPPLIED("err.invalid.device.capabilities.supplied.msg", "err.invalid.data.code"),            
    INVALID_DEVICE_TYPE_SUPPLIED("err.invalid.device.type.supplied.msg", "err.invalid.data.code"),
    
    MISSING_SCRIPT_DELIVERY_STATUS("err.missing.script.delivery.status.msg","err.invalid.data.item.supplied.code"),
    
    DATA_NOT_EXPECTED("err.invalid.data.data.not.expected.msg", "err.invalid.data.code"),
    SCRIPT_DELIVERY_STATUS_DATA_NOT_EXPECTED("err.invalid.data.script.delivery.status.data.not.expected.msg","err.invalid.data.code"),
    SCRIPT_DELETION_DETAILS_DATA_NOT_EXPECTED("err.invalid.data.script.deletion.details.data.not.expected.msg","err.invalid.data.code"),
    TRANSACTION_DETAILS_DATA_NOT_EXPECTED("err.invalid.data.transaction.details.data.not.expected.msg","err.invalid.data.code"),
    DEVICE_DETAILS_DATA_NOT_EXPECTED("err.invalid.data.device.details.data.not.expected.msg","err.invalid.data.code"),
    
    DATA_ITEM_MISSING("err.data.item.missing.msg", "err.data.item.missing.code"),
    
    MISSING_TRANSACTION_DETAILS("err.data.item.missing.transaction.details.msg","err.invalid.message.code"),
    MISSING_DELETION_DETAILS("err.data.item.missing.deletion.details.msg","err.invalid.message.code"),
    MISSING_DEVICE_DETAILS("err.data.item.missing.device.details.msg","err.invalid.message.code"),
    MISSING_DELIVERY_STATUS("err.data.item.missing.delivery.status.msg","err.invalid.message.code"),
    
    INVALID_TRANSACTION_DATE("err.data.item.invalid.transaction.date.msg","err.invalid.data.code"),
    INVALID_TRANSACTION_ATC("err.data.item.invalid.transaction.atc.msg","err.invalid.data.code"),
    INVALID_TRANSACTION_SCRIPT_BYTES("err.data.item.invalid.transaction.script.bytes.msg","err.invalid.data.code"),
    
    INVALID_DELETION_DATE("err.data.item.invalid.deletion.date.msg","err.invalid.data.code"),
    
    DATA_ITEM_MISSING_NON_PARAM_SCRIPT_BUSINESS_FUNCTION("err.data.item.missing.business.function.non.param.script.msg",
                                                         "err.data.item.missing.code"),
    DATA_ITEM_MISSING_PARAMETER_SCRIPT_BUSINESS_FUNCTION("err.data.item.missing.business.function.param.script.msg",
                                                         "err.data.item.missing.code"),
    DEFAULT_BF_FOR_PARAM_SCRIPT_MISSING("err.default.business.function.for.param.script.missing.msg",
                                                         "err.invalid.data.code"),
    NO_MATCHING_CARD("err.no.matching.card.msg", "err.no.matching.card.code"),
    CARD_ALREADY_EXISTS("err.card.already.exists.msg", "err.card.already.exists.code"),
    NO_CARD_TYPE("err.no.card.type.msg", "err.no.card.type.code"),
    NO_CARD_PROFILE("err.no.card.profile.msg", "err.no.card.profile.code"),
    SUSPENED_OR_CANCELLED_CARD("err.suspended.or.cancelled.card.msg", "err.suspended.or.cancelled.card.code"),
    
    
    //SendScriptRequest related error messages

    INVALID_MESSAGE_EMPTY_CARD_ID("err.invalid.data.card.id.empty.msg","err.invalid.data.card.id.empty.code"),
    
    INVALID_MESSAGE_EMPTY_DATA_CONTEXT("err.invalid.message.empty.script.empty.datacontext.msg","err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_DATA_CONTEXT_ID("err.invalid.message.empty.script.empty.datacontext.id.msg","err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_DATA_CONTEXT_SCOPE("err.invalid.message.empty.script.empty.datacontext.scope.msg","err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_SCRIPT_FUNCTION("err.function.is.not.present.msg","err.function.is.not.present.code"),
    INVALID_MESSAGE_EMPTY_SCRIPT_FUNCTION_NAME("err.invalid.message.empty.script.empty.scipt.function.name.msg","err.invalid.message.code"),
    INVALID_MESSAGE_EMPTY_RESTAGE_COUNT("err.invalid.message.invalid.restage.count.msg","err.invalid.message.code"),
    
    INVALID_MESSAGE_EMPTY_BUS_APP("err.invalid.message.empty.script.empty.bus.app.msg","err.invalid.message.empty.script.empty.bus.app.code"),
    INVALID_MESSAGE_EMPTY_BUS_APP_NAME("err.invalid.message.empty.script.empty.bus.app.name.msg","err.invalid.message.empty.script.empty.bus.app.name.code"),
    
    INVALID_DATA_NON_EXISTENT_SCOPE("err.invalid.message.empty.script.invalid.scope.msg","err.invalid.message.code"),

        
    card_does_not_exist("err.card.does.not.exist.msg", "err.card.does.not.exist.code"),
    card_status_expired("err.card.status.expired.msg", "err.card.status.expired.code"),
    card_status_suspended("err.card.status.suspended.msg", "err.card.status.suspended.code"),
    card_status_cancelled("err.card.status.cancelled.msg", "err.card.status.cancelled.code"),
    invalid_card_status("err.invalid.card.status.msg", "err.invalid.card.status.code"),
    tracking_ref_already_exists("err.tracking.ref.already.exists.msg", "err.tracking.ref.already.exists.code"),
    role_name_does_not_have_necessary_permissions_for_scope("err.role.name.does.not.have.necessary.permissions.for.scope.msg","err.role.name.does.not.have.necessary.permissions.for.scope.code"),
    
    CARD_ID_HAS_INCORRECT_LENGH("err.invalid.data.card.id.incorrect.length.msg","err.invalid.data.card.id.incorrect.length.code"),
    CARD_ID_HAS_INVALID_FORMAT("err.invalid.data.card.id.invalid.format.msg","err.invalid.data.card.id.invalid.format.code"),
    
    function_is_not_present("err.function.is.not.present.msg", "err.function.is.not.present.code"),
    incorrect_parameters_for_function("err.incorrect.parameters.for.function.msg","err.incorrect.parameters.for.function.code"),
    card_update_functionality_disabled("err.card.update.functionality.disabled.msg","err.card.update.functionality.disabled.code"),
    
    invalid_data_context_scope_name_datacontextname("err.invalid.data.context.scope.name.datacontextname.msg","err.invalid.data.context.scope.scope.name.datacontextname.code"),
    invalid_validity_period("err.invalid.validity.period.msg", "err.invalid.validity.period.code"),
    business_application_is_not_present_for_product_at_version("err.business.application.is.not.present.for.product.at.version.msg","err.business.application.is.not.present.for.product.at.version.code"),
    business_application_at_version_is_not_within_its_validity_period("err.business.application.at.version.is.not.within.its.validity.period.msg","err.business.application.at.version.is.not.within.its.validity.period.code"),
    business_application_is_not_associated_with_a_scriptable_application("err.business.application.is.not.associated.with.a.scriptable.application.msg","err.business.application.is.not.associated.with.a.scriptable.application.code"),
    BUS_APP_HAS_MULTIPLE_SCRIPTABLE_APPS("err.business.application.is.associated.with.multiple.scriptable.application.msg","err.business.application.is.not.associated.with.a.scriptable.application.code"),
    
    REASON_NOT_SCRIPTABLE_APP ("err.business.application.is.not.associated.with.a.scriptable.application.not.scriptable.reason.msg","err.business.application.is.not.associated.with.a.scriptable.application.code"),
    REASON_NO_APP_TYPE ("err.business.application.is.not.associated.with.a.scriptable.application.no.apptype.reason.msg","err.business.application.is.not.associated.with.a.scriptable.application.code"),
    REASON_NO_APP_VERSION ("err.business.application.is.not.associated.with.a.scriptable.application.no.appversion.reason.msg","err.business.application.is.not.associated.with.a.scriptable.application.code"),
    REASON_NO_MATCHING_APPLICATION ("err.business.application.is.not.associated.with.a.scriptable.application.no.matching.application.reason.msg","err.business.application.is.not.associated.with.a.scriptable.application.code"),
    
    INVALID_STAGE_SCRIPT_RESPONSE_DATA("err.invalid.stage.script.response.data.msg","err.invalid.stage.script.response.data.code"),
    
    INVALID_STAGE_SCRIPT_RESPONSE_INVALID_TRACKING_REF("err.invalid.stage.script.response.invalid.tracking.ref.msg","err.invalid.stage.script.response.data.code"),
    INVALID_STAGE_SCRIPT_RESPONSE_EMPTY_TRF ("err.invalid.stage.script.response.empty.tracking.ref.msg","err.invalid.stage.script.response.data.code"),
    INVALID_STAGE_SCRIPT_RESPONSE_INVALID_STATUS ("err.invalid.stage.script.response.invalid.response.status.msg","err.invalid.stage.script.response.data.code"),
    
    INVALID_STAGE_SCRIPT_RESPONSE_INVALID_ERROR_STATUS ("err.invalid.stage.script.response.invalid.response.error.status.msg","err.invalid.stage.script.response.data.code"),
    INVALID_STAGE_SCRIPT_RESPONSE_EMPTY_STATUS_DESCRIPTION ("err.invalid.stage.script.response.invalid.error.status.description.msg","err.invalid.stage.script.response.data.code"),
    INVALID_STAGE_SCRIPT_RESPONSE_EMPTY_STATUS_CODE ("err.invalid.stage.script.response.invalid.error.status.code.msg","err.invalid.stage.script.response.data.code"),

    INVALID_STAGE_SCRIPT_RESPONSE_INVALID_OK_STATUS ("err.invalid.stage.script.response.invalid.ok.status.msg","err.invalid.stage.script.response.data.code"),
    
    ERR_GENRATED_BY_SCRIPTING_SYSTEM("err.generated.by.scripting.system.msg","err.generated.by.scripting.system.code");
    
    //End SendScriptRequest related error messages

    /**
     * Enum constructor that takes Keyes for error strings and codes.
     * <p/>
     * @param errMsgString   key for error string
     * @param errMsgConstant key for error code.
     */
    MsgConstant(String errMsgString, String errMsgConstant)
    {
        msgConstant = errMsgString;
        codeConstant = errMsgConstant;
    }

    /**
     * Returns the error code key
     * <p/>
     * @return String
     */
    public String getCodeConstant()
    {
        return codeConstant;
    }

    /**
     * Returns the error message key.
     * <p/>
     * @return String
     */
    public String getMsgConstant()
    {
        return msgConstant;
    }
    /**
     * Field for error message key
     */
    private String msgConstant;
    /**
     * Field for error code key.
     */
    private String codeConstant;
}
