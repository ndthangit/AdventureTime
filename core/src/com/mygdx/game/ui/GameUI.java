package com.mygdx.game.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.component.PlayerComponent;

public class GameUI extends Table{
	private final CoreGame game;
	private final Image heartImage[];
	private final Table heartTable;
	public int life;
	public int maxlife;
	
	public GameUI(Skin skin, CoreGame game) {
		super(skin);
		this.game = game;
		life = 12;
		maxlife = 12;
		heartTable = new Table();
		setFillParent(true);
		heartImage = new Image[3];
		updateHeart();
	}
	
	public void updateHeart() {
		int i;
		for (i=4; i<=maxlife; i += 4) {
			heartImage[(int)(i/4)-1] = new Image(setHeartImage(4));
			heartImage[(int)(i/4)-1].setScale(3);
		}
		
		for (i=4; i<=life; i+= 4) {
			heartImage[(int)(i/4)-1] = new Image(setHeartImage(0));
			heartImage[(int)(i/4)-1].setScale(3);	
		}
		
		if (i<=maxlife) {
			heartImage[(int)(i/4)-1] = new Image(setHeartImage(i-life));
			heartImage[(int)(i/4)-1].setScale(3);
			i += 4;
		}
		
		for (i=0; i<3; i++) {
			heartTable.add(heartImage[i]).expand(false, true).top().padTop(50).left().padLeft(20).padRight(20);
		}
		add(heartTable).expand().left().top();		
	}
	
	private Texture setHeartImage(int i) {
		Texture texture = new Texture(Gdx.files.internal("HUD/Heart/Heart-" + i + ".png"));
		return texture;
	}
	
}
;