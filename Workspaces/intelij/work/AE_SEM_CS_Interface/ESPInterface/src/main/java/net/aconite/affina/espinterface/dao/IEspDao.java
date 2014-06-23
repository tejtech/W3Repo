/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.dao;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author wakkir.muzammil
 */
public interface IEspDao<L,R,I,F,P> 
{   
    public L getById(BigDecimal oId); 
    public L getByName(String name);
    public L getByTrackId(Integer scopeOId,String trackId);
    public List<L> getList(F filterObject,P pagingObject); 
    public BigDecimal getTotalCount(F filterObject);
    public R add(I ormObject); 
    public R delete(BigDecimal oId); 
    public R update(I ormObject);        
}
