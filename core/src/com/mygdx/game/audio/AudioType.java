package com.mygdx.game.audio;

public enum AudioType {
	PEACEFUL("Musics/5 - Peaceful.ogg", true, 0.3f),
	ACCEPT("Sounds/Menu/Accept.wav", false, 0.5f),
	CANCEL ("Sounds/Menu/Cancel.wav",false,0.3f),
	GAMEOVER2 ("Sounds/Game/GameOver2.wav", false, 0.3f),
	SWORD("Sounds/Game/Sword.wav", false, 0.5f),
	LOL("Musics/Legends Never Die.mp3", true, 0.2f),
	LOL1("Musics/31 - POPSTARS.mp3", true, 0.2f);
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
