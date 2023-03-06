package com.nrifintech.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nrifintech.exception.ResourceNotFoundException;
import com.nrifintech.model.User;
import com.nrifintech.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	
	public User addUser(User user)
	{
		userRepository.save(user);
		return user;
	}
	

	public List<User> getAllUsers()
	{
		List<User> userList=new ArrayList<User>();
		
		for(User a:userRepository.findAll())
		{
			userList.add(a);
		}
		return userList;
	}
	
	public ResponseEntity<User> updateUser(Integer userId,User newUser) throws ResourceNotFoundException{
		User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found for the id "+userId));
		user.setName(newUser.getName());
		user.setAge(newUser.getAge());
		user.setEmail(newUser.getEmail());
		user.setUsername(newUser.getUsername());
		user.setPassword(newUser.getPassword());
		user.setFine(newUser.getFine());
		userRepository.save(user);
		return ResponseEntity.ok().body(user);
	}
	
	public ResponseEntity<User> deleteUser(Integer userId) throws ResourceNotFoundException{
		User user =userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found for this id "+userId));
		userRepository.delete(user);
		return ResponseEntity.ok().body(user);
	}
	
	public ResponseEntity<User> getUserById(int userId) throws ResourceNotFoundException
	{
		User user =userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found for this id "+userId));
		return ResponseEntity.ok().body(user);
	}
	
	public ResponseEntity<User> getUserByusername(String username) throws ResourceNotFoundException
	{
		for(User user:userRepository.findAll())
		{
			if(user.getUsername().equals(username))
			{
				return ResponseEntity.ok().body(user);
			}
		}
		return ResponseEntity.ok().body(null);
	}
	
	public ResponseEntity<Double> getfineByUserId(int userId) throws ResourceNotFoundException
	{
		User user =userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found for this id "+userId));
		return ResponseEntity.ok().body(user.getFine());
	}
	
	public ResponseEntity<Double> getFineByusername(String username) throws ResourceNotFoundException
	{
		for(User user:userRepository.findAll())
		{
			if(user.getUsername().equals(username)) 
			{
				return ResponseEntity.ok().body(user.getFine());
			}
		}
		return ResponseEntity.ok().body(null);
	}
}
