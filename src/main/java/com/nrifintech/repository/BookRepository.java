package com.nrifintech.repository;

import org.springframework.data.repository.CrudRepository;

import com.nrifintech.model.Book;

public interface BookRepository extends CrudRepository<Book,Integer>
{

}
