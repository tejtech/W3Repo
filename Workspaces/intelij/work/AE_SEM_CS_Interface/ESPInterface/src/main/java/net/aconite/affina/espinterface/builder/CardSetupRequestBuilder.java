/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.builder;

import com.platform7.standardinfrastructure.appconfig.AppConfig;
import com.platform7.standardinfrastructure.database.AffinaTOPLinkSessionManager;
import net.aconite.affina.espinterface.exceptions.EspMessageBuilderException;
import net.aconite.affina.espinterface.xmlmapping.sem.AppType;
import net.aconite.affina.espinterface.xmlmapping.sem.CardSetupRequest;
import net.aconite.affina.espinterface.xmlmapping.sem.CardType;
import net.aconite.affina.espinterface.xmlmapping.sem.StageScriptRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import net.acointe.affina.esp.AffinaEspUtilException;
import net.acointe.affina.esp.AffinaEspUtils;
import org.eclipse.persistence.queries.DataReadQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;

/**
 * @author wakkir.muzammil
 */
public class CardSetupRequestBuilder implements IMessageBuilder
{
    private static final Logger logger = LoggerFactory.getLogger(CardSetupRequestBuilder.class.getName());
    
    AffinaTOPLinkSessionManager ses=(AffinaTOPLinkSessionManager) AppConfig.getBean("sessionManager_pma");
    
    public List<CardSetupRequest> buildCSList(MessageContent messageContent) throws EspMessageBuilderException
    {
        if (messageContent.getTrackingReference() == null || messageContent.getTrackingReference().trim().length() == 0)
        {
            throw new EspMessageBuilderException("AE tracking reference value for the card setup request cannot be null or empty");
        }
        
        try
        {
            List<CardSetupRequest> requests=getCardSetupRequests(messageContent.getScopeName(),messageContent.getTrackingReference());
            return requests;
        }
        catch (AffinaEspUtilException ex) 
        {
            throw new EspMessageBuilderException(ex);
        }

        /*
        Vector cardSetupList=null;
        try
        {
            Persistent cardSetupDAO = GenericPersistentDAO.getPersistent();

            Expression requestExp = new ExpressionBuilder().get(EspConstant.DB_REQUEST).get(EspConstant.DB_TRACKING_ID).equal(messageContent.getTrackingReference());

            Expression appDetailExp = new ExpressionBuilder().get(EspConstant.DB_APP_DETAILS).get(EspConstant.DB_SCOPE).get(EspConstant.DB_SCOPE_NAME).equal(messageContent.getScopeName());
            
            Expression compoundExp=requestExp.and(appDetailExp);
                    
            cardSetupList = cardSetupDAO.executeReadQuery(compoundExp,ESPCardSetup.class, null, (String[])null);
        }
        catch (Exception ex)
        {
            throw new EspMessageBuilderException(ex.getMessage(), ex);
        }

        if (cardSetupList == null || cardSetupList.isEmpty())
        {
            throw new EspMessageBuilderException("Warning: Affina is configured to send card information to SEM interface, but no application is configured to provide this information.  Tracking reference : '" + messageContent.getTrackingReference() + "'");
        }
        else
        {
            logger.debug(cardSetupList.size()+" CardSetupRequest will be generated for the AE tracking reference "+messageContent.getTrackingReference());
        }   
        
        List<CardSetupRequest> requests = new ArrayList<CardSetupRequest>();

        for (Object aCardSetupList : cardSetupList)
        {
            try 
            {
                ESPCardSetup cardSetup = (ESPCardSetup) aCardSetupList;
                ESPApplicationDetail espAppDetail=cardSetup.getESPApplicationDetail();
                
                if(espAppDetail==null)
                {
                    throw new EspMessageBuilderException("Esp Application details cannot be null");
                }

                CardSetupRequest request = new CardSetupRequest();
                request.setTrackingReference(cardSetup.getEspTrackingId());
                
                CardType cardType = new CardType();                
                cardType.setExpirationYear(AffinaEspUtils.getYearInYYYY(espAppDetail.getExpiryDate()));
                cardType.setExpirationMonth(AffinaEspUtils.getMonthInMM(espAppDetail.getExpiryDate()));                                
                cardType.setPAN(espAppDetail.getPan());
                cardType.setPANSequence(espAppDetail.getPanSequenceNumber());
                request.setCard(cardType);

                AppType appType = new AppType();               
                Application application=espAppDetail.getApplication();
                appType.setApplicationType(application.getApplicationType());
                appType.setApplicationVersion(application.getApplicationVersion());
                request.setApplication(appType);

                requests.add(request);
            } 
            catch (AffinaEspUtilException ex) 
            {
                throw new EspMessageBuilderException(ex);
            }
        }
        */        
        
    }

