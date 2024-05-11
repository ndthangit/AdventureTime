package com.mygdx.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.CoreGame;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.ui.MenuUI;
import com.mygdx.game.ui.OptionUI;
public class OptionScreen extends AbstractScreen<OptionUI>{
    private final Stage stage;
    Skin skin;
    public OptionScreen(CoreGame game) {
        super(game);
        stage = game.getStage();
        this.screenUI = (OptionUI) getscreenUI(game.getLoadAsset().getGameSkin(), game);
        this.mapManager = game.getMapManager();
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
