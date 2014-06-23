/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao;

import net.aconite.affina.espinterface.helper.Result;
import net.aconite.affina.espinterface.model.AbstractModel;

/**
 *
 * @author thushara.pethiyagoda
 */
public interface IESPScriptDAO
{
    /**
     *
     * @param <C>
     * @param trf
     * @param scopeName
     * @return
     */
    public <C extends AbstractModel> Result<C> getESPStageScriptDetail(String trf, String scopeName);
}
