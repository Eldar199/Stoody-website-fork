package com.loam.stoody.controller;

import com.loam.stoody.model.Role;
import com.loam.stoody.model.User;
import com.loam.stoody.repository.RoleRepository;
import com.loam.stoody.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }

    @GetMapping("/register")
    public String getRegister(){
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute("user") User user, HttpServletRequest request) throws ServletException {
        if(userRepository.findUserByUsername(user.getUsername()).isPresent()){
            return "redirect:/register?error=true";
        } else if(userRepository.findUserByEmail(user.getEmail()).isPresent()){
            return "redirect:/register?error=true";
        }

        String password = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(2).get());// Simple User Role
        user.setRoles(roles);

        System.out.println(user);
        userRepository.save(user);

        request.login(user.getEmail(), password); // TODO: OrkhanGG should we use user name instead of email?

        return "redirect:/";
    }
}
