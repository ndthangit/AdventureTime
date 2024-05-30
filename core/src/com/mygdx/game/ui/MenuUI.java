package com.mygdx.game.ui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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

        menu = new Table();

        screen = new Table();
        screen.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("OOP_Project.png")))));
        createScreen();
        add(screen).expand().fill();
        setDebug(false, false);
    }

    void createScreen() {
        screen.clearChildren();
        createTitle();
//        screen.add(bg).expand().fill();
        screen.add(menu).expand().fillX().bottom().padBottom(100).row();
        createMenu();
    }

    void createTitle() {
        TextButton title = new TextButton("ADVENTURE TIME", skin, "black huge");
        screen.add(title).expandX().fillX().padTop(200).top().row();
    }

    void createMenu() {
        menu.clearChildren();
        createStartGame();
        createOptionGame();
        createQuitGame();
    }

    void createStartGame() {
        TextButton startButton = new TextButton("START", skin, "black huge");
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
        TextButton optionButton = new TextButton("OPTION", skin, "black huge");
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
        TextButton quitButton = new TextButton("QUIT", skin, "black huge");
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