package com.nrifintech.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.nrifintech.exception.ResourceNotFoundException;
import com.nrifintech.model.User;
import com.nrifintech.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JavaMailSender javamailsender;
	
	@Value("${spring.mail.username}")
	private String sendermail;
	
	private int[] otp=new int[5];
	
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
	
	
	public ResponseEntity<String> matchingOtp(String username)
	{
		Random r=new Random();
		for(User user:userRepository.findAll())
		{
			if(user.getUsername().equals(username))
			{
				for(int i=0;i<5;i++)
				{
					otp[i]=r.nextInt(9);
				}
				System.out.println(Arrays.toString(otp));
				String Text="To reset your password use this passcode"+Arrays.toString(otp);
				SimpleMailMessage smg=new SimpleMailMessage();
				smg.setFrom(sendermail);
				smg.setTo(user.getEmail());
				smg.setText(Text);
				smg.setSubject("Password Recovery");
				javamailsender.send(smg);
				return ResponseEntity.ok().body("Passcode sent to your mail");
			}
			else
			{
				return ResponseEntity.ok().body("User Not Found");
			}
		}
		return null;
	}
}
