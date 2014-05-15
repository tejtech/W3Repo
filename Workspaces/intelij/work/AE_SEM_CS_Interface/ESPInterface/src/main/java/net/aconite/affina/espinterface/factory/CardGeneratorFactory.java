/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.factory;

import net.aconite.affina.espinterface.cardselection.*;
import net.aconite.affina.espinterface.persistence.Persistent;

/**
 *
 * @author thushara.pethiyagoda
 */
public class CardGeneratorFactory
{
    public static CardGenerator getCardgenerator()
    {
        return new SelectableCardGenerator();
    }
}
