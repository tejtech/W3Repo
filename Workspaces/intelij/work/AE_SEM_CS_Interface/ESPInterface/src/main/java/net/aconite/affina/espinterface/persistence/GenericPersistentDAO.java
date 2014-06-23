/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.persistence;

import com.platform7.pma.application.Application;
import com.platform7.pma.product.PMAProduct;
import com.platform7.pma.product.PMAProductPart;
import com.platform7.pma.product.PlatformDependentPart;
import com.platform7.pma.request.emvscriptrequest.ESPApplicationDetail;
import com.platform7.pma.request.emvscriptrequest.ESPBusinessFunction;
import com.platform7.pma.request.emvscriptrequest.ESPCardSetup;
import com.platform7.pma.request.emvscriptrequest.ESPStageScriptFilter;
import com.platform7.standardinfrastructure.appconfig.AppConfig;
import com.platform7.standardinfrastructure.database.*;
import com.platform7.standardinfrastructure.distributedworkflow.*;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import com.platform7.standardinfrastructure.multiissuer.ScopeNotFoundException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.exceptions.PersistentException;
import net.aconite.affina.espinterface.factory.*;
import net.aconite.affina.espinterface.validators.IStageScriptValidator;
import net.aconite.affina.espinterface.validators.StageScriptValidator;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.queries.ReadObjectQuery;
import org.eclipse.persistence.queries.ReportQuery;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.slf4j.*;

/**
 * NOTE: This class is Spring framework dependent in obtaining data sources.
 * <p/>
 * This class has two constructors one that uses a default class with id sessionManager_pma with a database connection.
 * <p/>
 * The second constructor allows the caller to specify its own db related details.
 * <p/>
 * <
 * p/> This is a generic Data Access Object class for convenience that encapsulates lot of the work that would otherwise
 * have to be repeated.
 * <p/>
 * Also this class provides a method (doTransactionAndCommit()) to do work in single transaction while managing its
 * Units Of Work in doTransactionAndCommit()
 * <p/>
 * Further a method (getRegisteredObject())is introduced so that caller can create and retrieve an object which would be
 * registered with the persistence cache at the same time.
 * <p/>
 * @author thushara.pethiyagoda
 */
public class GenericPersistentDAO implements Persistent
{

    private static final Logger logger = LoggerFactory.getLogger(GenericPersistentDAO.class);
    private ThreadLocal<Workable> workable = new ThreadLocal<Workable>();
    /**
     *
     */
    private final AffinaTOPLinkSessionManager sm;
    /**
     *
     */
    private ThreadLocal<Boolean> inTrans = new ThreadLocal<Boolean>();
    /**
     *
     */
    private final static Map<String, String> dbConnectionTypeMap = new HashMap<String, String>();
    private final static GenericPersistentDAO instance;

    static
    {
        dbConnectionTypeMap.put("JNDI", "sessionManager_pma");
        dbConnectionTypeMap.put("NONE_JNDI", "local_sessionManager_pma");
        String conType = System.getProperty("DBConnectionType", "JNDI");
        instance = new GenericPersistentDAO(DBConnectionType.valueOf(conType));
    }

    /**
     * Default connection to PMA that uses JNDI based connection.
     */
    protected GenericPersistentDAO()
    {
        this(dbConnectionTypeMap.get("sessionManager_pma"));
    }

    /**
     *
     */
    protected GenericPersistentDAO(DBConnectionType dnConType)
    {
        this(dbConnectionTypeMap.get(dnConType.name()));
    }

    /**
     * Constructs a DAO using pre configured bean with a database connection related properties. To use this constructor
     * a Spring Bean with id 'sessionManager_pma' that has a DB connection should exists.
     * <p/>
     */
    protected GenericPersistentDAO(String dbManagerBeanName)
    {
        sm = PersistentContextFactory.getSessionManager(dbManagerBeanName);
    }

    /**
     *
     * @return
     */
    public static Persistent instance()
    {
        return instance;
    }

    public boolean isInNonContainerTransaction()
    {
        return inTrans.get() != null && inTrans.get();
    }

