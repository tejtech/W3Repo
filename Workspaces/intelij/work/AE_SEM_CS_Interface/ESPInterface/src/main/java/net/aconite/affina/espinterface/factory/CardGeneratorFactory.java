/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.factory;

import net.aconite.affina.espinterface.cardselection.*;
import net.aconite.affina.espinterface.model.ScriptableCard;

/**
 *
 * @author thushara.pethiyagoda
 */
public class CardGeneratorFactory
{
    /**
     * Factory method to get the instance of CardGenerator.
     * @return 
     */
    public static CardGenerator<ScriptableCard> getCardgenerator()
    {
        return SelectableCardGenerator.getInstance();
    }
}
