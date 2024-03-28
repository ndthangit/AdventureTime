package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.CoreGame;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.CollisionArea;
import com.mygdx.game.map.Map;
import com.mygdx.game.ui.GameUI;

public class MainGameScreen extends AbstractScreen<GameUI> {
	private final BodyDef bodyDef;
	private final FixtureDef fixtureDef;
	
	private final AssetManager assetManager;
	private final OrthogonalTiledMapRenderer mapRender;
	private final OrthographicCamera gameCamera;
	private final GLProfiler profiler;
	
	private Map map;
	
	public MainGameScreen (CoreGame game) {
		super(game, game.getLoadAsset().getGameSkin());
		bodyDef = new BodyDef();
		fixtureDef = new FixtureDef();
		this.mapRender = new OrthogonalTiledMapRenderer(null, CoreGame.UNIT_SCALE, game.getSpriteBatch());
		assetManager = game.getAssetManager();
		gameCamera = game.getGameCamera();
		profiler = new GLProfiler(Gdx.graphics);
		profiler.enable();
		
		final TiledMap tiledMap = assetManager.get("maps/Demo-abandon-village-map.tmx",TiledMap.class);
		mapRender.setMap(tiledMap);
		map = new Map(tiledMap);
		
		game.getEcsEngine().createPlayer(map.getStartPosition(), 1, 1);
		spawnCollisionArea();
	}
	
	private void spawnCollisionArea() {
		for (final CollisionArea collisionArea: map.getCollisionAreas()) {
			resetBodiesAndFixtureDefinition();
			bodyDef.position.set(collisionArea.getX(), collisionArea.getY());
			Body body = world.createBody(bodyDef);
			
			fixtureDef.filter.categoryBits = CoreGame.BIT_GROUND;
			fixtureDef.filter.maskBits = -1;
			ChainShape cShape = new ChainShape();
			cShape.createLoop(collisionArea.getVertices());
			fixtureDef.shape = cShape;
			body.createFixture(fixtureDef);	
			body.setUserData("GROUND");
			cShape.dispose();
		}
	}
	
	private void resetBodiesAndFixtureDefinition() {
		bodyDef.gravityScale = 1;
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.fixedRotation = false;
		
		fixtureDef.isSensor = false;
		fixtureDef.restitution = 0;
		fixtureDef.friction = 0;
		fixtureDef.density = 0;
		fixtureDef.filter.categoryBits = 0x0001;
		fixtureDef.filter.maskBits = -1;
		fixtureDef.shape = null;
		
	} 

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 1);
		
		viewPort.apply(true);
		mapRender.setView(gameCamera);
		mapRender.render();
		box2DDebugRenderer.render(world, viewPort.getCamera().combined);
		world.step(1/60f, 6, 2);
		profiler.reset();
	}
	
	

	@Override
	public void dispose() {
		mapRender.dispose();
	}

	@Override
	protected GameUI getscreenUI(Skin skin) {
		return new GameUI(skin);
	}

	@Override
	public void keyPressed(InputManager manager, GameKey gameKey) {
		
	}

	@Override
	public void keyUp(InputManager manager, GameKey gameKey) {
		
	}
}
