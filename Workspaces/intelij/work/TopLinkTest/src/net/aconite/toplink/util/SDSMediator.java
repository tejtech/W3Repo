package net.aconite.toplink.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import com.platform7.affina.operations.utils.*;
import com.platform7.pma.card.SoftCard;
import com.platform7.pma.card.partactions.CardRequest;
import com.platform7.pma.card.stageddelivery.StagedDelivery;
import com.platform7.pma.miscellaneous.PMAWorkflowOperations;
import com.platform7.pma.request.IssuerCardRequest;
import com.platform7.pma.request.Request;
import com.platform7.pma.request.data.BureauStatus;
import com.platform7.pma.request.uploadrequest.CardEvent;
import com.platform7.pma.request.uploadrequest.UploadManifestRequest;
import com.platform7.sds.bureaudestination.BureauDeliveryBatch;
import com.platform7.sds.bureaudestination.BureauDeliveryBatchState;
import com.platform7.sds.bureaudestination.BureauDeliveryRequest;
import com.platform7.sds.bureaudestination.BureauDestination;
import com.platform7.sds.common.SDSWorkflowOperations;
import com.platform7.sds.common.SDSWorkflowOperationsAdapter;
import com.platform7.sds.deliverypackage.*;
import com.platform7.standardinfrastructure.appconfig.AppConfig;
import com.platform7.standardinfrastructure.database.AffinaTOPLinkDataSourceConnector;
import com.platform7.standardinfrastructure.database.AffinaTOPLinkSessionManager;
import com.platform7.standardinfrastructure.database.QueryUtils;
import com.platform7.standardinfrastructure.database.TOPLinkDeploymentOrDevelopmentProjectCreator;
import com.platform7.standardinfrastructure.distributedworkflow.WorkflowOperations;
import com.platform7.standardinfrastructure.distributedworkflow.WorkflowOperationsAdapter;
import com.platform7.standardinfrastructure.distributedworkflow.WorkflowOperationsException;
import com.platform7.standardinfrastructure.distributedworkflow.WorkflowStub;
import com.platform7.standardinfrastructure.ejb.messagebeans.MessageSendProperties;
import com.platform7.standardinfrastructure.jms.OperationsMessenger;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import com.platform7.standardinfrastructure.systemlogging.Message;
import com.platform7.stdpma.XMLWriter;
import com.platform7.stdsds.ejb.common.DestinationId;
import com.platform7.stdsds.ejb.common.User;
import com.platform7.stdsds.ejb.destinationdelivery.DestinationDeliveryBeanProxyFactory;
import com.platform7.stdsds.ejb.destinationdelivery.DestinationDeliveryDelegate;
import com.platform7.stdsds.ejb.exception.DelegateException;
import com.platform7.stdsds.jmsimpl.JMSPackageListenerLocator;

import org.apache.log4j.Logger;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.platform.database.oracle.Oracle10Platform;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.queries.ReportQuery;
import org.eclipse.persistence.sequencing.DefaultSequence;
import org.eclipse.persistence.sessions.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.sql.DataSource;

public class SDSMediator {

    private static final Logger log = Logger.getLogger(SDSMediator.class);

    private WorkflowInitialisation workflowInitialisation = null;
    private boolean DEBUG = false;
    private static HashMap dpStateNumberandNameMapping = new HashMap(6);
    private int noOfPedingDps = 0;
    UnitOfWork uow;
    DatabaseSession session;
    //DatabaseSession dbSession;

    private static final String DESTINATION_SERVER_NAME = "destination";

    static {
        dpStateNumberandNameMapping.put(new Integer(DeliveryPackageState.PENDING), "Pending ");
        dpStateNumberandNameMapping.put(new Integer(DeliveryPackageState.DELIVERING), "Delivering ");
        dpStateNumberandNameMapping.put(new Integer(DeliveryPackageState.PARTIALLY_DELIVERED), "Partially Delivered ");
        dpStateNumberandNameMapping.put(new Integer(DeliveryPackageState.DELIVERED), "Delivered ");
        dpStateNumberandNameMapping.put(new Integer(DeliveryPackageState.CANCELLED), "Cancelled ");
        dpStateNumberandNameMapping.put(new Integer(DeliveryPackageState.EXPIRED), "Expired  ");
    }

