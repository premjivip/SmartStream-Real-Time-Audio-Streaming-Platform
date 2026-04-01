package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.JwtToken;
import com.example.demo.entity.User;
import com.example.demo.repository.JwtRepo;
import com.example.demo.repository.UserRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServicee {
	
	private UserRepo repo;
	private JwtRepo jwtRepo;
	private BCryptPasswordEncoder encoder;
	private Key SINING_KEY;
	
	
	private static final Logger log =
			LoggerFactory.getLogger(UserServicee.class);
	

	public UserServicee(UserRepo repo,JwtRepo jwtRepo,@Value("${jwt.secret}") String jwtsecret) {
		this.repo = repo;
		this.jwtRepo = jwtRepo;
		
		encoder = new BCryptPasswordEncoder();
		
		this.SINING_KEY =
				Keys.hmacShaKeyFor(jwtsecret.getBytes(StandardCharsets.UTF_8));
	}
	
	public boolean register(User user) {
		Optional<User> userExcit = repo.findByEmail(user.getEmail());
		
		if(userExcit.isPresent()) {
			return false;
		}
		
		String  passwordd = encoder.encode(user.getPassword());
		
		user.setPassword(passwordd);
		
		repo.save(user);
		
		return true;
	}
	
	
	
	
	public User Authendication(String username, String password) {
	    List<User> users = repo.findAllByUsername(username); // returns list of users with this username

	    if (users.isEmpty()) {
	        throw new RuntimeException("Username not found");
	    }

	    User user = users.get(0);

	    
	    if (encoder.matches(password, user.getPassword())) {
	        return user;
	    }

	    throw new RuntimeException("Password not matching");
	}
	
	
	public String tokenGeneration(User user) {

	    LocalDateTime now = LocalDateTime.now();

	    Optional<JwtToken> existingTokenOpt = jwtRepo.findByUser(user);

	    if (existingTokenOpt.isPresent()) {
	        JwtToken existToken = existingTokenOpt.get();

	        if (now.isBefore(existToken.getExpir_at())) {
	            return existToken.getToken();
	        }
	    }

	    String token = Jwts.builder()
	            .setSubject(user.getUsername())
	            .claim("role", user.getGender())
	            .setIssuedAt(new Date())
	            .setExpiration(new Date(System.currentTimeMillis() + 3600000))
	            .signWith(SINING_KEY)
	            .compact();

	    jwtRepo.save(new JwtToken(user, token, now.plusHours(1)));

	    return token;
	}
	
	  
	
	 public String extractUsername(HttpServletRequest request) {

	        String header = request.getHeader("user");

	        if (header == null || !header.startsWith("Bearer ")) {
	            throw new RuntimeException("Invalid Authorization header");
	        }

	        String token = header.substring(7);

	        Claims claims = validateToken(token);

	        return claims.getSubject();
	    }
	 
	
	    public Claims validateToken(String token) {

	        return Jwts.parserBuilder()
	                .setSigningKey(SINING_KEY)
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }
	    
	    @Transactional
	    public void makePremium(Integer userId) {
	        User user = repo.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        user.setPremium(true); 
	        repo.save(user);         
	        System.out.println("User " + userId + " is now premium!");
	    }

}
