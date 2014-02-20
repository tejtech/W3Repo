package com.byteslounge.spring.tx;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.byteslounge.spring.tx.model.User;
import com.byteslounge.spring.tx.user.IUserManager;

public class Main 
{
	public static void main(String[] args) 
        {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");

		IUserManager userManager = (IUserManager) ctx.getBean("userManager");

		List<User> list = userManager.findAllUsers();
		System.out.println("User count: " + list.size());

		User user = new User();
		user.setUsername("johndoe");
		user.setName("John Doe");
		userManager.insertUser(user);
		System.out.println("User inserted!");

		list = userManager.findAllUsers();
		System.out.println("User count: " + list.size());

	}
}
