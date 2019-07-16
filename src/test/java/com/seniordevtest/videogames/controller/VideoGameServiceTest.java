package com.seniordevtest.videogames.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seniordevtest.videogames.mappers.GameStorageModelToGameMapper;
import com.seniordevtest.videogames.mappers.GameToGameStorageModelMapper;
import com.seniordevtest.videogames.model.Game;
import com.seniordevtest.videogames.model.response.GameResponse;
import com.seniordevtest.videogames.model.response.GameResponseList;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class VideoGameServiceTest {

	private static final String VALID_DEVELOPER_NAME = "Namco";
	private static final String INVALID_DEVELOPER_NAME = "Companies House";
	private static final String VALID_TITLE = "title";
	private static final String VALID_UPDATED_TITLE = "updated title";
	private static final String GAME_ID = "UHH87878JUGW@";
	private static final String GAME_ID_NOT_FOUND = "9878IUYWTDH";

    @Mock
    private GameResponseList allgames;
    
    @Mock
    private VideoGameRepository videoGameRepo;

    @Mock
    private VideoGameStorageModel videoGameStorageModelToReturnFromDb;
    
    @Mock
    private List<VideoGameStorageModel> videoGameStorageModelListToReturnFromDb;

    @Mock
    private GameStorageModelToGameMapper gameStorageModelToGameMapper;

    @Mock
    private GameToGameStorageModelMapper gameToGameStorageModelMapper;

    @Mock
    private GameResponse expectedGameResponse;
    
    @InjectMocks
    private VideoGameService videoGameService;
    
    @BeforeEach
    public void preTest() {
    	videoGameStorageModelListToReturnFromDb.add(createVideoGameStorageModel(VALID_DEVELOPER_NAME));
    }

    @Test
    @DisplayName("Test findAllGames and return a GameResponseList object")
    public void verifyFindAllGames() {

        when(videoGameRepo.findAll()).thenReturn(videoGameStorageModelListToReturnFromDb);

        VideoGameStorageModel[] vga = new VideoGameStorageModel[videoGameStorageModelListToReturnFromDb.size()];
		vga = videoGameStorageModelListToReturnFromDb.toArray(vga);

		when(gameStorageModelToGameMapper.gameStorageModelsToGameResponseList(vga)).thenReturn(new ArrayList<GameResponse>());

        allgames = videoGameService.findAllGames();
        
        assertNotNull(allgames);
    }

    @Test
    @DisplayName("Test findGameByID and return a GameResponse object")
    public void verifyFindGameById() {

        when(videoGameRepo.findById(GAME_ID)).thenReturn(Optional.of(videoGameStorageModelToReturnFromDb));
        when(gameStorageModelToGameMapper.gameStorageModelToGameResponse(videoGameStorageModelToReturnFromDb)).thenReturn(expectedGameResponse);

        GameResponse gameResponse = videoGameService.findGameById(GAME_ID);
        
        assertSame(expectedGameResponse, gameResponse);
    }

    @Test
    @DisplayName("Test createGame and return a GameResponse object")
    public void verifyGameCreation() {

    	Game game = new Game();
    	
        when(videoGameRepo.insert(videoGameStorageModelToReturnFromDb)).thenReturn(videoGameStorageModelToReturnFromDb);
        when(gameToGameStorageModelMapper.gameToGameStorageModel(game)).thenReturn(videoGameStorageModelToReturnFromDb);
		when(gameStorageModelToGameMapper.gameStorageModelToGameResponse(videoGameStorageModelToReturnFromDb)).thenReturn(expectedGameResponse);

        GameResponse gameResponse = videoGameService.createGame(game);
        
        assertSame(expectedGameResponse, gameResponse);
    }

    @Test
    @DisplayName("Test updateGame and return a GameResponse object")
    public void verifyGameUpdate() {

    	Game game = new Game();
    	game.setDeveloper(VALID_DEVELOPER_NAME);
    	game.setTitle(VALID_TITLE);
    	
    	when(videoGameRepo.findByTitleAndDeveloper(game.getTitle(), game.getDeveloper())).thenReturn(videoGameStorageModelToReturnFromDb);
        when(videoGameRepo.save(videoGameStorageModelToReturnFromDb)).thenReturn(videoGameStorageModelToReturnFromDb);
        when(gameToGameStorageModelMapper.gameToGameStorageModel(game)).thenReturn(videoGameStorageModelToReturnFromDb);
		when(gameStorageModelToGameMapper.gameStorageModelToGameResponse(videoGameStorageModelToReturnFromDb)).thenReturn(expectedGameResponse);

        GameResponse gameResponse = videoGameService.updateGame(game);
        
        assertSame(expectedGameResponse, gameResponse);
    }
    
    private VideoGameStorageModel createVideoGameStorageModel(String developer) {
    	VideoGameStorageModel vgsm = new VideoGameStorageModel();
    	vgsm.setDeveloper(developer);
		return vgsm;
    }
}
