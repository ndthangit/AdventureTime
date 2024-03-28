package com.mygdx.game.input;

public interface KeyInputListener {
	void keyPressed(final InputManager manager, final GameKey gameKey);
	void keyUp(final InputManager manager, final GameKey gameKey);
}
