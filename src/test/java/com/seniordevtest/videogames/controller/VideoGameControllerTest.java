package com.seniordevtest.videogames.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.seniordevtest.videogames.VideoGamesApplication;
import com.seniordevtest.videogames.model.Game;
import com.seniordevtest.videogames.model.response.Developer;
import com.seniordevtest.videogames.model.response.GameResponse;
import com.seniordevtest.videogames.model.response.GameResponseList;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class VideoGameControllerTest {

	private static final String VALID_DEVELOPER_NAME = "Namco";
	private static final String INVALID_DEVELOPER_NAME = "Companies House";
	private static final String VALID_TITLE = "title";
	private static final String VALID_UPDATED_TITLE = "updated title";
	private static final String GAME_ID = "UHH87878JUGW@";
	private static final String GAME_ID_NOT_FOUND = "9878IUYWTDH";
	
    @Mock
    private HttpServletRequest request;

    @Mock
    private GameResponse gameResponse;

    @Mock
    private List<GameResponse> gameResponses;

    @Mock
    private VideoGamesApplication videoGameApplication;

    @Mock
    private VideoGameService videoGameService;

    private VideoGameController videoGameController;

    private Game validGame;
    private Game validGameWithMissingTitle;
	private Game validUpdatedGame;
    private Game invalidGame;
	private GameResponse validGameResponse;
	private GameResponse validUpdatedGameResponse;
	private GameResponse validGameResponseWithMissingTitle;
	private GameResponse invalidGameResponse;
    
    @BeforeEach
    public void preTest() {
        videoGameController = new VideoGameController(videoGameService);
        
        Map<String, Developer> devs = new HashMap<>();
        Developer dev = new Developer();
        dev.setName(VALID_DEVELOPER_NAME);
        
        devs.put(VALID_DEVELOPER_NAME, dev);
        
        videoGameApplication.listOfValidDevelopers = devs;
        
        validGame = createGameObject(VALID_DEVELOPER_NAME, VALID_TITLE);
        validUpdatedGame = createGameObject(VALID_DEVELOPER_NAME, VALID_UPDATED_TITLE);
        validGameWithMissingTitle = createGameObject(VALID_DEVELOPER_NAME, null);
        invalidGame = createGameObject(INVALID_DEVELOPER_NAME, VALID_TITLE);
        
        validGameResponse = createGameResponseObject(VALID_DEVELOPER_NAME, VALID_TITLE);
        validUpdatedGameResponse = createGameResponseObject(VALID_DEVELOPER_NAME, VALID_UPDATED_TITLE);
        validGameResponseWithMissingTitle = createGameResponseObject(VALID_DEVELOPER_NAME, null);
        invalidGameResponse = createGameResponseObject(INVALID_DEVELOPER_NAME, VALID_TITLE);
    }

    @Test
    @DisplayName("Test creation of game with successful response of HTTP CREATED")
    public void verifyCreationOfGameWithHttpCreated() {
        
        when(videoGameService.createGame(validGame)).thenReturn(validGameResponse);
		
        ResponseEntity<GameResponse> responseEntity = videoGameController.createGame(validGame);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertSame(validGameResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test creation of game with unsuccessful response of HTTP BAD REQUEST")
    public void verifyCreationOfGameWithHttpBadRequest() {

        ResponseEntity<GameResponse> responseEntity = videoGameController.createGame(validGameWithMissingTitle);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertSame(null, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test creation of game with unsuccessful response of HTTP UNAUTHORIZED")
    public void verifyCreationOfGameWithHttpUnauthorized() {
		
        ResponseEntity<GameResponse> responseEntity = videoGameController.createGame(invalidGame);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertSame(null, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test retrieving list of games with successful response of HTTP OK")
    public void verifyGetListOfGames() {
        
        List<GameResponse> listOfGameResponse = new ArrayList<>();
        listOfGameResponse.add(validGameResponse);
        
        GameResponseList expectedGameResponseList = new GameResponseList();
        expectedGameResponseList.setItems(listOfGameResponse);
        expectedGameResponseList.setItemsPerPage(0);
        expectedGameResponseList.setStartIndex(0);
        expectedGameResponseList.setTotalResults(0);
        
        when(videoGameService.findAllGames()).thenReturn(expectedGameResponseList);
		
        ResponseEntity<GameResponseList> responseEntity = videoGameController.getGames();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertSame(expectedGameResponseList, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test retrieving list of games where there are no games found with response of HTTP NOT_FOUND")
    public void verifyGetListOfGamesWithHttpNotFound() {
        
        when(videoGameService.findAllGames()).thenReturn(null);
		
        ResponseEntity<GameResponseList> responseEntity = videoGameController.getGames();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertSame(null, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test retrieving a game by id response of HTTP OK")
    public void verifyGetListOfGamesByIdWithHttpOk() {
        
        when(videoGameService.findGameById(GAME_ID)).thenReturn(validGameResponse);
		
        ResponseEntity<GameResponse> responseEntity = videoGameController.getGameById(GAME_ID);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertSame(validGameResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test retrieving a game by id that doesn't exist response of HTTP NOT_FOUND")
    public void verifyGetListOfGamesByIdWithHttpNotFound() {
        
        when(videoGameService.findGameById(GAME_ID_NOT_FOUND)).thenReturn(null);
		
        ResponseEntity<GameResponse> responseEntity = videoGameController.getGameById(GAME_ID_NOT_FOUND);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertSame(null, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test update of game with successful response of HTTP NO_CONTENT")
    public void verifyUpdateOfGameWithHttpNoContent() {
        
        when(videoGameService.updateGame(validUpdatedGame)).thenReturn(validUpdatedGameResponse);
		
        ResponseEntity<GameResponse> updateResponseEntity = videoGameController.updateGame(validUpdatedGame);

        assertEquals(HttpStatus.NO_CONTENT, updateResponseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test update of game with unsuccessful response of HTTP BAD_REQUEST")
    public void verifyUpdateOfGameWithHttpBadRequest() {
		
        ResponseEntity<GameResponse> updateResponseEntity = videoGameController.updateGame(validGameWithMissingTitle);

        assertEquals(HttpStatus.BAD_REQUEST, updateResponseEntity.getStatusCode());
        assertSame(null, updateResponseEntity.getBody());
    }

    @Test
    @DisplayName("Test update of game with unsuccessful response of HTTP NOT_FOUND")
    public void verifyUpdateOfGameWithHttpNotFound() {
		
        ResponseEntity<GameResponse> updateResponseEntity = videoGameController.updateGame(validUpdatedGame);

        assertEquals(HttpStatus.NOT_FOUND, updateResponseEntity.getStatusCode());
        assertSame(null, updateResponseEntity.getBody());
    }

    @Test
    @DisplayName("Test deletion of game with successful response of HTTP NO_CONTENT")
    public void verifyDeletionOfGameWithHttpNoContent() {
        
        when(videoGameService.findGameById(GAME_ID)).thenReturn(validUpdatedGameResponse);
		
        ResponseEntity<GameResponse> updateResponseEntity = videoGameController.deleteGame(GAME_ID, VALID_DEVELOPER_NAME);

        assertEquals(HttpStatus.NO_CONTENT, updateResponseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test deletion of game with unsuccessful response of HTTP UNAUTHORIZED")
    public void verifyDeletionOfGameWithHttpUnauthorized() {
        
        ResponseEntity<GameResponse> updateResponseEntity = videoGameController.deleteGame(GAME_ID, INVALID_DEVELOPER_NAME);

        assertEquals(HttpStatus.UNAUTHORIZED, updateResponseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test deletion of game with unsuccessful response of HTTP NOT_FOUND")
    public void verifyDeletionOfGameWithHttpNotFound() {
        
        when(videoGameService.findGameById(GAME_ID)).thenReturn(null);
		
        ResponseEntity<GameResponse> updateResponseEntity = videoGameController.deleteGame(GAME_ID, VALID_DEVELOPER_NAME);

        assertEquals(HttpStatus.NOT_FOUND, updateResponseEntity.getStatusCode());
    }
    
    private Game createGameObject(String developerName, String title) {

        Game game = new Game();
        game.setDeveloper(developerName);
        game.setTitle(title);

        return game;
    }
    
    private GameResponse createGameResponseObject(String developerName, String title) {

        GameResponse expectedGameResponse = new GameResponse();
        expectedGameResponse.setDeveloper(developerName);
        expectedGameResponse.setTitle(title);

        return expectedGameResponse;
    }
}
