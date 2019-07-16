package com.seniordevtest.videogames.controller;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoGameRepository extends MongoRepository<VideoGameStorageModel, String> {
    VideoGameStorageModel findByTitle(String gametitle);
    VideoGameStorageModel findByTitleAndDeveloper(String gametitle, String developer);
}
