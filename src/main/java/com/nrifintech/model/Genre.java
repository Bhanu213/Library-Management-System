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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="genre")
public class Genre

{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="genreSequence")
	@SequenceGenerator(initialValue=1000,name="genreSequence",sequenceName="genreSequence")
	@Column(name="genreId",nullable=false)
	private int genreId;
	
	@Column(name="genrename",nullable=false)
	private String genrename;
	
	@OneToMany(mappedBy = "genre",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	private List<Book> books=new ArrayList<Book>();
	
	
	
	public Genre(int genreId, String name, List<Book> books)
	{
		super();
		this.genreId = genreId;
		this.genrename = name;
		this.books = books;
	}

	public Genre()
	{
		super();
	}
	
	public int getGenreId()
	{
		return genreId;
	}
	
	public void setGenreId(int genreId)
	{
		this.genreId = genreId;
	}
	
	
	public String getName()
	{
		return genrename;
	}
	
	public void setName(String genrename)
	{
		this.genrename = genrename;
	}

	@Override
	public String toString() 
	{
		return "Genre [genreId=" + genreId + ", name=" + genrename + ", books=" + books + "]";
	}
	
	
	
}
