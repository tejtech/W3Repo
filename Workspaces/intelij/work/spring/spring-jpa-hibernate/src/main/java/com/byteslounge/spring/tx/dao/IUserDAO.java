package com.byteslounge.spring.tx.dao;

import java.util.List;

import com.byteslounge.spring.tx.model.User;

public interface IUserDAO 
{

	void insertUser(User user);

	List<User> findAllUsers();
}
