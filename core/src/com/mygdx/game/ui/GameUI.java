package com.mygdx.game.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.boss.Boss;
import com.mygdx.game.entity.component.BossComponent;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.screens.ScreenType;

public class GameUI extends Table {
	private final CoreGame game;
	private final Table heartBG;
	private final Stack faceSet;
	private final Image heartImage[];
	private final Array<Image> heart;
	private final Table heartTable;
	private final Entity player;
	public final PlayerComponent playerCmp;
	private final Table bagTable;
	private final ImageButton[] bagItems;
	private final Stack[] stacks;

	private final Table numTable;
	private final Table lifeBar;
	
	public GameUI(Skin skin, CoreGame game) {
		super(skin);
		this.game = game;
		player = game.getEcsEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
		playerCmp = player.getComponent(PlayerComponent.class);

		this.faceSet = new Stack();
		this.heartBG = new Table(skin);
		this.bagTable = new Table(skin);
		this.lifeBar = new Table(skin);
		this.bagItems = new ImageButton[5];
		this.stacks = new Stack[5];

		heartTable = new Table();
		heartImage = new Image[ (int) playerCmp.maxLife/4];
		heart = new Array<Image>();
		setFillParent(true);
		this.numTable = new Table();
		setDebug(false, false);
		bagTable.setFillParent(true);
		createHeart();
		createBag();
		createNumTable();
	}

	public Entity getPlayer() {
		return player;
	}

	public void createHeart() {
		heartBG.setFillParent(true);
		Image heartBGImage = new Image(new Texture(Gdx.files.internal("Backgrounds/FacesetBox10.png")));
		heartBG.add(heartBGImage);
		this.addActor(heartBG);
		heartBG.setPosition(-565, 305);
		heartTable.setFillParent(true);

		Image characterBG = new Image(new Texture(Gdx.files.internal("Backgrounds/FacesetBox1.png")));
		faceSet.add(characterBG);
		Image character = new Image(new Texture(Gdx.files.internal("Actor/Characters/BlackNinjaMage/Faceset.png")));
		faceSet.add(character);
//        stack.debug();
		heartTable.add(faceSet).pad(15);

		int i;
		for (i = 4; i <= playerCmp.maxLife; i += 4) {
			heart.add(new Image(setHeartImage(4)));
		}

		for (i = 4; i <= playerCmp.life; i += 4) {
			heart.set(((int)(i/4)) -1, new Image(setHeartImage(0)));
		}

		if (i <= playerCmp.maxLife) {
			heart.set(((int)(i/4)) -1, new Image(setHeartImage(i - playerCmp.life)));
			i += 4;
		}
		for (i = 0; i < playerCmp.maxLife / 4; i++) {
			heartTable.add(heart.get(i)).pad(10);
		}

		this.addActor(heartTable);
		heartTable.left().top().padLeft(30).padTop(10);
//        heartTable.debug();
	}


	public void updateHeart() {

		heartTable.clearChildren();

		heartTable.setFillParent(true);
		Stack stack = new Stack();
		Image characterBG = new Image(new Texture(Gdx.files.internal("Backgrounds/FacesetBox1.png")));
		stack.add(characterBG);
		Image character = new Image(new Texture(Gdx.files.internal("Actor/Characters/BlackNinjaMage/Faceset.png")));
		stack.add(character);
		heartTable.add(stack).pad(15);
		heart.clear();
		int i;

		for (i = 4; i <= playerCmp.maxLife; i += 4) {
			heart.add(new Image(setHeartImage(4)));
		}

		for (i = 4; i <= playerCmp.life; i += 4) {
			heart.set(((int)(i/4)) -1, new Image(setHeartImage(0)));
		}

		if (i <= playerCmp.maxLife) {
			heart.set(((int)(i/4)) -1, new Image(setHeartImage(i - playerCmp.life)));
			i += 4;
		}
		for (i = 0; i < playerCmp.maxLife / 4; i++) {
			heartTable.add(heart.get(i)).pad(10);
		}
		if (playerCmp.life <= 0) {
			game.setScreen(ScreenType.DEAD);
		}
	}

