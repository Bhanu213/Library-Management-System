package com.nrifintech.repository;

import org.springframework.data.repository.CrudRepository;

import com.nrifintech.model.User;

public interface UserRepository extends CrudRepository<User,Integer> {

}
