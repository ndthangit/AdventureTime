package com.mygdx.game.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.CoreGame;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.character.player.PlayerType;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.MapListener;
import com.mygdx.game.map.MapType;
import com.mygdx.game.ui.GameUI;
import com.mygdx.game.view.GameRenderer;

public class MainGameScreen extends AbstractScreen<GameUI> implements MapListener {

	private boolean isMusicLoaded;

	public MainGameScreen (CoreGame game) {
		super(game);
		mapManager = game.getMapManager();
		mapManager.addMapListener(this);
		mapManager.setMap();
		isMusicLoaded = false;
		this.screenUI = (GameUI) getscreenUI(game.getLoadAsset().getGameSkin(), game);
		game.setGameUI(screenUI);

	}

	@Override
	public void show() {
		super.show();
		isMusicLoaded = false;
		screenUI.updateHeart();
//		mapRender.setMap(assetManager.get(mapManager.getCurrentMapType().getFilePath(), TiledMap.class));
	}
	
	@Override
	public void render(float delta) {
		if (!isMusicLoaded && game.getAssetManager().isFinished()) {
			switch (game.getMapManager().getCurrentMapType()) {
				case DOJO:
					isMusicLoaded = true;
					audioManager.playAudio(AudioType.LOL);
					break;
				case BEGIN_FOREST:
					audioManager.playAudio(AudioType.LOL1);
			}
		}
	}
	
	

	@Override
	public void dispose() {
	}

	@Override
	protected GameUI getscreenUI(Skin skin, CoreGame game) {
		return new GameUI(skin, this.game);
	}

	@Override
	public void keyPressed(InputManager manager, GameKey gameKey) {
		if (gameKey==GameKey.ATTACK) {
			if (game.getAssetManager().isLoaded(AudioType.SWORD.getFilePath())) {
				audioManager.playAudio(AudioType.SWORD);
			}
		}
	}

	@Override
	public void keyUp(InputManager manager, GameKey gameKey) {
		
	}

	@Override
	public void mapChange(Map map) {
		
	}
}
