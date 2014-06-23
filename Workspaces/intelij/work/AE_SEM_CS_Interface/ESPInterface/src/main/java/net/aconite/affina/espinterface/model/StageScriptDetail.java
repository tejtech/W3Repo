/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.model;

import com.platform7.pma.request.emvscriptrequest.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public class StageScriptDetail extends AbstractModel
{    
    protected static final long serialVersionUID = -3327516991124225943L;  
    public static final String FIELD_primaryKey = "primaryKey";    
    public static final String FIELD_trackingReference = "trackingReference";    
    public static final String FIELD_scriptOrder = "scriptOrder";      
    public static final String FIELD_scriptableManifestApplicationOID = "scriptableManifestApplicationOID";
    public static final String FIELD_espStageScriptFilterOID = "espStageScriptFilterOID";
    public static final String FIELD_scope = "scope";
    public static final String FIELD_scopeOID = "scopeOID";       
    public static final String FIELD_dateCreated = "dateCreated";
    public static final String FIELD_status = "status";
    public static final String FIELD_PARENT = "parent";
    
    /** Parent representation of StageScriptDetail */
    private ESPStageScriptDetail parent;
    private String scope;   
   
    
    public StageScriptDetail(ESPStageScriptDetail parentObj, String scopeName)
    {
        parent = parentObj;
        scope = scopeName;
    }
    
    
    public ESPStageScriptDetail getParent()
    {
        return parent;
    }
    
    public String getScope()
    {
        return scope;
    }
}
