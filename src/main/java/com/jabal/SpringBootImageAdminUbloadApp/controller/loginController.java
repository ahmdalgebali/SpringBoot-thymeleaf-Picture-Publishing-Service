package com.jabal.SpringBootImageAdminUbloadApp.controller;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.jabal.SpringBootImageAdminUbloadApp.entity.User;
import com.jabal.SpringBootImageAdminUbloadApp.repository.RoleRepository;
import com.jabal.SpringBootImageAdminUbloadApp.repository.UserRepository;


@Controller
public class loginController {
	

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private RoleRepository roleRepo;

	@GetMapping(value = {"/login"})
	public String login() {
		return "login";
	}
	
	@GetMapping("/access-denied")
	public String showAccessDenied() {
		return "access-denied";
	}
	
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());     
        return "signup_form";
    }
     
    @PostMapping("/process_register")
    public String processRegister(User user) {   	
    	user.setRoles(new HashSet<>(roleRepo.findByName("USER")));
        user.setEnabled(true);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        try {
			userRepo.save(user);
	        return "redirect:/register?success";

		} catch (Exception e) {
			// TODO Auto-generated catch block
	        return "redirect:/register?bad";
		}      
    }
    

}	

