/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.builder;

import com.platform7.pma.application.Application;
import com.platform7.pma.request.emvscriptrequest.ESPApplicationDetail;
import com.platform7.pma.request.emvscriptrequest.ESPCardSetup;
import net.aconite.affina.espinterface.exceptions.EspMessageBuilderException;
import net.aconite.affina.espinterface.xmlmapping.sem.AppType;
import net.aconite.affina.espinterface.xmlmapping.sem.CardSetupRequest;
import net.aconite.affina.espinterface.xmlmapping.sem.CardType;
import net.aconite.affina.espinterface.xmlmapping.sem.StageScriptRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.acointe.affina.esp.AffinaEspUtilException;
import net.acointe.affina.esp.AffinaEspUtils;
import net.aconite.affina.espinterface.dao.CardSetupDetailDao;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;

/**
 * @author wakkir.muzammil
 */
public class CardSetupRequestBuilder implements IMessageBuilder
{
    private CardSetupDetailDao cardSetupDetailDao=new CardSetupDetailDao();
    
    private static final Logger logger = LoggerFactory.getLogger(CardSetupRequestBuilder.class.getName());
        
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
       

    }

    public Object build(MessageContent messageContent) throws EspMessageBuilderException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<StageScriptRequest> buildSSList(MessageContent messageContent) throws EspMessageBuilderException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private List<CardSetupRequest> getCardSetupRequests(String scopeName,String aeTrackId)
        throws AffinaEspUtilException, EspMessageBuilderException
    {        
        FilterCriteria filterObject=new FilterCriteria();
        filterObject.setScopeName(scopeName);
        filterObject.setTrackId(aeTrackId);
        List<ESPCardSetup> espCardSetups=cardSetupDetailDao.getList(filterObject, null);

        List<CardSetupRequest> requests = new ArrayList<CardSetupRequest>();

        if(espCardSetups != null)
        {
            for(ESPCardSetup espCardSetup : espCardSetups)
            {
                ESPApplicationDetail appDetail = espCardSetup.getESPApplicationDetail();
                Application app = appDetail.getApplication();

                String pan = appDetail.getPan();
                String psn = appDetail.getPanSequenceNumber();
                Date expDate = appDetail.getExpiryDate();

                String applicationType = app.getApplicationType();
                String applicationVersion = app.getApplicationVersion();

                String semTrackId = espCardSetup.getEspTrackingId();

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
        }

        return requests;
    }
}
