/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.persistence;

/**
 * A class that defines the work which can be carried out by database worker,
 * takes an  argument, and holds a response.
 * The work is carried out in a (non-container) transactional context.
 * @author thushara.pethiyagoda
 */
abstract public class Workable<T, R>
{
    private T data;
    private R response;

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public R getResult()
    {
        return response;
    }

    public void setResponse(R response)
    {
        this.response = response;
    }

    /**
     * Implement this method to execute activities that can be done within a database transaction.
     */
    abstract public void doWork();

}
