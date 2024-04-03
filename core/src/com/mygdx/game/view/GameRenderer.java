package com.mygdx.game.view;

import java.util.EnumMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.MapListener;

public class GameRenderer implements Disposable, MapListener{
	
	public static final String TAG = GameRenderer.class.getSimpleName();
	
	private final OrthographicCamera gameCamera;
	private final FitViewport viewport;
	private final SpriteBatch spriteBatch;
	private final AssetManager assetManager;
	
	private final ImmutableArray<Entity> animatedEntities;
	private final OrthogonalTiledMapRenderer mapRenderer;
	private final Array<TiledMapTileLayer> tiledMapLayers;
	
	private final GLProfiler profiler;
	private final Box2DDebugRenderer b2dDebugRenderer;
	private final World world;
	private final EnumMap<AnimationType, Animation<Sprite>> animationCache;
	
	
	public GameRenderer (CoreGame game) {
		this.assetManager = game.getAssetManager();
		this.viewport = game.getScreenViewport();
		this.gameCamera = game.getGameCamera();
		this.spriteBatch = game.getSpriteBatch();
		
		animationCache = new EnumMap<AnimationType, Animation<Sprite>>(AnimationType.class);
		
		animatedEntities = game.getEcsEngine().getEntitiesFor(Family.all(AnimationComponent.class, Box2DComponent.class).get());
		
		this.mapRenderer = new OrthogonalTiledMapRenderer(null, CoreGame.UNIT_SCALE, game.getSpriteBatch());
		game.getMapManager().addMapListener(this);
		tiledMapLayers = new Array<TiledMapTileLayer>();
		
		this.profiler = new GLProfiler(Gdx.graphics);
		profiler.enable();
		if (profiler.isEnabled()) {
			this.b2dDebugRenderer = new Box2DDebugRenderer();
			this.world = game.getWorld();
		}
		else {
			this.b2dDebugRenderer = null;
			this.world = null;
		}
		
//		game.getEcsEngine().createPlayer(game.getMapManager().getCurrentMap().getStartPosition(), 0.5f, 0.5f);
		
	}

	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 1);
		
		viewport.apply(false);
		spriteBatch.begin();
		if (mapRenderer.getMap() != null) {
			AnimatedTiledMapTile.updateAnimationBaseTime();
			mapRenderer.setView(gameCamera);
			for (final TiledMapTileLayer layer: tiledMapLayers) {
				mapRenderer.renderTileLayer(layer);
			}
		}
		for (final Entity entity: animatedEntities) {
			renderEntity(entity, delta);
		}
		spriteBatch.end();
		b2dDebugRenderer.render(world, viewport.getCamera().combined);
		world.step(1/60f, 6, 2);
		profiler.reset();
	}
	
	private void renderEntity(Entity entity, float delta) {
		final Box2DComponent box2DComponent = ECSEngine.box2dCmpMapper.get(entity);
		final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);
		
		if (aniComponent.aniType != null) {
			final Animation<Sprite> animation = getAnimation(aniComponent.aniType);
			final Sprite frame = animation.getKeyFrame(aniComponent.aniTime);
			box2DComponent.renderPosition.lerp(box2DComponent.body.getPosition(), delta);
			frame.setBounds(box2DComponent.renderPosition.x - aniComponent.width * 0.5f, box2DComponent.renderPosition.y - aniComponent.height * 0.5f, aniComponent.width, aniComponent.height);
			frame.draw(spriteBatch);
		}
	}

	private Animation<Sprite> getAnimation(AnimationType aniType) {
		Animation<Sprite> animation = animationCache.get(aniType);
		if (animation == null) {
			Gdx.app.debug(TAG, "Creating new animation of type " + aniType);
			final AtlasRegion atlasRegion = assetManager.get(aniType.getAtlasPath(), TextureAtlas.class).findRegion(aniType.getAtlasKey());
			final TextureRegion[][] textureRegions = atlasRegion.split(16, 16);
			animation = new Animation<Sprite>(aniType.getFrameTime(), getKeyFrame(textureRegions, aniType.getColIndex()), Animation.PlayMode.LOOP);
			animationCache.put(aniType, animation);
		}
		return animation;
	}

	private Array<? extends Sprite> getKeyFrame(TextureRegion[][] textureRegions, int colIndex) {
		final Array<Sprite> keyFrame = new Array<Sprite>();
		for (TextureRegion[] subRegions : textureRegions) {
			final Sprite sprite = new Sprite(subRegions[colIndex]);
			sprite.setOriginCenter();
			keyFrame.add(sprite);
		}
		return keyFrame;
	}

	@Override
	public void dispose() {
		if (b2dDebugRenderer != null) {
			b2dDebugRenderer.dispose();
		}
		mapRenderer.dispose();
		
	}

	@Override
	public void mapChange(Map map) {
		mapRenderer.setMap(map.getTiledMap());
		map.getTiledMap().getLayers().getByType(TiledMapTileLayer.class, tiledMapLayers);
//		if (dummySprite == null) {
//			dummySprite = assetManager.get("Actor/Characters/BlackNinjaMage/blackninjamage.atlas", TextureAtlas.class).createSprite("Dead");
//			dummySprite.setOriginCenter();
//		}
		
	}

}
