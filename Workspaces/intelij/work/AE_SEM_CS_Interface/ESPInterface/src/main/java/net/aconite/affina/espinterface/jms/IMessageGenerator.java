/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.jms;

import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;

/**
 *
 * @author wakkir.muzammil
 */
public interface IMessageGenerator
{
    public <T> Message<T> generateMessage(MessageHeaders headers, T sourceData);
    public <T> Message<T> generateMessage(MessageHeaders headers, T sourceData, String jmsType);
}
