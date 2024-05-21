package com.mygdx.game.screens;

import com.badlogic.gdx.Screen;

public enum ScreenType {
	GAME (MainGameScreen.class),
	LOAD (LoadingScreen.class),
	MENU (MainMenuScreen.class),
	OPTION (OptionScreen.class),
	DEAD (DeadScreen.class),
	INSTRUCT(InstructionScreen.class),
	END (EndScreen.class),;
	
	private final Class<? extends AbstractScreen> screenClass;
	
	ScreenType (Class<? extends AbstractScreen> screenClass) {
		this.screenClass = screenClass;
	}
	
	public Class<? extends Screen> getScreenClass() {
		return screenClass;
	}
}
