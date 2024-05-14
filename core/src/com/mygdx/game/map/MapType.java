package com.mygdx.game.map;

public enum MapType {
	DOJO ("maps/Dojo.tmx", 20, 20),
	BEGIN_FOREST ("maps/Begin_forest.tmx", 64, 31),
	TOWN("maps/Town.tmx", 64, 64),;
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