    /**
     * Gets all the bureau Names as a Set of Strings
     *
     * @param scope -> Scope Name w.r.to which bureaus should be fetched
     */
    public Set getAllBureaus(String scope, String service) throws Exception {
        HashSet bureauSet = null;
        Vector bureauVector = null;

        workflowInitialisation = WorkflowInitialisation.getInstance();
        workflowInitialisation.initialise(service, "SDS");
        SDSWorkflowOperations sdsWorkflowop = (SDSWorkflowOperations) workflowInitialisation.getSDSWorkflowOperation();
        bureauSet = (HashSet) sdsWorkflowop.getAllBureaus(scope);

        return bureauSet;
    }

    public SDSMediator()
    {
        try
        {
            File springConfigFile = new File("c://affina//conf//import-spring-config.xml");
            log.trace("Loading spring config file: " + springConfigFile);
            ApplicationContext factoryX = new FileSystemXmlApplicationContext("file:///" + springConfigFile);
            session = setProp();
            session.login();
            getStagedDeliveries();
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            session.release();
            session.logout();
        }
    }

    //////////////////////////////////////////////////////////////

    private void getStagedDeliveries() {

        uow = session.acquireUnitOfWork();
        ReadAllQuery query = new ReadAllQuery();
        /*ReportQuery q = new ReportQuery(SoftCard.class, null);
        q*/
        //query.setReferenceClass(CardEvent.class);
        // Object obj = uow.executeQuery(query);
        //System.out.println("xxxx:"+obj.toString());

        query.setReferenceClass(SoftCard.class);
        Object obj2 = uow.executeQuery(query);
        System.out.println("xxxx2:" + obj2.toString());


    }


    private void getStagedDeliveries2() {

        ExpressionBuilder builder = new ExpressionBuilder();
        Expression trnTest = builder.get("trackingid").equal("12345750818");
        Object obj2 = session.readObject(CardEvent.class, trnTest);
        //System.out.println("xxxx2:" + obj2.getClass().getName());
        System.out.println("xxxx2:" + obj2.toString());


    }

    /////////////////////////////////////////////////////////////////////////

    private DatabaseSession setProp() throws Exception {
        String sdsUserName = System.getProperty("SDSUserName");
        String sdsPassWord = System.getProperty("SDSPassword");

        if (sdsUserName != null) {
            sdsUserName = sdsUserName.trim();// + "_" + service.trim();
        }
        if (sdsPassWord != null) {
            sdsPassWord = sdsPassWord.trim();// + "_" + service.trim();
        }
        AffinaTOPLinkSessionManager sdsDatabase = (AffinaTOPLinkSessionManager)
                AppConfig.getBean("sessionManager_pma");
        TOPLinkDeploymentOrDevelopmentProjectCreator tlpc =
                new TOPLinkDeploymentOrDevelopmentProjectCreator(sdsDatabase.getDataSource(), "pma", "affina",
                        "com.platform7.stdpma.databaseproject.toplinkproject", false);

        Project dbProject = createProj(sdsDatabase, "com.platform7.stdpma.databaseproject.toplinkproject");
        DatabaseSession dbSession = dbProject.createDatabaseSession();
        //dbSession.login();
        return dbSession;
    }