    public boolean isInContainerTransaction()
    {
        return !isInNonContainerTransaction() && sm.isInContainerTransaction();
    }

    /**
     * Implement this method if a set of operations is required to be executed a within a transaction via the
     * implementation class.
     * <p/>
     * The by calling doTransactionAndCommit() above will do the work for you.
     */
    private void doTransactionalWork()
    {
        doWork();
    }

    /**
     * Will perform the transactional work as defined by the implementation class.
     */
    private <T> void doWork()
    {
        workable.get().doWork();
    }

    /**
     *
     * @param workable
     */
    public void addTransactionalWorker(Workable workable)
    {
        this.workable.set(workable);
    }

    public <T> void doTransactionalWorkAndCommit(Workable worker)
    {
        addTransactionalWorker(worker);
        doTransactionalWorkAndCommit();
    }

    /**
     * Performs Transactional work. This method throws PersistentException which is a RuntimeException.
     */
    public void doTransactionalWorkAndCommit()
    {
        boolean transactionComplete = false;

        while (!transactionComplete)
        {
            try
            {
                transactionComplete = true;
                inTrans.set(true);
                sm.getUnitOfWork();
                doTransactionalWork();
                sm.getUnitOfWork().commit();
            }
            catch (PersistentException ex)
            {
                final String errorMsg = "Error trying to commit work: " + ex.getMessage();
                logger.error(errorMsg, ex);
                rollBack();
                throw new PersistentException(errorMsg, ex);
            }
            catch (OptimisticLockException ex)
            {
                logger.warn("Optimistic lock exception, will retry", ex);
                transactionComplete = false;
                rollBack();
            }
            // Let runtime exceptions propagate, as this is how error responses are passed back to the caller.
            finally
            {
                inTrans.set(false);
                sm.release();
            }
        }
    }

    private void rollBack()
    {
        if (isInNonContainerTransaction())
        {
            sm.release();
        }
        else if (isInContainerTransaction())
        {
            sm.getUnitOfWork().revertAndResume();
        }
    }

