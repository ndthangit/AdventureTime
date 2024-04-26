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

public class MenuUI extends Table  {
    private final Skin skin;
    private final CoreGame game;
    private Table menu;
    private Table screen;
    private LoadingUI loadingUI;
    public MenuUI(Skin skin, CoreGame game) {
        super(skin);
        setFillParent(true);
        this.skin = skin;
        this.game = game;
        //Label titleLabel = new Label("Main Menu", skin, "title");
        //add(titleLabel).colspan(2).padBottom(50).row();
//Tạo title cho game nhưng hiện tại đang bị lỗi
        menu = new Table();
        screen = new Table();
        createScreen();
        add(screen).expand().fill();
        setDebug(false, false);
    }

    void createScreen() {
        screen.clearChildren();
        createTitle();
        screen.add(menu).expand().fillX().bottom().padBottom(100).row();
        createMenu();
    }

    void createTitle() {
        TextButton title = new TextButton("ADVENTURE TIME", skin, "huge");
        screen.add(title).expandX().fillX().padTop(200).top().row();
    }

    void createMenu() {
        menu.clearChildren();
        createStartGame();
        createOptionGame();
        createQuitGame();
    }

    void createStartGame() {
        TextButton startButton = new TextButton("START", skin, "huge");
        startButton.getLabel().setWrap(true);
        startButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            game.startGame(); // Gọi phương thức startGame() trong class CoreGame
            return true;
            }
        });
        menu.add(startButton).expand().fillX().padBottom(50).row();
    }
    void createOptionGame() {
        TextButton optionButton = new TextButton("OPTION", skin, "huge");
        optionButton.getLabel().setWrap(true);
        optionButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.optionGame(); // Gọi phương thức optionGame() trong class CoreGame
                return true;
            }
        });
        menu.add(optionButton).expand().fillX().padBottom(50).row();
    }

    void createQuitGame() {
        TextButton quitButton = new TextButton("QUIT", skin, "huge");
        quitButton.getLabel().setWrap(true);
        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.quitGame(); // Gọi phương thức quitGame() trong class CoreGame
                return true;
            }
        });
        menu.add(quitButton).expand().fillX().padBottom(50).row();
    }
}