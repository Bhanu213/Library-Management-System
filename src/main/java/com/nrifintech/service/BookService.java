package com.nrifintech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.model.Book;
import com.nrifintech.repository.BookRepository;

@Service
public class BookService
{
	@Autowired
	private BookRepository bookrepo;
	
	public Book addPost(Book b)
	{
		bookrepo.save(b);
		return b;
	}
}
