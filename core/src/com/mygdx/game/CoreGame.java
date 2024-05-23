package com.mygdx.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.audio.AudioManager;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.MapManager;
import com.mygdx.game.map.MapType;
import com.mygdx.game.screens.ScreenType;
import com.mygdx.game.ui.GameUI;
import com.mygdx.game.view.GameRenderer;

import java.util.EnumMap;

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
	public static final short BIT_WEAPON = 1 << 3;
	public static final short BIT_ENEMY = 1 << 4;
	public static final short BIT_ITEM = 1 << 5;
	public static final short BIT_DOOR = 1 << 6;
	public static final short BIT_BOSS = 1 << 7;
	public static final short BIT_DAMAGE_AREA = 1 << 8;

	public static final float FIXED_TIME_STEP = 1/60f;

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
	private GameUI gameUI; 
	private LoadAsset loadAsset;
	
	private InputManager inputManager;
	
	private ECSEngine ecsEngine;
	
	private GameRenderer gameRenderer;

	private float accumulator = 0;

	private RayHandler rayHandler;

	private ScreenType screenType;


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
		box2DDebugRenderer.setDrawBodies(false);

		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0, 0, 0, 0.05f);
		
		//initialize assetManager
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));
		loadAsset = new LoadAsset(this);
		loadAsset.getLoadingAssetUI();
		loadAsset.getGameAssetUI();
		stage = new Stage(new FitViewport(1280, 720), spriteBatch);
		

		
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

		// map
		mapManager = new MapManager(this);
		
		//gamerenderer
		gameRenderer = new GameRenderer(this);
		
		screenCache = new EnumMap<ScreenType, Screen> (ScreenType.class);		
		mapManager.setNextMapType(MapType.DOJO);
		screenType = ScreenType.MENU;
		setScreen(ScreenType.MENU);
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

	public RayHandler getRayHandler() {
		return rayHandler;
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

	public WorldContactListener getWorldContactListener() {
		return worldContactListener;
	}

	public GameUI getGameUI() {
		return gameUI;
	}

	public void setGameUI(GameUI gameUI) {
		this.gameUI = gameUI;
	}

	public ScreenType getScreenType() {
		return screenType;
	}

	public void setScreen(final ScreenType screenType) {
		this.screenType = screenType;
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

		final float deltaTime = Math.min(0.25f, Gdx.graphics.getDeltaTime());
		accumulator += deltaTime;
		ecsEngine.update(Gdx.graphics.getDeltaTime());
		while (accumulator >= FIXED_TIME_STEP) {
			world.step(FIXED_TIME_STEP, 6, 2);
			accumulator -=  FIXED_TIME_STEP;
		}
		gameRenderer.render( accumulator/FIXED_TIME_STEP);
		stage.getViewport().apply();
		stage.act(deltaTime);
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

	// function of menu
	public void startGame() {
		// Logic to start the game
		setScreen(ScreenType.LOAD);
	}

	public void optionGame() {
		setScreen(ScreenType.OPTION);
	}

	public void replayGame() {
		// Logic to start the game
		mapManager.destroyPlayer();
		mapManager.setMap();
		setScreen(ScreenType.LOAD);
	}

	public void quitGame() {
		// Logic to quit the game
		Gdx.app.exit(); // Exit the application
	}

	public boolean isSoundEnabled() {
		return audioManager.musicEnabled;
	}

	public void switchSound() {
		audioManager.musicEnabled = audioManager.musicEnabled ? false : true;
	}

}
