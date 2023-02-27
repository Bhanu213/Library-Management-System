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
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="roleId")
	private int roleId;
	
	@Column(name="name")
	private String name;
	
	@OneToMany(mappedBy = "role",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	private List<User> books=new ArrayList<User>();

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role(int roleId, String name) {
		super();
		this.roleId = roleId;
		this.name = name;
	}

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

}