    private Project createProj(AffinaTOPLinkSessionManager tlpc,
                               String projectClassName) throws Exception {
        Class projectClass = Class.forName(projectClassName);
        Project project = (Project) projectClass.newInstance();


        log.trace("creating Toplink DB login and setting properties");
        DatabaseLogin dbl = new DatabaseLogin();
        // This required to make generated SQL stmts work in oracle (such as correct syntax of "select for update")
        dbl.usePlatform(new Oracle10Platform());

        // If we've been passed an external DataSource, use that.
        /* if (tlpc.getDataSource() instanceof DataSource)
        {
            log.trace("telling Toplink to use external DataSource");
            DataSource ds =  tlpc.getDataSource();
            Connector dsc = new AffinaTOPLinkDataSourceConnector(ds);
            dbl.setUsesExternalConnectionPooling(true);
            dbl.setConnector(dsc);
            log.trace("Toplink is to use an external transaction controller?: ");
            dbl.setUsesExternalTransactionController(false);
        }
        else
        {*/
        dbl.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dbl.setConnectionString("jdbc:oracle:thin:@localhost:1521:AffinaDB");
        dbl.setUsesExternalConnectionPooling(false);

        dbl.setUserName("pma");
        dbl.setPassword("affina");
        dbl.useNativeSequencing();
        dbl.addSequence(new DefaultSequence("CIN_SEQUENCE_", 1));
        dbl.addSequence(new DefaultSequence("CERT_SN_SEQUENCE_", 1));

        dbl.setUsesByteArrayBinding(false);
        dbl.setUsesStreamsForBinding(false);
        dbl.setUsesStringBinding(false);
        if (dbl.shouldUseByteArrayBinding()) { // Can only be used with binding.
            dbl.setUsesStreamsForBinding(false);
        }
        dbl.setShouldForceFieldNamesToUpperCase(false);
        dbl.setShouldOptimizeDataConversion(true);
        dbl.setShouldTrimStrings(true);

        dbl.setUsesBatchWriting(true);
        dbl.setShouldBindAllParameters(true);
        dbl.setShouldCacheAllStatements(true);

        if (dbl.shouldUseBatchWriting()) { // Can only be used with batch writing.
            dbl.setUsesJDBCBatchWriting(true);
        }

        project.setLogin(dbl);

        return project;
    }

    /**
     * Gets all the bureau Names as a Set of Strings
     */
    public Set getAllBureaus() throws Exception {
        HashSet bureauSet = null;
        Vector bureauVector = null;

        workflowInitialisation = WorkflowInitialisation.getInstance();
        workflowInitialisation.initialise(null, "SDS");

        SDSWorkflowOperations sdsWorkflowop = (SDSWorkflowOperations) workflowInitialisation.getSDSWorkflowOperation();
        bureauSet = (HashSet) sdsWorkflowop.getAllBureaus();
        return bureauSet;
    }

    /**
     * Gets all the bureau Names as a Set of Strings
     */
    public Set getAllDirectAndBatchBureaus() throws Exception {
        HashSet bureauSet = null;
        Vector bureauVector = null;

        workflowInitialisation = WorkflowInitialisation.getInstance();
        workflowInitialisation.initialise(null, "SDS");
        SDSWorkflowOperations sdsWorkflowop = (SDSWorkflowOperations) workflowInitialisation.getSDSWorkflowOperation();
        bureauSet = (HashSet) sdsWorkflowop.getAllDirectAndBatchBureaus();
        return bureauSet;
    }

    /**
     * Gets all the Staged Delivery Packges as a Set of Strings for a given
     * bureau Name and service.
     * Note: Usage of Toplink artifacts is a short gap arrangement. This will
     * be either replaced or moved to some other module.
     * @param bureauName -> The Name of the Bureau for which Staged DeliveryPackages
     *                      will be retrived from DataBase.
     **/
/*   public Set getDeliveryPackages(String bureauName,String service) throws Exception {
      HashSet dpSet = null;
      Vector deliveryPackages = null;

      workflowInitialisation = com.platform7.affina.operations.utils.WorkflowInitialisation.getInstance();
      workflowInitialisation.initialise(service,"SDS");
      SDSWorkflowOperations sdsWorkflowop = (SDSWorkflowOperations) workflowInitialisation.getSDSWorkflowOperation();
      dpSet =(HashSet) sdsWorkflowop.getDeliveryPackages(bureauName);
        return dpSet;
   }
*/

