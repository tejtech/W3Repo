/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wak.test.sort;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author wakkir.muzammil
 */
public class FindTopValuesImplTest
{    
    int[] anyOldOrderValues = {120, 5, 0, 999, 77,-999,55,34,-2,65,2,-1};
    int[] expectedMaxRangeValues = {34,55, 65, 77, 120, 999};
    
    public FindTopValuesImplTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
         
    }
    
    @After
    public void tearDown()
    {
    }
    
    @Test
    public final void whenIntArrayAreUsedThenGetMaxValue()
    {        
        FindTopValues x=new FindTopValuesImpl();
        int maxvalue=x.findMaxValue(anyOldOrderValues);
        Assert.assertEquals(999, maxvalue);
    }
    
    @Test
    public final void whenIntArrayAreUsedThenGetMaxValueRange()
    {        
        FindTopValues x=new FindTopValuesImpl();
        int[] maxvalues=x.findTopNValues(anyOldOrderValues, expectedMaxRangeValues.length);
        Assert.assertArrayEquals(expectedMaxRangeValues,maxvalues);
    }
}
