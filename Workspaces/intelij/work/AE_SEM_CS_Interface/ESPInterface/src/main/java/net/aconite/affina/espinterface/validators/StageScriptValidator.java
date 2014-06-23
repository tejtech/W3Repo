/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.validators;

import com.platform7.pma.application.Application;
import com.platform7.pma.card.CardStatus;
import com.platform7.pma.card.SoftCard;
import java.util.Date;
import net.acointe.affina.esp.AffinaEspUtilException;
import net.acointe.affina.esp.AffinaEspUtils;
import net.aconite.affina.espinterface.constants.EspConstant;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wakkir.muzammil
 */
public class StageScriptValidator implements IStageScriptValidator
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(StageScriptValidator.class.getName());

    public boolean isValidScriptableApplication(Application app)
    {
        boolean isValid=false;
        if(app==null)
        {
            return false;
        }
        
        boolean isScriptable=(EspConstant.TRUE.equalsIgnoreCase(app.getScriptable()));
        boolean isAppTypeExist=(app.getApplicationType()!=null && app.getApplicationType().trim().length()>0);
        boolean isAppVersionExist=(app.getApplicationVersion()!=null && app.getApplicationVersion().trim().length()>0);
        
        isValid=(isScriptable && isAppTypeExist && isAppVersionExist);
        if(!isValid)
        {
            logger.debug("Invalid scriptable application found! > {isScriptable:{}, isAppTypeExist:{}, isAppVersionExist:{}}",isScriptable,isAppTypeExist,isAppVersionExist);
        }
        
        return isValid;                
    }
    
    public boolean isValidScriptableCard(SoftCard softcard)
    {
        boolean isValid=false;
        
        if(softcard==null)
        {
            return false;
        }       
        try 
        {
            boolean isCardExist=(softcard.getPlasticNumber()!=null && softcard.getPlasticNumber().trim().length()>0);
       
            long validFromInLong=softcard.getValidFrom().getTime();
            long validToInLong = AffinaEspUtils.getMonthEndDateTime(softcard.getValidTo());
            long currentTimeInLong=(new Date()).getTime();
            boolean isNotExpired=((validFromInLong<=currentTimeInLong) && (currentTimeInLong<=validToInLong));
            
            boolean isCardPending=(currentTimeInLong<validFromInLong);
            boolean isCardStatusOk=(CardStatus.ACTIVE==softcard.getCardStatus() || isCardPending );

            isValid=(isCardExist && isNotExpired && isCardStatusOk);
            
            if(!isValid)
            {
                logger.debug("Invalid scriptable card found! > {isCardExist: {}, isNotExpired:{}, isCardStatusOk:{}, isCardPending:{}}",isCardExist,isNotExpired,isCardStatusOk,isCardPending);
            }
        } 
        catch (AffinaEspUtilException ex) 
        {
            logger.warn(ex.getMessage());
        }
        
        return isValid;                
    }
    
}
