package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.CoreGame;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.character.player.PlayerType;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.items.weapon.Weapon;
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
					break;
				case TOWN:
					audioManager.playAudio(AudioType.REVELATION);
					break;
				case SAMU_BOSS:
					audioManager.playAudio(AudioType.TEMPLE);
					break;
				case CAVE:
					audioManager.playAudio(AudioType.DUNGEON);
					break;
				case ABANDONVIL:
					audioManager.playAudio(AudioType.ABANDON);
					break;
				case SPIRIT_BOSS:
					audioManager.playAudio(AudioType.SPIRIT);
					break;
			} // them am thanh map
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
		PlayerComponent playerCmp = game.getGameUI().playerCmp;

		switch (gameKey) {
			case USE_ITEM_1:
				playerCmp.useItem(0);
				break;
			case USE_ITEM_2:
				playerCmp.useItem(1);
				break;
			case USE_ITEM_3:
				playerCmp.useItem(2);
				break;
			case USE_ITEM_4:
				playerCmp.useItem(3);
				break;
			// error
            case SWAP_WEAPON:
					playerCmp.indWeapon = (playerCmp.indWeapon + 1) % playerCmp.weaponList.size;
					playerCmp.weapon = playerCmp.weaponList.get(playerCmp.indWeapon);
                break;
		}
		game.getGameUI().updateHeart();
		game.getGameUI().updateNumTable();
		game.getGameUI().updateBag();
	}

	@Override
	public void keyUp(InputManager manager, GameKey gameKey) {
		
	}

	@Override
	public void mapChange(Map map) {
		
	}
}
