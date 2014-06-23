/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.exceptions.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import net.aconite.affina.espinterface.constants.MsgConstant;


/**
 *
 * @author thushara.pethiyagoda
 * Creates a handle to an external resource e.g a properties file.
 */
public class ErrorMessageBundle {
    /** Field ResurceBundle.*/
    private static ResourceBundle rb;
    /**
     * Static block to call init.
     */
    static {
        init("errormsgs");
    }
    /**
     * Initialises the resource bundle handle.
     * @param resource
     */
    private static void init(String resource) {
        rb =  ResourceBundle.getBundle(resource);
    }
    /**
     * Gets a value from the resource bundle
     * @param ece
     * @return String
     */
    public static String getMessage(MsgConstant ece) {
        if(ece == null)
            return null;

        return rb.getString(ece.getMsgConstant());
    }
    /**
     * This method extracts the message from the bundle and replace any parameters
     * with its values given by paramSubstitution.
     * If paramSubstitution null or empty then substitution will be ignored.
     * @param ece
     * @param paramSubstitution
     * @return 
     */
    public static String getMessage(MsgConstant ece, String ...paramSubstitution) 
    { 
        String value = getMessage(ece);
        if(paramSubstitution != null && paramSubstitution.length > 0)
        {
            value = MessageFormat.format(value, (Object[])paramSubstitution);
        }
        return value;
    }
    /**
     * Gets a value from the resource bundle corresponding to the key
     * identified by MsgConstant
     * @param ece
     * @return String
     */
    public static String getMessageCode(MsgConstant ece) {
        if(ece == null)
            return null;

        return rb.getString(ece.getCodeConstant());
    }
}
