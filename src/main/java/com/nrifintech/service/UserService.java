package com.nrifintech.service;


import java.util.ArrayList;
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
	
	private String passcode="";
	
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
	
	
	public ResponseEntity<String> accountRecovery(String username)
	{
		int flag=1;
		Random r=new Random();
		for(User user:userRepository.findAll())
		{
			if(user.getUsername().equals(username))
			{
				for(int i=0;i<5;i++)
				{
					passcode+=r.nextInt(9);
				}
				user.setPassword(passcode);
				userRepository.save(user);
				String Text="To reset your password use this verification code "+passcode;
				SimpleMailMessage smg=new SimpleMailMessage();
				smg.setFrom(sendermail);
				smg.setTo(user.getEmail());
				smg.setText(Text);
				smg.setSubject("Account Recovery");
				javamailsender.send(smg);
				flag=0;
				break;
			}
			else
			{
				flag=1;
			}
		}
		if(flag==0)
		{
			return ResponseEntity.ok().body("Verification code sent to your mail");
		}
		else
		{
			return ResponseEntity.ok().body("User Not Found");
		}
	}
	
	public ResponseEntity<String> updatePassword(String passcode,String password)
	{
		int flag=1;
		if(this.passcode.equals(passcode))
		{
			for(User u:userRepository.findAll())
			{
				if(u.getPassword().equals(passcode))
				{
					u.setPassword(password);
					userRepository.save(u);
					flag=0;
					break;
				}
				else
				{
					flag=1;
				}
			}
			if(flag==1)
			{
				return ResponseEntity.ok().body("Something Went Wrong, Password Not Changed");
			}
			else
			{
				return ResponseEntity.ok().body("Password Updated");
			}
		}
		else
		{
			return ResponseEntity.ok().body("Incorrect Verification Code");
		}
	}
}
