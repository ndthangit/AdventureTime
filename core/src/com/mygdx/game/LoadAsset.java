package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;

public class LoadAsset {
	final private AssetManager assetManager;
	final private CoreGame context;
	private Skin loadingSkin;
	private Skin gameSkin;
	
	public LoadAsset(CoreGame context) {
		this.context = context;
		this.assetManager = context.getAssetManager();		
	}
	
	public void getLoadingAssetUI() {
		final ObjectMap<String, Object> resources = new ObjectMap<String, Object>();
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("HUD/Font/NormalFont.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		
		fontParameter.minFilter = Texture.TextureFilter.Linear;
		fontParameter.magFilter = Texture.TextureFilter.Linear;
		
		final int[] sizeFonts = {16,24,28,36,48};
		for (int size: sizeFonts) {
			fontParameter.size = size;
			resources.put("font_"+size, fontGenerator.generateFont(fontParameter));
		}
		fontGenerator.dispose();
		final SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("HUD/HUD.atlas", resources);
		assetManager.load("HUD/LoadingHUD.json", Skin.class, skinParameter);
		assetManager.finishLoading();
		loadingSkin = assetManager.get("HUD/LoadingHUD.json", Skin.class);
		context.setSkin(loadingSkin);
	}
	
	public void getGameAssetUI() {
		//setup markup color
		Colors.put("Red", Color.RED);
		Colors.put("Blue", Color.BLUE);
		Colors.put("Black", Color.BLACK);
		
		//generate ttf bitmaps
		final ObjectMap<String, Object> resources = new ObjectMap<String, Object>();
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("HUD/Font/NormalFont.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		
		fontParameter.minFilter = Texture.TextureFilter.Linear;
		fontParameter.magFilter = Texture.TextureFilter.Linear;
		
		final int[] sizeFonts = {16,24,28,36,48};
		for (int size: sizeFonts) {
			fontParameter.size = size;
			final BitmapFont bitMapFont = fontGenerator.generateFont(fontParameter);
			bitMapFont.getData().markupEnabled = true;
			resources.put("font_"+size, bitMapFont);
		}
		fontGenerator.dispose();
		
		//load skin
		final SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("HUD/HUD.atlas", resources);
		assetManager.load("HUD/GameHUD.json", Skin.class, skinParameter);
		assetManager.finishLoading();
		gameSkin = assetManager.get("HUD/GameHUD.json", Skin.class);
	}
	
	public Skin getloadingSkin() {
		return loadingSkin;
	}

	public Skin getGameSkin() {
		return gameSkin;
	}
}
