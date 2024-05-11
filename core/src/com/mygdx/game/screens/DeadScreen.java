package com.mygdx.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.CoreGame;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.ui.DeadUI;
import com.mygdx.game.ui.OptionUI;

public class DeadScreen extends AbstractScreen<DeadUI> {
    private final Stage stage;
    Skin skin;
    public DeadScreen(CoreGame game) {
        super(game);
        stage = game.getStage();
        this.screenUI = (DeadUI)getscreenUI(game.getLoadAsset().getGameSkin(), game);
        this.mapManager = game.getMapManager();
    }

    @Override
    protected Table getscreenUI(Skin skin, CoreGame game) {
        return new DeadUI(skin, this.game);
    }

    @Override
    public void keyPressed(InputManager manager, GameKey gameKey) {

    }

    @Override
    public void keyUp(InputManager manager, GameKey gameKey) {

    }
}