    /**
     *
     * @return
     */
    public UnitOfWork getUnitOfWork()
    {
        if (!isInContainerTransaction() && !isInNonContainerTransaction())
        // You must either be in a Container transaction (eg running in the Introduced Card container),
        // or in a non-container manager transaction, eg running as a Workable in the GenericPersistentDAO.
        // Any other attempts to use a UnitOfWork can result in a memory leak (unless you manage the UoW properly).
        {
            throw new RuntimeException("Attempting to access a UnitOfWork outside of a transaction");
        }
        UnitOfWork uow = sm.getUnitOfWork();      
        return uow;
    }  
    /**
     * This will create a new object and registers it with the current persistence manager cache so that the persistent
     * manager knows that it is something it should take care of when it comes to executing CRUD commands.
     * <p/>
     * @param <R> The generic type of the object represented by the passed in class.
     * @param cls the Class of the object being passed.
     * <p/>
     * @return Returns the object after registering with the persistence cache.
     */
    public <R> R getRegisteredObject(Class<R> cls)
    {
        Object v = null;
        try
        {
            v = cls.newInstance();
            v = getUnitOfWork().registerNewObject(v);
        }
        catch (Exception ex)
        {
            final String errorMsg = "Error trying to register object with the ORM cache.";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        return cls.cast(v);
    }
    
    public <R> boolean deleteAllObject(List<R> deleteObjects)
    {
        try
        {            
            getUnitOfWork().deleteAllObjects(deleteObjects);
        }
        catch (Exception ex)
        {
            final String errorMsg = "Error trying to delete all object with the ORM cache.";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        return true;
    }
    
    public <R> R deleteObject(R deleteObject)
    {
       
        try
        {            
            return (R)getUnitOfWork().deleteObject(deleteObject);
        }
        catch (Exception ex)
        {
            final String errorMsg = "Error trying to delete object with the ORM cache.";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
       
    }


    public com.platform7.standardinfrastructure.multiissuer.Scope getScope(String name)
    {
        try
        {
            UnitOfWork uow = getUnitOfWork();
            return com.platform7.standardinfrastructure.multiissuer.Scope.locate(uow, name);
        }
        catch (Exception ex)
        {
            final String errorMsg = "Error trying to fetch scope " + name;
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
    }

    /**
     *
     * @param selectionCriteria
     * @param cls
     * @param ordering
     * @param partialAttributes < p/>
     * <p/>
     * @return
     */
    public Vector executeReadQuery(Expression selectionCriteria, Class cls, Expression ordering,
                                   String... partialAttributes)
    {
        UnitOfWork uow = getUnitOfWork();
        try
        {
            ReadAllQuery q = new ReadAllQuery();
            q.setReferenceClass(cls);
            q.setSelectionCriteria(selectionCriteria);
            q.conformResultsInUnitOfWork();
            q.bindAllParameters();
            if (partialAttributes != null)
            {
                for (String attr : partialAttributes)
                {
                    q.addPartialAttribute(attr);
                }
            }
            if (ordering != null)
            {
                q.addOrdering(ordering);
            }
            return (Vector) uow.executeQuery(q);
        }
        catch (Exception ex)
        {
            final String errorMsg = "Error trying to query data.";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        finally
        {
            
        }
    }

    /**
     *
     * @param query < p/>
     * <p/>
     * @return
     */
    public Vector executeQuery(ReadAllQuery query)
    {
        UnitOfWork uow = getUnitOfWork();
        try
        {
            return (Vector) uow.executeQuery(query);
        }
        catch (Exception ex)
        {
            final String errorMsg = "Error trying to query data.";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        finally
        {
           
        }
    }
    
    /**
     *
     * @param query < p/>
     * <p/>
     * @return
     */
    public Object executeReportQuery(ReportQuery query)
    {
        UnitOfWork uow = getUnitOfWork();
        try
        {
            return uow.executeQuery(query);
        }
        catch (Exception ex)
        {
            final String errorMsg = "Error trying to query data.";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        finally
        {
            
        }
    }
    
    
    ////////#############################################################///////
    //Temporary added here and need Mark's help to replace this
    AffinaTOPLinkSessionManager ses = (AffinaTOPLinkSessionManager) AppConfig.getBean("sessionManager_pma");

    
    //////////////scope/////////////////////////////////////////////////////////
    
    public com.platform7.standardinfrastructure.multiissuer.Scope getScopeById(BigDecimal oid)
    {
        try
        {
            UnitOfWork uow = getUnitOfWork();
            return com.platform7.standardinfrastructure.multiissuer.Scope.locate(uow, oid);
        }
        catch(Exception ex)
        {
            final String errorMsg = "Error trying to fetch scopeByOid "+oid;
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
    }
    
    public com.platform7.standardinfrastructure.multiissuer.Scope getScopeByName(String name)
    {
        try
        {
            UnitOfWork uow = getUnitOfWork();
            return com.platform7.standardinfrastructure.multiissuer.Scope.locate(uow, name);
        }
        catch(ScopeNotFoundException ex)
        {
            logger.warn("Scope  not found", ex.getMessage());
            return null;
        }
        catch(Exception ex)
        {
            final String errorMsg = "Error trying to fetch scopeByName "+name;
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
    }
    
    /*
     * This method returns list of scopes for selected filter parameters
     * <p/>
     * @param filter
     * <p/>
     * @return List<Scope>
     */
    public List<Scope> getScopes(FilterCriteria filter)//, PagingCriteria paging)
    {
        List<Scope> recordList = new ArrayList<Scope>();
        UnitOfWork uow = null;
        try
        {
            uow = getUnitOfWork();
            //uow=ses.getUnitOfWork();
            Iterator it=com.platform7.standardinfrastructure.multiissuer.Scope.locateAll(uow); 
            
            while (it.hasNext())
            {
                recordList.add((Scope) it.next());
            }

            return recordList;
        }
        catch (Exception ex)
        {
            final String errorMsg = "Error trying to fetch scopes ";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        finally
        {
            if (uow != null)
            {
                //uow.release();
            }
        }
    }
    /////////////PMAProduct/////////////////////////////////////////////////////
    
    public PMAProduct getProductById(BigDecimal oid)
    {
        try
        {
            UnitOfWork uow = getUnitOfWork();
            return PMAProduct.locate(uow, oid);
        }
        catch(Exception ex)
        {
            final String errorMsg = "Error trying to fetch ProductById "+oid;
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
    }
    
    public PMAProduct getProductByName(String name)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<PMAProduct> getProducts(FilterCriteria filter)//, PagingCriteria paging)
    {
        List<PMAProduct> recordList = null;//=new ArrayList<PMAProduct>();
        UnitOfWork uow = null;
        try
        {
            if (filter != null && filter.getScopeId() != null)
            {
                uow = getUnitOfWork();
                //uow=ses.getUnitOfWork();
                recordList=PMAProduct.locateAllByScope(uow,new BigDecimal(filter.getScopeId().intValue())); 
                
            }
            return recordList;
        }
        catch (Exception ex)
        {
            final String errorMsg = "Error trying to fetch products ";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        finally
        {
            if (uow != null)
            {
               //uow.release();
            }
        }
    }

    /////////////////ProductPart////////////////////////////////////////////////
    
    public PMAProductPart getProductPartById(BigDecimal oid)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PMAProductPart getProductPartByName(String name)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<PMAProductPart> getProductParts(FilterCriteria filter)//, PagingCriteria paging)
    {
        List<PMAProductPart> recordList = new ArrayList<PMAProductPart>();
        UnitOfWork uow = null;
        try
        {
            if (filter != null && filter.getProductId() != null)
            {
                uow = getUnitOfWork();
                //uow=ses.getUnitOfWork();

                PMAProduct product = PMAProduct.locate(uow, new BigDecimal(filter.getProductId().intValue()));

                PMAProductPart[] partMandatory = product.getMandatoryPMAProductParts();
                PMAProductPart[] partOptional = product.getOptionalPMAProductParts();

                for (int i = 0; i < partMandatory.length; i++)
                {
                    recordList.add(partMandatory[i]);
                }
                for (int i = 0; i < partOptional.length; i++)
                {
                    recordList.add(partOptional[i]);
                }                

            }
            return recordList;
        }
        catch (Exception ex)
        {
            final String errorMsg = "Error trying to fetch productparts ";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        finally
        {
            if (uow != null)
            {
                //uow.release();
            }
        }
    }

    ////////////////Application/////////////////////////////////////////////////
    
    public Application getApplicationById(BigDecimal oid)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Application getApplicationByName(String name)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Application> getApplications(FilterCriteria filter)//, PagingCriteria paging)
    {
        IStageScriptValidator ssValidator = new StageScriptValidator();
        List<Application> recordList = new ArrayList<Application>();
        UnitOfWork uow = null;
        try
        {
            if (filter != null && filter.getProductPartId() != null)
            {
                uow = getUnitOfWork();
                //uow=ses.getUnitOfWork();

                PMAProductPart productPart=PMAProductPart.locate(uow,new BigDecimal(filter.getProductPartId().intValue()));
                
                HashMap platformDependentPartList=productPart.getPlatformDependentParts();//getApplications();                
                                                
                Iterator it=platformDependentPartList.values().iterator();
                
                while(it.hasNext())
                {
                    PlatformDependentPart part = (PlatformDependentPart) it.next();

                    ArrayList appList = part.getApplications();

                    for (int j = 0; j < appList.size(); j++)
                    {
                        Application app = (Application) appList.get(j);

                        if (ssValidator.isValidScriptableApplication(app))
                        {
                            recordList.add(app);
                        }
                    }
                }

            }
            return recordList;
        }
        catch (Exception ex)
        {
            final String errorMsg = "Error trying to fetch productparts ";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        finally
        {
            if (uow != null)
            {
                //uow.release();
            }
        }
    }
    /////////////////BusinessFunction///////////////////////////////////////////
    
    public ESPBusinessFunction getBusinessFunctionById(BigDecimal oid)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ESPBusinessFunction getBusinessFunctionByName(String name)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
   
    public List<ESPBusinessFunction> getBusinessFunctions(FilterCriteria filter)//, PagingCriteria paging)
    {
        List<ESPBusinessFunction> recordList = new ArrayList<ESPBusinessFunction>();
        UnitOfWork uow = null;
        try
        {
            if (filter != null && filter.getApplicationId() != null)
            {
                uow = getUnitOfWork();
                //uow=ses.getUnitOfWork();

                Application application=Application.locate(uow,BigDecimal.valueOf(filter.getApplicationId().longValue()));
                                                
                if(EspConstant.TRUE.equalsIgnoreCase(application.getScriptable()))
                {
                    Iterator it = application.getBusinessFunctions().iterator();
                    while (it.hasNext())
                    {
                        ESPBusinessFunction busFunc = (ESPBusinessFunction) it.next();

                        if (!EspConstant.PARAMETERIZED_BUSINESS_FUNCTION_NAME.equalsIgnoreCase(busFunc.getName()))
                        {
                            recordList.add(busFunc);
                        }
                    }
                }
            }
            return recordList;
        }
        catch (Exception ex)
        {
            final String errorMsg = "Error trying to fetch productparts ";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        finally
        {
            if (uow != null)
            {
                //uow.release();
            }
        }
    }
    /////////////////StageScriptFilter///////////////////////////////////////////
    
    public ESPStageScriptFilter getStageScriptFiltersById(BigDecimal oid)//, PagingCriteria paging)
    {
        try
        {
            UnitOfWork uow = getUnitOfWork();
            return ESPStageScriptFilter.locateFiltersByOID(uow, oid);
        }
        catch(Exception ex)
        {
            final String errorMsg = "Error trying to fetch locateFiltersByStatus "+oid;
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
    }
    
    public ESPStageScriptFilter getStageScriptFiltersByTrackId(BigDecimal scopeOID,String trackId)//, PagingCriteria paging)
    {
        try
        {
            UnitOfWork uow = getUnitOfWork();
            return ESPStageScriptFilter.locateFiltersByTrackId(uow,scopeOID, trackId);
        }
        catch(Exception ex)
        {
            final String errorMsg = "Error trying to fetch locateFiltersByTrackId "+trackId+" for scopeOID "+scopeOID;
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
    }
    
        
    public List<ESPStageScriptFilter> getStageScriptFiltersByStatusAndScope(FilterCriteria filter)//, PagingCriteria paging)
    {
        List<ESPStageScriptFilter> recordList=null;
        UnitOfWork uow=null;
        try
        {
            if(filter!=null && filter.getScopeId()!=null && filter.getStatus()!=null)
            {
                uow = getUnitOfWork();
                //uow=ses.getUnitOfWork();
                recordList = ESPStageScriptFilter.locateFiltersByStatusAndScope(uow, BigDecimal.valueOf(filter.getScopeId().longValue()),filter.getStatus());
            }    
            else
            {
                logger.warn("Scope OID & Status must be exist to getList of filtersByStatusAndScope");
            }
            return recordList;
        }
        catch (Exception ex)
        {
            final String errorMsg = "Error trying to fetch 'stage script filter' ";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        finally
        {
            if (uow != null)
            {
               //uow.release();
            }
        }
    }    
    
    public List<ESPStageScriptFilter> getStageScriptFiltersByCheckDuplicate(FilterCriteria filter)//, PagingCriteria paging)
    {
        List<ESPStageScriptFilter> recordList=null;
        UnitOfWork uow=null;
        try
        {
            if(filter!=null && filter.getScopeId()!=null && filter.getStatus()!=null && filter.getApplicationId()!=null && filter.getBusinessFunctionId()!=null)
            {
                uow = getUnitOfWork();  
                if(filter.getCardId()!=null && filter.getCardId().length()>0)
                {
                    
                    recordList = ESPStageScriptFilter.locateFiltersByCheckDuplicateWithCardId(uow, BigDecimal.valueOf(filter.getScopeId().longValue()),BigDecimal.valueOf(filter.getApplicationId().longValue()),BigDecimal.valueOf(filter.getBusinessFunctionId().longValue()),filter.getCardId(),filter.getStatus());
                }
                else
                {
                    recordList = ESPStageScriptFilter.locateFiltersByCheckDuplicateWithNullCardId(uow, BigDecimal.valueOf(filter.getScopeId().longValue()),BigDecimal.valueOf(filter.getApplicationId().longValue()),BigDecimal.valueOf(filter.getBusinessFunctionId().longValue()),filter.getStatus());
                }
            }    
            else
            {
                logger.warn("Scope OID, Application OID, BusinessFucntionOID & Status must be exist to getList of filtersByStatusAndScope");
            }
            return recordList;
        }
        catch(Exception ex)
        {
            final String errorMsg = "Error trying to fetch 'stage script filter' ";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        finally
        {
            if(uow!=null)
            {
               //uow.release();
            }
        }
    }
    
     public ESPCardSetup getCardSetupDetailById(BigDecimal oid)
     {
        try
        {
            UnitOfWork uow = getUnitOfWork();
            return ESPCardSetup.locateCardSetupByOID(uow, oid);
        }
        catch(Exception ex)
        {
            final String errorMsg = "Error trying to fetch locateCardSetupByOID "+oid;
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
     }
    
    public List<ESPCardSetup> getCardSetupDetails(Scope scope, String aeTrackId)//, PagingCriteria paging)
    {
        List<ESPCardSetup> recordList=null;
        UnitOfWork uow=null;
        try
        {
            if(scope!=null && aeTrackId!=null && aeTrackId.length()>0)
            {
                uow = getUnitOfWork();  
                recordList = ESPCardSetup.locateByScopeAndAeTRN(uow,scope,aeTrackId);
            }    
            else
            {
                logger.warn("Scope, aeTrackId must be exist to getList of locateByScopeAndAeTRN");
            }
            return recordList;
        }
        catch(Exception ex)
        {
            final String errorMsg = "Error trying to fetch 'getCardSetupDetails' ";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        finally
        {
            if(uow!=null)
            {
               //uow.release();
            }
        }
    }
    
    public List<ESPCardSetup> getCardSetupDetailsBySemTrn(Scope scope, String semTrackId)//, PagingCriteria paging)
    {
        List<ESPCardSetup> recordList=null;
        UnitOfWork uow=null;
        try
        {
            if(scope!=null && semTrackId!=null && semTrackId.length()>0)
            {
                uow = getUnitOfWork();  
                recordList = ESPCardSetup.locateByScopeAndSemTRN(uow,scope,semTrackId);
            }    
            else
            {
                logger.warn("Scope, semTrackId must be exist to getList of locateByScopeAndSemTRN");
            }
            return recordList;
        }
        catch(Exception ex)
        {
            final String errorMsg = "Error trying to fetch 'getCardSetupDetailsBySemTrn' ";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        finally
        {
            if(uow!=null)
            {
               //uow.release();
            }
        }
    }
    
    public List<ESPApplicationDetail> getESPApplicationDetails(Scope scope, String pan,String psn,Long expDate)//, PagingCriteria paging)
    {
        List<ESPApplicationDetail> recordList=null;
        UnitOfWork uow=null;
        try
        {
            if(scope!=null && pan!=null && pan.length()>0 && psn!=null && psn.length()>0 && expDate!=null)
            {
                uow = getUnitOfWork();  
                recordList.add(ESPApplicationDetail.locateByPanPsnExpDate(uow, pan, psn, new Timestamp(expDate),scope)); 
            }    
            else
            {
                logger.warn("Scope, pan,psn & expDate must be exist to locateByPanPsnExpDate");
            }
            return recordList;
        }
        catch(Exception ex)
        {
            final String errorMsg = "Error trying to fetch 'getESPApplicationDetail' ";
            logger.error(errorMsg, ex);
            throw new PersistentException(errorMsg + " " + ex.toString(), ex);
        }
        finally
        {
            if(uow!=null)
            {
               //uow.release();
            }
        }
    }
    
    

    
}
