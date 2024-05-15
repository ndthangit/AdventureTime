package com.mygdx.game.audio;

public enum AudioType {
	PEACEFUL("Musics/5 - Peaceful.ogg", true, 0.3f),
	ACCEPT("Sounds/Menu/Accept.wav", false, 0.5f),
	CANCEL ("Sounds/Menu/Cancel.wav",false,0.3f),
	GAMEOVER2 ("Sounds/Game/GameOver2.wav", false, 0.3f),
	SWORD("Sounds/Game/Sword.wav", false, 0.5f),
	LOL("Musics/4 - Village.ogg", true, 0.3f),
	LOL1("Musics/1 - Adventure Begin.ogg", true, 0.2f),
	CHILL("Musics/27 - Chill.ogg", false, 0.3f),
	GOLD1("Sounds/Game/Gold1.wav",false,0.3f),
	HIT("Sounds/Game/Hit1.wav",false, 0.3f),
	KILL("Sounds/Game/Kill.wav", false, 0.5f);
	
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
