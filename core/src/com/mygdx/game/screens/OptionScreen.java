package com.mygdx.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.CoreGame;
import com.mygdx.game.ui.OptionUI;

public class OptionScreen {
    private final Stage stage;
    private final OptionUI optionUI;

    public OptionScreen(Skin skin, CoreGame game, Viewport viewport) {
        stage = new Stage(viewport);
        optionUI = new OptionUI(skin, game);

        Table rootTable = new Table(skin);
        rootTable.setFillParent(true);
        rootTable.add(optionUI).center();

        stage.addActor(rootTable);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
