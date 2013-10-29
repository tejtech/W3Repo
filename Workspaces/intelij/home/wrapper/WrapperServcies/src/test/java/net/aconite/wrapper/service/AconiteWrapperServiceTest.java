package net.aconite.wrapper.service;


import net.aconite.wrapper.client.WrapperClientException;
import net.aconite.wrapper.client.WrapperClientMain;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;


public class AconiteWrapperServiceTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AconiteWrapperService.class);

    @BeforeClass
    public static void testSetup()
    {
    }

    @AfterClass
    public static void testCleanup()
    {
        // Teardown for data used by the unit tests
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void throwsWrapperClientException()
    {
        exception.expect(WrapperClientException.class);
        exception.expectMessage("xxxError!. Connection refused :");
        WrapperClientMain clientMain=new WrapperClientMain();
        String[] commands=new String[]{"stop"};
        try
        {
            clientMain.initClient(commands);
        }
        catch(WrapperClientException ex)
        {
            //System.out.println("XXXXX"+ex.getMessage());
            //assertTrue(ex.getMessage().indexOf("Error!. Connection refused :")>0);
        }
    }


    @Test(expected = AssertionError.class)
    public void testStopConverterBeforeServerStart()
    {
        WrapperClientMain clientMain=new WrapperClientMain();
        String[] commands=new String[]{"stop"};
        try
        {
            clientMain.initClient(commands);
        }
        catch(WrapperClientException ex)
        {
            //System.out.println("XXXXX"+ex.getMessage());
            assertTrue(ex.getMessage().indexOf("Error!. Connection refused :")>0);
        }
    }

    @Test(expected = AssertionError.class)
    public void testExceptionIsThrown()
    {
        WrapperServerMain serverMain = new WrapperServerMain();
        try
        {
            serverMain.initServer();
        }
        catch(Exception ex)
        {

        }
    }

    @Test(expected = AssertionError.class)
    public void testStopServer()
    {
        WrapperClientMain clientMain=new WrapperClientMain();
        String[] commands=new String[]{"stop"};
        try
        {
            clientMain.initClient(commands);
        }
        catch(WrapperClientException ex)
        {
            assertTrue(ex.getMessage().indexOf("Error!. Connection refused :")>0);
        }
    }


}
