package com.seniordevtest.videogames.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.seniordevtest.videogames.model.response.Developer;

public class LoadGameDevelopers {
    
	private static final String ACCESS_KEY = "AKIASPEKVOGUUIAYA4WW";
	private static final String SECRET_KEY = "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";
	private static final String BUCKET_NAME = "ch-senior-dev-test";

	private LoadGameDevelopers() {
	}

	public static Map<String, Developer> loadDevelopers() {
        
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_WEST_1)
                .build();

        S3Object s3object = s3client.getObject(BUCKET_NAME, "developers.json");
        S3ObjectInputStream inputStream = s3object.getObjectContent();

    	JSONTokener tokener = new JSONTokener(inputStream);
        JSONObject jo = new JSONObject(tokener);
	    JSONArray jsonArray = jo.getJSONArray("developers");

	    Map<String, Developer> devList = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject explrObject = jsonArray.getJSONObject(i);
            Developer dev = new Developer();
            String devName = explrObject.getString("name");
            dev.setName(devName);
            dev.setHeadquarters(explrObject.getString("headquarters"));
            devList.put(devName, dev);
        }
		return devList;
    }
}
