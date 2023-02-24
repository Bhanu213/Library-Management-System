package com.nrifintech.model;

public class Status {
	private int statusId;
	private String description;

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Status(int statusId, String description) {
		super();
		this.statusId = statusId;
		this.description = description;
	}
}
