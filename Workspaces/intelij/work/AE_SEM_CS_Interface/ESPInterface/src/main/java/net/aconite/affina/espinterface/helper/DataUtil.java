/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.helper;

import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.exceptions.*;
import net.aconite.affina.espinterface.exceptions.util.*;
import net.aconite.affina.espinterface.model.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class DataUtil
{

    /**
     *
     * @param value
     * <p/>
     * @return
     */
    public static boolean isNull(Object value)
    {
        return value == null;
    }

    /**
     * Checks whether string is empty if it is not null.
     * <p/>
     * @param value
     * <
     * p/>
     * @return
     */
    public static boolean isEmpty(String value)
    {
        return (isNull(value)) || value.trim().length() == 0;
    }

    /**
     *
     * @param value String representation of a long value.
     * <p/>
     * @return returns true if the value is a date.
     */
    public static boolean isDate(String value)
    {
        long date;
        try
        {
            date = Long.parseLong(value);
        }
        catch (Exception ex)
        {
            return false;
        }
        return DateHelper.isDate(date);
    }

    /**
     * Should be double digit with a range of 0-99.
     * <p/>
     * @param value
     * <p/>
     * @return true or false
     */
    public static boolean isPSNValid(String value)
    {
        boolean valid = true;
        if (value.trim().length() != 2)
        {
            valid = false;
        }
        if (valid)
        {
            try
            {
                int parsedValue = Integer.parseInt(value);
                if (parsedValue < 0 || parsedValue > 99)
                {
                    valid = false;
                }
            }
            catch (Exception ex)
            {
                valid = false;
            }
        }
        return valid;
    }
    
    /**
     * 
     * @param value
     * @return 
     */
    public static boolean isEqualIgnoreCaseToTrue(String value)
    {
        return "true".equalsIgnoreCase(value);
    }    
    
    /**
     * Extracts the error message from property file.
     * @param mc Enum that defines a mapping to the string representation defined in the Resource Bundle
     * @return The error message
     */
    public static String getErrorMessage(MsgConstant mc)
    {
        return ErrorMessageBundle.getMessage(mc);
    }
    /**
     * @see ErrorMessageBundle      
     */
    public static String getErrorMessage(MsgConstant mc, String ... substituion)
    {
        return ErrorMessageBundle.getMessage(mc, substituion);
    }

    /**
     * Extracts the error message code from property file.
     * @param mc Enum that defines a mapping to the string representation defined in the Resource Bundle 
     * @return The error code
     */
    public static String getErrorCode(MsgConstant mc)
    {
        return ErrorMessageBundle.getMessageCode(mc);
    }
    
    /**
     *
     * @param msg
     * @param reason
     * @param code
     */
    public static void throwScriptValidationException(MsgConstant msg,
                                                MsgConstant reason,
                                                MsgConstant code)
    {
        throw createScriptValidationException(msg, reason, code);
    }

    /**
     *
     * @param <Z>
     * @param msg
     * @param reason
     * @param code
     * <p/>
     * @return
     */
    public static <Z extends AbstractModel> Result<Z> generateErrorResult(MsgConstant msg,
                                              MsgConstant reason,
                                              MsgConstant code)
    {
        return generateErrorResult(msg, reason, code, null, null);
    }

    /**
     *
     * @param <Z>
     * @param msg
     * @param reason
     * @param code
     * @param trackingRef
     * @param data
     * <p/>
     * @return
     */
    public static <Z extends AbstractModel> Result<Z> generateErrorResult(MsgConstant msg,
                                              MsgConstant reason,
                                              MsgConstant code,
                                              String trackingRef, Z data)
    {
        ScriptValidationException exp = createScriptValidationException(msg, reason, code);
        return Result.getInstance(false, exp, trackingRef, data);
    }

    /**
     *
     * @param msg
     * @param reason
     * @param code
     * <p/>
     * @return
     */
    public static ScriptValidationException createScriptValidationException(MsgConstant msg, MsgConstant reason,
                                                                      MsgConstant code)
    {
        return new ScriptValidationException(DataUtil.getErrorMessage(msg),
                                             DataUtil.getErrorMessage(reason),
                                             DataUtil.getErrorCode(code), ScriptProcessingRuntimeException.ERROR_VALIDATION);
    }
    /**
     * 
     * @param msg
     * @param reason
     * @param code
     * @return 
     */
    public static ScriptValidationException createScriptValidationApplicationException(MsgConstant msg, MsgConstant reason,
                                                                      MsgConstant code)
    {
        return new ScriptValidationException(DataUtil.getErrorMessage(msg),
                                             DataUtil.getErrorMessage(reason),
                                             DataUtil.getErrorCode(code), ScriptProcessingRuntimeException.ERROR_APPLICATION);
    }

    /**
     * 
     * @param msg
     * @param reason
     * @param code
     * @param substitutionParams
     * @return 
     */
    public static ScriptValidationException createScriptValidationException(MsgConstant msg, MsgConstant reason,
                                                                      MsgConstant code, String... substitutionParams)
    {
        return new ScriptValidationException(DataUtil.getErrorMessage(msg, substitutionParams),
                                             DataUtil.getErrorMessage(reason, substitutionParams),
                                             DataUtil.getErrorCode(code), ScriptProcessingRuntimeException.ERROR_VALIDATION);
    } 
    
    public static <Z extends AbstractModel> Result<Z> createScriptValidationResult(MsgConstant msg, MsgConstant reason,
                                                                      MsgConstant code, String trf,String... substitutionParams)
    {
        ScriptValidationException ex = createScriptValidationException(msg,reason,code,substitutionParams);
        return Result.<Z>getInstance(false, ex, trf, null);
    } 
    /**
     * 
     * @param msg
     * @param reason
     * @param code
     * @param substitutionParams
     * @return 
     */
    public static ScriptValidationException createScriptValidationApplicationException(MsgConstant msg, MsgConstant reason,
                                                                      MsgConstant code, String... substitutionParams)
    {
        return new ScriptValidationException(DataUtil.getErrorMessage(msg, substitutionParams),
                                             DataUtil.getErrorMessage(reason, substitutionParams),
                                             DataUtil.getErrorCode(code), ScriptProcessingRuntimeException.ERROR_APPLICATION);
    }  
}
