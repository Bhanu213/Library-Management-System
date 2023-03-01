package com.nrifintech.model;


import java.sql.Clob;
import java.sql.SQLException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.sql.rowset.serial.SerialException;


@Entity
@Table(name="book")
public class Book
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="bookId",nullable=false)
	private int bookId;
	
	@Column(name="title",nullable=false)
	private String title;
	
	
	@Column(name="qty")
	private int qty;
	
	@Column(name="date",nullable=false)
	private String date;
	
	@Lob
	@Column(name="image",nullable=false)
	private Clob url;
	
	@Column(name="isbn",nullable=false)
	private long isbn;
	
	@Column(name="description",nullable=false)
	private String description;

	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.EAGER)
	@JoinColumn(name="authorId",nullable=false)
	private Author author;
	
	
	
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.EAGER)
	@JoinColumn(name="genreId",nullable=false)
	private Genre genre;

	public int getBookId() 
	{
		return bookId;
	}

	public void setBookId(int bookId) 
	{
		this.bookId = bookId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Author getAuthor()
	{
		return author;
	}

	public void setAuthor(Author author)
	{
		this.author = author;
	}

	public int getQty()
	{
		return qty;
	}

	public void setQty(int qty)
	{
		this.qty = qty;
	}

	public Genre getGenre()
	{
		return genre;
	}

	public void setGenre(Genre genre)
	{
		this.genre = genre;
	}
	
	public String getDate() 
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getUrl() 
	{
		try
		{
			return url.getSubString(1, (int)url.length());
		} 
		catch (SQLException e)
		{
			return e.getMessage();
		}
	}

	public void setUrl(String url) 
	{
		try 
		{
			this.url = new javax.sql.rowset.serial.SerialClob(url.toCharArray());
		}
		catch (Exception e) 
		{
			
			e.getMessage();
		}
	}

	public long getISBN() 
	{
		return isbn;
	}

	public void setISBN(long isbn) 
	{
		this.isbn = isbn;
	}

	

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Book() 
	{
		super();
		
	}

	public Book(int bookId, String title, int qty, String date, Clob url, long isbn, String description,
			Author author, Genre genre) 
	{
		super();
		this.bookId = bookId;
		this.title = title;
		this.qty = qty;
		this.date = date;
		this.url = url;
		this.isbn = isbn;
		this.description = description;
		this.author = author;
		this.genre = genre;
	}

	@Override
	public String toString() {
		return "Book [bookId=" + bookId + ", title=" + title + ", qty=" + qty + ", date=" + date + ", url=" + String.valueOf(url)
				+ ", ISBN=" + isbn + ", description=" + description + ", author=" + author + ", genre=" + genre + "]";
	}

	

	
}