	private Drawable setHeartImage(int i) {
		TextureAtlas atlas = game.getAssetManager().get("HUD/HeartAtlasN.atlas", TextureAtlas.class);
		TextureAtlas.AtlasRegion region = atlas.findRegion("Heart-" + i);
		Drawable drawable = new TextureRegionDrawable(region);
		return drawable;
	}

	public void createBag() {
		bagTable.setFillParent(true);
		Image wpBG = new Image(new Texture(Gdx.files.internal("Backgrounds/wpBG2.png")));
		stacks[0] = new Stack();
		stacks[0].add(wpBG);
		TextureAtlas atlas = game.getAssetManager().get(playerCmp.weapon.atlasPath, TextureAtlas.class);
		TextureAtlas.AtlasRegion region = atlas.findRegion(playerCmp.weapon.key);

		Drawable drawable = new TextureRegionDrawable(region);
		bagItems[0] = new ImageButton(drawable);
		stacks[0].add(bagItems[0]);
		System.out.println("size : "+ stacks[0].getChildren().size);
		bagTable.add(stacks[0]).center().row();
		bagTable.row();

		for (int i = 1; i < 5; i++) {
			stacks[i] = new Stack();
			Image BG = new Image(new Texture(Gdx.files.internal("Backgrounds/BackGround1.png")));
			stacks[i].add(BG);
			bagTable.add(stacks[i]).center().pad(0).padLeft(10).row();
			bagTable.row();
		}
		this.addActor(bagTable);
		// Set table position
		bagTable.setPosition(0, (Gdx.graphics.getHeight() / 2) - 270);
		bagTable.bottom().left();
	}

	public void updateBag() {

		if (playerCmp.weaponList.size > 0) {
			stacks[0].removeActor(bagItems[0]);
			TextureAtlas atlasWP = game.getAssetManager().get(playerCmp.weapon.atlasPath, TextureAtlas.class);
			TextureAtlas.AtlasRegion regionWP = atlasWP.findRegion(playerCmp.weapon.key);
			Drawable drawableWP = new TextureRegionDrawable(regionWP);
			bagItems[0] = new ImageButton(drawableWP);
			stacks[0].add(bagItems[0]);
		}

		for (int i = 1; i < 5; i++) {
			if (playerCmp.inventory[i-1] != null){
				TextureAtlas atlas = game.getAssetManager().get(playerCmp.inventory[i-1].atlasPath, TextureAtlas.class);
				TextureAtlas.AtlasRegion region = atlas.findRegion(playerCmp.inventory[i-1].key);
				Drawable drawable = new TextureRegionDrawable(region);
				drawable.setMinHeight(80);
				drawable.setMinWidth(80);
				if (stacks[i].getChildren().size==1){
					bagItems[i] = new ImageButton(drawable);
					stacks[i].add(bagItems[i]);
				} else {
					stacks[i].removeActor(bagItems[i]);
					bagItems[i] = new ImageButton(drawable);
					stacks[i].add(bagItems[i]);
				}
				System.out.println(playerCmp.inventory[i-1].key);
				Gdx.app.debug("tag"," item have added successfully ");
			} else {
				stacks[i].removeActor(bagItems[i]);
			}
		}
	}

	public void createNumTable() {
		numTable.setFillParent(true);
		TextureAtlas atlas = game.getAssetManager().get("number/Number.atlas", TextureAtlas.class);
		for (int i = 1; i <= 4 ; i++) {
			TextureAtlas.AtlasRegion region = atlas.findRegion("0");
			Drawable drawable = new TextureRegionDrawable(region);
			drawable.setMinHeight(30);
			drawable.setMinWidth(30);

			Image numImage = new Image(drawable);
			numTable.add(numImage).padRight(60).padTop(60).row();
		}
		this.addActor(numTable);
		numTable.setPosition(-555, -90);
	}

