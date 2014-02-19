/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.spring.tx.orm;

import javax.persistence.*;

@Entity
@Table(name="XUSER")
public class User 
{
        @Id
        //@GeneratedValue//(strategy = GenerationType.SEQUENCE, generator = USER_SEQ_)
        @Column(name="ID")
        private int id;
        
        @Column(name="USERNAME")
        private String username;
        
        @Column(name="NAME")
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
