/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.cardselection;

import com.platform7.pma.application.*;
import com.platform7.pma.card.SoftCard;
import net.aconite.affina.espinterface.model.ScriptableCard;

/**
 *
 * @author thushara.pethiyagoda
 */
public interface CardGenerator
{
    public ScriptableCard generateScriptableCard(final String pan, final String psn, final long expirationDate, final String scopeName);
    public SoftCard generateSoftCard(final String pan, final String psn, final long expirationDate);
    public SoftCard generateSoftCard(final String pan, final long expirationDate, final String scopeName);
    public Application getApplicationByApplicationID(final String appID);
    public Application getApplicationByApplicationOID(final long appOID);
    public String getCurrentApplicationVersionByApplicationID(final String appID);
    public SoftCard generateSoftCard(final String pan, final String psn, final long expirationDate, final String scopeName);
    
    
}