	public void updateNumTable() {
		numTable.clearChildren();
		numTable.setFillParent(true);
		TextureAtlas atlas = game.getAssetManager().get("number/Number.atlas", TextureAtlas.class);
		for (int i = 1; i <= 4 ; i++) {
			if(playerCmp.inventory[i-1] != null) {
				TextureAtlas.AtlasRegion region = atlas.findRegion(String.valueOf(playerCmp.inventory[i-1].quatity));
				Drawable drawable = new TextureRegionDrawable(region);
				drawable.setMinHeight(30);
				drawable.setMinWidth(30);

				Image numImage = new Image(drawable);
				numTable.add(numImage).padRight(60).padTop(60).row();
			}
			else {
				TextureAtlas.AtlasRegion region = atlas.findRegion("0");
				Drawable drawable = new TextureRegionDrawable(region);
				drawable.setMinHeight(30);
				drawable.setMinWidth(30);

				Image numImage = new Image(drawable);
				numTable.add(numImage).padRight(60).padTop(60).row();
			}
		}
	}

	public void createLifeBar(Boss boss) {
		lifeBar.clearChildren();
		lifeBar.setFillParent(true);
		TextureAtlas atlas = game.getAssetManager().get("HUD/lifebar.atlas", TextureAtlas.class);
		TextureAtlas.AtlasRegion region = atlas.findRegion("LeftHealth");
		Drawable drawable = new TextureRegionDrawable(region);
		drawable.setMinHeight(20);
		drawable.setMinWidth(20);

		Image numImage = new Image(drawable);
		lifeBar.add(numImage);
		for (int i = 1; i < 60 ; i++) {
			region = atlas.findRegion("MiddleHealth");
			drawable = new TextureRegionDrawable(region);
			drawable.setMinHeight(20);
			drawable.setMinWidth(20);

			numImage = new Image(drawable);
			lifeBar.add(numImage);
		}

		region = atlas.findRegion("RightHealth");
		drawable = new TextureRegionDrawable(region);
		drawable.setMinHeight(20);
		drawable.setMinWidth(20);

		numImage = new Image(drawable);
		lifeBar.add(numImage);
		this.addActor(lifeBar);
		lifeBar.setPosition(0, -300);
	}

	public void updateLifeBar(BossComponent bossCmp) {
		lifeBar.clearChildren();
		TextureAtlas atlas = game.getAssetManager().get("HUD/lifebar.atlas", TextureAtlas.class);
		TextureAtlas.AtlasRegion region = atlas.findRegion("LeftHealth");
		Drawable drawable = new TextureRegionDrawable(region);
		drawable.setMinHeight(20);
		drawable.setMinWidth(20);

		Image numImage = new Image(drawable);
		lifeBar.add(numImage);
		int i;
		for (i = 1; i < bossCmp.life ; i++) {
			region = atlas.findRegion("MiddleHealth");
			drawable = new TextureRegionDrawable(region);
			drawable.setMinHeight(20);
			drawable.setMinWidth(20);

			numImage = new Image(drawable);
			lifeBar.add(numImage);
		}

		for (; i < bossCmp.maxLife ; i++) {
			region = atlas.findRegion("MiddleLife");
			drawable = new TextureRegionDrawable(region);
			drawable.setMinHeight(20);
			drawable.setMinWidth(20);

			numImage = new Image(drawable);
			lifeBar.add(numImage);
		}

		if (bossCmp.life == bossCmp.maxLife) {
			region = atlas.findRegion("RightHealth");
			drawable = new TextureRegionDrawable(region);
			drawable.setMinHeight(20);
			drawable.setMinWidth(20);
		}
		else {
			region = atlas.findRegion("RightLife");
			drawable = new TextureRegionDrawable(region);
			drawable.setMinHeight(20);
			drawable.setMinWidth(20);
		}
		numImage = new Image(drawable);
		lifeBar.add(numImage);
	}

	public void destroyLifeBar() {
		lifeBar.clearChildren();
	}
}
;