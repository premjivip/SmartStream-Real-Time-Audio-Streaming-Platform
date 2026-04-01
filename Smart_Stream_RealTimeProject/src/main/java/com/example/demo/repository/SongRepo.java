package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.entity.Songs;

@Repository
public interface SongRepo extends JpaRepository<Songs, Integer> {

}
