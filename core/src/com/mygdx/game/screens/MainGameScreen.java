package com.mygdx.game.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.CoreGame;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.MapListener;
import com.mygdx.game.map.MapType;
import com.mygdx.game.ui.GameUI;

public class MainGameScreen extends AbstractScreen<GameUI> implements MapListener {
	
//	private final OrthogonalTiledMapRenderer mapRender;
//	private final OrthographicCamera gameCamera;
//	private final GLProfiler profiler;
//	private final AssetManager assetManager;
	
	
	public MainGameScreen (CoreGame game) {
		super(game, game.getLoadAsset().getGameSkin());
//		mapRender = new OrthogonalTiledMapRenderer(null, CoreGame.UNIT_SCALE, game.getSpriteBatch());
//		gameCamera = game.getGameCamera();
//		profiler = new GLProfiler(Gdx.graphics);
//		this.assetManager = game.getAssetManager();
//		profiler.enable();
		mapManager = game.getMapManager();
		mapManager.addMapListener(this);
		mapManager.setMap();
		
		game.getEcsEngine().createPlayer(mapManager.getCurrentMap().getStartPosition(), 0.75f, 0.75f);
	}

	@Override
	public void show() {
		super.show();
//		mapRender.setMap(assetManager.get(mapManager.getCurrentMapType().getFilePath(), TiledMap.class));
	}
	
	@Override
	public void render(float delta) {
//		ScreenUtils.clear(0, 0, 0, 1);
//		
//		viewPort.apply(false);
//		if (mapRender.getMap() != null) {
//			mapRender.setView(gameCamera);
//			mapRender.render();
//		}
//		box2DDebugRenderer.render(world, viewPort.getCamera().combined);
//		world.step(1/60f, 6, 2);
//		profiler.reset();
	}
	
	

	@Override
	public void dispose() {
	}

	@Override
	protected GameUI getscreenUI(Skin skin, CoreGame game) {
		return new GameUI(skin, this.game);
	}

	@Override
	public void keyPressed(InputManager manager, GameKey gameKey) {
		if (gameKey == GameKey.CHANGE_MAP_1) {
			mapManager.setNextMapType(MapType.MAP_1);
			game.setScreen(ScreenType.LOAD);
		}
		else if (gameKey == GameKey.CHANGE_MAP_2) {
			mapManager.setNextMapType(MapType.MAP_2);
			game.setScreen(ScreenType.LOAD);
		}
	}

	@Override
	public void keyUp(InputManager manager, GameKey gameKey) {
		
	}

	@Override
	public void mapChange(Map map) {
		
	}
}
