package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.CoreGame;
import com.mygdx.game.LoadAsset;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.KeyInputListener;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.ui.LoadingUI;

public class LoadingScreen extends AbstractScreen<LoadingUI> {
	private final AssetManager assetManager;
	private final LoadAsset loadAsset;
	private boolean isMusicLoaded;
	
	public LoadingScreen(CoreGame game) {
		super(game, game.getLoadAsset().getloadingSkin());
		this.assetManager = game.getAssetManager();
		this.loadAsset = game.getLoadAsset();
		
		assetManager.load("maps/Demo-abandon-village-map.tmx", TiledMap.class);
		
		//audio
		isMusicLoaded = false;
		for (final AudioType audioType: AudioType.values()) {
			if (audioType.isMusic()) {
				assetManager.load(audioType.getFilePath(), Music.class);
			}
			else {
				assetManager.load(audioType.getFilePath(), Sound.class);
			}
		}
	}
	
	@Override
	public void show() {
		super.show();
	}
	
	public void hide() {
		super.hide();
//		audioManager.stopCurrentMusic();
	}

	@Override
	public void render(float delta) {
		
		ScreenUtils.clear(0, 0, 0, 1);
		
		if (!isMusicLoaded && assetManager.isLoaded(AudioType.PEACEFUL.getFilePath())) {
			isMusicLoaded = true;
			audioManager.playAudio(AudioType.PEACEFUL);
		}
		
		if (assetManager.update()) {
			((LoadingUI) screenUI).setPressButton();
		}
		
//		((LoadingUI) screenUI).setProgress(assetManager.getProgress());
		
	}

	@Override
	protected Table getscreenUI(Skin skin) {
		return new LoadingUI(skin);
	}

	@Override
	public void keyPressed(InputManager manager, GameKey gameKey) {
		game.setScreen(ScreenType.GAME);
		
	}

	@Override
	public void keyUp(InputManager manager, GameKey gameKey) {
		// TODO Auto-generated method stub
		
	}
}
