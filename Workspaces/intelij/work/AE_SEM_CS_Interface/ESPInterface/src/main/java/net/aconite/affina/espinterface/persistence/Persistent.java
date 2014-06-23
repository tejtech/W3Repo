/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.persistence;

import com.platform7.pma.application.Application;
import com.platform7.pma.product.PMAProduct;
import com.platform7.pma.product.PMAProductPart;
import com.platform7.pma.request.emvscriptrequest.ESPApplicationDetail;
import com.platform7.pma.request.emvscriptrequest.ESPBusinessFunction;
import com.platform7.pma.request.emvscriptrequest.ESPCardSetup;
import com.platform7.pma.request.emvscriptrequest.ESPStageScriptFilter;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
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
    public <R> boolean deleteAllObject(List<R> deleteObjects);
    public <R> R deleteObject(R deleteObject);
    public void addTransactionalWorker(Workable workable);
    public com.platform7.standardinfrastructure.multiissuer.Scope getScope(String name);
    
    public com.platform7.standardinfrastructure.multiissuer.Scope getScopeById(BigDecimal oid);
    public com.platform7.standardinfrastructure.multiissuer.Scope getScopeByName(String name);
    public List<Scope> getScopes(FilterCriteria filter);//, PagingCriteria paging);
    
    public PMAProduct getProductById(BigDecimal oid);
    public PMAProduct getProductByName(String name);
    public List<PMAProduct> getProducts(FilterCriteria filter);//, PagingCriteria paging);
    
    public PMAProductPart getProductPartById(BigDecimal oid);
    public PMAProductPart getProductPartByName(String name);
    public List<PMAProductPart> getProductParts(FilterCriteria filter);//, PagingCriteria paging);
    
    public Application getApplicationById(BigDecimal oid);
    public Application getApplicationByName(String name);
    public List<Application> getApplications(FilterCriteria filter);//, PagingCriteria paging);
    
    public ESPBusinessFunction getBusinessFunctionById(BigDecimal oid);
    public ESPBusinessFunction getBusinessFunctionByName(String name);
    public List<ESPBusinessFunction> getBusinessFunctions(FilterCriteria filter);//, PagingCriteria paging);
    
    public ESPStageScriptFilter getStageScriptFiltersById(BigDecimal oid);
    public ESPStageScriptFilter getStageScriptFiltersByTrackId(BigDecimal scopeOID,String trackId);
    public List<ESPStageScriptFilter> getStageScriptFiltersByStatusAndScope(FilterCriteria filter);
    public List<ESPStageScriptFilter> getStageScriptFiltersByCheckDuplicate(FilterCriteria filter);
    
    public ESPCardSetup getCardSetupDetailById(BigDecimal oid);    
    public List<ESPCardSetup> getCardSetupDetails(Scope scope, String aeTrackId);
    public List<ESPCardSetup> getCardSetupDetailsBySemTrn(Scope scope, String semTrackId);    
    public List<ESPApplicationDetail> getESPApplicationDetails(Scope scope, String pan,String psn,Long expDate);
    
    
    public UnitOfWork getUnitOfWork();
    public boolean isInNonContainerTransaction();
}
