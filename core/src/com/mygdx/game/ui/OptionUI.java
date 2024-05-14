package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.CoreGame;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.screens.ScreenType;

public class OptionUI extends Table {
    private final Table menu;
    private final CoreGame game;
    private final Skin skin;

    public OptionUI(Skin skin, CoreGame game) {
        super(skin);
        this.game = game;
        this.skin = skin;
        setFillParent(true);
        menu = new Table();
        add(menu).expand().fill();
        createMenu();
        setDebug(false, false);
    }

    private void createMenu() {
        menu.clearChildren();
        TextButton Title = new TextButton("SETTINGS", skin, "huge");
        TextButton InstructionButton = new TextButton("INSTRUCTION", skin, "huge");
        InstructionButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(ScreenType.INSTRUCT);
                return true;
            }
        });
        TextButton labelSwitchSound = new TextButton("MUSIC: ", skin, "huge");
        TextButton switchSound;
        if (game.isSoundEnabled()) {
            switchSound = new TextButton("ON", skin, "huge");
        }
        else {
            switchSound = new TextButton("OFF", skin, "huge");
        }
        switchSound.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.switchSound(); // Gọi phương thức startGame() trong class CoreGame
                createMenu();
                return true;
            }
        });
        TextButton returnButton = new TextButton("RETURN", skin, "huge");
        returnButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(ScreenType.MENU);
                return true;
            }
        });
        menu.add(Title).padBottom(200).row();
        menu.add(InstructionButton).padBottom(50).row();
        menu.add(labelSwitchSound);
        menu.add(switchSound).row();
        menu.add(returnButton).padTop(50).row();
    }

}
