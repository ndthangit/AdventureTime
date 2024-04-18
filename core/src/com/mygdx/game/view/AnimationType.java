package com.mygdx.game.view;

public enum AnimationType {
	HERO_DOWN("Actor/Characters/BlackNinjaMage/blackninjamage.atlas", "Walk", 0.05f,0),
	HERO_UP("Actor/Characters/BlackNinjaMage/blackninjamage.atlas", "Walk", 0.05f, 1),
	HERO_LEFT("Actor/Characters/BlackNinjaMage/blackninjamage.atlas", "Walk", 0.05f, 2),
	HERO_RIGHT("Actor/Characters/BlackNinjaMage/blackninjamage.atlas", "Walk", 0.05f, 3),
	
	HERO_ATTACK_DOWN("Actor/Characters/BlackNinjaMage/blackninjamage.atlas", "Attack", 0.05f, 0),
	HERO_ATTACK_UP("Actor/Characters/BlackNinjaMage/blackninjamage.atlas", "Attack", 0.05f, 1),
	HERO_ATTACK_LEFT("Actor/Characters/BlackNinjaMage/blackninjamage.atlas", "Attack", 0.05f, 2),
	HERO_ATTACK_RIGHT("Actor/Characters/BlackNinjaMage/blackninjamage.atlas", "Attack", 0.05f, 3);

	public String getAtlasPath() {
		return atlasPath;
	}

	public String getAtlasKey() {
		return atlasKey;
	}

	public float getFrameTime() {
		return frameTime;
	}

	public int getColIndex() {
		return colIndex;
	}

	private final String atlasPath;
	private final String atlasKey;
	private final float frameTime;
	private final int colIndex;
	
	AnimationType(String atlasPath, String atlasKey, float frameTime, int colIndex) {
		this.atlasPath = atlasPath;
		this.atlasKey = atlasKey;
		this.frameTime = frameTime;
		// TODO Auto-generated constructor stub
		this.colIndex = colIndex;
	}
}
