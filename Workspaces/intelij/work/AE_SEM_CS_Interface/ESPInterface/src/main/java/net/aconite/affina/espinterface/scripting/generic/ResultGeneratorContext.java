/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.scripting.generic;

import net.aconite.affina.espinterface.cardselection.*;
import net.aconite.affina.espinterface.helper.*;
import net.aconite.affina.espinterface.model.*;

/**
 * The context that supports different strategies of generating results.
 * @author thushara.pethiyagoda
 */
public class ResultGeneratorContext implements ResultGenerator
{
    /** Field ResultGenerator. */
    private ResultGenerator resultGenerator;
    
    /**
     * Constructs a result generator context with an associated ResultGenerator.
     * @param rg 
     */
    public ResultGeneratorContext(final ResultGenerator rg)
    {
        resultGenerator = rg;
    }
    
    /**
     * Constructs a result generator context with a fully qualified path name of an
     * associated ResultGenerator. This will be instantiated via reflection.
     * @param implClass The fully qualified path name of the implementing class.
     */
    @SuppressWarnings("ReflectiveInstantiation")
    public ResultGeneratorContext(String implClass)
    {
        try
        {
            resultGenerator = SelectableCardGenerator.createInstance(implClass);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }        
    }

    /**
     * Generates a Result based ResultGenerator associated with this context.
     * @return Result
     */ 
    public <T extends ResultBoundData, R extends AbstractModel> Result<R> generateResult(T arg1, R arg2)
    {
        return resultGenerator.generateResult(arg1, arg2);
    }    
}
