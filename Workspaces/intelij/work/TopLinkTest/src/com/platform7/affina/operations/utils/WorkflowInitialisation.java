package com.platform7.affina.operations.utils;/*
 *  Class responsible for creating pma or sds workflowoperations objects based on service
 *  which are used to carry out operator gui operations.
 */

import com.platform7.pma.miscellaneous.PMAWorkflowOperations;
import com.platform7.sds.common.SDSWorkflowOperations;
import com.platform7.standardinfrastructure.appconfig.AppConfig;
import com.platform7.standardinfrastructure.database.AffinaTOPLinkSessionManager;
import com.platform7.standardinfrastructure.jms.OperationsMessenger;
import com.platform7.standardinfrastructure.systemlogging.Message;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Vector;

public class WorkflowInitialisation {

    private static Logger log = Logger.getLogger(WorkflowInitialisation.class);
    private AffinaTOPLinkSessionManager pmaDatabase = null;
    private AffinaTOPLinkSessionManager sdsDatabase = null;
    private final HashMap workflowOperations = new HashMap();
    private String pmaUserName = null;
    private	String pmaPassWord = null;
    private final String repUserName = null;
    private final String repPassWord = null;
    private	String pmaAppProjectClassName = null;
    private	String pmaDatabaseConnection = null;
    private final String pmaService = null;
    private String sdsUserName = null;
    private	String sdsPassWord = null;
    private	String sdsAppProjectClassName = null;
    private	String sdsDatabaseConnection = null;
    private PMAWorkflowOperations pmawop = null;
    private PMAWorkflowOperations repwop = null;
    private SDSWorkflowOperations sdswop = null;
    private String pmaServiceQueue = null;
    private String sdsServiceQueue = null;
    private static WorkflowInitialisation workflowInitialisation = null;
    private final Vector oprTopLinkConnections =  new Vector();
    private static String osAuth;

    public static WorkflowInitialisation getInstance() {

        if(WorkflowInitialisation.workflowInitialisation == null) {
            osAuth = System.getProperty("OS_AUTH", "false");
            workflowInitialisation = new WorkflowInitialisation();
        }

        return workflowInitialisation;
    }

