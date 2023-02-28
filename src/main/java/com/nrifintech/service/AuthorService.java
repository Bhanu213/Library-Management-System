package com.nrifintech.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.model.Author;
import com.nrifintech.repository.AuthorRepository;

@Service
public class AuthorService
{
	@Autowired
	AuthorRepository authorrepo;
	
	public List<Author> getall()
	{
		List<Author> la=new ArrayList<Author>();
		
		for(Author a:authorrepo.findAll())
		{
			la.add(a);
		}
		return la;
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
}
