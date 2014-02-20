/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.spring.tx.jms;

import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.jms.JmsHeaders;

/**
 *
 * @author wakkir.muzammil
 */
public class ProcessHandler 
{
    //@Autowired
    //private IUserManager userManager;
    
    @ServiceActivator
    public void endProcess(Message inMessage) throws MyException
    {        
        Message outMessage = processProgressMessage(inMessage);
    }   
    
    @ServiceActivator
    public Message process(Message inMessage) throws MyException 
    {
        Message outMessage = processProgressMessage(inMessage);
        return outMessage;
    }
    
    private Message processProgressMessage(Message inMessage) throws MyException
    {
        MessageHeaders inHeaders = inMessage.getHeaders();
        Object inPayload = inMessage.getPayload();
        
        //validateJMS(inHeaders,"");
        //callDBAction();        
        validateJMS(inHeaders,"ONE");

        System.out.println("process : Incoming Message header: "+ inHeaders);
        System.out.println("process : Message payload: "+ inPayload);

        Message outMessage = inMessage;

        return outMessage;
    }
    
    private void validateJMS(MessageHeaders inHeaders,String jmsType) throws MyException
    {
        if(inHeaders.get(JmsHeaders.TYPE)==null)
        {
           throw new MyException("Invalid JMS Type received !!!");
        }        
        else if(jmsType.equalsIgnoreCase(inHeaders.get(JmsHeaders.TYPE)+""))
        {
           throw new MyException("JMS ONE Type received !!!");
        }
        
    }
    /*
    private void callDBAction()
    {        
        List<User> list = userManager.findAllUsers();
        System.out.println("User count: " + list.size());

        User user = new User("testUser1","test User1 Name");

        userManager.insertUser(user);
        System.out.println("User inserted!");

        list = userManager.findAllUsers();
        System.out.println("User count: " + list.size());
    }
    * */
}
