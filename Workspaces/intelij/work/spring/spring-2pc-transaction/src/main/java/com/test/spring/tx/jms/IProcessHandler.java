/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.spring.tx.jms;


import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
/**
 *
 * @author wakkir.muzammil
 */
public interface IProcessHandler 
{
    public Message process(Message<MessagingException> message);
    public void endProcess(Message<MessagingException> message); 
}
