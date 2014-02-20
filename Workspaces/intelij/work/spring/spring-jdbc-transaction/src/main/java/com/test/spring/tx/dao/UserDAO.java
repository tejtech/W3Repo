package com.test.spring.tx.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import com.test.spring.tx.model.User;

@Service
public class UserDAO extends JdbcDaoSupport implements IUserDAO {

	@Autowired
	public UserDAO(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public void insertUser(User user) {
		getJdbcTemplate().update(
			"INSERT INTO XUSER (ID, USERNAME, NAME) VALUES (?, ?, ?)",
		    new Object[] {
                                user.getId(),
				user.getUsername(),
				user.getName()
			}
		);
	}

	@Override
	public User getUser(String username) {
		User user = getJdbcTemplate().
			queryForObject("SELECT DISTINCT * FROM XUSER WHERE USERNAME = ?",
			new Object[] { username },
			new UserMapper()
			);
		return user;
	}
	
	@Override
	public List<User> getUsers() {
		List<User> users = getJdbcTemplate().
			query("SELECT * FROM XUSER",
			new UserMapper()
			);
		return users;
	}
	
	private class UserMapper implements RowMapper<User>{

		@Override
		public User mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			User user = new User();
			user.setId(rs.getInt("ID"));
			user.setUsername(rs.getString("USERNAME"));
			user.setName(rs.getString("NAME"));
			return user;
		}
		
	}

}
