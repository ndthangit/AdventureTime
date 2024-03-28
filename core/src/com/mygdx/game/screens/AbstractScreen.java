package com.mygdx.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.CoreGame;
import com.mygdx.game.audio.AudioManager;
import com.mygdx.game.input.KeyInputListener;
import com.mygdx.game.input.InputManager;

public abstract class AbstractScreen<T extends Table> implements Screen, KeyInputListener {
	
	protected final CoreGame game;
	protected final FitViewport viewPort;
	protected final World world;
	protected final Box2DDebugRenderer box2DDebugRenderer;
	protected final Stage stage;
	protected T screenUI;
	protected InputManager inputManager;
	protected AudioManager audioManager;
	
	public AbstractScreen(final CoreGame context, Skin skin) {
		this.game = context;
		viewPort = context.getScreenViewport();
		world = context.getWorld();
		box2DDebugRenderer = context.getBox2DDebugRenderer();
		inputManager = game.getInputManager();
		
		
		stage = context.getStage();
		this.screenUI = (T) getscreenUI(skin);
		audioManager = context.getAudioManager();
	}
	
	protected abstract Table getscreenUI(final Skin skin);	
	
	@Override
	public void resize(int width, int height) {
		viewPort.update(width, height);
		stage.getViewport().update(width, height, true);
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
