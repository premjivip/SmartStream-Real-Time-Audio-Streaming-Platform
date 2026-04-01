package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Songs {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @Column
	    private String titel;

	    @Column
	    private String audio_url; // store URL

	    @Column
	    private String premium; // optional for premium flag

	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User user;

	    // Getters and setters
	    public int getId() { return id; }
	    public void setId(int id) { this.id = id; }

	    public String getTitel() { return titel; }
	    public void setTitel(String titel) { this.titel = titel; }

	    public String getAudio_url() { return audio_url; }
	    public void setAudio_url(String audio_url) { this.audio_url = audio_url; }

	    public String getPremium() { return premium; }
	    public void setPremium(String premium) { this.premium = premium; }

	    public User getUser() { return user; }
	    public void setUser(User user) { this.user = user; }   

	   
}
