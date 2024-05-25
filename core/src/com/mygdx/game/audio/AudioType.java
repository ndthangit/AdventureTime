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
	HIT("Sounds/Game/Hit.wav",false, 0.5f),
	HIT1("Sounds/Game/Hit3.wav",false, 0.5f),
	HIT2("Sounds/Game/Hit2.wav", false, 0.5f),
	KILL("Sounds/Game/Kill.wav", false, 0.5f),
	REVELATION("Musics/3 - Revelation.ogg", true, 0.3f),
	TEMPLE("Musics/12 - Temple.ogg", true, 0.5f),
	DUNGEON("Musics/21 - Dungeon.ogg", true, 0.5f),
	SPIRIT("Musics/2 - The Cave.ogg", true, 0.5f),
	ABANDON("Musics/10 - Dark Castle.ogg", true, 0.5f),
	FX("Sounds/Game/Fx.wav", false, 0.5f),
	EXPLOSION("Sounds/Game/Explosion2.wav",false ,0.5f ),
	ALERT("Sounds/Game/Alert.wav", false, 0.5f),
	POWERUP("Sounds/Game/PowerUp1.wav", false, 0.5f),
	FIREBALL("Sounds/Game/Fireball.wav",false,0.5f),
	END("Musics/20 - Good Time.ogg", true, 0.3f),
	SWORD2("Sounds/Game/Sword2.wav", false, 0.5f),;
	
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
