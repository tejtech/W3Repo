/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.demo;

import com.platform7.standardinfrastructure.appconfig.AppConfig;
import com.platform7.standardinfrastructure.database.AffinaTOPLinkSessionManager;

/**
 *
 * @author wakkir.muzammil
 */
public class CrossCheckData 
{
    public static void insertCardSetupRequestRecord(String pan,String psn,String year, String month,String appType,String appVersion,String trackId)
    {
        insertRequestRecord("CardSetupRequest",pan,psn,year, month,appType,appVersion,trackId);
    }
    
    public static void insertScriptStatusUpdateRecord(String pan,String psn,String year, String month,String appType,String appVersion,String trackId)
    {
        insertRequestRecord("ScriptStatusUpdate",pan,psn,year, month,appType,appVersion,trackId);
    
    }
    //insertCardSetupRequestRecord("CardSetupRequest",req.getCard().getPAN(),req.getCard().getPANSequence(),req.getCard().getExpirationYear(),req.getCard().getExpirationMonth(),req.getApplication().getApplicationType(),req.getApplication().getApplicationVersion(),req.getTrackingReference());
    private static synchronized void insertRequestRecord(String msgType,String pan,String psn,String year, String month,String appType,String appVersion,String trackId)
    {
        String insertQuery="insert into PMA.TEST_ESP_REQUEST_DATA (PAN,PSN,EXPYEAR,EXPMONTH,APPTYPE,APPVERSION,TRACKID,DATECREATED,MSGTYPE) Values ('"+pan+"','"+psn+"','"+year+"','"+month+"','"+appType+"','"+appVersion+"','"+trackId+"',sysdate,'"+msgType+"')";
    
        System.out.println("insertQuery :  \n"+insertQuery);

        AffinaTOPLinkSessionManager ses=(AffinaTOPLinkSessionManager) AppConfig.getBean("sessionManager_pma");
        ses.getUnitOfWork().executeNonSelectingSQL(insertQuery);        
        ses.getUnitOfWork().commitAndResume();
        
        //Persistent dao = GenericPersistentDAO.getPersistent();        
        //boolean b= dao.executeUpdateQuery(insertQuery);
        //System.out.println("updated ?  "+b);
        
        /*
        DROP table PMA.TEST_ESP_REQUEST_DATA;
        CREATE TABLE PMA.TEST_ESP_REQUEST_DATA
        (   
            PAN           VARCHAR2(20) ,
            PSN           VARCHAR2(03) ,
            EXPYEAR       VARCHAR2(20),
            EXPMONTH      VARCHAR2(20) ,
            APPTYPE       VARCHAR2(100),
            APPVERSION    VARCHAR2(20) ,
            TRACKID       VARCHAR2(200) ,
            DATECREATED   DATE ,
            MSGTYPE Varchar2(40)
        )TABLESPACE PMA_TAB01
        
        --delete from TEST_ESP_REQUEST_DATA;
        select * from TEST_ESP_REQUEST_DATA order by 7,1,2,5,6
        
        SELECT 
        d.tracking_id,
        b.plastic_number as pan ,
        b.pan_sequence_number as psn ,
        b.expiration_date as expdate,
        c.application_type as appType,
        c.application_version as appVersion,
        a.tracking_id_alias,
        t.*
        --SELECT count (*)
        from  PMA.ESP_CARD_SETUP_DETAILS a inner join
        ESP_APPLICATION_DETAILS b on a.application_detail_oid=b.oid inner join 
        applications c on b.application_oid=c.oid inner join 
        requests d on d.oid=a.request_oid inner join
        TEST_ESP_REQUEST_DATA t on t.pan=b.plastic_number  
        and t.psn=b.pan_sequence_number  
        and t.trackid=a.tracking_id_alias  
        and t.appType=c.application_type and 
        and t.appVersion=c.application_version

        */

        
    }
}
