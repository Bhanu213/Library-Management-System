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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
	public RedirectView registerUser(@Valid @ModelAttribute("user") User user,BindingResult res,Model m,RedirectAttributes ra) {
		try {
			
			if(res.hasErrors()) {
				System.out.println("Error");
				m.addAttribute("user",user);
				return new RedirectView("/home#signup");
			}
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			User result=this.userRepository.save(user);
			System.out.println(result);
			m.addAttribute("user",new User());
			
		}  catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("user",user);
			ra.addFlashAttribute("signup_msg", "User is already registered. Duplicate entry.");
				return new RedirectView("/home#signup");
			}
		    ra.addFlashAttribute("signupSuccess_msg", "Successfully registered you can now login.");
			return new RedirectView("/home#signup");
	}
	
	@GetMapping("/login-error")
    public RedirectView login(RedirectAttributes ra) {
		ra.addFlashAttribute("login_msg", "Incorrect Username or password.");
        return new RedirectView("/home#login");
    }
	@GetMapping("/signupPage")
    public RedirectView signupPage() {
        return new RedirectView("/home#signup");
    }
	@GetMapping("/loginPage")
    public RedirectView loginPage() {
        return new RedirectView("/home#login");
    }
}
