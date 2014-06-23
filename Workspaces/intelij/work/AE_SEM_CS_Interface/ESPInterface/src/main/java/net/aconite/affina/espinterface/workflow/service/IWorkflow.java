package net.aconite.affina.espinterface.workflow.service;

import java.util.List;
import java.util.Map;

/**
 * User: wakkir
 * Date: 07/03/14
 * Time: 23:11
 */
public interface IWorkflow
{

    /**
     * Method for processing workflow.
     *
     * @param parameters maps of object which are needed for workflow processing
     * @return true in case that workflow is done without errors otherwise false
     */

    public List<Object> processWorkflow(String workflofName, Map<String, Object> parameters);

}