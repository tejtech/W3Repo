/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.validators;

import net.aconite.affina.espinterface.builder.MessageContent;
import net.aconite.affina.espinterface.exceptions.EspResponseValidationException;
import net.aconite.affina.espinterface.xmlmapping.sem.ErrorType;
import net.aconite.affina.espinterface.xmlmapping.sem.StatusType;

/**
 * @author wakkir.muzammil
 */
public interface IResponseValidator
{
    public MessageContent validate(MessageContent messageContent) throws EspResponseValidationException;
    
}
