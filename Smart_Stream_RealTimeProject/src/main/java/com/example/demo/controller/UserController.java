package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Gender;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.UserServicee;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api")
public class UserController {
	
	private final UserServicee service;
    private final UserRepo repo;

    public UserController(UserServicee service, UserRepo repo) {
        this.service = service;
        this.repo = repo;
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }


    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

   
    @GetMapping("/registerUser")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String gender) {

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPremium(false);
        user.setGender(Gender.valueOf(gender.toUpperCase()));

        boolean saved = service.register(user);

        if (!saved) {
            return "error";
        }

        return "success";
    }

    // LOGIN PAGE
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // LOGIN USER
    @GetMapping("/jwtToken")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            HttpServletResponse response) {

        User user = service.Authendication(username, password);

        // Store in session
        session.setAttribute("userId", user.getId());
        session.setAttribute("isPremium", user.isPremium());

        // Optional: JWT token in cookie
        String token = service.tokenGeneration(user);
        ResponseCookie cookie = ResponseCookie.from("AuthToken", token)
                .httpOnly(true)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return "redirect:/api/successLogin";
    }
    
    @GetMapping("/successLogin")
    public String successLoginPage(HttpSession session, Model model) {
        
        return "successLogin";
    }

  
    @GetMapping("/paymentPage")
    public String paymentPage(@RequestParam Integer userId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("amount", 499);
        return "payment";
    }

 
    @GetMapping("/processPayment")
    public String processPayment(@RequestParam Integer userId,
                                 @RequestParam String paymentMethod,
                                 HttpSession session) {

        service.makePremium(userId); 

        session.setAttribute("isPremium", true); 

        return "redirect:/songs/list";
    }

}
