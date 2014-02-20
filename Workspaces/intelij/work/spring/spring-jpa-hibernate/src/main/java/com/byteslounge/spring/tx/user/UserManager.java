package com.byteslounge.spring.tx.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.byteslounge.spring.tx.dao.IUserDAO;
import com.byteslounge.spring.tx.model.User;
import com.byteslounge.spring.tx.user.IUserManager;

@Service
public class UserManager implements IUserManager 
{

	@Autowired
	private IUserDAO userDAO;

	@Override
	@Transactional
	public void insertUser(User user) 
        {
		userDAO.insertUser(user);
	}

	@Override
	public List<User> findAllUsers() 
        {
		return userDAO.findAllUsers();
	}

}
