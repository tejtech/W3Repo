/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.persistence;

import com.platform7.pma.application.Application;
import com.platform7.pma.card.manifestapplication.ManifestApplication;
import com.platform7.pma.product.PMAProduct;
import com.platform7.pma.product.PMAProductPart;
import com.platform7.pma.request.emvscriptrequest.ESPBusinessFunction;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import java.util.*;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.webservice.restful.service.model.StageScript;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.queries.*;
import org.eclipse.persistence.sessions.UnitOfWork;

/**
 *
 * @author thushara.pethiyagoda
 */
public interface Persistent
{
    public <T>void doTransactionalWorkAndCommit();
    public <T>void doTransactionalWorkAndCommit(Workable worker);
    public Vector executeReadQuery(Expression selectionCriteria, Class cls, Expression ordering, String... partialAttributes);
    public Vector executeQuery(ReadAllQuery query);
    public Object executeReportQuery(ReportQuery query);
    public <R> R getRegisteredObject(Class<R> cls);
    public void addTransactionalWorker(Workable workable);
    public com.platform7.standardinfrastructure.multiissuer.Scope getScope(String name);
    
    public List<Scope> getScopes(FilterCriteria filter);//, PagingCriteria paging);
    public List<PMAProduct> getProducts(FilterCriteria filter);//, PagingCriteria paging);
    public List<PMAProductPart> getProductParts(FilterCriteria filter);//, PagingCriteria paging);
    public List<Application> getApplications(FilterCriteria filter);//, PagingCriteria paging);
    public List<ESPBusinessFunction> getBusinessFunctions(FilterCriteria filter);//, PagingCriteria paging);
    public List<StageScript> getApplicationInstances(FilterCriteria filter);//, PagingCriteria paging);
    
    
    public UnitOfWork getUnitOfWork();
    public boolean isInNonContainerTransaction();
}
