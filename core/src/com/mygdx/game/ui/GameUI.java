package com.mygdx.game.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.GameObjectComponent;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.items.Item;
import com.mygdx.game.items.food.Food;
import com.mygdx.game.items.food.FoodType;
import com.mygdx.game.screens.ScreenType;

public class GameUI extends Table {
	private final CoreGame game;
	private final Image heartImage[];
	private final Table heartTable;
	private final Entity player;
	private final PlayerComponent playerCmp;
	private final Table bagTable;
	private final ImageButton[] bagItems;
	private final Stack[] stacks;
	
	public GameUI(Skin skin, CoreGame game) {
		super(skin);
		this.game = game;
		player = game.getEcsEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        this.bagTable = new Table(skin);
        this.bagItems = new ImageButton[4];
		this.stacks = new Stack[4];
        playerCmp = player.getComponent(PlayerComponent.class);
		heartTable = new Table();
		setFillParent(true);
		heartImage = new Image[ (int) playerCmp.maxLife/4];
		bagTable.setFillParent(true);
		createHeart();
		createBag();
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

		if(playerCmp.life<=0){
			game.setScreen(ScreenType.DEAD);
		}
	}
	
	private Texture setHeartImage(int i) {
		Texture texture = new Texture(Gdx.files.internal("HUD/Heart/Heart-" + i + ".png"));
		return texture;

	}

	public void createBag() {
		bagTable.clearChildren();

		for (int i = 0; i < 4; i++) {
			stacks[i] = new Stack();
			Image BG = new Image(new Texture(Gdx.files.internal("Backgrounds/BackGround1.png")));
			stacks[i].add(BG);
			bagTable.add(stacks[i]).center().pad(0).padLeft(10).row();
			bagTable.row();
		}

		this.addActor(bagTable);

		// Set table position
		bagTable.setPosition(0, (Gdx.graphics.getHeight() / 2) - 200);
		bagTable.bottom().left();
//        bagTable.debug();
	}

	public void updateBag() {
		createBag();
		TextureAtlas atlas = null;
		for (int i = 0; i < 4; i++) {
			if (playerCmp.inventory[i] == null) {continue;}
			if (atlas == null) {
				atlas = new TextureAtlas(playerCmp.inventory[i].atlasPath);
			}
			TextureAtlas.AtlasRegion region = atlas.findRegion(playerCmp.inventory[i].key);
			Drawable drawable = new TextureRegionDrawable(region);
			drawable.setMinHeight(80);
			drawable.setMinWidth(80);
			bagItems[i]= new ImageButton(drawable);
			stacks[i].add(bagItems[i]);
		}
	}
}
;