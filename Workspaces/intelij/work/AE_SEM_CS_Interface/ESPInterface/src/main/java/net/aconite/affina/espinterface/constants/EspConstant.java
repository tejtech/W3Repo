/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.constants;

/**
 * @author wakkir.muzammil
 */
public class EspConstant
{
    //from Affina to Esp
    public final static String CARD_SETUP_ALERT = "CardSetupAlert";
    public final static String STAGE_SCRIPT_ALERT = "StageScriptAlert";

    //from Esp to Sem
    public final static String CARD_SETUP_REQUEST = "CardSetupRequest";
    public final static String STAGE_SCRIPT_REQUEST = "StageScriptRequest";
    public final static String SCRIPT_STATUS_RESPONSE = "ScriptStatusResponse";

    //from Sem to Esp
    public final static String STAGE_SCRIPT_RESPONSE = "StageScriptResponse";
    public final static String CARD_SETUP_RESPONSE = "CardSetupResponse";

    public final static String PROGRESS_MESSAGE = "ProgressMessage";
    public final static String ERROR_MESSAGE = "ErrorMessage";

    //--------------------------------------------
    public final static String MQ_MESSAGE_TYPE = "espMessageType";
    public final static String MQ_MESSAGE_HASHCODE = "espMessageHashcode";
    public final static String MQ_ESP_ERROR = "esp_error";
    public final static String MQ_SHOW_VALIDATION_ERROR = "show_validation_error";
    public final static String MQ_SHOW_PROGRESS_MESSAGE = "show_progress_message";
    public final static String JMS_TEXT_MESSAGE = "jms_text";
    public final static String JMS_BYTES_MESSAGE = "jms_bytes";

    //------Security Related Constant-----------------------------------
    public final static String SC_ENCRYPTED_MESSAGE = "encryptedMessage";
    public final static String SC_DECRYPTED_MESSAGE = "decryptedMessage";
    
    //---------------------------------------------------------------------------
    public final static String VT_MODULE_NAME_VALUE = "esp_interface";
    public final static String VT_SERVICE_NAME_VALUE = "";
    public final static String VT_APPSERVER_SUFFIX_VALUE = "1";

    //------Velocity Template Related Constant-----------------------------------
    public final static String VT_CURRENT_DATETIME = "currentDateTime";
    public final static String VT_MODULE_NAME = "moduleName";
    public final static String VT_SERVICE_NAME = "serviceName";
    public final static String VT_APPSERVER_SUFFIX = "appServerSuffix";
    public final static String VT_RESPONSE_TYPE = "responseType";
    public final static String VT_REQUEST_TYPE = "requestType";
    public final static String VT_TRACKING_REFERENCE = "trackingReference";
    public final static String VT_STATUS = "status";
    public final static String VT_ERROR_DATA = "errorData";
    public final static String VT_ERROR_DESCRIPTION = "errorDescription";
    public final static String VT_ERROR_CODE = "errorCode";

    //------Database Table Related Constant-----------------------------------
    public final static String DB_AE_TRACKING_ID = "aeTrackingId";
    public final static String DB_ESP_TRACKING_ID = "espTrackingId";
    
    public final static String DB_SCOPE_NAME = "name";
    public final static String DB_SCOPE = "scope";
    
    public final static String DB_TRACKING_ID = "trackingid";
    public final static String DB_REQUEST = "request";
    public final static String DB_PRIMARY_KEY = "primaryKey";
    
    public final static String DB_APP_DETAILS = "espApplicationDetail";

    public final static String PAN_SEQUENCE_ID = "ApplicationIdentifier";
    
    //---------------------------------------------------------------------
    
    public final static String TRUE = "true";
    public final static String FALSE = "false";


}