    /**
     * Gets all the Staged Delivery Packges as a list for a given
     * bureau Name.
     * Note: Usage of Toplink artifacts is a short gap arrangement. This will
     * be either replaced or moved to some other module.
     *
     * @param bureauName -> The Name of the Bureau for which Staged DeliveryPackages
     *                   will be retrived from DataBase.
     */
    public ArrayList getDeliveryPackages(String bureauName, String dp_oid, boolean flag, int no_Of_Rows_To_Fetch, String traversing) throws Exception {
        DeliveryPackage temp;
        DestSpecificDataStore dsdt;
        ArrayList resultElement = null;
        List tempElement = null;
        Vector dpVec = null;
        SDSWorkflowOperations sdsWorkflowop = null;
        try {
            workflowInitialisation = WorkflowInitialisation.getInstance();
            workflowInitialisation.initialise(null, "SDS");
            sdsWorkflowop = (SDSWorkflowOperations) workflowInitialisation.getSDSWorkflowOperation();

            if (dp_oid == null || dp_oid.equalsIgnoreCase("null"))
                dpVec = sdsWorkflowop.fetchDeliveryPackages(bureauName, null, flag, no_Of_Rows_To_Fetch, traversing);
            else
                dpVec = sdsWorkflowop.fetchDeliveryPackages(bureauName, new BigDecimal(dp_oid), flag, no_Of_Rows_To_Fetch, traversing);

            //setNumberOfPendingDps(sdsWorkflowop.getPendingDPCount());
            int size = dpVec.size();
            resultElement = new ArrayList(size);

            for (int i = 0; i < size; i++) {
                temp = (DeliveryPackage) dpVec.get(i);
                dsdt = temp.getDestinationSpecificData();
                tempElement = new ArrayList(7);
                tempElement.add(temp.getPackageId());
                tempElement.add(getStateDetail(temp.getState()));
                tempElement.add(temp.getDateProduced());

                if (dsdt.getElement("IssuingSystem") != null)
                    tempElement.add(dsdt.getElement("IssuingSystem").getValue());
                else
                    tempElement.add("Not Available");

                if (dsdt.getElement("BusinessProduct") != null)
                    tempElement.add(dsdt.getElement("BusinessProduct").getValue());
                else
                    tempElement.add("Not Available");

                if (dsdt.getElement("StockReference") != null)
                    tempElement.add(dsdt.getElement("StockReference").getValue());
                else
                    tempElement.add("Not Available");

                tempElement.add(temp.getPrimaryKey());
                resultElement.add(tempElement);
            }
        } finally {
            if (dpVec != null)
                dpVec.removeAllElements();

            dpVec = null;
            sdsWorkflowop = null;
            workflowInitialisation = null;
            System.gc();
        }

        return resultElement;
    }


    public ArrayList getDeliveryPackages(Vector bureauName, String dp_oid, boolean flag, int no_Of_Rows_To_Fetch, String traversing) throws Exception {
        DeliveryPackage temp;
        DestSpecificDataStore dsdt;
        ArrayList resultElement = null;
        List tempElement = null;
        Vector dpVec = null;
        SDSWorkflowOperations sdsWorkflowop = null;
        try {
            workflowInitialisation = WorkflowInitialisation.getInstance();
            workflowInitialisation.initialise(null, "SDS");
            sdsWorkflowop = (SDSWorkflowOperations) workflowInitialisation.getSDSWorkflowOperation();

            if (bureauName.size() > 0) {
                Iterator it = bureauName.listIterator();
                resultElement = new ArrayList();
                while (it.hasNext()) {
                    String bName = (String) it.next();
                    if (dp_oid == null || dp_oid.equalsIgnoreCase("null"))
                        dpVec = sdsWorkflowop.fetchDeliveryPackages(bName, null, flag, no_Of_Rows_To_Fetch, traversing);
                    else
                        dpVec = sdsWorkflowop.fetchDeliveryPackages(bName, new BigDecimal(dp_oid), flag, no_Of_Rows_To_Fetch, traversing);

                    //setNumberOfPendingDps(sdsWorkflowop.getPendingDPCount());
                    tempElement = new ArrayList();
                    if (dpVec != null && !dpVec.isEmpty()) {
                        temp = (DeliveryPackage) dpVec.get(0);
                        tempElement.add(temp.getPackageId());
                    } else {
                        tempElement.add("");
                    }
                    tempElement.add(bName);
                    //tempElement.add(getNumberOfPendingDps());
                    resultElement.add(tempElement);
                }
            }
        } finally {
            if (dpVec != null)
                dpVec.removeAllElements();
            dpVec = null;
            sdsWorkflowop = null;
            workflowInitialisation = null;
            System.gc();
        }
        return resultElement;
    }

