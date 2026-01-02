package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.smart.entities.User;
import com.smart.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/do_register")
    public String registerUser(@ModelAttribute User user, Model model) {

        // CHECK IF EMAIL EXISTS
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            model.addAttribute("user", new User());
            model.addAttribute("error", "Email already registered!");
            return "signup";
        }

        // SAVE NEW USER
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        model.addAttribute("success", "Registered successfully! Please login.");
        return "redirect:/signin";
    }
}
