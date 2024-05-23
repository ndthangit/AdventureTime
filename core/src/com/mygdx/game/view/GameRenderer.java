package com.mygdx.game.view;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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
import com.mygdx.game.map.Map;
import com.mygdx.game.map.MapListener;
import com.mygdx.game.map.MapManager;
import com.mygdx.game.map.MapType;
import com.mygdx.game.screens.ScreenType;

import java.util.EnumMap;

import static com.mygdx.game.CoreGame.UNIT_SCALE;

public class GameRenderer implements Disposable, MapListener{
	
	public static final String TAG = GameRenderer.class.getSimpleName();

	private final CoreGame game;
	private final OrthographicCamera gameCamera;
	private final FitViewport viewport;
	private final SpriteBatch spriteBatch;
	private final AssetManager assetManager;
	private final MapManager mapManager;

	private final ImmutableArray<Entity> gameObjectEntities;
	private final ImmutableArray<Entity> animatedEntities;
	private final ImmutableArray<Entity> weaponEntities;
	private final ImmutableArray<Entity> effectEntities;
	private final ImmutableArray<Entity> bossEntities;
	private final ImmutableArray<Entity> itemEntities;
	private final ImmutableArray<Entity> hideEntities;
	private final ImmutableArray<Entity> damageEntities;
	private final ImmutableArray<Entity> uiEntities;
	private final OrthogonalTiledMapRenderer mapRenderer;
	private final Array<TiledMapTileLayer> tiledMapLayers;
	
	private final GLProfiler profiler;
	private final Box2DDebugRenderer b2dDebugRenderer;
	private final World world;
	private final RayHandler rayHandler;
	private final ObjectMap<String, EnumMap<AnimationType, Animation<Sprite>>> animationCache;
	private final ObjectMap<String, EnumMap<EffectType, Animation<Sprite>>> effectCache;

	private IntMap<Animation<Sprite>> mapAnimations;


	public GameRenderer (CoreGame game) {
		this.game = game;
		this.mapManager = game.getMapManager();
		this.assetManager = game.getAssetManager();
		this.viewport = game.getScreenViewport();
		this.gameCamera = game.getGameCamera();
		this.spriteBatch = game.getSpriteBatch();

        animationCache = new ObjectMap<String, EnumMap<AnimationType, Animation<Sprite>>>();
		effectCache = new ObjectMap<String, EnumMap<EffectType, Animation<Sprite>>>();

		gameObjectEntities = game.getEcsEngine().getEntitiesFor(Family.all(GameObjectComponent.class, Box2DComponent.class, AnimationComponent.class).get());
		itemEntities = game.getEcsEngine().getEntitiesFor(Family.all(ItemComponent.class, Box2DComponent.class).get());
		animatedEntities = game.getEcsEngine().getEntitiesFor(Family.all(AnimationComponent.class, Box2DComponent.class).get());
		weaponEntities = game.getEcsEngine().getEntitiesFor(Family.all(WeaponComponent.class, AnimationComponent.class, Box2DComponent.class).get());
		effectEntities = game.getEcsEngine().getEntitiesFor(Family.all(EffectComponent.class).get());
		hideEntities = game.getEcsEngine().getEntitiesFor(Family.all(HideLayerComponent.class, AnimationComponent.class).get());
		damageEntities = game.getEcsEngine().getEntitiesFor(Family.all(DamageAreaComponent.class,Box2DComponent.class, AnimationComponent.class).get());
		bossEntities = game.getEcsEngine().getEntitiesFor(Family.all(BossComponent.class).get());
		uiEntities = game.getEcsEngine().getEntitiesFor(Family.all(UIComponent.class).get());
		this.mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, game.getSpriteBatch());
		game.getMapManager().addMapListener(this);
		tiledMapLayers = new Array<TiledMapTileLayer>();
		rayHandler = game.getRayHandler();
		this.profiler = new GLProfiler(Gdx.graphics);
		profiler.enable();
		if (profiler.isEnabled()) {
			this.b2dDebugRenderer = game.getBox2DDebugRenderer();
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

		if( game.getScreenType() == ScreenType.GAME) {
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

			//render entities
			for (final Entity entity : gameObjectEntities) {
				renderGameObject(entity, delta);
			}

			for (final Entity entity : itemEntities) {
				renderItem(entity, delta);
			}

			for (final Entity entity : animatedEntities) {
				renderAnimated(entity, delta);
			}

			for (final Entity entity : weaponEntities) {
				renderWeapon(entity, delta);
			}

			for (final Entity entity : damageEntities) {
				renderArea(entity, delta);
			}

			for (final Entity entity: effectEntities) {
				rederEffect(entity, delta);
			}

			for (final Entity entity : hideEntities) {
				renderHideLayer(entity, delta);
			}

			for (final Entity entity : uiEntities) {
				renderUI(entity, delta);
			}

			spriteBatch.end();
			b2dDebugRenderer.render(world, viewport.getCamera().combined);
			rayHandler.setCombinedMatrix(gameCamera);
			if(this.mapManager.getCurrentMapType() == MapType.CAVE){
				rayHandler.updateAndRender();
			}
		}
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
			final Animation<Sprite> animation = getAnimation(aniComponent.path, aniComponent.aniType,(int) (aniComponent.width / UNIT_SCALE), (int) (aniComponent.height / UNIT_SCALE), aniComponent.isSquare);
			final Sprite frame = animation.getKeyFrame(aniComponent.aniTime);
			box2DComponent.renderPosition.lerp(box2DComponent.body.getPosition(), delta);
			frame.setBounds(box2DComponent.renderPosition.x - aniComponent.width * 0.5f, box2DComponent.renderPosition.y - aniComponent.height * 0.5f, aniComponent.width, aniComponent.height);
			if(aniComponent.isDamaged){
				frame.setColor(Color.RED);
				aniComponent.setDamaged(false, 3, delta);
			}
			else{
				frame.setColor(Color.WHITE);
			}
			frame.setAlpha(aniComponent.alpha);
			frame.draw(spriteBatch);
			aniComponent.isFinished = animation.isAnimationFinished(aniComponent.aniTime);
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

	private void renderArea(Entity entity, float delta) {
		final DamageAreaComponent damageAreaComponent = ECSEngine.damageAreaCmpMapper.get(entity);
		if (damageAreaComponent.type == EffectType.NONE) {
			return;
		}
	}

	private void rederEffect(Entity entity, float delta) {
		final EffectComponent effectComponent = ECSEngine.effectCmpMapper.get(entity);
		final Box2DComponent box2DComponent = ECSEngine.box2dCmpMapper.get(entity);
		final EntityComponent entityComponent = ECSEngine.playerCmpMapper.get(entity);
		if (effectComponent.type == EffectType.NONE) return;
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
            final TextureRegion[][] textureRegions = atlasRegion.split(effectComponent.type.getWidth()*6, effectComponent.type.getHeight()*6);
            final Array<Sprite> keyFrame = new Array<Sprite>();
            for (TextureRegion subRegion : textureRegions[0]) {
				Gdx.app.debug("effect", effectComponent.toString());
                final Sprite sprite = new Sprite(subRegion);
                sprite.setOriginCenter();
                keyFrame.add(sprite);
            }
            animation = new Animation<Sprite>(type.getFrameTime(), keyFrame, type.getMode());
            subCache.put(type, animation);
			Gdx.app.debug("effect", effectComponent.toString());
        }
		if (box2DComponent != null) {
			effectComponent.position.set(box2DComponent.body.getPosition());
		}
        final Sprite frame = animation.getKeyFrame(effectComponent.aniTime);
        frame.setBounds(effectComponent.position.x - effectComponent.width * 0.5f, effectComponent.position.y - effectComponent.height * 0.5f, effectComponent.width, effectComponent.height);
		frame.setOriginCenter();
		frame.setRotation(-(effectComponent.direction.getCode()+type.getFixDir()) * 90);
		frame.draw(spriteBatch);
		if (animation.getPlayMode() == Animation.PlayMode.NORMAL && animation.isAnimationFinished(effectComponent.aniTime)) {
				entity.add(new RemoveComponent());
		}
	}

