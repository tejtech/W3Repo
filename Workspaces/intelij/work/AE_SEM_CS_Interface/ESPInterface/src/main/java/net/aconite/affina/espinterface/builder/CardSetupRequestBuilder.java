/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.builder;

import com.platform7.pma.application.Application;
import com.platform7.pma.request.emvscriptrequest.ESPApplicationDetail;
import com.platform7.pma.request.emvscriptrequest.ESPCardSetup;
import com.platform7.standardinfrastructure.appconfig.AppConfig;
import com.platform7.standardinfrastructure.database.AffinaTOPLinkSessionManager;
import com.platform7.standardinfrastructure.multiissuer.Scope;
import net.aconite.affina.espinterface.exceptions.EspMessageBuilderException;
import net.aconite.affina.espinterface.persistence.GenericPersistentDAO;
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

    private List<CardSetupRequest> getCardSetupRequests(String scopeName,String aeTrackId)
        throws AffinaEspUtilException, EspMessageBuilderException
    {
        Scope scope = GenericPersistentDAO.instance().getScope(scopeName);

        List<ESPCardSetup> espCardSetups = ESPCardSetup.locateByScopeAndAeTRN(
            GenericPersistentDAO.instance().getUnitOfWork(),
            scope,
            aeTrackId);

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
