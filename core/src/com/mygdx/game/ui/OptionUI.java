package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.CoreGame;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.screens.OptionScreen;
public class OptionUI extends Table {
    private final CheckBox soundCheckBox;
    private final CoreGame game;

    public OptionUI(Skin skin, CoreGame game) {
        super(skin);
        this.game = game;
        setFillParent(true);

        soundCheckBox = new CheckBox("Sound", skin);
        soundCheckBox.setChecked(game.isSoundEnabled());
        soundCheckBox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean soundEnabled = soundCheckBox.isChecked();
                game.setSoundEnabled(soundEnabled);
            }
        });

        add(soundCheckBox).padBottom(20).row();

        setDebug(false, false);
    }
}
