package com.test.spring.tx.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.spring.tx.dao.IUserDAO;
import com.test.spring.tx.model.User;

/**
 *
 * @author wakkir.muzammil
 */

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
                
                userDAO.insertUser(new User("testUser2","Test2 User's Name"));
                
                //int a=1/0;
                
                userDAO.insertUser(new User("testUser3","Test3 User's Name"));
                userDAO.insertUser(new User("testUser4","Test4 User's Name"));
	}

	@Override
	public List<User> findAllUsers() 
        {
		return userDAO.findAllUsers();
	}

}
