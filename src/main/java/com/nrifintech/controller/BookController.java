package com.nrifintech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.model.Author;
import com.nrifintech.model.Book;
import com.nrifintech.service.AuthorService;
import com.nrifintech.service.BookService;

@RestController
@RequestMapping("/book")
public class BookController
{
	@Autowired
	private BookService bs;
	
	@Autowired
	private AuthorService as;
	
	
	@RequestMapping(method=RequestMethod.POST,value="/add")
	public Book addbook(@RequestBody Book b)
	{
		return bs.addPost(b);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/show")
	public List<Book> show()
	{
		return bs.getall();
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/showauthor")
	public List<Author> showauthor()
	{
		return as.getall();
	}
}