    private void initialisePMAproperties(final String service){
        try{
            log.trace("==> initialisePMAproperties");
            if(pmawop != null) {
                return;
            }
            OperationsMessenger pmaMsg=null;
            PMAWorkflowOperations pmawop1 = null;
            String temp = null;

            pmaUserName = System.getProperty("PMAUserName");
            pmaPassWord = System.getProperty("PMAPassword");
            pmaAppProjectClassName = System.getProperty("PMAProject");
            pmaDatabaseConnection = System.getProperty("DatabaseConnection");
            if((osAuth != null) && osAuth.trim().equalsIgnoreCase("true")){
                final String dbName = System.getProperty("pma_DATABASENAME");
                pmaDatabaseConnection = pmaDatabaseConnection.substring(0, pmaDatabaseConnection.indexOf("@") + 1);
                pmaDatabaseConnection += dbName;
            }
            log.trace("Service: " + service);
            if(service != null) {
                if(service.indexOf("Upload") > -1) { // Is this ever executed at all?
                    temp = service.substring(6).trim();
                    // pmaServiceQueue = "PMA.WORKUPLOAD.PMA." + temp; // do not hardcode!
                    pmaServiceQueue = System.getProperty("PmaUploadQueue", "ValueShouldBeDefinedInWeb.xml");
                } else if(service.indexOf("AccountDeletion") > -1) {
                    temp = service.substring(6).trim();
                    pmaServiceQueue = System.getProperty("PmaAccountDeletionQueue");
                    log.trace("pmaServiceQueue - PmaAccountDeletionQueue: " + pmaServiceQueue);
                } else {
                    temp = service.trim();
                    // pmaServiceQueue = "PMA.WORK.PMA." + temp; // do not hardcode!
                    pmaServiceQueue = System.getProperty("PMAQueue1");
                }

                if((pmaServiceQueue == null) || (pmaDatabaseConnection == null) || (pmaAppProjectClassName == null)) {
                    throw new Exception("PMADB properties are not set correctly:");
                }

                if(pmaUserName != null) {
                    pmaUserName = pmaUserName.trim() + "_" + temp;
                }
                if(pmaPassWord != null) {
                    pmaPassWord = pmaPassWord.trim() + "_" + temp;
                }

                pmaDatabase = (AffinaTOPLinkSessionManager) AppConfig.getBean("sessionManager_pma");

                pmaMsg = new OperationsMessenger(pmaServiceQueue, null);
                if((pmaMsg != null) && (pmaDatabase != null)) {
                    pmawop1 = new PMAWorkflowOperations(temp, pmaDatabase, pmaMsg);
                }

                pmawop = pmawop1;
                oprTopLinkConnections.add(pmaDatabase);
                workflowOperations.put(service + "PMA", pmawop);
            } else {
                if((pmaDatabaseConnection == null) || (pmaAppProjectClassName == null)) {
                    throw new Exception("PMADB properties are not set correctly:");
                }

                if(pmaUserName != null) {
                    pmaUserName = pmaUserName.trim();
                }
                if(pmaPassWord != null) {
                    pmaPassWord = pmaPassWord.trim();
                }

                log.trace("pmaUserName " + pmaUserName + ", pmaDatabaseConnection " + pmaDatabaseConnection + ", pmaAppProjectClassName " + pmaAppProjectClassName);

                pmaDatabase = (AffinaTOPLinkSessionManager) AppConfig.getBean("sessionManager_pma");

                pmawop1 = new PMAWorkflowOperations(null, pmaDatabase, null);
                pmawop = pmawop1;
                oprTopLinkConnections.add(pmaDatabase);
                workflowOperations.put("PMA", pmawop);
            }
        }catch(final Exception e){
            Message.alarm("Connecting to PMA database failed: " + e.getMessage());
        }
    }

    private void initialiseREPproperties() {
        try{
            log.trace("Inside initialiseREPproperties");
            pmaAppProjectClassName = System.getProperty("PMAProject");
            pmaDatabaseConnection = System.getProperty("DatabaseConnection");
            if((osAuth != null) && osAuth.trim().equalsIgnoreCase("true")){
                final String dbName = System.getProperty("pma_DATABASENAME");
                pmaDatabaseConnection = pmaDatabaseConnection.substring(0, pmaDatabaseConnection.indexOf("@") + 1);
                pmaDatabaseConnection += dbName;
            }

            if((pmaDatabaseConnection == null) || (pmaAppProjectClassName == null)) {
                throw new Exception("REPDB properties are not set correctly:");
            }

            pmaDatabase = (AffinaTOPLinkSessionManager) AppConfig.getBean("sessionManager_pma");
            final PMAWorkflowOperations pmawop1 = new PMAWorkflowOperations(null, pmaDatabase, null);
            repwop = pmawop1;
            oprTopLinkConnections.add(pmaDatabase);
            workflowOperations.put("REP", repwop);
        }catch(final Exception e){
            Message.alarm("Connecting to PMA database failed: " + e.getMessage());
        }
    }

