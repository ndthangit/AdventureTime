package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
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
        instruct.add(Title).expandX().fillX().padBottom(50).top().row();
        TextButton returnButton = new TextButton("RETURN", skin, "huge");
        // Thêm hình ảnh vào instruct table
        Texture texture = new Texture(Gdx.files.internal("instruction game IT3070.png"));
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        Image image = new Image(drawable);
        image.setScaling(Scaling.contain);
        instruct.add(image).width(400).height(300).row();
        returnButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(ScreenType.OPTION);
                return true;
            }
        });
        instruct.add(returnButton).padTop(50).row();
    }
}
