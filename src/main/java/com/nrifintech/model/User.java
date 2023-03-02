package com.nrifintech.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	
	@Column(name="username",nullable=false)
	private String username;
	
	@Column(name="password",nullable=false)
	private String password;
	
	@Column(name="fine",nullable=false)
	private Double fine;
	
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.EAGER)
	@JoinColumn(name="role_id",nullable=false)
	private Role role;
	
	@OneToMany(mappedBy = "user",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	private List<Issue> issues = new ArrayList<>();

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

	public Double getFine() 
	{
		return fine;
	}

	public void setFine(Double fine)
	{
		this.fine = fine;
	}

	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role) 
	{
		this.role = role;
	}
    
	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	public User(String name, int age, String email, String username, String password, Double fine, Role role)
	{
		super();
		this.name = name;
		this.age = age;
		this.email = email;
		this.username = username;
		this.password = password;
		this.fine = fine;
		this.role = role;
	}

	public User() 
	{
		super();
	}
	
	

}