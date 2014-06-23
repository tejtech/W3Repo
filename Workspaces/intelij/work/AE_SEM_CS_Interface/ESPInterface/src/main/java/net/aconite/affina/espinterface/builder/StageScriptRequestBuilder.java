/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.builder;

import net.aconite.affina.espinterface.exceptions.EspMessageBuilderException;
import net.aconite.affina.espinterface.xmlmapping.sem.CardSetupRequest;
import net.aconite.affina.espinterface.xmlmapping.sem.CardType;
import net.aconite.affina.espinterface.xmlmapping.sem.NVPType;
import net.aconite.affina.espinterface.xmlmapping.sem.StageScriptRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.acointe.affina.esp.AffinaEspUtilException;
import net.acointe.affina.esp.AffinaEspUtils;

/**
 * @author wakkir.muzammil
 */
public class StageScriptRequestBuilder implements IMessageBuilder
{
    private static final Logger logger = LoggerFactory.getLogger(StageScriptRequestBuilder.class.getName());

    public List<StageScriptRequest> buildSSList(MessageContent messageContent) throws EspMessageBuilderException
    {
        if (messageContent.getTrackingReference() == null || messageContent.getTrackingReference().trim().length() == 0)
        {
            throw new EspMessageBuilderException("AE tracking reference value cannot be null or empty");
        }

        Date date = new Date();
        long time = date.getTime();
        String outTrackId = messageContent.getTrackingReference() + "-" + time;

        List<StageScriptRequest> requests = new ArrayList<StageScriptRequest>();

        for (int i = 0; i < 1; i++)
        {
            try 
            {
                StageScriptRequest request = new StageScriptRequest();
                request.setTrackingReference(outTrackId);

                CardType cardType = new CardType();
                //Accoding to the SEM specification, the expiration date value must be hex of the java milliseconds
                //cardType.setExpirationDate(Long.toHexString(1475190000000l).toUpperCase());
                
                cardType.setExpirationYear(AffinaEspUtils.getYearInYYYY(date));
                cardType.setExpirationMonth(AffinaEspUtils.getMonthInMM(date));
                cardType.setPAN("0129002312");
                cardType.setPANSequence("01");
                request.setCard(cardType);

                StageScriptRequest.BusinessFunction bs = new StageScriptRequest.BusinessFunction();
                bs.setFunctionName("Function One");
                request.setBusinessFunction(bs);

                StageScriptRequest.Action ac = new StageScriptRequest.Action();
                ac.setEndDate("20");
                ac.setRestageAutomatically(BigInteger.ONE);
                ac.setStartDate("0");
                request.setAction(ac);

                NVPType nvp1 = new NVPType();
                nvp1.setName("MyName");
                nvp1.setValue("MyValue");

                request.getScriptDataItem().add(nvp1);

                requests.add(request);
            } 
            catch (AffinaEspUtilException ex) 
            {
                throw new EspMessageBuilderException(ex);
            }
        }

        logger.debug("Stage Script Request generated for AE Tracking Reference : "+outTrackId+"\n" +requests.toString());

        return requests;
    }

    public List<CardSetupRequest> buildCSList(MessageContent messageContent) throws EspMessageBuilderException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object build(MessageContent messageContent) throws EspMessageBuilderException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
