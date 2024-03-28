package com.mygdx.game.input;

import com.badlogic.gdx.Input;

public enum GameKey {
	UP(Input.Keys.W, Input.Keys.UP), 
	DOWN(Input.Keys.S, Input.Keys.DOWN), 
	LEFT(Input.Keys.A, Input.Keys.LEFT), 
	RIGHT(Input.Keys.D, Input.Keys.RIGHT), 
	SELECT(Input.Keys.U, Input.Keys.NUM_4), 
	ATTACK(Input.Keys.J, Input.Keys.NUM_1), 
	BACK(Input.Keys.O, Input.Keys.NUM_6);
	
	final int[] keyCode;
	
	GameKey(final int... keyCode) {
		this.keyCode = keyCode;
	}
}
