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
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.GameObjectComponent;
import com.mygdx.game.entity.component.PlayerComponent;

public class GameUI extends Table{
	private final CoreGame game;
	private final Image heartImage[];
	private final Table heartTable;
	private final Entity player;
	private final PlayerComponent playerCmp;
	
	public GameUI(Skin skin, CoreGame game) {
		super(skin);
		this.game = game;
		player = game.getEcsEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
		playerCmp = player.getComponent(PlayerComponent.class);
		heartTable = new Table();
		setFillParent(true);
		heartImage = new Image[ (int) playerCmp.maxLife/4];
		createHeart();
	}

	public void createHeart() {
		int i;
		for (i=4; i<=playerCmp.maxLife; i += 4) {
			heartImage[(int)(i/4)-1] = new Image(setHeartImage(4));
			heartImage[(int)(i/4)-1].setScale(3);
		}

		for (i=4; i<=playerCmp.life; i+= 4) {
			heartImage[(int)(i/4)-1] = new Image(setHeartImage(0));
			heartImage[(int)(i/4)-1].setScale(3);
		}

		if (i<=playerCmp.maxLife) {
			heartImage[(int)(i/4)-1] = new Image(setHeartImage(i-playerCmp.life));
			heartImage[(int)(i/4)-1].setScale(3);
			i += 4;
		}
		for (i=0; i<playerCmp.maxLife/4; i++) {
			heartTable.add(heartImage[i]).expand(false, true).top().padTop(50).left().padLeft(20).padRight(20);
		}
		add(heartTable).expand().left().top();
	}
	
	public void updateHeart() {
		int i;
		heartTable.clearChildren();
		for (i=4; i<=playerCmp.maxLife; i += 4) {
			heartImage[(int)(i/4)-1] = new Image(setHeartImage(4));
			heartImage[(int)(i/4)-1].setScale(3);
		}

		for (i=4; i<=playerCmp.life; i+= 4) {
			heartImage[(int)(i/4)-1] = new Image(setHeartImage(0));
			heartImage[(int)(i/4)-1].setScale(3);
		}

		if (i<=playerCmp.maxLife) {
			heartImage[(int)(i/4)-1] = new Image(setHeartImage(i-playerCmp.life));
			heartImage[(int)(i/4)-1].setScale(3);
			i += 4;
		}

		for (i=0; i<playerCmp.maxLife/4; i++) {
			heartTable.add(heartImage[i]).expand(false, true).top().padTop(50).left().padLeft(20).padRight(20);
		}
	}
	
	private Texture setHeartImage(int i) {
		Texture texture = new Texture(Gdx.files.internal("HUD/Heart/Heart-" + i + ".png"));
		return texture;
	}
	
}
;