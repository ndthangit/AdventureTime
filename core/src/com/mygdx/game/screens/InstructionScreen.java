package com.mygdx.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.CoreGame;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.ui.InstructionUI;

public class InstructionScreen extends AbstractScreen<InstructionUI>{
    private final Stage stage;
    Skin skin;
    public InstructionScreen(CoreGame game) {
        super(game);
        stage = game.getStage();
        this.screenUI = (InstructionUI) getscreenUI(game.getLoadAsset().getGameSkin(), game);
        this.mapManager = game.getMapManager();
    }

    public void dispose() {
        stage.dispose();
    }
    @Override
    protected Table getscreenUI(Skin skin, CoreGame game) {
        return new InstructionUI(skin, this.game);
    }

    @Override
    public void keyPressed(InputManager manager, GameKey gameKey) {

    }

    @Override
    public void keyUp(InputManager manager, GameKey gameKey) {

    }
}
