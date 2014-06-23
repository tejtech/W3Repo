/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.scripting.generic;

import net.aconite.affina.espinterface.helper.Result;
import net.aconite.affina.espinterface.model.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public interface Validator<T, Z extends AbstractModel>
{
    /**
     * Implement this to perform any validation and return a Result.
     * @param arg Represents and data to be validated or to be used by validation routines.
     *            For e.g If a Script Request is validated then T will represent that ScriptRequest type.
     * @return Result holding any helpful data of Type Z, that it self is a type of AbstractModel.
     */
    public Result<Z> validate(T arg);
}
