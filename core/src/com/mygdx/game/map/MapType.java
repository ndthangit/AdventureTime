package com.mygdx.game.map;

public enum MapType {
	DOJO ("maps/Dojo.tmx", 20, 20),
	BEGIN_FOREST ("maps/Begin_forest.tmx", 64, 31),
	TOWN("maps/Town.tmx", 64, 64),
	SAMU_BOSS ("maps/Samu_Boss.tmx", 25, 25),
	CAVE ("maps/Cave.tmx", 30, 25),
	ABANDONVIL ("maps/Abandon_Village.tmx", 25, 25),
	SPIRIT_BOSS ("maps/Spirit_boss.tmx", 25, 25),;
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
