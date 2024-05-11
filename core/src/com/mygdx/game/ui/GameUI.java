package com.mygdx.game.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
	private final Table bagTable;
	private final Image bagItems[];
	
	public GameUI(Skin skin, CoreGame game) {
		super(skin);
		this.game = game;
		player = game.getEcsEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        this.bagTable = new Table(skin);
        this.bagItems = new Image[4];
        playerCmp = player.getComponent(PlayerComponent.class);
		heartTable = new Table();
		setFillParent(true);
		heartImage = new Image[ (int) playerCmp.maxLife/4];
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
	}
	
	private Texture setHeartImage(int i) {
		Texture texture = new Texture(Gdx.files.internal("HUD/Heart/Heart-" + i + ".png"));
		return texture;

	}

	public void createBag() {
		TextureAtlas backGroundBag = new TextureAtlas(Gdx.files.internal("Items/Weapons/weapon.atlas"));
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("Items/Weapons/weapon.atlas"));
		Stack stack = new Stack();
		Texture texture = new Texture(Gdx.files.internal("Treasure Hunters/Treasure Hunters/Wood and Paper UI/Sprites/Big Banner/32.png"));
		Image image = new Image(texture);
		stack.add(image);
		Table subtable = new Table();
		for (int i = 0; i < 4; i++) {
			TextureAtlas.AtlasRegion region = atlas.findRegion("Katana");
			bagItems[i] = new Image(region);
			// bagItems[i].setDrawable(new TextureRegionDrawable(backGround));
			bagItems[i].setSize(region.originalWidth, region.originalHeight);
			subtable.add(bagItems[i]).size(region.originalWidth * 3, region.originalHeight * 3).expand().pad(10).center().row();
			// bagTable.add(bagItems[i]).expand().right().top().padTop(20).left().padLeft(20).padRight(20);
		}
		stack.add(subtable);
		bagTable.setPosition(50, Gdx.graphics.getHeight() / 2);
		bagTable.add(stack).size(50, 50).expand().center().row();
		bagTable.debug();
		// bagTable.setBackground(back);
		addActor(bagTable);
	}
	
}
;