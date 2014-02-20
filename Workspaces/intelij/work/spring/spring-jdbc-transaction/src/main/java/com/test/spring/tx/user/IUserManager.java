package com.test.spring.tx.user;

import java.util.List;

import com.test.spring.tx.model.User;

public interface IUserManager 
{
    User getUser(String username);

    List<User> getUsers();

    void insertUser(User user);
}
