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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="role")
public class Role 
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="roleId",nullable=false)
	private int roleId;
	
	@Column(name="name",nullable=false)
	private String name;
	
	@OneToMany(mappedBy = "role",fetch=FetchType.EAGER,cascade=CascadeType.MERGE)
	private List<User> users=new ArrayList<User>();

	public int getRoleId() 
	{
		return roleId;
	}

	public void setRoleId(int roleId) 
	{
		this.roleId = roleId;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Role(String name)
	{
		super();
		this.name = name;
	}

	public Role() 
	{
		super();
	}

}
