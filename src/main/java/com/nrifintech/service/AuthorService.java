package com.nrifintech.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.nrifintech.exception.ResourceNotFoundException;
import com.nrifintech.model.Author;
import com.nrifintech.repository.AuthorRepository;

@Service
public class AuthorService
{
	@Autowired
	private AuthorRepository authorrepo;
	
	
	public Author addAuthor(Author a) {
		authorrepo.save(a);
		return a;
	}
	
	public ResponseEntity<Author> updateAuthor(Integer authorId,Author anew) throws ResourceNotFoundException{
		Author a=authorrepo.findById(authorId).orElseThrow(()-> new ResourceNotFoundException("Author not found for the id "+authorId));
		a.setAuthorName(anew.getAuthorName());
		authorrepo.save(a);
		return ResponseEntity.ok().body(a);
	}
	
	
	public ResponseEntity<Author> deleteAuthor(Integer authorId) throws ResourceNotFoundException{
		Author a =authorrepo.findById(authorId).orElseThrow(()->new ResourceNotFoundException("Author not found for this id "+authorId));
		authorrepo.delete(a);
		return ResponseEntity.ok().body(a);
	} 
	
	public List<Author> getAllAuthors()
	{
		List<Author> al=new ArrayList<Author>();
		
		for(Author a:authorrepo.findAll())
		{
			al.add(a);
		}
		return al;
	}
	
	public ResponseEntity<Author> getAuthorById(int authorId) throws ResourceNotFoundException{
		Author a =authorrepo.findById(authorId).orElseThrow(()->new ResourceNotFoundException("Author not found for this id "+authorId));
		return ResponseEntity.ok().body(a);
	}
	
	
	public String getAuthorname(int authorId)
	{
		for(Author a:authorrepo.findAll())
		{
			if(a.getAuthorId()==authorId)
			{
				return a.getAuthorName();
			}
		}
		return null;
	}
	
	
	public int getAuthorId(String name)
	{
		int flag=0;
		for(Author a:authorrepo.findAll())
		{
			if(a.getAuthorName().equalsIgnoreCase(name))
			{
				flag=a.getAuthorId();
			}
		}
		
		if(flag==0)
		{
			return 0;
		}
		else
		{
			return flag;
		}
	}
	                          
//	public String getAuthorname(int authorId)
//	{
//		for(Author a:authorrepo.findAll())
//		{
//			if(a.getAuthorId()==authorId)
//			{
//				return a.getAuthorName();
//			}
//		}
//		
//		throw new RuntimeException("Author not found for the id "+ authorId);
//	}
//	
//	
//	public int getAuthorId(String name)
//	{
//		int flag=0;
//		for(Author a:authorrepo.findAll())
//		{
//			if(a.getAuthorName().equalsIgnoreCase(name))
//			{
//				flag=a.getAuthorId();
//			}
//		}
//		
//		if(flag==0)
//		{
//			throw new RuntimeException("Author not found for the name "+ name);
//		}
//		else
//		{
//			return flag;
//		}
//	}
//	
//	public Author createAuthor(Author a) {
//		authorrepo.save(a);
//		return a;
//	}
//	
//	public Author updateAuthor(int authorId,Author anew) 
//	{
//		Author a=authorrepo.findById(authorId).get();
//		a.setAuthorName(anew.getAuthorName());
//		authorrepo.save(a);
//		return a;
//	}
//	
//	public Author getAuthorbyId(int authorId) {
//		Author a = authorrepo.findById(authorId).get();
//		return a;
//	}
//	
//	public Author getAuthorbyName(String authorName) {
//		int id = this.getAuthorId(authorName);
//		Author a = authorrepo.findById(id).get();
//		return a;
//	}
//	
//	public String deleteAuthor(int id) {
//		Optional<Author> a = authorrepo.findById(id);
//		if(a.isPresent()) {
//			authorrepo.delete(a.get());
//			return "Author is deleted with id "+id;
//		}
//		else {
//			throw new RuntimeException("Author not found for the id "+id);
//		}
//		
//	}
//	
//	public int showAuthorIdService(String name)
//	{
//		System.out.println("CheckPoint");
//		int flag=0;
//		for(Author a:authorrepo.findAll())
//		{
//			if(a.getAuthorName().equalsIgnoreCase(name))
//			{
//				flag=a.getAuthorId();
//			}
//		}
//		
//		if(flag==0)
//		{
//			return 0;
//		}
//		else
//		{
//			return flag;
//		}
//	}
//	
}
