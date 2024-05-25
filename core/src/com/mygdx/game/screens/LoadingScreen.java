package com.mygdx.game.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.CoreGame;
import com.mygdx.game.LoadAsset;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.ui.GameUI;
import com.mygdx.game.ui.LoadingUI;

public class LoadingScreen extends AbstractScreen<LoadingUI> {
	private final AssetManager assetManager;
	private final LoadAsset loadAsset;
	private boolean isMusicLoaded;
	
	public LoadingScreen(CoreGame game) {
		super(game);
		this.assetManager = game.getAssetManager();
		this.loadAsset = game.getLoadAsset();
		this.mapManager = game.getMapManager();
		this.screenUI = (LoadingUI) getscreenUI(game.getLoadAsset().getGameSkin(), game);
		//load characters and effects
		assetManager.load("Items/Weapons/weapon.atlas", TextureAtlas.class);

		assetManager.load("Actor/Characters/BlackNinjaMage/blackninjamage.atlas", TextureAtlas.class);
		assetManager.load("Actor/Monsters/SkullBlue/skullblue.atlas", TextureAtlas.class);
		assetManager.load("Actor/Monsters/Cyclope/Cyclope.atlas", TextureAtlas.class);
		assetManager.load("Actor/Monsters/Slime2/slime2.atlas", TextureAtlas.class);
		assetManager.load("Actor/Monsters/Slime4/slime4.atlas", TextureAtlas.class);
		assetManager.load("Actor/Monsters/Reptile2/reptile2.atlas", TextureAtlas.class);
		assetManager.load("Actor/Monsters/Larva/larva.atlas", TextureAtlas.class);
		assetManager.load("Actor/Characters/RedFighter/redfighter.atlas", TextureAtlas.class);
		assetManager.load("Actor/Characters/RedSamurai/redsamurai.atlas", TextureAtlas.class);
		assetManager.load("Actor/Characters/Samurai/samurai.atlas", TextureAtlas.class);
		assetManager.load("Actor/Characters/RedNinja2/redninja2.atlas", TextureAtlas.class);
		assetManager.load("Actor/Characters/OrangeSorcerer/orangeSorcerer.atlas", TextureAtlas.class);
		assetManager.load("Actor/Characters/RedNinja/redninja.atlas", TextureAtlas.class);
		assetManager.load("Actor/Characters/Skeleton/skeleton.atlas", TextureAtlas.class);
		assetManager.load("Actor/Monsters/Skull/skull.atlas", TextureAtlas.class);
		assetManager.load("Actor/Monsters/Spirit/spirit.atlas", TextureAtlas.class);
		assetManager.load("Actor/Monsters/Spirit2/spirit2.atlas", TextureAtlas.class);


		assetManager.load("Actor/Boss/GiantBlueSamurai/giantbluesamurai.atlas", TextureAtlas.class);
		assetManager.load("Actor/Boss/GiantSpirit/giantspirit.atlas", TextureAtlas.class);

		assetManager.load("FX/Effect.atlas", TextureAtlas.class);
		assetManager.load("Items/Food/food.atlas", TextureAtlas.class);
		assetManager.load("Items/special.atlas", TextureAtlas.class);

		assetManager.load("HUD/HeartAtlasN.atlas", TextureAtlas.class);
		assetManager.load("HUD/miniheart.atlas", TextureAtlas.class);
		assetManager.load("number/Number.atlas", TextureAtlas.class);
		assetManager.load("HUD/lifebar.atlas", TextureAtlas.class);
		assetManager.load("Items/Key.atlas", TextureAtlas.class);

		//audio
		isMusicLoaded = false;
		for (final AudioType audioType: AudioType.values()) {
			if (audioType.isMusic()) {
				assetManager.load(audioType.getFilePath(), Music.class);
			}
			else {
				assetManager.load(audioType.getFilePath(), Sound.class);
			}
		}
	}


	
	@Override
	public void show() {
		assetManager.load(mapManager.getNextMapType().getFilePath(), TiledMap.class);
		super.show();
		isMusicLoaded = false;
		audioManager.stopCurrentMusic();
	}
	
	public void hide() {
		super.hide();
		audioManager.stopCurrentMusic();
	}

	@Override
	public void render(float delta) {
		
		ScreenUtils.clear(0, 0, 0, 1);
		
		if (!isMusicLoaded && assetManager.isLoaded(AudioType.PEACEFUL.getFilePath())) {
			isMusicLoaded = true;
			audioManager.playAudio(AudioType.PEACEFUL);
		}
		
		if (assetManager.update()) {
			((LoadingUI) screenUI).setPressButton();
		}
		
	}

	@Override
	protected Table getscreenUI(Skin skin, CoreGame game) {
		return new LoadingUI(skin, this.game);
	}

	@Override
	public void keyPressed(InputManager manager, GameKey gameKey) {
		if (assetManager.isFinished()) {
			mapManager.setMap();
			game.setScreen(ScreenType.GAME);
		}
	}

	@Override
	public void keyUp(InputManager manager, GameKey gameKey) {
		// TODO Auto-generated method stub
		
	}
}
