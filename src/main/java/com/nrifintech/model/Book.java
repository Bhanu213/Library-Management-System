package com.nrifintech.model;



import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="book")
public class Book
{
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name="bookId",nullable=false)
	private int bookId;
	
	@Column(name="title",nullable=false)
	private String title;
	
	
	@Column(name="qty")
	private int qty;
	
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="authorId",nullable=false)
	private Author author;
	
	
	
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
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

	public Book(String title, Author author, int qty, Genre genre)
	{
		super();
		this.title = title;
		this.author = author;
		this.qty = qty;
		this.genre = genre;
	}

	public Book()
	{
		super();
	}
}
