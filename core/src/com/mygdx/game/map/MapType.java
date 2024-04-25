package com.mygdx.game.map;

public enum MapType {
	MAP_1 ("maps/Dojo.tmx"),
	MAP_2 ("maps/Begin_forest.tmx");
	private final String filePath;
	
	MapType(final String filePath) {
		this.filePath = filePath;
	}
	
	public String getFilePath() {
		return filePath;
	}
}
