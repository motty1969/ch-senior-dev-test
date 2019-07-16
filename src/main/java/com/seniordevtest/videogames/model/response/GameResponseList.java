package com.seniordevtest.videogames.model.response;

import java.util.List;

public class GameResponseList {
	private Integer itemsPerPage;
	private Integer startIndex;
	private Integer totalResults;
	private List<GameResponse> items;
	
	public Integer getItemsPerPage() {
		return itemsPerPage;
	}
	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}
	public Integer getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	public Integer getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	public List<GameResponse> getItems() {
		return items;
	}
	public void setItems(List<GameResponse> items) {
		this.items = items;
	}
	 
}
