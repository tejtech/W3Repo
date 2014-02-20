package com.test.spring.tx;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.test.spring.tx.model.User;
import com.test.spring.tx.user.IUserManager;

/**
 *
 * @author wakkir.muzammil
 */

public class Main 
{
    /*
     DELETE PMA.XUSER; 
     CREATE TABLE PMA.XUSER 
     (
      ID                        NUMBER(13,0),
            USERNAME            VARCHAR2(50),
      NAME                      VARCHAR2(100)
     )TABLESPACE PMA_TAB01;

     --DROP SEQUENCE PMA.XUSER_;
     --CREATE SEQUENCE PMA.XUSER_ INCREMENT BY 1 START WITH 1;

     select * from xuser;  
    */
	public static void main(String[] args) 
        {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");

                /*
		IUserManager userManager = (IUserManager) ctx.getBean("userManager");

		List<User> list = userManager.findAllUsers();
		System.out.println("User count: " + list.size());

		User user = new User("testUser1","test User1 Name");
		
		userManager.insertUser(user);
		System.out.println("User inserted!");

		list = userManager.findAllUsers();
		System.out.println("User count: " + list.size());
                */
                
                System.out.println("Done it!!!");

	}
}
