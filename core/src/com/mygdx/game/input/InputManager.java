package com.mygdx.game.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

public class InputManager implements InputProcessor {
	private final GameKey[] keyMapping;
	private final boolean[] keyState;
	private final Array<KeyInputListener> listeners;

	public InputManager() {
		super();
		this.keyMapping = new GameKey[256];
		
		for(final GameKey gameKey: GameKey.values()) {
			for (final int code : gameKey.keyCode) {
				keyMapping[code] = gameKey;
			}
		}
		this.keyState = new boolean[GameKey.values().length];
		listeners = new Array<KeyInputListener>();
	}
	
	public void addInputListener(final KeyInputListener listener) {
		listeners.add(listener);
	}
	
	public void removeinputListener(final KeyInputListener listener) {
		listeners.removeValue(listener, true);
	}
	@Override
	public boolean keyDown(int keycode) {
		final GameKey gameKey = keyMapping[keycode];
		if (gameKey == null) {
			return false;
		}
		notifyKeyDown(gameKey);
		
		return false;
		
	}

	public void notifyKeyDown(GameKey gameKey) {
		keyState[gameKey.ordinal()] = true;
		for (final KeyInputListener listener: listeners) {
			listener.keyPressed(this, gameKey);
		}
		
	}

	@Override
	public boolean keyUp(int keycode) {
		final GameKey gameKey = keyMapping[keycode];
		if (gameKey == null) {
			return false;
		}
		notifyKeyUp(gameKey);
		
		return false;
		
	}
	
	public void notifyKeyUp(GameKey gameKey) {
		keyState[gameKey.ordinal()] = false;
		for (final KeyInputListener listener: listeners) {
			listener.keyUp(this, gameKey);
		}
	}
	
	public boolean isKeyPressed(final GameKey gameKey) {
		return keyState[gameKey.ordinal()];
	}
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}

}
