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
public class StageScriptFilter extends AbstractModel
{
    protected static final long serialVersionUID = -3327516993124215943L;  
    public static final String FIELD_STAGE_SCRIPT_FILTER_PARENT = "parent";
    private ESPStageScriptFilter parent;
    
    public StageScriptFilter(ESPStageScriptFilter parentObj)
    {
        parentObj = parent;
    }
    
    public ESPStageScriptFilter getParent()
    {
        return parent;
    }
}
