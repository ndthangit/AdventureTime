package com.mygdx.game.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.CoreGame;

public class AudioManager {
	public boolean musicEnabled = true;
	private AudioType currentMusicType;
	private Music currentMusic;
	private final AssetManager assetManager;
	
	
	public AudioManager(CoreGame game) {
		assetManager = game.getAssetManager();
		currentMusic = null;
		currentMusicType = null;
	}
	
	public void playAudio(final AudioType type) {
		if (!musicEnabled) {
			return;
		}
		if (type.isMusic()) {
			//play music
			if (currentMusicType == type) {
				return;
			}
			else if (currentMusicType != null) {
				currentMusic.stop();
			}
			
			currentMusicType = type;
			currentMusic = assetManager.get(type.getFilePath(), Music.class);
			currentMusic.setLooping(true);
			currentMusic.setVolume(type.getVolume());
			currentMusic.play();
		}
		else {
			assetManager.get(type.getFilePath(), Sound.class).play(type.getVolume());
		}
	}

	public void stopCurrentMusic() {
		if (currentMusic != null) {
			currentMusic.stop();
			currentMusic = null;
			currentMusicType = null;
		}
		
	}
}
