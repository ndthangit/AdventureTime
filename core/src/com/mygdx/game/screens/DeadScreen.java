package com.mygdx.game.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.CoreGame;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.ui.DeadUI;
import com.mygdx.game.ui.OptionUI;

public class DeadScreen extends AbstractScreen<DeadUI> {
    private final Stage stage;
    Skin skin;
    CoreGame game;
    private final AssetManager assetManager;
    public DeadScreen(CoreGame game) {
        super(game);
        this.game = game;
        stage = game.getStage();
        assetManager = game.getAssetManager();
        this.screenUI = (DeadUI)getscreenUI(game.getLoadAsset().getGameSkin(), game);
        this.mapManager = game.getMapManager();
    }

    @Override
    public void show() {
        super.show();
        audioManager.stopCurrentMusic();
        audioManager.playAudio(AudioType.GAMEOVER2);
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
