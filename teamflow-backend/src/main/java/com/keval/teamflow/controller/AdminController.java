package com.keval.teamflow.controller;

import com.keval.teamflow.domain.models.User;
import com.keval.teamflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    @Autowired
    public AdminController(UserRepository userRepository){
        this.userRepository= userRepository;
    }
    @GetMapping("/info")
    public User getUserDetails(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        assert userDetails != null;
        return userRepository.findByEmail(userDetails.getUsername()).get();
    }
}
