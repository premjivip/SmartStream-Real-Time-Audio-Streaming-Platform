package com.example.demo.entity;

import java.time.LocalDateTime;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class JwtToken {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "jwt_id")
    private int jwtId;

  
	
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 512)
    private String token;

    @Column
    private LocalDateTime expir_at = LocalDateTime.now();

    
	public JwtToken() {
		super();
	}


	public JwtToken(Integer jwtId, User user, String token, LocalDateTime expir_at) {
		super();
		this.jwtId = jwtId;
		this.user = user;
		this.token = token;
		this.expir_at = expir_at;
	}


	public JwtToken(User user, String token, LocalDateTime expir_at) {
		super();
		this.user = user;
		this.token = token;
		this.expir_at = expir_at;
	}


	public Integer getJwtId() {
		return jwtId;
	}


	public void setJwtId(Integer jwtId) {
		this.jwtId = jwtId;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public LocalDateTime getExpir_at() {
		return expir_at;
	}


	public void setExpir_at(LocalDateTime expir_at) {
		this.expir_at = expir_at;
	}


	@Override
	public int hashCode() {
		return Objects.hash(expir_at, jwtId, token, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JwtToken other = (JwtToken) obj;
		return Objects.equals(expir_at, other.expir_at) && Objects.equals(jwtId, other.jwtId)
				&& Objects.equals(token, other.token) && Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "JwtToken [jwtId=" + jwtId + ", user=" + user + ", token=" + token + ", expir_at=" + expir_at + "]";
	}

}
