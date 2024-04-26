package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.CoreGame;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.ui.MenuUI;

public class MainMenuScreen extends AbstractScreen<MenuUI>{

	private SpriteBatch batch;
	private Sprite sprite;
	Texture texture;
	public MainMenuScreen(CoreGame game) {
		super(game);
		this.screenUI = (MenuUI) getscreenUI(game.getLoadAsset().getGameSkin(), game);
		this.mapManager = game.getMapManager();

		batch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("HUD/Shuriken.png"));
		sprite = new Sprite(texture, 20, 20, 50, 50);
		sprite.setPosition(0, 0);
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
//		ScreenUtils.clear(0, 0, 0, 1);
//		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
//		batch.begin();
//		batch.draw(sprite, 10, 10);
//		batch.end();
	}
//public MainMenuScreen(CoreGame game) {
//		
//		super(game);
//		
//		this.assetManager = game.getAssetManager();
//		assetManager.load("maps/Demo-abandon-village-map.tmx", TiledMap.class);
//	}
//
//	@Override
//	public void render(float delta) {
//		
//		ScreenUtils.clear(0, 0, 0, 1);
//	}

	@Override
	protected Table getscreenUI(Skin skin, CoreGame game) {
		// TODO Auto-generated method stub
		return new MenuUI(skin, this.game);
	}

	@Override
	public void keyPressed(InputManager manager, GameKey gameKey) {
		if (gameKey == GameKey.DOWN) {
			mapManager.setMap();
			game.setScreen(ScreenType.GAME);
		}
		else if (gameKey == GameKey.SELECT) {

		}
	}

	@Override
	public void keyUp(InputManager manager, GameKey gameKey) {

	}
}
