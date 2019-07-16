package com.seniordevtest.videogames.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.seniordevtest.videogames.VideoGamesApplication;
import com.seniordevtest.videogames.model.Game;
import com.seniordevtest.videogames.model.response.GameResponse;
import com.seniordevtest.videogames.model.response.GameResponseList;

@Controller
@RequestMapping("/games")
public class VideoGameController {

    private VideoGameService videoGameService;
	
    public VideoGameController(VideoGameService videoGameService) {
    	this.videoGameService = videoGameService;
	}

    @PostMapping
	public ResponseEntity<GameResponse> createGame(@RequestBody Game game) {
		
		if(!validGameDetails(game)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		if(!VideoGamesApplication.listOfValidDevelopers.containsKey(game.getDeveloper())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
			

			GameResponse gameResponse = videoGameService.createGame(game);
			
			if(gameResponse != null) {
				return ResponseEntity.status(HttpStatus.CREATED).body(gameResponse);
			}
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
  
	@GetMapping
	public ResponseEntity<GameResponseList> getGames() {

		GameResponseList gameResponseList = videoGameService.findAllGames();
		
		if(gameResponseList != null) {
			return ResponseEntity.status(HttpStatus.OK).body(gameResponseList);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
    
	@GetMapping("/{gameId}")
	public ResponseEntity<GameResponse> getGameById(@PathVariable("gameId") String gameId) {
		
		GameResponse gameResponse = videoGameService.findGameById(gameId);

		if(gameResponse != null) {
			return ResponseEntity.status(HttpStatus.OK).body(gameResponse);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	@PutMapping("/{gameId}")
	public ResponseEntity<GameResponse> updateGame(@RequestBody Game game) {
		
		if(!validGameDetails(game)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		if(!VideoGamesApplication.listOfValidDevelopers.containsKey(game.getDeveloper())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
			GameResponse gameResponse = videoGameService.updateGame(game);
			if(gameResponse == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			} else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
		}
	}

	@DeleteMapping("/{gameId}")
	public ResponseEntity<GameResponse> deleteGame(@PathVariable("gameId") String gameId, @RequestParam(required = true, value = "developer") String developer) {
		
		if(!VideoGamesApplication.listOfValidDevelopers.containsKey(developer)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
			GameResponse gameResponse = videoGameService.findGameById(gameId);
			if(gameResponse == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			} else {
				videoGameService.deleteGame(gameId);
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
		}
	}
	
	private boolean validGameDetails(Game game) {
		boolean valid = true;
		
		// Developer and Title must be populated
		if(game.getDeveloper() == null || game.getTitle() == null) {
			valid = false;
		}
		
		return valid;
	}
}
