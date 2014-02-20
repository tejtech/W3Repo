/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.spring.tx.jms;

/**
 *
 * @author wakkir.muzammil
 */
public class MyException extends Exception
{
    private static final long serialVersionUID = 4233390499647614871L;
    private String errorCode="0";

    public MyException()
    {
        super();
    }

    public MyException(String message)
    {
        super(message);
    }
    
    public MyException(String message,String errorCode)
    {
        super(message);
        this.errorCode=errorCode;
    }

    public MyException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public MyException(String message, Throwable cause,String errorCode)
    {
        super(message, cause);
        this.errorCode=errorCode;
    }

    public MyException(Throwable cause)
    {
        super(cause);
    }

}
