package com.nrifintech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import com.nrifintech.model.User;
import com.nrifintech.service.BookService;
import com.nrifintech.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/showusers")
	public List<User> getAll()
	{
		return userService.getAllUsers();
	}
	
	@PostMapping("/adduser")
	public User create(@RequestBody User user)
	{
		userService.addUser(user);
		return user;
	}
	
	@GetMapping("showuserbyid/{id}")
	public ResponseEntity<User> getAuthorById(@PathVariable int id) throws ResourceNotFoundException{
		return userService.getUserById(id);
	}
	
	@PutMapping("/updateuser/{id}")
	public ResponseEntity<User> updateBook(@PathVariable int id, @RequestBody User user)
			throws ResourceNotFoundException {
		return userService.updateUser(id, user);
	}
	
	@DeleteMapping("/deleteuser/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable int id) throws ResourceNotFoundException {
		return userService.deleteUser(id);
	}
	

}
