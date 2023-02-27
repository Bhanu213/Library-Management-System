package com.nrifintech.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nrifintech.model.Genre;
import com.nrifintech.repository.GenreRepository;

@Service
public class GenreService 
{
	@Autowired
	private GenreRepository genrerepo;

	public List<Genre> showall()
	{
		List<Genre> glist=new ArrayList<Genre>();
		for(Genre g:genrerepo.findAll())
		{
			glist.add(g);
		}
		return glist;
	}
	
	public int getGenreIdService(String genreName)
	{
		for(Genre g:genrerepo.findAll())
		{
			if(genreName.equalsIgnoreCase(g.getName()))
			{
				return g.getGenreId();
			}
		}
		return 0; 
	}

}
