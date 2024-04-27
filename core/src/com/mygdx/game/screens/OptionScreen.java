package com.mygdx.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.CoreGame;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.ui.LoadingUI;
import com.mygdx.game.ui.MenuUI;
import com.mygdx.game.ui.OptionUI;
public class OptionScreen extends AbstractScreen<OptionUI>{
    private final Stage stage;
    private final OptionUI optionUI;
    Skin skin;
    public OptionScreen(Skin skin, CoreGame game, Viewport viewport) {
        super(game);
        stage = new Stage(viewport);
        optionUI = new OptionUI(skin, game);
        Table rootTable = new Table(skin);
        rootTable.setFillParent(true);
        rootTable.add(optionUI).center();
        stage.addActor(rootTable);
        this.screenUI = (OptionUI) getscreenUI(game.getLoadAsset().getGameSkin(), game);
    }
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
    @Override
    protected Table getscreenUI(Skin skin, CoreGame game) {
        return new OptionUI(skin, this.game);
    }

    @Override
    public void keyPressed(InputManager manager, GameKey gameKey) {

    }

    @Override
    public void keyUp(InputManager manager, GameKey gameKey) {

    }
}
