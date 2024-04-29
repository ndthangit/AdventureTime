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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.CoreGame;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.*;
import com.mygdx.game.items.Item;
import com.mygdx.game.items.food.Food;
import com.mygdx.game.map.GameObjectType;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.MapListener;

public class GameRenderer implements Disposable, MapListener{
	
	public static final String TAG = GameRenderer.class.getSimpleName();

	private final CoreGame game;
	private final OrthographicCamera gameCamera;
	private final FitViewport viewport;
	private final SpriteBatch spriteBatch;
	private final AssetManager assetManager;


	private final ImmutableArray<Entity> gameObjectEntities;
	private final ImmutableArray<Entity> animatedEntities;
	private final ImmutableArray<Entity> weaponEntities;
	private final ImmutableArray<Entity> effectEntities;
	private final ImmutableArray<Entity> itemEntities;
	private final ImmutableArray<Entity> hideEntities;
	private final OrthogonalTiledMapRenderer mapRenderer;
	private final Array<TiledMapTileLayer> tiledMapLayers;
	
	private final GLProfiler profiler;
	private final Box2DDebugRenderer b2dDebugRenderer;
	private final World world;
	private final ObjectMap<String, EnumMap<AnimationType, Animation<Sprite>>> animationCache;
	private final ObjectMap<String, EnumMap<EffectType, Animation<Sprite>>> effectCache;

	private IntMap<Animation<Sprite>> mapAnimations;


	public GameRenderer (CoreGame game) {
		this.game = game;

		this.assetManager = game.getAssetManager();
		this.viewport = game.getScreenViewport();
		this.gameCamera = game.getGameCamera();
		this.spriteBatch = game.getSpriteBatch();

        animationCache = new ObjectMap<String, EnumMap<AnimationType, Animation<Sprite>>>();
		effectCache = new ObjectMap<String, EnumMap<EffectType, Animation<Sprite>>>();

		gameObjectEntities = game.getEcsEngine().getEntitiesFor(Family.all(GameObjectComponent.class, Box2DComponent.class, AnimationComponent.class).get());
		animatedEntities = game.getEcsEngine().getEntitiesFor(Family.all(AnimationComponent.class, Box2DComponent.class).get());
		weaponEntities = game.getEcsEngine().getEntitiesFor(Family.all(WeaponComponent.class, AnimationComponent.class, Box2DComponent.class).get());
		effectEntities = game.getEcsEngine().getEntitiesFor(Family.all(EffectComponent.class).get());
		itemEntities = game.getEcsEngine().getEntitiesFor(Family.all(ItemComponent.class, Box2DComponent.class).get());
		hideEntities = game.getEcsEngine().getEntitiesFor(Family.all(HideLayerComponent.class, AnimationComponent.class).get());
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
		if(game.getMapManager().getCurrentMapType() == game.getMapManager().getNextMapType()) {
			spriteBatch.begin();
			mapRenderer.setView(gameCamera);
			if (mapRenderer.getMap() != null) {
				AnimatedTiledMapTile.updateAnimationBaseTime();
				for (final TiledMapTileLayer layer : tiledMapLayers) {
					mapRenderer.renderTileLayer(layer);
				}
			}

			if (game.getEcsEngine().getItemArray().size > 0) {
				for(final Item item : game.getEcsEngine().getItemArray()) {
					game.getEcsEngine().createItem(item);
				}
				game.getEcsEngine().getItemArray().clear();
			}



			for (final Entity entity : animatedEntities) {
				renderAnimated(entity, delta);
			}

			for (final Entity entity : itemEntities) {
				renderItem(entity, delta);
			}

			for (final Entity entity : weaponEntities) {
				renderWeapon(entity, delta);
			}

			for (final Entity entity: effectEntities) {
				rederEffect(entity, delta);
			}

			for (final Entity entity : gameObjectEntities) {
				renderGameObject(entity, delta);
			}

			for (final Entity entity : hideEntities) {
				renderHideLayer(entity, delta);
			}

			spriteBatch.end();
			b2dDebugRenderer.render(world, viewport.getCamera().combined);
		}
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

	private void renderHideLayer(Entity entity, float delta) {
		final HideLayerComponent hideLayerComponent = ECSEngine.hideLayerCmpMapper.get(entity);
		final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);
		if (hideLayerComponent.animationIndex != -1) {
			final Animation<Sprite> animation = mapAnimations.get(hideLayerComponent.animationIndex);
			final Sprite frame = animation.getKeyFrame(aniComponent.aniTime);
			frame.setBounds(hideLayerComponent.position.x - aniComponent.width / 2, hideLayerComponent.position.y - aniComponent.height / 2, aniComponent.width, aniComponent.height);
			frame.setOriginCenter();
			frame.draw(spriteBatch);
		}
	}

