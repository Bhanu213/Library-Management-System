package com.nrifintech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.exception.ResourceNotFoundException;
import com.nrifintech.model.Author;
import com.nrifintech.service.AuthorService;

@RestController
@RequestMapping("/author")
public class AuthorController {
	@Autowired
	private AuthorService authorService;

	@GetMapping("/show")
	public List<Author> getall() {
		return authorService.getAllAuthors();
	}

	@PostMapping("/addpost")
	public Author create(@RequestBody Author a) {
		authorService.addAuthor(a);
		return a;
	}

	@PutMapping("/update/{authorId}")
	public ResponseEntity<Author> updateBook(@PathVariable int authorId, @RequestBody Author author)
			throws ResourceNotFoundException {
		return authorService.updateAuthor(authorId, author);
	}

	@DeleteMapping("/delete/{authorId}")
	public ResponseEntity<Author> deleteAuthor(@PathVariable int authorId) throws ResourceNotFoundException {
		return authorService.deleteAuthor(authorId);
	}
	
	@GetMapping("/show/{authorId}")
	public ResponseEntity<Author> getAuthorById(@PathVariable  int authorId) throws ResourceNotFoundException{
		return authorService.getAuthorById(authorId);
	}
}
