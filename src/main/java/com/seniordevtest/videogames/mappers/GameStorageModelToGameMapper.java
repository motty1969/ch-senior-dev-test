package com.seniordevtest.videogames.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.web.context.annotation.RequestScope;

import com.seniordevtest.videogames.controller.VideoGameStorageModel;
import com.seniordevtest.videogames.model.response.GameResponse;

@RequestScope
@Mapper(componentModel = "spring")
public interface GameStorageModelToGameMapper {

    @Mappings({
            @Mapping(source = "gameStorageModel.title", target ="title"),
            @Mapping(source = "gameStorageModel.developer", target = "developer"),
            @Mapping(source = "gameStorageModel.releaseDate", target = "releaseDate"),
            @Mapping(source = "gameStorageModel.genres", target = "genres")
    })
    GameResponse gameStorageModelToGameResponse(VideoGameStorageModel gameStorageModel);
    List<GameResponse> gameStorageModelsToGameResponseList(VideoGameStorageModel[] gameStorageModel);
}