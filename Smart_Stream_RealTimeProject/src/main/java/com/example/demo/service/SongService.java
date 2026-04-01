package com.example.demo.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.example.demo.entity.Songs;
import com.example.demo.repository.SongRepo;

@Service
public class SongService {
	
	private final SongRepo repo;

    public SongService(SongRepo repo) { this.repo = repo; }

    public List<Songs> getAllSong() { return repo.findAll(); }

    public void saveSong(Songs song) { repo.save(song); }

    public Songs getSongById(int id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Song not found"));
    }
}
