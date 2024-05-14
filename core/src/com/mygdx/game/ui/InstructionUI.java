package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.CoreGame;
import com.mygdx.game.screens.ScreenType;

public class InstructionUI extends Table {
    private final Table instruct;
    private final CoreGame game;
    private final Skin skin;
    public InstructionUI(Skin skin, CoreGame game) {
        super(skin);
        this.game = game;
        this.skin = skin;
        setFillParent(true);
        instruct = new Table();
        add(instruct).expand().fill();
        createMenu();
        setDebug(false, false);
    }
    private void createMenu() {
        instruct.clearChildren();
        TextButton Title = new TextButton("GETTING STARTED", skin, "huge");
        instruct.add(Title).expandX().fillX().padBottom(200).top().row();
    }
}
