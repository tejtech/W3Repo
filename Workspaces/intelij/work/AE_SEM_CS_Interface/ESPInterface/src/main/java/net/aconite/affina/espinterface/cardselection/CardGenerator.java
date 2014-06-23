/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.cardselection;

import com.platform7.pma.card.SoftCard;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import java.util.Vector;
import net.aconite.affina.espinterface.helper.Result;
import net.aconite.affina.espinterface.model.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public interface CardGenerator<Z extends AbstractModel>
{
    public ScriptableCard generateScriptableCard(final String pan, final String psn, final long expirationDate, final Scope scope);
    public SoftCard generateSoftCard(final String pan, final long expirationDate, final Scope scope);
    public SoftCard generateSoftCard(final String pan, final String psn, final long expirationDate, final Scope scope);  
    public Vector generateSoftCards(final String pan, final String psn, final long expirationDate, final Scope scope);   
    public Result<Z> generateValidCard(final String cardId, final Scope scope, String trackingReference);
}
