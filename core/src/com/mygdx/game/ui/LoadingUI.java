package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.CoreGame;

public class LoadingUI extends Table{
	private final ProgressBar progressBar;
	private final TextButton pressAnyKeyButton;
	private final TextButton textButton;
	
	public LoadingUI(Skin skin, CoreGame game) {
		super(skin);
		setFillParent(true);		
		progressBar = null;
//		progressBar = new ProgressBar(0, 1, 0.01f, false, skin, "default");
//		progressBar.setAnimateDuration(1);
		textButton = new TextButton("LOADING...", skin, "huge");
		textButton.getLabel().setWrap(true);
		
		pressAnyKeyButton = new TextButton("PRESS ANY KEY", skin, "huge");
		pressAnyKeyButton.getLabel().setWrap(true);
		pressAnyKeyButton.setVisible(false);
		
		add(textButton).expand().fillX().row();
		add(pressAnyKeyButton).expand().fillX().row();
//		add(progressBar).expand().fill().bottom().padBottom(20);	
		setDebug(false, false);
	}
	
	public void setProgress(final float progress) {
		progressBar.setValue(progress);
		
	}
	
	public void setPressButton() {
		if (!pressAnyKeyButton.isVisible()) {
			pressAnyKeyButton.setVisible(true);
			pressAnyKeyButton.setColor(1, 1, 1, 0);
			pressAnyKeyButton.addAction(Actions.forever(Actions.sequence(Actions.alpha(1, 1), Actions.alpha(0, 1))));
			textButton.setVisible(false);
		}
	}
}
