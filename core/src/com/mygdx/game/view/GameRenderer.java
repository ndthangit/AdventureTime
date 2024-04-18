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
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.GameObjectComponent;
import com.mygdx.game.entity.component.WeaponComponent;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.MapListener;

import static com.mygdx.game.CoreGame.FIXED_TIME_STEP;

public class GameRenderer implements Disposable, MapListener{
	
	public static final String TAG = GameRenderer.class.getSimpleName();
	
	private final OrthographicCamera gameCamera;
	private final FitViewport viewport;
	private final SpriteBatch spriteBatch;
	private final AssetManager assetManager;

	private final ImmutableArray<Entity> gameObjectEntities;
	private final ImmutableArray<Entity> animatedEntities;
	private final ImmutableArray<Entity> weaponEntities;
	private final OrthogonalTiledMapRenderer mapRenderer;
	private final Array<TiledMapTileLayer> tiledMapLayers;
	
	private final GLProfiler profiler;
	private final Box2DDebugRenderer b2dDebugRenderer;
	private final World world;
	private final EnumMap<AnimationType, Animation<Sprite>> animationCache;
	private final ObjectMap<String, TextureRegion[][]> regionCache;
	private IntMap<Animation<Sprite>> mapAnimations;


	public GameRenderer (CoreGame game) {
		this.assetManager = game.getAssetManager();
		this.viewport = game.getScreenViewport();
		this.gameCamera = game.getGameCamera();
		this.spriteBatch = game.getSpriteBatch();
		
		animationCache = new EnumMap<AnimationType, Animation<Sprite>>(AnimationType.class);
		regionCache = new ObjectMap<String, TextureRegion[][]>();

		gameObjectEntities = game.getEcsEngine().getEntitiesFor(Family.all(GameObjectComponent.class, Box2DComponent.class, AnimationComponent.class).get());
		animatedEntities = game.getEcsEngine().getEntitiesFor(Family.all(AnimationComponent.class, Box2DComponent.class).exclude(GameObjectComponent.class).get());
		weaponEntities = game.getEcsEngine().getEntitiesFor(Family.all(WeaponComponent.class, AnimationComponent.class, Box2DComponent.class).get());

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
	}

	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 1);
		viewport.apply(false);
		spriteBatch.begin();

		mapRenderer.setView(gameCamera);
		if (mapRenderer.getMap() != null) {
			AnimatedTiledMapTile.updateAnimationBaseTime();
			for (final TiledMapTileLayer layer: tiledMapLayers) {
				mapRenderer.renderTileLayer(layer);
			}
		}

		for (final Entity entity: gameObjectEntities) {
			renderGameObject(entity, delta);

		}

		for (final Entity entity: animatedEntities) {
			renderEntity(entity, delta);
		}
		
		for (final Entity entity: weaponEntities) {
			renderWeapon(entity, delta);
		}
		eventChecker();
		spriteBatch.end();
		b2dDebugRenderer.render(world, viewport.getCamera().combined);
//		world.step(FIXED_TIME_STEP, 6, 2);
		profiler.reset();

	}

	private void renderGameObject(Entity entity, float delta) {
		final Box2DComponent box2DComponent = ECSEngine.box2dCmpMapper.get(entity);
		final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);
		final GameObjectComponent gameObjectComponent = ECSEngine.gameObjCmpMapper.get(entity);

		if (gameObjectComponent.animationIndex != -1) {
			final Animation<Sprite> animation = mapAnimations.get(gameObjectComponent.animationIndex);
			final Sprite frame = animation.getKeyFrame(aniComponent.aniTime);
			box2DComponent.renderPosition.lerp(box2DComponent.body.getPosition(), delta);
			frame.setBounds(box2DComponent.renderPosition.x - aniComponent.width / 2, box2DComponent.renderPosition.y - aniComponent.height / 2, aniComponent.width, aniComponent.height);
			frame.setOriginCenter();
			frame.setRotation(box2DComponent.body.getAngle() * MathUtils.degreesToRadians);
			frame.draw(spriteBatch);
		}
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

	private void renderWeapon(Entity entity, float delta) {
		final Box2DComponent box2DComponent = ECSEngine.box2dCmpMapper.get(entity);
		final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);
		final WeaponComponent weaponComponent = ECSEngine.weaponCmpMapper.get(entity);
		final AtlasRegion atlasRegion = assetManager.get(weaponComponent.type.getAtlasPath(), TextureAtlas.class).findRegion(weaponComponent.type.getAtlasKey());
		final Sprite frame;
		frame = new Sprite(atlasRegion);
		box2DComponent.renderPosition.lerp(box2DComponent.body.getPosition(), delta);
		frame.setBounds(box2DComponent.renderPosition.x - aniComponent.width / 2, box2DComponent.renderPosition.y - aniComponent.height / 2, aniComponent.width, aniComponent.height);
		frame.setOriginCenter();
		frame.setRotation(-weaponComponent.direction * 90);
		frame.draw(spriteBatch);
	}

	private Animation<Sprite> getAnimation(AnimationType aniType) {
		Animation<Sprite> animation = animationCache.get(aniType);
		if (animation == null) {
			Gdx.app.debug(TAG, "Creating new animation of type " + aniType);
			final AtlasRegion atlasRegion = assetManager.get(aniType.getAtlasPath(), TextureAtlas.class).findRegion(aniType.getAtlasKey());
			final TextureRegion[][] textureRegions = atlasRegion.split(48, 48);
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

	private void eventChecker() {
		if (weaponEntities.size()>0) {
			Entity weapon = weaponEntities.first();
			Box2DComponent wb2dComponent = ECSEngine.box2dCmpMapper.get(weapon);
			AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(weapon);
			Rectangle rectangle1 = new Rectangle();
			Rectangle rectangle2 = new Rectangle();
			rectangle1.x = wb2dComponent.body.getPosition().x ;
			rectangle1.y = wb2dComponent.body.getPosition().y;
			rectangle1.height = animationComponent.height;
			rectangle1.width = animationComponent.width;
			Box2DComponent gb2dComponent;
			AnimationComponent ganiComponent;
			for (Entity gameObj: gameObjectEntities) {
				gb2dComponent = ECSEngine.box2dCmpMapper.get(gameObj);
				ganiComponent = ECSEngine.aniCmpMapper.get(gameObj);
				rectangle2.height = ganiComponent.height;
				rectangle2.width = ganiComponent.width;
				rectangle2.x = gb2dComponent.body.getPosition().x;
				rectangle2.y = gb2dComponent.body.getPosition().y;
				
				if (Intersector.overlaps(rectangle1, rectangle2)) {
					Gdx.app.debug(TAG, "Overlaps " + rectangle1 + " and " + rectangle2);
				}
			}
		}
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
		mapAnimations = map.getMapAnimations();
	}

}