    private void initialiseSDSproperties(final String service){
        try{
            log.trace("==> initialiseSDSproperties");
            OperationsMessenger sdsMsg=null;
            SDSWorkflowOperations sdswop1 = null;

            sdsUserName = System.getProperty("SDSUserName");
            sdsPassWord = System.getProperty("SDSPassword");
            sdsAppProjectClassName = System.getProperty("SDSProject");

            sdsDatabaseConnection = System.getProperty("DatabaseConnection");
            if((osAuth != null) && osAuth.trim().equalsIgnoreCase("true")){
                final String dbName = System.getProperty("sds_DATABASENAME");
                sdsDatabaseConnection = sdsDatabaseConnection.substring(0, sdsDatabaseConnection.indexOf("@") + 1);
                sdsDatabaseConnection += dbName;
            }
            if(service != null) {
                // String defaultSdsServiceQueue = "SDS.WORK.SDS." + service.trim(); // do not hardcode!
                sdsServiceQueue = System.getProperty("SDSQueue1");
                if((sdsServiceQueue == null) || (sdsDatabaseConnection == null) || (sdsAppProjectClassName == null)) {
                    throw new Exception("SDS DB properties are not set correctly:");
                }
                if(sdsUserName != null) {
                    sdsUserName = sdsUserName.trim() + "_" + service.trim();
                }
                if(sdsPassWord != null) {
                    sdsPassWord = sdsPassWord.trim() + "_" + service.trim();
                }
                sdsDatabase = (AffinaTOPLinkSessionManager) AppConfig.getBean("sessionManager_sds");

                sdsMsg = new OperationsMessenger(sdsServiceQueue, null);
                if((sdsDatabase != null) && (sdsMsg !=null)) {
                    sdswop1 = new SDSWorkflowOperations(service, sdsDatabase, sdsMsg);
                }
                sdswop = sdswop1;
                oprTopLinkConnections.add(sdsDatabase);
                workflowOperations.put(service + "SDS", sdswop);
            } else {
                if((sdsDatabaseConnection == null) || (sdsAppProjectClassName == null)) {
                    throw new Exception("SDS DB properties are not set correctly:");
                }
                if(sdsUserName != null) {
                    sdsUserName = sdsUserName.trim();
                }
                if(sdsPassWord != null) {
                    sdsPassWord = sdsPassWord.trim();
                }
                log.trace("sdsUserName " + sdsUserName + ", sdsDatabaseConnection " + sdsDatabaseConnection  + ", sdsAppProjectClassName " + sdsAppProjectClassName);
                sdsDatabase = (AffinaTOPLinkSessionManager) AppConfig.getBean("sessionManager_sds");
                sdswop1 = new SDSWorkflowOperations(null, sdsDatabase, null);
                sdswop = sdswop1;
                oprTopLinkConnections.add(sdsDatabase);
                workflowOperations.put("SDS", sdswop);
            }
        }catch(final Exception e){
            Message.alarm("Connecting to SDS database failed: " + e.getMessage());
        }
    }

    /**
     * Initialise PMA or SDS properties based on the service.
     */
    public void initialise(String service,final String database) throws Exception {
        if(service != null) {
            service = service.trim();
        }

        log.trace("Initialising workflow operations");
        if(database.trim().equalsIgnoreCase("SDS")) {
            if(service != null) {
                sdswop = (SDSWorkflowOperations)workflowOperations.get(service + "SDS");
            } else {
                sdswop = (SDSWorkflowOperations)workflowOperations.get("SDS");
            }
            if(sdswop == null) {
                initialiseSDSproperties(service);
            }
        } else if(database.trim().equalsIgnoreCase("PMA")) {
            if(service != null) {
                pmawop = (PMAWorkflowOperations)workflowOperations.get(service + "PMA");
            } else {
                pmawop = (PMAWorkflowOperations)workflowOperations.get("PMA");
            }

            if(pmawop == null) {
                initialisePMAproperties(service);
            }
        } else if(database.trim().equalsIgnoreCase("REP")) {
            log.debug(" db == REP");
            repwop = (PMAWorkflowOperations)workflowOperations.get("REP");
            if(repwop == null) {
                log.debug(" repwop is null");
                initialiseREPproperties();
            }
        }
    }

    public PMAWorkflowOperations getPMAWorkflowOperation() {
        return pmawop;
    }

    public SDSWorkflowOperations getSDSWorkflowOperation() {
        return sdswop;
    }

    public PMAWorkflowOperations getREPWorkflowOperation() {
        if(pmaDatabaseConnection != null){
            log.trace("DatabaseConnection in Rep "+pmaDatabaseConnection);
            System.setProperty("DatabaseConnection",pmaDatabaseConnection);
        }
        return repwop;
    }
    public Vector getAcquiredDBResource() {
        return oprTopLinkConnections;
    }
}