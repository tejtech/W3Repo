package com.test.spring.tx.dao;

import java.util.List;

import com.test.spring.tx.model.User;

public interface IUserDAO
{	
    User getUser(String username);

    List<User> getUsers();

    void insertUser(User user);
}
