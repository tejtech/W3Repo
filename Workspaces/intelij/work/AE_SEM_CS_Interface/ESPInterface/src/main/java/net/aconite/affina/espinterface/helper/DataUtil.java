/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.helper;

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
}
