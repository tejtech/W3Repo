package com.test.spring.tx.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author wakkir.muzammil
 */

@Entity
@Table(name="XUSER")
@SequenceGenerator(name = User.XUSER_SEQ, sequenceName = "XUSER_", allocationSize = 1)
public class User 
{

        protected static final String XUSER_SEQ = "XUSERSeq";
        
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XUSER_SEQ)
	@Column(name="ID", nullable = false)
	private int id;
	
	@Column(name="USERNAME", nullable = false)
	private String username;
	
	@Column(name="NAME", nullable = false)
	private String name;
        
        public User() 
        {
           
        }
        
        public User(String username, String name) 
        {            
            this.username = username;
            this.name = name;
        }
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
