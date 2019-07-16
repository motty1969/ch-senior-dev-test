package com.seniordevtest.videogames.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seniordevtest.videogames.mappers.GameStorageModelToGameMapper;
import com.seniordevtest.videogames.mappers.GameToGameStorageModelMapper;
import com.seniordevtest.videogames.model.Game;
import com.seniordevtest.videogames.model.response.GameResponse;
import com.seniordevtest.videogames.model.response.GameResponseList;

@Service
public class VideoGameService {
	
    @Autowired
    private GameToGameStorageModelMapper gameToGameStorageModelMapper;
	
    @Autowired
    private GameStorageModelToGameMapper gameStorageModelToGameMapper;
	
    @Autowired
    private VideoGameRepository videoGamerepo;
    
    public GameResponseList findAllGames() {
    	
    	List<VideoGameStorageModel> videoGameModelList = videoGamerepo.findAll();
    	
    	if(videoGameModelList == null || videoGameModelList.isEmpty()) {
    		return null;
    	} else {
    		
    		VideoGameStorageModel[] vga = new VideoGameStorageModel[videoGameModelList.size()];
    		vga = videoGameModelList.toArray(vga);
    		List<GameResponse> listOfGames = gameStorageModelToGameMapper.gameStorageModelsToGameResponseList(vga);

    		GameResponseList gameResponseList = new GameResponseList();
    		gameResponseList.setItemsPerPage(20);
    		gameResponseList.setStartIndex(1);
    		gameResponseList.setTotalResults(listOfGames.size());
    		gameResponseList.setItems(listOfGames);
    		
    		return gameResponseList;
    	}
    }

    public GameResponse findGameById(String gameId) {
    	Optional<VideoGameStorageModel> videoGame = videoGamerepo.findById(gameId);
    	
    	if(videoGame.isPresent()) {
    		return gameStorageModelToGameMapper.gameStorageModelToGameResponse(videoGame.get());
    	} else {
    		return null;
    	}
    }
    
	public GameResponse createGame(Game game) {
		
		VideoGameStorageModel createGame = gameToGameStorageModelMapper.gameToGameStorageModel(game);

		createGame = videoGamerepo.insert(createGame);
		
		return gameStorageModelToGameMapper.gameStorageModelToGameResponse(createGame);
	}
	
	public void deleteGame(String gameId) {
		videoGamerepo.deleteById(gameId);
	}

	public GameResponse updateGame(Game game) {

		VideoGameStorageModel ovgsm = videoGamerepo.findByTitleAndDeveloper(game.getTitle(), game.getDeveloper());
		
		if(ovgsm != null) {
			VideoGameStorageModel ovgsmUpdate = gameToGameStorageModelMapper.gameToGameStorageModel(game);
			ovgsmUpdate.setId(ovgsm.getId());
			
			ovgsmUpdate = videoGamerepo.save(ovgsmUpdate);
			
			return gameStorageModelToGameMapper.gameStorageModelToGameResponse(ovgsmUpdate);
		}

		return null;
	}
}
