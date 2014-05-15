/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.aconite.affina.espinterface.webservice.restful.common;

/**
 *
 * @author wakkir.muzammil
 */
public class PagingCriteria 
{
    private Integer limit;
    private Integer page;
    private Integer start;
    private String sort;
    private String dir;

    public  PagingCriteria(Integer start,Integer limit, String sort,String dir)
    {
        this.start=start;
        this.limit=limit;
        this.sort=sort;
        this.dir=dir;
    }    

    public  PagingCriteria(Integer start,Integer limit,Integer page,String sort,String dir)
    {
        this.start=start;
        this.limit=limit;
        this.page=page;
        this.sort=sort;
        this.dir=dir;
    }

  

    /**
     * @return the limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * @return the page
     */
    public Integer getPage() 
    {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(Integer page) 
    {
        this.page = page;
    }

    /**
     * @return the start
     */
    public Integer getStart() 
    {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(Integer start) 
    {
        this.start = start;
    }

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public String getDir()
    {
        return dir;
    }

    public void setDir(String dir)
    {
        this.dir = dir;
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\nStart  : ").append(start);
        sb.append("\nLimit  : ").append(limit);
        sb.append("\nPage   : ").append(page);
        sb.append("\nSort   : ").append(sort);
        sb.append("\nDir    : ").append(dir);
        sb.append("\n");
        return sb.toString();
    }
    
}