	private void renderAnimated(Entity entity, float delta) {
		
		final Box2DComponent box2DComponent = ECSEngine.box2dCmpMapper.get(entity);
		final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);
		
		if (aniComponent.aniType != null) {
			final Animation<Sprite> animation = getAnimation(aniComponent.path, aniComponent.aniType);
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
		frame.setRotation(-weaponComponent.direction.getCode() * 90);
		frame.draw(spriteBatch);
	}

	private void renderItem(Entity entity, float delta) {
		final ItemComponent itemComponent = ECSEngine.itemCmpMapper.get(entity);
		final Box2DComponent box2DComponent = ECSEngine.box2dCmpMapper.get(entity);
		final AtlasRegion atlasRegion = assetManager.get(itemComponent.item.atlasPath, TextureAtlas.class).findRegion(itemComponent.item.key);
		final Sprite frame;
		frame = new Sprite(atlasRegion);
		box2DComponent.renderPosition.lerp(box2DComponent.body.getPosition(), delta);
		frame.setBounds(box2DComponent.renderPosition.x - box2DComponent.width/2, box2DComponent.renderPosition.y - box2DComponent.height / 2, box2DComponent.width, box2DComponent.height);
		frame.setOriginCenter();
		frame.draw(spriteBatch);
	}

	private void rederEffect(Entity entity, float delta) {
		final EffectComponent effectComponent = ECSEngine.effectCmpMapper.get(entity);
        String path = effectComponent.path;
        EffectType type = effectComponent.type;
        EnumMap<EffectType, Animation<Sprite>> subCache = effectCache.get(path);
        if (subCache == null) {
            subCache = new EnumMap<EffectType, Animation<Sprite>>(EffectType.class);
            effectCache.put(path, subCache);
        }
        Animation<Sprite> animation = subCache.get(type);
        if (animation == null) {
            Gdx.app.debug(TAG, "Creating new effect of type " + type);
            final TextureAtlas.AtlasRegion atlasRegion = assetManager.get(path, TextureAtlas.class).findRegion(type.getAtlasKey());
            final TextureRegion[][] textureRegions = atlasRegion.split(32, 32);
            final Array<Sprite> keyFrame = new Array<Sprite>();
            for (TextureRegion subRegion : textureRegions[0]) {
				Gdx.app.debug("effect", effectComponent.toString());
                final Sprite sprite = new Sprite(subRegion);
                sprite.setOriginCenter();
                keyFrame.add(sprite);
            }
            animation = new Animation<Sprite>(type.getFrameTime(), keyFrame, Animation.PlayMode.NORMAL);
            subCache.put(type, animation);
			Gdx.app.debug("effect", effectComponent.toString());
        }
        final Sprite frame = animation.getKeyFrame(effectComponent.aniTime);
        frame.setBounds(effectComponent.position.x - effectComponent.width * 0.5f, effectComponent.position.y - effectComponent.height * 0.5f, effectComponent.width, effectComponent.height);
		frame.setOriginCenter();
		frame.setRotation(-(effectComponent.direction.getCode()+1) * 90);
		frame.draw(spriteBatch);
	}

	private Animation<Sprite> getAnimation(String path, AnimationType aniType) {
		EnumMap<AnimationType, Animation<Sprite>> subCache = animationCache.get(path);

		if (subCache == null) {
			subCache = new EnumMap<AnimationType, Animation<Sprite>>(AnimationType.class);
			animationCache.put(path, subCache);
		}
		Animation<Sprite> animation = subCache.get(aniType);
		if (animation == null) {
				Gdx.app.debug(TAG, "Creating new animation of type " + aniType);
				final AtlasRegion atlasRegion = assetManager.get(path, TextureAtlas.class).findRegion(aniType.getAtlasKey());
				final TextureRegion[][] textureRegions = atlasRegion.split(48, 48);
				animation = new Animation<Sprite>(aniType.getFrameTime(), getKeyFrame(textureRegions, aniType.getColIndex()), Animation.PlayMode.LOOP);
				subCache.put(aniType, animation);
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
		mapAnimations = map.getMapAnimations();
	}

}
