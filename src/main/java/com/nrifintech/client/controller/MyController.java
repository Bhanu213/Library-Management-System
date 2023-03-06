package com.nrifintech.client.controller;

import javax.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import com.nrifintech.model.User;
import com.nrifintech.repository.UserRepository;


@Controller
public class MyController {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/home")
	public String home(){
		return "home";
	}
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult res,Model m) {
		try {
			
			if(res.hasErrors()) {
				System.out.println("Error");
				m.addAttribute("user",user);
				return "home";
			}
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			User result=this.userRepository.save(user);
			System.out.println(result);
			m.addAttribute("user",new User());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			m.addAttribute("user",user);
					}
		
		return "home";
	}
}
