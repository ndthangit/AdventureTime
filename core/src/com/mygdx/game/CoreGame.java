package com.mygdx.game;

import java.util.EnumMap;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.BaseTmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.audio.AudioManager;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.MapManager;
import com.mygdx.game.map.MapType;
import com.mygdx.game.screens.ScreenType;
import com.mygdx.game.view.GameRenderer;

public class CoreGame extends Game {


	private static final String TAG = CoreGame.class.getSimpleName();
	
	private SpriteBatch spriteBatch;
	private EnumMap<ScreenType, Screen> screenCache;
	private OrthographicCamera gameCamera;
	private FitViewport screenViewport;
	
	public static final float UNIT_SCALE = 4 / 64f;
	public static final short BIT_PLAYER = 1 << 0;
	public static final short BIT_GROUND = 1 << 1;
	public static final short BIT_GAME_OBJECT = 1 << 2;

	public static final BodyDef BODY_DEF = new BodyDef();
	public static final FixtureDef FIXTURE_DEF = new FixtureDef();
	
	private World world;
	private WorldContactListener worldContactListener;
	private Box2DDebugRenderer box2DDebugRenderer;
	
	private AssetManager assetManager;
	private AudioManager audioManager;
	private MapManager mapManager;
	
	private Stage stage;
	private Skin skin;
	private LoadAsset loadAsset;
	
	private InputManager inputManager;
	
	private ECSEngine ecsEngine;
	
	private GameRenderer gameRenderer;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		spriteBatch = new SpriteBatch();
		
		//box 2d stuff
		Box2D.init();
		world = new World(new Vector2(0, 0), true);
		worldContactListener = new WorldContactListener();
		world.setContactListener(worldContactListener);
		box2DDebugRenderer = new Box2DDebugRenderer();
		box2DDebugRenderer.setDrawBodies(true);
		
		//initialize assetManager
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));
		loadAsset = new LoadAsset(this);
		loadAsset.getLoadingAssetUI();
		loadAsset.getGameAssetUI();
		stage = new Stage(new FitViewport(1280, 720), spriteBatch);
		
		// map
		mapManager = new MapManager(this);
		
		//audio
		audioManager = new AudioManager(this);
		
		//input
		inputManager = new InputManager();
		Gdx.input.setInputProcessor(new InputMultiplexer(inputManager, stage));
		
		//set first screen
		gameCamera = new OrthographicCamera();
		screenViewport = new FitViewport(16, 9, gameCamera);
			
		
		//entity
		ecsEngine = new ECSEngine(this);
		
		//gamerenderer
		gameRenderer = new GameRenderer(this);
		
		screenCache = new EnumMap<ScreenType, Screen> (ScreenType.class);		
		mapManager.setNextMapType(MapType.MAP_1);
		setScreen(ScreenType.LOAD);		
	}

	public Stage getStage() {
		return stage;
	}


	public Skin getSkin() {
		return skin;
	}
	
	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	public FitViewport getScreenViewport() {
		return screenViewport;
	}

	public World getWorld() {
		return world;
	}

	public Box2DDebugRenderer getBox2DDebugRenderer() {
		return box2DDebugRenderer;
	}

	public OrthographicCamera getGameCamera() {
		return gameCamera;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}
	
	public LoadAsset getLoadAsset() {
		return loadAsset;
	}

	public InputManager getInputManager() {
		return inputManager;
	}

	public ECSEngine getEcsEngine() {
		return ecsEngine;
	}

	public AudioManager getAudioManager() {
		return audioManager;
	}

	public MapManager getMapManager() {
		return mapManager;
	}

	public void setScreen(final ScreenType screenType) {
		final Screen screen = screenCache.get(screenType);
		if (screen == null) {
			try {
				Gdx.app.debug(TAG, "Creating new screen: " + screenType);
				final Screen newScreen = (Screen) ClassReflection.getConstructor(screenType.getScreenClass(),CoreGame.class).newInstance(this);
				screenCache.put(screenType, newScreen);
				setScreen(newScreen);		
				
			} catch (ReflectionException e) {
				throw new GdxRuntimeException("Screen " + screenType + " could not be created " + e);
			}
		} 
		else {
			Gdx.app.debug(TAG, "Switching to screen: " + screenType);
			setScreen(screen);
		}
	}
	
	public static void resetBodiesAndFixtureDefinition() {
		BODY_DEF.gravityScale = 1;
		BODY_DEF.type = BodyDef.BodyType.StaticBody;
		BODY_DEF.fixedRotation = false;
		
		FIXTURE_DEF.isSensor = false;
		FIXTURE_DEF.restitution = 0;
		FIXTURE_DEF.friction = 0;
		FIXTURE_DEF.density = 0;
		FIXTURE_DEF.filter.categoryBits = 0x0001;
		FIXTURE_DEF.filter.maskBits = -1;
		FIXTURE_DEF.shape = null;
	}
	
	@Override
	public void render()  {
		super.render();
		ecsEngine.update(Gdx.graphics.getDeltaTime());
		gameRenderer.render(Gdx.graphics.getDeltaTime());
		stage.getViewport().apply();
		stage.act();
		stage.draw();
	}
	
	@Override
	public void dispose()  {
		super.dispose();
		box2DDebugRenderer.dispose();
		world.dispose();
		assetManager.dispose();
		spriteBatch.dispose();
		stage.dispose();
	}

}
