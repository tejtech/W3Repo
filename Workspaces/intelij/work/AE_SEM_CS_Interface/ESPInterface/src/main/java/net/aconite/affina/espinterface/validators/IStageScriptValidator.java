/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.validators;

import com.platform7.pma.application.Application;
import com.platform7.pma.card.SoftCard;

/**
 *
 * @author wakkir.muzammil
 */
public interface IStageScriptValidator 
{
    public boolean isValidScriptableApplication(Application app);
    
    public boolean isValidScriptableCard(SoftCard softcard);
    
}
