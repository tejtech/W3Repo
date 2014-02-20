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
 *
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
    NO_MATCHING_BUSINESS_FUNCTION("err.no.matching.business.function.msg", "err.no.matching.business.function.code"),
    
    INVALID_DATA("err.invalid.data.msg", "err.invalid.data.code"),
    INVALID_DATA_INVALID_EXP_DATE_FORMAT("err.invalid.data.invalid.exp.date.msg", "err.invalid.data.code"),
    INVALID_DATA_INVALID_PUBLISHED_DATE_FORMAT("err.invalid.data.invalid.published.date.msg", "err.invalid.data.code"),
    
    
    INVALID_DATA_ITEM_SUPPLIED("err.invalid.data.item.supplied.msg", "err.invalid.data.item.supplied.code"),
    INVALID_DATA_ITEM_SCRIPT_DELIVERY_STATUS_SUPPLIED("err.invalid.data.item.invalid.script.delivery.status.supplied.msg", "err.invalid.data.item.supplied.code"),
    
    
    DATA_ITEM_MISSING("err.data.item.missing.msg", "err.data.item.missing.code"),
    DATA_ITEM_MISSING_NON_PARAM_SCRIPT_BUSINESS_FUNCTION("err.data.item.missing.business.function.non.param.script.msg", "err.data.item.missing.code"),
    DATA_ITEM_MISSING_PARAMETER_SCRIPT_BUSINESS_FUNCTION("err.data.item.missing.business.function.param.script.msg", "err.data.item.missing.code"),
    
    
    
    NO_MATCHING_CARD("err.no.matching.card.msg", "err.no.matching.card.code"),
    CARD_ALREADY_EXISTS("err.card.already.exists.msg", "err.card.already.exists.code"),
    NO_CARD_TYPE("err.no.card.type.msg", "err.no.card.type.code"),
    NO_CARD_PROFILE("err.no.card.profile.msg", "err.no.card.profile.code"),
    
    
    SUSPENED_OR_CANCELLED_CARD("err.suspended.or.cancelled.card.msg", "err.suspended.or.cancelled.card.code");

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
