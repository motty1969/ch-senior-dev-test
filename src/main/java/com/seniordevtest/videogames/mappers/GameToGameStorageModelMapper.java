package com.seniordevtest.videogames.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.seniordevtest.videogames.controller.VideoGameStorageModel;
import com.seniordevtest.videogames.model.Game;

@RequestScope
@Mapper(componentModel = "spring")
@Component
public interface GameToGameStorageModelMapper {

    @Mappings({
            @Mapping(source = "game.title", target ="title"),
            @Mapping(source = "game.developer", target = "developer"),
            @Mapping(source = "game.releaseDate", target = "releaseDate"),
            @Mapping(source = "game.genres", target = "genres")
    })
    VideoGameStorageModel gameToGameStorageModel(Game game);
}