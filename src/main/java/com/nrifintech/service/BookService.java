package com.nrifintech.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.model.Author;
import com.nrifintech.model.Book;
import com.nrifintech.model.Genre;
import com.nrifintech.repository.BookRepository;

@Service
public class BookService
{
	@Autowired
	private BookRepository bookrepo;
	
	@Autowired
	private AuthorService as;
	
	@Autowired
	private GenreService gs;
	
	public Book addPost(Book b)
	{
		Author a=b.getAuthor();
		Genre g=b.getGenre();
		a.setAuthorId(as.showAuthorIdService(a.getAuthorName()));
		g.setGenreId(gs.getGenreIdService(g.getName()));
		b.setAuthor(a);
		b.setGenre(g);
		bookrepo.save(b);
		return b;
	}
	
	public List<Book> getall()
	{
		List<Book> bl=new ArrayList<Book>();
		for(Book b:bookrepo.findAll())
		{
			bl.add(b);
		}
		return bl;
	}
}
