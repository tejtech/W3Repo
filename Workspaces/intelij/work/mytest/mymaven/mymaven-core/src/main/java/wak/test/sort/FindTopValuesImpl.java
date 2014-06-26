/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wak.test.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


/**
 *
 * @author wakkir.muzammil
 */
public class FindTopValuesImpl implements FindTopValues
{
        
    @Override
    public int findMaxValue(int[] anyOldOrderValues)
    {
        Arrays.sort(anyOldOrderValues);//, Collections.reverseOrder()
        return anyOldOrderValues[anyOldOrderValues.length-1];
    }

    @Override
    public int[] findTopNValues(int[] anyOldOrderValues, int n)
    {  
        Arrays.sort(anyOldOrderValues);//, Collections.reverseOrder()
        
        return Arrays.copyOfRange(anyOldOrderValues,anyOldOrderValues.length-n,anyOldOrderValues.length);
    }
    
    
    public static void main(String[] args) 
    {
        FindTopValuesImpl x=new FindTopValuesImpl();
       int[] anyOldOrderValues = {120, 5, 0, 999, 77,-999,55,34,-2,65,2,-1};
        
       Arrays.sort(anyOldOrderValues);
       System.out.println(Arrays.toString(anyOldOrderValues));
       
       System.out.println(x.findMaxValue(anyOldOrderValues));        
        
       System.out.println(Arrays.toString(x.findTopNValues(anyOldOrderValues, 5)));
    }

    
}

class MyComparator implements Comparator<Integer>
{

    @Override
    public int compare(Integer o1, Integer o2)
    {
        
        if(o2 > o1)
        {
            return 1;
        }
        else
        {
            return 0;
        }
           
    }

    
    
}
