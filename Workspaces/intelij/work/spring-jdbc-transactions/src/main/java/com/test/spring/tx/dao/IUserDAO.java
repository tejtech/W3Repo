package com.test.spring.tx.dao;

import java.util.List;

import com.test.spring.tx.orm.User;

public interface IUserDAO {

	void insertUser(User user);
	
	User getUser(String username);
	
	List<User> getUsers();
}
