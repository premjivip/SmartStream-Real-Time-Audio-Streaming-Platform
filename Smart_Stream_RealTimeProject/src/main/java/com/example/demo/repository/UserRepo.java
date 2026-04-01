package com.example.demo.repository;



import java.util.List;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.tokens.Token.ID;

import com.example.demo.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
	
	public List<User> findAllByUsername(String username);

    // Find a user by id (already provided by JpaRepository)
    @Override
    public Optional<User> findById(Integer userId);

    // Find user by username (optional, for login)
    public Optional<User> findByUsername(String username);
    
    public Optional<User> findByEmail(String email);

	

}