    /**
     * SDS Workflow Operations - Business Queries
     * <p/>
     * answer the Workflow Stub which has a Bureau Return Request
     * workflow with the given filename. Null is answered if the
     * workflow cannot be found on the associated service.
     */
    public String getBureauReturnWorkflowWithFilename(String service,
                                                      String fileName) throws Exception {
        String resultElement = null;
        WorkflowOperations wFOps = null;
        WorkflowStub stub = null;

        if ((fileName == null) ||
                ((fileName != null) && (fileName.equals("")))) {
            throw new RuntimeException("Incorrect Input for the fileName.");
        }
        workflowInitialisation = WorkflowInitialisation.getInstance();
        workflowInitialisation.initialise(service, "SDS");
        SDSWorkflowOperations sdsWorkflowop = (SDSWorkflowOperations) workflowInitialisation.getSDSWorkflowOperation();
        if (fileName != null) {
            try {
                stub = sdsWorkflowop.getBureauReturnWorkflowWithFilename(fileName);
            }
            catch (WorkflowOperationsException woe) {
                throw new Exception("Internal Error: " + woe.getMessage());
            }
            if (stub == null)
                resultElement = "workflow cannot be found  for BRF file " + fileName;
            else
                resultElement = makeWFElementsAsStr(stub);
        }
        return resultElement;
    }

    /**
     * SDS Workflow Operations - Business Queries
     * <p/>
     * answer the Workflow Stub which has a Bureau Return Request
     * workflow with the given filename. Null is answered if the
     * workflow cannot be found.
     */
    public String getBureauReturnWorkflowWithFilename(String fileName)
            throws Exception {
        String resultElement = null;
        WorkflowOperations wFOps = null;
        WorkflowStub stub = null;

/*      if ( (fileName == null) ||
          ( (fileName != null) && (fileName.equals("")))) {
        throw new RuntimeException("Incorrect Input for the fileName.");
      }*/
        workflowInitialisation = WorkflowInitialisation.getInstance();
        workflowInitialisation.initialise(null, "SDS");
        SDSWorkflowOperations sdsWorkflowop = (SDSWorkflowOperations) workflowInitialisation.getSDSWorkflowOperation();
        if (fileName != null) {
            try {
                stub = sdsWorkflowop.getBureauReturnWorkflowWithFilename(fileName);
            }
            catch (WorkflowOperationsException woe) {
                throw new Exception("Internal Error: " + woe.getMessage());
            }
            if (stub == null)
                resultElement = "workflow cannot be found  for BRF file " + fileName;
            else
                resultElement = makeWFElementsAsStr(stub);
        }
        return resultElement;
    }

    /**
     * SDS Workflow Operations - Business Queries
     * <p/>
     * answer the Workflow Stub which has a Bureau Delivery Request
     * workflow with the given filename. Null is answered if the
     * workflow cannot be found on the associated service.
     */
    public ArrayList getOutputFileDetails(String scope, String service,
                                          String fileName) throws Exception {
        ArrayList resultElement = null;
        String tempElement = "";
        String batchid = null;
        BigDecimal batchOID = null;
        Vector deliveryPackages = null;
        WorkflowStub stub = null;
        Iterator deliveryPackagesItr = null;

        if ((fileName == null) ||
                ((fileName != null) && (fileName.equals("")))) {
            throw new RuntimeException("Incorrect Input for the fileName.");
        }

        workflowInitialisation = WorkflowInitialisation.getInstance();
        workflowInitialisation.initialise(service, "SDS");
        SDSWorkflowOperations sdsWorkflowop = (SDSWorkflowOperations) workflowInitialisation.getSDSWorkflowOperation();

        if (sdsWorkflowop != null) {

            try {
                stub = sdsWorkflowop.getBureauDeliveryWorkflowWithFilename(fileName);
            }
            catch (WorkflowOperationsException woe) {
                throw new Exception("Internal Error: " + woe.getMessage());
            }

            resultElement = new ArrayList();

            if (stub == null)
                throw new Exception("Output file " + fileName + " is not a file that affina has produced");
            else {
                SDSWorkflowOperationsAdapter sdsOperationsAdapter = SDSWorkflowOperationsAdapter
                        .makeAdaptor(stub);
                BureauDeliveryBatch deliveryBatch = sdsOperationsAdapter.getBureauDeliveryBatch();
                resultElement.add(new Integer(deliveryBatch.numberOfPackagesInBatch()).toString());

                /*  batchid = deliveryBatch.getBatchId();
             batchOID = new BigDecimal(batchid.trim());
             System.out.println("bio = " + batchOID.string());

             ExpressionBuilder expressionBuilder = new ExpressionBuilder();
             Expression expression = expressionBuilder.get("m_batchId").equal(batchOID.longValue());
             deliveryPackages =  sdsDatabase.takeUnitOfWork().readAllObjects(com.platform7.sds.deliverypackage.DeliveryPackage.class,expression);*/

                deliveryPackagesItr = sdsWorkflowop.getPackages(deliveryBatch);

                if (deliveryPackagesItr != null) {

                    while (deliveryPackagesItr.hasNext()) {

                        DeliveryPackage temp = (DeliveryPackage) deliveryPackagesItr.next();
                        tempElement = temp.getPackageId() + ",";
                        tempElement = tempElement + getStateDetail(temp.getState()) + ",";
                        tempElement = tempElement + temp.getDateProduced() + ",";
                        tempElement = tempElement + temp.getDateExpired();
                        resultElement.add(tempElement);
                        tempElement = "";

                    } // end of while
                } // end of if
            } //end of else
        }//end of if
        return resultElement;
    }

