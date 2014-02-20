package com.test.spring.tx.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.test.spring.tx.dao.IUserDAO;
import com.test.spring.tx.model.User;

/**
 *
 * @author wakkir.muzammil
 */

@Repository
public class UserDAO implements IUserDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void insertUser(User user) {
		entityManager.persist(user);
	}

	@Override
	public List<User> findAllUsers() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = builder.createQuery(User.class);
		Root<User> root = cq.from(User.class);
		cq.select(root);
		return entityManager.createQuery(cq).getResultList();
	}

}
