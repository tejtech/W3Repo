/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.scripting.generic;

import net.aconite.affina.espinterface.helper.Result;
import net.aconite.affina.espinterface.helper.ResultBoundData;
import net.aconite.affina.espinterface.model.*;

/**
 *
 * @author thushara.pethiyagoda
 */
public interface ResultGenerator
{
    /**
     * Generates a Result.
     * @return Result
     */
    public <T extends ResultBoundData, R extends AbstractModel> Result<R> generateResult(T arg1, R arg2);
}
