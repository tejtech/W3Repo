package com.test.spring.tx.model;

public class User {

	private int id;
	private String username;
	private String name;
        
        public User() 
        {
           
        }

        public User(int id, String username, String name) 
        {
            this.id = id;
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