    public Object build(MessageContent messageContent) throws EspMessageBuilderException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<StageScriptRequest> buildSSList(MessageContent messageContent) throws EspMessageBuilderException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    //This method will be updated with the ORM objects to get data
    private List<CardSetupRequest> getCardSetupRequests(String scopeName,String aeTrackId) throws AffinaEspUtilException, EspMessageBuilderException
    {
        final String selectQuery="SELECT DISTINCT scp.name,req.tracking_id,apd.plastic_number as pan ,apd.pan_sequence_number as psn ,apd.expiration_date as expdate,app.application_type as appType,app.application_version as appVersion,csd.tracking_id_alias as semTrackId from  PMA.ESP_CARD_SETUP_DETAILS csd inner join ESP_APPLICATION_DETAILS apd on apd.oid=csd.application_detail_oid inner join applications app on app.oid=apd.application_oid inner join requests req on req.oid=csd.request_oid inner join scopes scp on scp.oid=apd.scope_oid and scp.oid=csd.scope_oid where 1=1 and scp.name=#scopeName and req.tracking_id=#aeTrackId order by 1,2,3,4,5,6,7";
    
        logger.debug("selectQuery :  \n"+selectQuery);

        DataReadQuery selectDataReadQuery = new DataReadQuery();
        selectDataReadQuery.addArgument("scopeName");
        selectDataReadQuery.addArgument("aeTrackId");
                
        selectDataReadQuery.bindAllParameters();

        Vector<String> args = new Vector<String>();
        args.add(scopeName);
        args.add(aeTrackId);   
        
        selectDataReadQuery.setSQLString(selectQuery);

        Vector dbRecords=(Vector)ses.getUnitOfWork().executeQuery(selectDataReadQuery, args);
        
        if (dbRecords == null || dbRecords.isEmpty())
        {
            throw new EspMessageBuilderException("Warning: Affina is configured to send card information to SEM interface, but no application is configured to provide this information.  Tracking reference : '"+aeTrackId+"' in "+scopeName+" scope");
        }
        else
        {
            logger.debug(dbRecords.size()+" CardSetupRequest will be generated for the AE tracking reference "+aeTrackId+" in "+scopeName+" scope");
        } 
        
        Enumeration em=dbRecords.elements();

        if(em==null )
        {
            throw new EspMessageBuilderException("Warning: Affina is configured to send card information to SEM interface, but no application is configured to provide this information.  Tracking reference : '"+aeTrackId+"' in "+scopeName+" scope");
        }
        
        List<CardSetupRequest> requests = new ArrayList<CardSetupRequest>();
        
        while(em.hasMoreElements())
        {
            DatabaseRecord record =(DatabaseRecord)em.nextElement();
            String pan=(String)record.get("PAN");
            String psn=(String)record.get("PSN");
            //Date rsPanExpiryDate = record.("expdate");
            Date expDate=(Date)record.get("EXPDATE");
            String applicationType=(String)record.get("APPTYPE");
            String applicationVersion=(String)record.get("APPVERSION");
            String semTrackId=(String)record.get("SEMTRACKID");
            
            //Date date=new Date(expDate.longValue());

            CardSetupRequest request = new CardSetupRequest();
            request.setTrackingReference(semTrackId);

            CardType cardType = new CardType();                
            cardType.setExpirationYear(AffinaEspUtils.getYearInYYYY(expDate));
            cardType.setExpirationMonth(AffinaEspUtils.getMonthInMM(expDate));                                
            cardType.setPAN(pan);
            cardType.setPANSequence(psn);
            request.setCard(cardType);

            AppType appType = new AppType();               
           
            appType.setApplicationType(applicationType);
            appType.setApplicationVersion(applicationVersion);
            request.setApplication(appType);

            requests.add(request);
        }
        
        return requests;

        
    }


}
