/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.helper;

import java.lang.reflect.Field;

/**
 * Data that would be used by a Result. Possibly for error reporting.
 * <p/>
 * @author thushara.pethiyagoda
 */
public class ResultBoundData
{

    public static final String FIELD_CARD_ID = "cardId";
    public static final String FIELD_SCOPE = "scope";
    public static final String FIELD_BUSINESS_APPLICATION_ID = "businessApplicationId";
    public static final String FIELD_BUSINESS_APPLICATION_VERSION = "businessApplicationVersion";
    
    public static final String FIELD_TRACKING_REF = "trackingRef";
    public static final String FIELD_BUSINESS_FUNCTION = "businessFunction";
    
    /**
     * 
     */
    private String cardId;
    /**
     * 
     */
    private String scope;
    /**
     * 
     */
    private String businessApplicationId;
    /**
     * 
     */
    private String businessApplicationVersion;
    /**
     * 
     */
    private String trackingRef;
    /**
     * 
     */
    private String businessFunction;
    /**
     * 
     */
    private String[] paramList;
    /**
     * 
     */
    private String role;
    /**
     * 
     */
    private String dataContext;
    /**
     * 
     */
    private String productId;
    /**
     * 
     */
    private String productVersion;

    /**
     * Constructs a ResultBoundData with the given parameters.
     * <p/>
     * @param cardId
     * @param scope
     * @param businessApplicationId
     * @param businessApplicationVersion
     * @param trackingRef
     * @param businessFunction
     * @param paramList
     * @param role
     * @param dataContext
     * @param productId
     * @param productVersion
     */
    public ResultBoundData(String cardId, String scope, String businessApplicationId, String businessApplicationVersion,
                           String trackingRef, String businessFunction, String[] paramList, String role,
                           String dataContext, String productId, String productVersion)
    {
        this.cardId = cardId;
        this.scope = scope;
        this.businessApplicationId = businessApplicationId;
        this.businessApplicationVersion = businessApplicationVersion;
        this.trackingRef = trackingRef;
        this.businessFunction = businessFunction;
        this.paramList = paramList;
        this.role = role;
        this.dataContext = dataContext;
        this.productId = productId;
        this.productVersion = productVersion;
    }

    /**
     * Returns the value set in the field name passed in as the parameter.
     * @param fieldName name of the field to extract the value from.
     * @return 
     */
    public String getProperty(String fieldName)
    {
        try
        {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object retValue = field.get(this);
            return retValue == null ? null : (String) retValue;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /**
     *
     * @return
     */
    public String getCardId()
    {
        return cardId;
    }

    /**
     *
     * @return
     */
    public String getScope()
    {
        return scope;
    }

    /**
     *
     * @return
     */
    public String getBusinessApplicationId()
    {
        return businessApplicationId;
    }

    /**
     *
     * @return
     */
    public String getBusinessApplicationVersion()
    {
        return businessApplicationVersion;
    }

    /**
     *
     * @return
     */
    public String getTrackingRef()
    {
        return trackingRef;
    }

    /**
     *
     * @return
     */
    public String getBusinessFunction()
    {
        return businessFunction;
    }

    /**
     *
     * @return
     */
    public String[] getParamList()
    {
        return paramList;
    }

    /**
     *
     * @return
     */
    public String getRole()
    {
        return role;
    }

    /**
     *
     * @return
     */
    public String getDataContext()
    {
        return dataContext;
    }

    /**
     *
     * @return
     */
    public String getProductId()
    {
        return productId;
    }

    /**
     *
     * @return
     */
    public String getProductVersion()
    {
        return productVersion;
    }
}
