package com.example.demo.controller;




import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Songs;
import com.example.demo.entity.User;
import com.example.demo.service.SongService;
import com.example.demo.utliti.FileUploder;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/songs")
public class SongController {


	 private final SongService service;

	    public SongController(SongService service) {
	        this.service = service;
	    }
	   

	    // Display Add Song page
	    @GetMapping("/addsong")
	    public String addSongPage() {
	        return "addsong";
	    }

	    @PostMapping("/save")
	    public String saveSong(@RequestParam("titel") String titel,
	                           @RequestParam("mediaFile") MultipartFile mediaFile,
	                           HttpSession session) throws IOException {

	        Integer userId = (Integer) session.getAttribute("userId");

	        String uploadDir = System.getProperty("user.dir") + "/uploads/";
	        File dir = new File(uploadDir);
	        if (!dir.exists()) dir.mkdirs();

	        String fileName = FileUploder.saveFile(mediaFile, uploadDir);

	        Songs song = new Songs();
	        song.setTitel(titel);
	        song.setAudio_url("/uploads/" + fileName);

	       
	        User user = new User();
	        user.setId(userId);
	        song.setUser(user);

	        service.saveSong(song);

	        return "redirect:/songs/list";
	    }
	
	    @GetMapping("/list")
	    public String showSongList(Model model) {
	        List<Songs> songs = service.getAllSong();
	        model.addAttribute("songs", songs);
	        return "songs";
	    }

	    // Stream song
	    @GetMapping("/listen/{id}")
	    public ResponseEntity<Resource> listenSong(@PathVariable int id) throws IOException {
	        Songs song = service.getSongById(id);
	        Path filePath = Paths.get(System.getProperty("user.dir") + song.getAudio_url());
	        Resource resource = new UrlResource(filePath.toUri());

	        // Detect correct media type (audio/mp3, video/mp4, etc.)
	        String contentType = Files.probeContentType(filePath);
	        MediaType mediaType;

	        if (contentType != null) {
	            mediaType = MediaType.parseMediaType(contentType);
	        } else {
	            // Default if type cannot be detected
	            mediaType = MediaType.APPLICATION_OCTET_STREAM;
	        }

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + song.getTitel() + "\"")
	                .contentType(mediaType)
	                .body(resource);
	    }

}
