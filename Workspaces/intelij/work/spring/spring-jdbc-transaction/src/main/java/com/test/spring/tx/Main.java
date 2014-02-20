package com.test.spring.tx;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.test.spring.tx.model.User;
import com.test.spring.tx.user.IUserManager;

public class Main 
{
    /*     
    --DELETE PMA.XUSER; 
    CREATE TABLE PMA.XUSER 
    (
      ID       NUMBER(13,0),
      USERNAME VARCHAR2(50),
      NAME     VARCHAR2(100)
    )TABLESPACE PMA_TAB01;

    SELECT * FROM xuser;
    */
    public static void main( String[] args )
    {
    	ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    	IUserManager userManager = (IUserManager) ctx.getBean("userManager");
    	
    	User user=new User(1,"testUser1","Test1 User's Name");
    	userManager.insertUser(user);
    	
    	System.out.println("User inserted!");
    	
    	user = userManager.getUser("testUser1");
    	
    	System.out.println("\nUser fetched!"
    		+ "\nId: " + user.getId()
    		+ "\nUsername: " + user.getUsername()
    		+ "\nName: " + user.getName());
    	
    	List<User> users = userManager.getUsers();
    	
    	System.out.println("\nUser list fetched!"
        	+ "\nUser count: " + users.size());
        
        //--------------------------
        
    	/*
    	userManager.insertUser(new User(2,"testUser2","Test User's Name"));
    	
    	System.out.println("User2 inserted!");
        
        List<User> users2 = userManager.getUsers();
    	
    	System.out.println("\nUser2 list fetched!" + "\nUser count: " + users2.size());
        */

    }
}
