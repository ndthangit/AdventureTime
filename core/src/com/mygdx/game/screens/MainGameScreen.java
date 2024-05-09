package com.mygdx.game.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.CoreGame;
import com.mygdx.game.WorldContactListener;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.character.player.PlayerType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.DoorLayerComponent;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.MapListener;
import com.mygdx.game.map.MapType;
import com.mygdx.game.ui.GameUI;
import com.mygdx.game.view.GameRenderer;

public class MainGameScreen extends AbstractScreen<GameUI> implements MapListener {
	private final AssetManager assetManager;
	private boolean isMusicLoaded;
	private final PlayerComponent playerCmp;
	private final Entity player;
	private boolean hasDoor;
	private boolean hasPlayer;
	public MainGameScreen (CoreGame game) {
		super(game);
		player = game.getEcsEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
		this.assetManager = game.getAssetManager();
		mapManager = game.getMapManager();
		mapManager.addMapListener(this);
		mapManager.setMap();
		this.screenUI = (GameUI) getscreenUI(game.getLoadAsset().getGameSkin(), game);
		game.setGameUI(screenUI);
		this.playerCmp = player.getComponent(PlayerComponent.class);
		hasDoor=game.getWorldContactListener().isHasDoor();
		hasPlayer=game.getWorldContactListener().isHasPlayer();
	}

	@Override
	public void show() {
		super.show();
//		mapRender.setMap(assetManager.get(mapManager.getCurrentMapType().getFilePath(), TiledMap.class));
	}
	
	@Override
	public void render(float delta) {
		if (!isMusicLoaded && assetManager.isFinished()) {
			switch (game.getMapManager().getCurrentMapType()) {
				case DOJO:
					isMusicLoaded = true;
					audioManager.playAudio(AudioType.LOL);
					break;
				case BEGIN_FOREST:
					isMusicLoaded = true;
					audioManager.playAudio(AudioType.PEACEFUL);
					break;
			}
		}
		if (playerCmp.life == 0) {
			if (assetManager.isLoaded(AudioType.GAMEOVER2.getFilePath())) {
				audioManager.playAudio(AudioType.GAMEOVER2);
				playerCmp.life=1;
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
			if (assetManager.isLoaded(AudioType.SWORD.getFilePath())) {
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
