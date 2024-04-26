package com.mygdx.game.ui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.CoreGame;

public class MenuUI extends Table implements ApplicationListener {
    private final TextButton startButton;
    private final TextButton optionButton;
    private final TextButton quitButton;
    private Stage stage;
    private Skin skin;
    private Table table;
    private LoadingUI loadingUI;
    public MenuUI(Skin skin, CoreGame game) {
        super(skin);
        setFillParent(true);
        //Label titleLabel = new Label("Main Menu", skin, "title");
        //add(titleLabel).colspan(2).padBottom(50).row();
//Tạo title cho game nhưng hiện tại đang bị lỗi
        startButton = new TextButton("START", skin, "huge");
        startButton.getLabel().setWrap(true);
        startButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.startGame(); // Gọi phương thức startGame() trong class CoreGame
                return true;
            }
        });

        optionButton = new TextButton("OPTION", skin, "huge");
        optionButton.getLabel().setWrap(true);
        optionButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.optionGame(); // Gọi phương thức optionGame() trong class CoreGame
                return true;
            }
        });

        quitButton = new TextButton("QUIT", skin, "huge");
        quitButton.getLabel().setWrap(true);
        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.quitGame(); // Gọi phương thức quitGame() trong class CoreGame
                return true;
            }
        });
        add(startButton).expand().fillX().row();
        add(optionButton).expand().fillX().row();
        add(quitButton).expand().fillX().row();
        setDebug(false, false);
    }


    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }



    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}