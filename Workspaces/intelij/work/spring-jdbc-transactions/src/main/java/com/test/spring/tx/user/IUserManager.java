package com.test.spring.tx.user;

import java.util.List;

import com.test.spring.tx.orm.User;

public interface IUserManager {

	void insertUser(User user);
	
	User getUser(String username);
	
	List<User> getUsers();
}
