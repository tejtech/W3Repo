package com.byteslounge.spring.tx.user;

import java.util.List;

import com.byteslounge.spring.tx.model.User;

public interface IUserManager {

	void insertUser(User user);

	List<User> findAllUsers();
}