	private void renderUI (Entity entity, float delta) {
		final UIComponent uiComponent = ECSEngine.uiCmpMapper.get(entity);
		if (!uiComponent.isShow) return;
		final Box2DComponent box2DComponent = ECSEngine.box2dCmpMapper.get(entity);
		final EnemyComponent enemyComponent = ECSEngine.enemyCmpMapper.get(entity);
		final AtlasRegion atlasUnderRegion = assetManager.get("HUD/miniheart.atlas", TextureAtlas.class).findRegion("LifeBarMiniUnder");
		final Sprite underframe;
		underframe = new Sprite(atlasUnderRegion);
		underframe.setBounds(box2DComponent.renderPosition.x - box2DComponent.width/2, box2DComponent.renderPosition.y + box2DComponent.height, 1, 2 * UNIT_SCALE);
		underframe.setOriginCenter();
		underframe.draw(spriteBatch);

		float width = ((float) enemyComponent.life) / enemyComponent.maxLife;

		final AtlasRegion atlasLifeRegion = assetManager.get("HUD/miniheart.atlas", TextureAtlas.class).findRegion("LifeBarMiniProgress");
		final Sprite lifeframe;
		lifeframe = new Sprite(atlasLifeRegion);
		lifeframe.setBounds(box2DComponent.renderPosition.x - box2DComponent.width/2, box2DComponent.renderPosition.y + box2DComponent.height, width, 2 * UNIT_SCALE);
		lifeframe.setOriginCenter();
		lifeframe.draw(spriteBatch);
	}

	private Animation<Sprite> getAnimation(String path, AnimationType aniType, int width, int height, boolean isSquare) {
		EnumMap<AnimationType, Animation<Sprite>> subCache = animationCache.get(path);

		if (subCache == null) {
			subCache = new EnumMap<AnimationType, Animation<Sprite>>(AnimationType.class);
			animationCache.put(path, subCache);
		}
		Animation<Sprite> animation = subCache.get(aniType);
		if (animation == null) {
				Gdx.app.debug(TAG, "Creating new animation of type " + aniType);
				final AtlasRegion atlasRegion = assetManager.get(path, TextureAtlas.class).findRegion(aniType.getAtlasKey());
				final TextureRegion[][] textureRegions = atlasRegion.split(3 * width, 3 * height);
				animation = new Animation<Sprite>(aniType.getFrameTime(), getKeyFrame(textureRegions, aniType.getColIndex(), isSquare), Animation.PlayMode.LOOP);
				subCache.put(aniType, animation);
		}
		return animation;
	}

	private Array<? extends Sprite> getKeyFrame(TextureRegion[][] textureRegions, int index, boolean isSquare) {
		final Array<Sprite> keyFrame = new Array<Sprite>();
		if (isSquare == true) {
			for (TextureRegion[] subRegions : textureRegions) {
				final Sprite sprite = new Sprite(subRegions[index]);
				sprite.setOriginCenter();
				keyFrame.add(sprite);
			}
		}
		else {
			TextureRegion[] subRegions = textureRegions[index];
			for (int i=0; i<subRegions.length; i++) {
				final Sprite sprite = new Sprite(subRegions[i]);
				sprite.setOriginCenter();
				keyFrame.add(sprite);
			}
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
