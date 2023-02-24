package com.nrifintech.model;

public class Role {
	private int roleId;
	private String name;

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
