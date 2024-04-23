package com.mygdx.game.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.player.PlayerType;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.MapListener;
import com.mygdx.game.map.MapType;
import com.mygdx.game.ui.GameUI;

public class MainGameScreen extends AbstractScreen<GameUI> implements MapListener {
	
	public MainGameScreen (CoreGame game) {
		super(game);
		mapManager = game.getMapManager();
		mapManager.addMapListener(this);
		mapManager.setMap();

		this.screenUI = (GameUI) getscreenUI(game.getLoadAsset().getGameSkin(), game);
		game.setGameUI(screenUI);

	}

	@Override
	public void show() {
		super.show();
//		mapRender.setMap(assetManager.get(mapManager.getCurrentMapType().getFilePath(), TiledMap.class));
	}
	
	@Override
	public void render(float delta) {
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
		if (gameKey == GameKey.CHANGE_MAP_1) {
			mapManager.setNextMapType(MapType.MAP_1);
			mapManager.destroyMap();
			game.setScreen(ScreenType.LOAD);
		}
		else if (gameKey == GameKey.CHANGE_MAP_2) {
			mapManager.setNextMapType(MapType.MAP_2);
			mapManager.destroyMap();
			game.setScreen(ScreenType.LOAD);
		}
	}

	@Override
	public void keyUp(InputManager manager, GameKey gameKey) {
		
	}

	@Override
	public void mapChange(Map map) {
		
	}
}
