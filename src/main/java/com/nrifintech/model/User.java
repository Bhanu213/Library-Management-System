package com.nrifintech.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="user")
public class User 
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="userId",nullable=false)
	private int id;
	
	@Column(name="name",nullable=false)
	private String name;
	
	@Column(name="age",nullable=false)
	private int age;
	
	@Column(name="email",nullable=false)
	private String email;
	
	@Column(name="username",nullable=false,unique=true)
	private String username;
	
	@Column(name="password")
	private String password;
	
	
	
	@Column(name="role",nullable=false)
	private String role;
	
//	@OneToMany(mappedBy = "user",fetch=FetchType.LAZY)
//	@JsonBackReference
//	private List<Issue> issues = new ArrayList<>();

	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public int getAge() 
	{
		return age;
	}

	public void setAge(int age) 
	{
		this.age = age;
	}

	public String getEmail() 
	{
		return email;
	}

	public void setEmail(String email) 
	{
		this.email = email;
	}

	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	

	public String getRole()
	{
		return role;
	}

	public void setRole(String role) 
	{
		this.role = role;
	}


	public User(String name, int age, String email, String username, String password, String role)
	{
		super();
		this.name = name;
		this.age = age;
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public User(int id, String name, int age, String email, String role) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.email = email;
		this.role = role;
	}

	public User() 
	{
		super();
	}
	
	

}