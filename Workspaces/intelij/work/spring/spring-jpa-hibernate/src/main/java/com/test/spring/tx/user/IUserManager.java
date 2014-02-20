package com.test.spring.tx.user;

import java.util.List;

import com.test.spring.tx.model.User;

public interface IUserManager {

	void insertUser(User user);

	List<User> findAllUsers();
}
