/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.model;

import java.lang.reflect.*;
import net.aconite.affina.espinterface.scripting.statusupdate.*;
import org.slf4j.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public abstract class AbstractModel implements java.io.Serializable
{
    private static final Logger logger = LoggerFactory.getLogger(ScriptUpdateProcessor.class);
    protected static final long serialVersionUID = -3327516993124225943L;  
    public static final String FIELD_SOFT_CARD = "softCard";
    public static final String FIELD_SCRIPTABLE_APPLICATION = "application";
    public static final String FIELD_BUSINESS_FUNCTION_NAME = "businessFunction";
    public static final String FIELD_BUS_FUNCTION = "bf";
    public static final String FIELD_BUS_FUNCTION_PARENT = "parent";
    public static final String FIELD_BP_LIST = "bpList";
    public static final String FIELD_BUS_PARAMETER_NAME = "name";
    public static final String FIELD_BUS_PARAMETER_ALIAS = "alias";
    public static final String FIELD_BUS_PARAMETER_PARENT = "parent";

    
    /**
     * Returns the value set in the field name passed in as the parameter.
     * @param fieldName name of the field to extract the value from.
     * @return 
     */
    @SuppressWarnings("unchecked")
    public <D> D getProperty(String fieldName)
    {
        try
        {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            D retValue = (D) field.get(this);
            return (retValue == null ? null :  retValue);
        }
        catch (Exception ex)
        {
            logger.error("Unable to reflectively extract value for :" + fieldName + 
                    " in " + this.getClass().getName(), ex);
            return null;
        }
    }
}