    /**
     * SDS Workflow Operations - Business Queries
     * <p/>
     * answer the Workflow Stub which has a Bureau Delivery Request
     * workflow with the given filename. Null is answered if the
     * workflow cannot be found.
     */
    public ArrayList getOutputFileDetails(String fileName)
            throws Exception {
        ArrayList resultElement = null;
        List tempElement = null;
        String batchid = null;
        BigDecimal batchOID = null;
        Vector deliveryPackages = null;
        WorkflowStub stub = null;
        Iterator deliveryPackagesItr = null;
        DeliveryPackage temp;
        DestSpecificDataStore dsdt;


        try {
            workflowInitialisation = WorkflowInitialisation.getInstance();
            workflowInitialisation.initialise(null, "SDS");
            SDSWorkflowOperations sdsWorkflowop = (SDSWorkflowOperations) workflowInitialisation.getSDSWorkflowOperation();

            if (sdsWorkflowop != null) {

                stub = sdsWorkflowop.getBureauDeliveryWorkflowWithFilename(fileName);
                resultElement = new ArrayList();

                if (stub == null)
                    throw new FileNotFoundException("OG1011");
                else {
                    SDSWorkflowOperationsAdapter sdsOperationsAdapter = SDSWorkflowOperationsAdapter
                            .makeAdaptor(stub);
                    BureauDeliveryBatch deliveryBatch = sdsOperationsAdapter.getBureauDeliveryBatch();
                    resultElement.add(new Integer(deliveryBatch.numberOfPackagesInBatch()).toString());
                    deliveryPackagesItr = sdsWorkflowop.getPackages(deliveryBatch);

                    if (deliveryPackagesItr != null) {

                        while (deliveryPackagesItr.hasNext()) {

                            temp = (DeliveryPackage) deliveryPackagesItr.next();
                            dsdt = temp.getDestinationSpecificData();
                            tempElement = new ArrayList();
                            tempElement.add(temp.getPackageId());
                            tempElement.add(getStateDetail(temp.getState()));
                            tempElement.add(temp.getDateProduced().toString());
                            tempElement.add(temp.getDateExpired().toString());
                            tempElement.add(dsdt.getElement("IssuingSystem").getValue());
                            tempElement.add(dsdt.getElement("BusinessProduct").getValue());
                            tempElement.add(dsdt.getElement("StockReference").getValue());
                            resultElement.add(tempElement);
                        } // end of while
                    } // end of if
                } //end of else
            }//end of if
        } catch (FileNotFoundException fe) {
            Message.error("FileNotFoundException : " + fe.getMessage());
            throw new Exception("OG1011");
        }
        return resultElement;
    }

    private String getStateDetail(int state) {
        Object stateName = null;

        if ((stateName = dpStateNumberandNameMapping.get(new Integer(state))) == null)
            return "Unknown";
        else
            return (String) stateName;

    }

    private String makeWFElementsAsStr(WorkflowStub stub)
    {
        WorkflowOperationsAdapter wa = new WorkflowOperationsAdapter(stub);
        StringBuffer sb = new StringBuffer(256);
        sb.append("<BR>Workflow OID: [");
        sb.append(wa.getWorkflowStubOID());
        sb.append("] ");
        sb.append(wa.getLogDescription());
        sb.append(" : ");
        sb.append(wa.getWorkflowState());
        sb.append(" @ ");
        sb.append(wa.getLastStateTransitionTime());
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////
    public static void main(String[] args)
    {
        new SDSMediator();
    }
    //////////////////////////////////////////////////////////////////////

}


