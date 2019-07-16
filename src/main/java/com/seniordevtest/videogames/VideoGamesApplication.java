package com.seniordevtest.videogames;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.seniordevtest.videogames.controller.LoadGameDevelopers;
import com.seniordevtest.videogames.model.response.Developer;

@SpringBootApplication
public class VideoGamesApplication {
	
	public static Map<String, Developer> listOfValidDevelopers = new HashMap<>();

	public static void main(String[] args) {
		
		listOfValidDevelopers = LoadGameDevelopers.loadDevelopers();
		
		SpringApplication.run(VideoGamesApplication.class, args);
	}

}
