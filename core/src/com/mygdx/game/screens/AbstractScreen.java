package com.mygdx.game.screens;

import box2dLight.RayHandler;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.CoreGame;
import com.mygdx.game.audio.AudioManager;
import com.mygdx.game.input.KeyInputListener;
import com.mygdx.game.map.MapManager;
import com.mygdx.game.input.InputManager;

public abstract class AbstractScreen<T extends Table> implements Screen, KeyInputListener {
	
	protected final CoreGame game;
	protected final FitViewport viewPort;
	protected final World world;
	protected final Box2DDebugRenderer box2DDebugRenderer;
	protected final RayHandler rayHandler;
	protected final Stage stage;
	protected T screenUI;
	protected InputManager inputManager;
	protected AudioManager audioManager;
	protected MapManager mapManager;
	
	public AbstractScreen(final CoreGame context) {
		this.game = context;
		viewPort = context.getScreenViewport();
		world = context.getWorld();
		rayHandler = context.getRayHandler();
		box2DDebugRenderer = context.getBox2DDebugRenderer();
		inputManager = game.getInputManager();
		mapManager = game.getMapManager();
		
		stage = context.getStage();
		
		audioManager = context.getAudioManager();
	}
	
	protected abstract Table getscreenUI(final Skin skin, CoreGame game);
	
	@Override
	public void resize(int width, int height) {
		viewPort.update(width, height);
		stage.getViewport().update(width, height, true);
		rayHandler.useCustomViewport(viewPort.getScreenX(), viewPort.getScreenY(), viewPort.getScreenWidth(), viewPort.getScreenHeight());

	}

	@Override
	public void show() {
		inputManager.addInputListener(this);
		stage.addActor(screenUI);
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		inputManager.removeinputListener(this);
		stage.getRoot().removeActor(screenUI);
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		
	}
}
