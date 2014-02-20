package com.test.spring.tx.dao;

import java.util.List;

import com.test.spring.tx.model.User;

public interface IUserDAO 
{

	void insertUser(User user);

	List<User> findAllUsers();
}
