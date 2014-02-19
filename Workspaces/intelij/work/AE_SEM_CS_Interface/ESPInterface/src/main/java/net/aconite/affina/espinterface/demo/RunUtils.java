/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.demo;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import net.acointe.affina.esp.AffinaEspUtils;
import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.helper.DateHelper;

/**
 *
 * @author wakkir.muzammil
 */
public class RunUtils 
{
    private void testEndDateTime()
    {
        Calendar cal = Calendar.getInstance(); 
        
        //cal.setTimeZone(TimeZone.getTimeZone("GMT"));  
        //cal.setTime(new Date());
        cal.set(2017, 12, 1);
        //cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
        cal.add(Calendar.MONTH, 1);
        
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -1);
        
        System.out.println("date1 : "+ cal.toString());
        System.out.println("TimeInMillis : "+cal.getTimeInMillis());
         System.out.println("TimeInMillis2 : "+(cal.getTime()).getTime());
         System.out.println("HOUR:"+cal.get(Calendar.HOUR));
         System.out.println("HOUR_OF_DAY:"+cal.get(Calendar.HOUR_OF_DAY));
         System.out.println("YEAR:"+cal.get(Calendar.YEAR));
         String str="0"+String.valueOf(cal.get(Calendar.MONTH)+1);
         System.out.println("MONTH:"+str.substring(str.length()-2,str.length()));
         System.out.println("DATE:"+cal.get(Calendar.DATE));


        long ltime= cal.getTimeInMillis()-0;

        SimpleDateFormat dateFormat = new SimpleDateFormat(AffinaEspUtils.DATE_FORMAT_ISO8601);
        System.out.println("date-1 : "+ dateFormat.format(new Date(ltime)).toUpperCase());    
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            
        System.out.println("longtime "+ltime+" in Hex : "+Long.toHexString(ltime).toUpperCase());
        System.out.println("date-2 : "+ dateFormat.format(new Date(ltime)).toUpperCase());
        
        System.out.println("#########");
        
        BigInteger bint=new BigInteger("16011F29618",16); 
        System.out.println("longtime "+bint.longValue()+" in Hex : "+Long.toHexString(bint.longValue()).toUpperCase());
        System.out.println("date-3 : "+ dateFormat.format(new Date(bint.longValue())).toUpperCase());
        
         System.out.println("#########");
         //1512079199999
         //1512086399999
         
         System.out.println("date-4 : "+ dateFormat.format(new Date()).toUpperCase());
         
        
    }
    
    private void testCurrentDateTime()
    {
        Calendar cal = Calendar.getInstance();         
        //cal.setTimeZone(TimeZone.getTimeZone("GMT"));  
        cal.setTime(new Date());       
        //cal.set(Calendar.HOUR_OF_DAY, 0);   
                
        System.out.println("YEAR:"+cal.get(Calendar.YEAR));
        String str="0"+String.valueOf(cal.get(Calendar.MONTH)+1);
        System.out.println("MONTH:"+str.substring(str.length()-2,str.length()));
        System.out.println("DATE:"+cal.get(Calendar.DATE));
        
        System.out.println("HOUR:"+cal.get(Calendar.HOUR));
        System.out.println("HOUR_OF_DAY:"+cal.get(Calendar.HOUR_OF_DAY));
        System.out.println("MINUTS:"+cal.get(Calendar.MINUTE));
        System.out.println("SECOND:"+cal.get(Calendar.SECOND));
        
        long ltime= cal.getTimeInMillis()-0;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(AffinaEspUtils.DATE_FORMAT_YYYY_MM_DD_HHMMSSmmm);
        System.out.println("date-1 : "+ dateFormat.format(new Date(ltime)).toUpperCase());  
        
        //dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));            
        //System.out.println("date-2 : "+ dateFormat.format(new Date(ltime)).toUpperCase());
        
        
        System.out.println("date-3 : "+DateHelper.getDefaultPayloadHeader());
       
    }
    
    public static void main(String[] args)
    {
        System.out.println("Hello World");
        
        RunUtils ru=new RunUtils();
        ru.testEndDateTime();
        //ru.testCurrentDateTime();
       
        
    }
}
