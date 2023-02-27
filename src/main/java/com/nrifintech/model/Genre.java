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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="genre")
public class Genre {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="genreId")
	private int genreId;
	
	@Column(name="name")
	private String name;
	
	@OneToMany(mappedBy = "genre",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	private List<Book> books=new ArrayList<Book>();
	
	
	
	public Genre(int genreId, String name, List<Book> books)
	{
		super();
		this.genreId = genreId;
		this.name = name;
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
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString() 
	{
		return "Genre [genreId=" + genreId + ", name=" + name + ", books=" + books + "]";
	}
	
	
	
}
