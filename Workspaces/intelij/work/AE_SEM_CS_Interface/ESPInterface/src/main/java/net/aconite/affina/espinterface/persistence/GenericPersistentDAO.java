/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.persistence;

import com.platform7.standardinfrastructure.database.*;
import java.util.*;
import net.aconite.affina.espinterface.constants.*;
import net.aconite.affina.espinterface.exceptions.PersistentException;
import net.aconite.affina.espinterface.factory.*;
import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.ReadAllQuery;
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
    private Workable workable;
    /**
     *
     */
    private final AffinaTOPLinkSessionManager sm;
    /**
     *
     */
    private boolean inTrans = false;
    /**
     *
     */
    private UnitOfWork transUOW;
    private final static Map<String, String> dbConnectionTypeMap = new HashMap<String, String>();

    static
    {
        dbConnectionTypeMap.put("JNDI", "sessionManager_pma");
        dbConnectionTypeMap.put("NONE_JNDI", "local_sessionManager_pma");
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
     * @param dbContext Application context obtained via spring.
     */
    protected GenericPersistentDAO(String dbManagerBeanName)
    {
        sm = PersistentContextFactory.getSessionManager(dbManagerBeanName);
    }

    /**
     *
     * @return
     */
    public static Persistent getPersistent()
    {
        String conType = System.getProperty("DBConnectionType", "JNDI");
        return new GenericPersistentDAO(DBConnectionType.valueOf(conType));
    }

    /**
     * Implement this method if a set of operations is required to be executed a within a transaction via the
     * implementation class.
     * <p/>
     * The by calling doTransactionAndCommit() above will do the work for you.
     */
    private <T> void doTransactionalWork(T arg)
    {
        doWork(arg);
    }

    /**
     * Will perform the transactional work as defined by the implementation class.
     */
    private <T> void doWork(T arg)
    {
        workable.doWork(arg);
    }

    /**
     *
     * @param workable
     */
    @Override
    public void addTransactionalWorker(Workable workable)
    {
        this.workable = workable;
    }

    /**
     * Performs Transactional work. This method throws PersistentException which is a RuntimeException.
     */
    @Override
    public synchronized <T> void doTransactionalWorkAndCommit(T arg)
    {
        boolean transactionComplete = false;

        while(!transactionComplete)
        {
            try
            {
                transactionComplete = true;
                transUOW = getUnitOfWork();
                inTrans = true;
                doTransactionalWork(arg);
                transUOW.commit();
            }
            catch (PersistentException ex)
            {
                logger.error("Error trying to commit work." + ex.getStackTrace().toString(), ex);
                rollBack();
                throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
            }
            catch (OptimisticLockException ex)
            {
                logger.warn("Optimistic lock exception, will retry",  ex);
                transactionComplete = false;
                rollBack();
            }
            // let other runtime exceptions propagate
            finally
            {
                try
                {
                    inTrans = false;
                    sm.release();
                }
                catch (PersistentException ex)
                {
                    throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
                }
                catch (RuntimeException ex)
                {
                    throw ex;
                }
            }
        }
    }

    private synchronized void rollBack()
    {
        if (sm != null && sm.getUnitOfWork() != null && transUOW != null)
        {
            transUOW.release();
        }
    }

    /**
     *
     * @param <T>
     * @param obj
     */
    @Override
    public synchronized <T> void delete(T obj)
    {
        try
        {
            UnitOfWork uow = getUnitOfWork();
            uow.registerObject(obj);
            uow.deleteObject(obj);
            if (!inTrans)
            {
                uow.commit();
            }
        }
        catch (Exception ex)
        {
            logger.error("Error trying to delete data.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
        }
    }

    /**
     *
     * @param <T>
     * @param obj
     */
    @Override
    public synchronized void deleteAll(Collection obj)
    {
        try
        {
            UnitOfWork uow = getUnitOfWork();
            uow.registerAllObjects(obj);
            uow.deleteAllObjects(obj);
            if (!inTrans)
            {
                uow.commit();
            }
        }
        catch (Exception ex)
        {
            logger.error("Error trying to delete data.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
        }
    }

    /**
     * This method throws PersistentException which is a RuntimeException.
     * <p/>
     * @param <T>
     * @param obj
     */
    @Override
    public synchronized <T> void save(T obj)
    {
        try
        {
            UnitOfWork uow = getUnitOfWork();
            uow.registerObject(obj);
            if (!inTrans)
            {
                uow.commit();
            }
        }
        catch (Exception ex)
        {
            logger.error("Error trying to save data.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
        }
    }

    /**
     *
     * @return
     */
    private synchronized UnitOfWork getUnitOfWork()
    {
        if (inTrans)
        {
            return transUOW;
        }
        return sm.getUnitOfWork();
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
    @Override
    public synchronized <R> R getRegisteredObject(Class<R> cls)
    {
        Object v = null;
        try
        {
            v = cls.newInstance();
            v = getUnitOfWork().registerNewObject(v);
        }
        catch (Exception ex)
        {
            logger.error("Error trying to register object with the ORM cache.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
        }
        return cls.cast(v);
    }

    /**
     *
     * @param <R>
     * @param cls < p/>
     * <p/>
     * @return
     */
    @Override
    public synchronized <R> Vector getRegisteredAllObjects(Collection<R> cls)
    {
        return getUnitOfWork().registerAllObjects(cls);
    }

    /**
     *
     * @param cls < p/>
     * <p/>
     * @return
     */
    @Override
    public synchronized Object getRegisteredExistingObject(Object cls)
    {
        Object v = null;
        try
        {
            v = getUnitOfWork().registerExistingObject(cls);
        }
        catch (Exception ex)
        {
            logger.error("Error trying to register object with the ORM cache.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
        }
        return v;
    }

    /**
     * Retrieves a DB object that matches the given ID.
     * <p/>
     * @param <T>     Generic type of the object.
     * @param id      ID of the database object.
     * @param cls
     * @param idField The name of the field that represents the ID. This is taken as a parameter as there may be no
     *                consistent naming mechanism for representing the id, so the caller can pass in the correct one.
     * <p/>
     * @return The object that matches th ID.
     */
    @Override
    public synchronized <T> T getObjectById(long id, Class<T> cls, String idField)
    {
        Object retValue = null;
        try
        {
            ExpressionBuilder builder = new ExpressionBuilder();
            Expression exp = builder.get(idField).equal(id);
            retValue = getUnitOfWork().readObject(cls, exp);
        }
        catch (Exception ex)
        {
            logger.error("Error trying trying to query data.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
        }
        return cls.cast(retValue);
    }

    /**
     * Returns an object by a foreign key object.
     * <p/>
     * @param <T>      Generic type parameter.
     * @param obj      Foreign key object value.
     * @param cls      Class of the object to be fetched.
     * @param objField The foreign key as defined in the object to be fetched.
     * <p/>
     * @return The object defined by the cls parameter..
     */
    @Override
    public <T> T getObjectByReferencingObject(Object obj, Class<T> cls, String objField)
    {
        Object retValue = null;
        try
        {
            ExpressionBuilder builder = new ExpressionBuilder();
            Expression exp = builder.get(objField).equal(obj);
            retValue = getUnitOfWork().readObject(cls, exp);
        }
        catch (Exception ex)
        {
            logger.error("Error trying to query data.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
        }
        return cls.cast(retValue);
    }

    /**
     * Returns an object by a foreign key object.
     * <p/>
     * @param <T>      Generic type parameter.
     * @param obj      Foreign key object value.
     * @param cls      Class of the object to be fetched.
     * @param objField The foreign key as defined in the object to be fetched.
     * <p/>
     * @return The object defined by the cls parameter..
     */
    @Override
    public synchronized <T> Vector getAllObjectsByReferencingObject(Object obj, Class<T> cls, String objField)
    {
        Vector retValue = null;
        try
        {
            ExpressionBuilder builder = new ExpressionBuilder();
            Expression exp = builder.get(objField).equal(obj);
            retValue = getUnitOfWork().readAllObjects(cls, exp);
        }
        catch (Exception ex)
        {
            logger.error("Error trying to query data.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
        }
        return retValue;
    }

    /**
     * Fetches an object from DB by its name.
     * <p/>
     * @param <T>
     * @param name    Name of the item to bee looked up.
     * @param cls     Class of the object.
     * @param idField The field name of the column to be looked up, as mapped within the domain object
     * <p/>
     * @return
     */
    @Override
    public synchronized <T> Vector getObjectByName(String value, Class<T> cls, String nameField)
    {
        Vector retValue = new Vector();
        try
        {
            ExpressionBuilder builder = new ExpressionBuilder();
            Expression exp = builder.get(nameField).equal(value);
            retValue = getUnitOfWork().readAllObjects(cls, exp);
        }
        catch (Exception ex)
        {
            logger.error("Error trying to query data.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
        }
        finally
        {
        }
        return retValue;
    }

    public com.platform7.standardinfrastructure.multiissuer.Scope getScope(String name)
    {
        try
        {
            UnitOfWork uow = getUnitOfWork();
            return com.platform7.standardinfrastructure.multiissuer.Scope.locate(uow, name);
        }
        catch(Exception ex)
        {
            logger.error("Error trying to fetch scope.", ex);
            throw new PersistentException("Error trying to fetch scope." + ex.toString(), ex);
        }
    }

    /**
     * Executes update, delete queries.
     * <p/>
     * @param sql < p/>
     * <p/>
     * @return
     */
    @Override
    public synchronized boolean executeUpdateQuery(String sql)
    {
        try
        {
            UnitOfWork uow = getUnitOfWork();
            uow.executeNonSelectingSQL(sql);
            if (!inTrans)
            {
                uow.commit();
            }
        }
        catch (Exception ex)
        {
            return false;
        }
        return true;
    }

    /**
     * Executes a results bearing select query.
     * <p/>
     * @param sql < p/>
     * <p/>
     * @return
     */
    @Override
    public synchronized Vector executeSelectQuery(String sql)
    {
        Vector res = new Vector();
        try
        {
            UnitOfWork uow = getUnitOfWork();
            res = uow.executeSQL(sql);
        }
        catch (Exception ex)
        {
            res = null;
        }
        return res;
    }

    /**
     * removes all rows from the given table.
     * <p/>
     * @param table
     */
    @Override
    public synchronized void emptyTable(String table)
    {
        try
        {
            String sql = "delete from " + table;
            executeUpdateQuery(sql);
        }
        catch (Exception ex)
        {
            logger.error("Error trying to query data.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
        }
    }

    /**
     * Reads the whole table represented by the parameter cls
     * <p/>
     * @param cls < p/>
     * <p/>
     * @return
     */
    @Override
    public Vector readTable(Class cls)
    {
        try
        {
            UnitOfWork uow = getUnitOfWork();
            return uow.readAllObjects(cls);
        }
        catch (Exception ex)
        {
            logger.error("Error trying to query data.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
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
    @Override
    public synchronized Vector executeReadQuery(Expression selectionCriteria, Class cls, Expression ordering,
                                   String... partialAttributes)
    {
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
            return (Vector) getUnitOfWork().executeQuery(q);
        }
        catch (Exception ex)
        {
            logger.error("Error trying to query data.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
        }
    }

    /**
     *
     * @param query < p/>
     * <p/>
     * @return
     */
    @Override
    public synchronized Vector executeQuery(ReadAllQuery query)
    {
        try
        {
            return (Vector) getUnitOfWork().executeQuery(query);
        }
        catch (Exception ex)
        {
            logger.error("Error trying to query data.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
        }
    }

    /**
     *
     * @param query < p/>
     * <p/>
     * @return
     */
    @Override
    public synchronized Object executeReportQuery(ReportQuery query)
    {
        try
        {
            return (Vector) getUnitOfWork().executeQuery(query);
        }
        catch (Exception ex)
        {
            logger.error("Error trying to query data.", ex);
            throw new PersistentException("Error trying to commit work." + ex.toString(), ex);
        }
    }
}
