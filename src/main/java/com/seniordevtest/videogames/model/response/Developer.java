package com.seniordevtest.videogames.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Developer {

	@JsonProperty
	private String name;
	@JsonProperty
	private String headquarters;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeadquarters() {
		return headquarters;
	}
	public void setHeadquarters(String headquarters) {
		this.headquarters = headquarters;
	}
	
}
