package com.mygdx.game.map;

public enum MapType {
	MAP_1 ("maps/Dojo.tmx", 20, 20),
	MAP_2 ("maps/Begin_forest.tmx", 64, 32);
	private final String filePath;
	private final int width;
	private final int height;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	MapType(final String filePath, int width, int height) {
		this.filePath = filePath;
        this.width = width;
        this.height = height;
    }
	
	public String getFilePath() {
		return filePath;
	}
}
