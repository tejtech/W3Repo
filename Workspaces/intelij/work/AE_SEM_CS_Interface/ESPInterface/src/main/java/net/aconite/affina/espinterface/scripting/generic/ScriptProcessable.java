/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.scripting.generic;

import net.aconite.affina.espinterface.helper.Result;
import net.aconite.affina.espinterface.model.*;

/**
 * This interface defines a contract to be adhered to by an implementor
 * providing functionality with processing scripts.
 * @author thushara.pethiyagoda
 */
public interface ScriptProcessable<T extends ScriptDataHolder, Z extends AbstractModel>
{
    public Result<Z> processScript(T scriptData);
}
