package com.mygdx.game.audio;

public enum AudioType {
	PEACEFUL("Musics/5 - Peaceful.ogg", true, 0.3f),
	ACCEPT("Sounds/Menu/Accept.wav", false, 0.5f);
	
	private final String filePath;
	private final boolean isMusic;
	private final float volume;
	AudioType(final String filePath, final boolean isMusic, final float volume) {
		this.filePath = filePath;
		this.isMusic = isMusic;
		this.volume = volume;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public boolean isMusic() {
		return isMusic;
	}
	public float getVolume() {
		return volume;
	}
}
