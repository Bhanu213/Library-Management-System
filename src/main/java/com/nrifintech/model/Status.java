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
@Table(name ="status")
public class Status {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="statusId")
	private int statusId;
	
	@Column(name="description")
	private String description;
	
	@OneToMany(mappedBy = "status",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	private List<Issue> issues = new ArrayList<>();

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
