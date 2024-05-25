package com.mygdx.game.entity.system;

import box2dLight.PointLight;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.WorldContactListener.CollisionListener;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.character.boss.Boss;
import com.mygdx.game.character.boss.BossType;
import com.mygdx.game.effect.DamageArea;
import com.mygdx.game.effect.Effect;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.*;
import com.mygdx.game.items.food.Food;
import com.mygdx.game.items.food.FoodType;
import com.mygdx.game.items.special.Special;
import com.mygdx.game.items.special.SpecialType;
import com.mygdx.game.items.weapon.Weapon;
import com.mygdx.game.items.weapon.WeaponType;
import com.mygdx.game.map.GameObject;
import com.mygdx.game.map.GameObjectType;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.MapType;
import com.mygdx.game.screens.ScreenType;
import com.mygdx.game.view.DirectionType;

import static com.mygdx.game.CoreGame.BIT_PLAYER;
import static com.mygdx.game.CoreGame.UNIT_SCALE;
import static com.mygdx.game.items.RandomItem.randomFood;
import static com.mygdx.game.items.RandomItem.randomHotWeapon;

public class CollisionSystem extends IteratingSystem implements CollisionListener {
    CoreGame game;

    public CollisionSystem(final CoreGame game) {
        super(Family.all(RemoveComponent.class).get());
        this.game = game;
        game.getWorldContactListener().addPlayerCollisionListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DamageAreaComponent damageAreaComponent = ECSEngine.damageAreaCmpMapper.get(entity);
        Box2DComponent box2DComponent = ECSEngine.box2dCmpMapper.get(entity);
        if (damageAreaComponent != null && damageAreaComponent.upgrade) {
            DamageArea damageArea = new DamageArea(box2DComponent.body.getPosition(), BIT_PLAYER, DirectionType.DOWN, 32, 32, 4, EffectType.BLAST, false, 0, false);
            game.getEcsEngine().createDamageArea(damageArea);
        }
        getEngine().removeEntity(entity);
    }

    @Override
    public void playerCollision(Entity player, Entity gameObj) {
        final GameObjectComponent gameObjCmp = ECSEngine.gameObjCmpMapper.get(gameObj);
        final PlayerComponent playerCmp = ECSEngine.playerCmpMapper.get(player);
    }

    @Override
    public void weaponCollision(Entity weapon, Entity gameObj) {
        final GameObjectComponent gameObjCmp = ECSEngine.gameObjCmpMapper.get(gameObj);
        final Box2DComponent box2DCmp = ECSEngine.box2dCmpMapper.get(gameObj);
        if (gameObjCmp.type == GameObjectType.GRASS) {
            box2DCmp.body.getPosition();
            Effect effect = new Effect(EffectType.CUTLEAVES, CoreGame.BIT_GAME_OBJECT , box2DCmp.body.getPosition(), DirectionType.DOWN);
            game.getEcsEngine().createEffect(effect);
            gameObj.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
        }
        if (gameObjCmp.type == GameObjectType.CHEST) {
            GameObjectComponent gameObjectCmp = ECSEngine.gameObjCmpMapper.get(gameObj);
            gameObjectCmp.gameObject.setIsUsed(true);
            gameObj.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
//            WeaponType weaponType = RandomItem.randomWeapon();
            if (game.getMapManager().getCurrentMapType() == MapType.CAVE) {
                Special special = new Special(SpecialType.LIFEPOT, box2DCmp.body.getPosition());
                game.getEcsEngine().getItemArray().add(special);
            }
            else {
                WeaponType weaponType = randomHotWeapon();
                Vector2 positionObj = box2DCmp.body.getPosition();
                positionObj.y -= 1f;
                positionObj.x -= 0f;
                Weapon weapon1 = new Weapon(weaponType, new Effect(EffectType.SLASHCURVED, CoreGame.BIT_PLAYER, positionObj, DirectionType.DOWN), positionObj, DirectionType.DOWN);
                game.getEcsEngine().getItemArray().add(weapon1);
            }
        }
    }

    @Override
    public void playerVSEnemy(Entity player, Entity enemy) {
        final PlayerComponent playerCmp = ECSEngine.playerCmpMapper.get(player);
        final EnemyComponent enemyCmp = ECSEngine.enemyCmpMapper.get(enemy);
        final BossComponent bossCmp = ECSEngine.bossCmpMapper.get(enemy);
        final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(player);
        if (playerCmp.vincible) return;

        int damage = enemyCmp != null ? enemyCmp.attack : bossCmp.attack;
        playerCmp.life =  Math.max(playerCmp.life - damage, 0);
        game.getAudioManager().playAudio(AudioType.HIT);

        PlayerCameraSystem.cameraShake(game, 1.f, 0.5f, Gdx.graphics.getDeltaTime() );
        aniComponent.isDamaged = true;

        if (playerCmp.life <= 0) {
            reset(player, playerCmp);
        }
        game.getGameUI().updateHeart();
    }

    @Override
    public void weaponVSEnemy(Entity weapon, Entity enemy) {
        final WeaponComponent weaponCmp = ECSEngine.weaponCmpMapper.get(weapon);
        final EnemyComponent enemyCmp = ECSEngine.enemyCmpMapper.get(enemy);
        final BossComponent bossCmp = ECSEngine.bossCmpMapper.get(enemy);
        final AnimationComponent aniCmp = ECSEngine.aniCmpMapper.get(enemy);
        final Box2DComponent box2dEnCmp = ECSEngine.box2dCmpMapper.get(enemy);
        final UIComponent uiCmp = ECSEngine.uiCmpMapper.get(enemy);

        aniCmp.isDamaged = true;
        if (enemyCmp != null) {
            box2dEnCmp.body.applyLinearImpulse(-box2dEnCmp.body.getLinearVelocity().x*box2dEnCmp.body.getMass(), -box2dEnCmp.body.getLinearVelocity().y*box2dEnCmp.body.getMass(), box2dEnCmp.body.getPosition().x, box2dEnCmp.body.getPosition().y, true);
            enemyCmp.stop = true;
            enemyCmp.life -= weaponCmp.attack;
            uiCmp.isShow = true;
            game.getAudioManager().playAudio(AudioType.FX);
            if (enemyCmp.life <= 0) {
                // thêm tạo cửa sổ để them do
                FoodType foodType = randomFood();
                Food food = new Food(foodType, box2dEnCmp.body.getPosition());
                game.getEcsEngine().getItemArray().add(food);
                enemy.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
            }
        }
        else {
            if (bossCmp.invincible) return;
            bossCmp.life -= weaponCmp.attack;
            bossCmp.isHit = true;
            aniCmp.isFinished = false;
            game.getGameUI().updateLifeBar(bossCmp);
            if (bossCmp.life <= 0) {
                game.getAudioManager().playAudio(AudioType.KILL);
                game.getMapManager().getCurrentMap().setBossKilled(true);
                game.getGameUI().destroyLifeBar();
                switch (bossCmp.type) {
                    case GIANTSPIRIT:
                        Special special = new Special(SpecialType.SCROLLFIRE, box2dEnCmp.body.getPosition());
                        game.getEcsEngine().getItemArray().add(special);
                        break;
                    case GIANTBLUESAMURAI:
                        game.setScreen(ScreenType.END);
                }
                enemy.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
            }
        }
    }

    @Override
    public void playerVSItem(Entity player, Entity item) {
        final PlayerComponent playerCmp = ECSEngine.playerCmpMapper.get(player);
        final ItemComponent itemCmp = ECSEngine.itemCmpMapper.get(item);
        if (itemCmp.item instanceof Food) {
            if (playerCmp.addItem(itemCmp.item)) {
                item.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
                game.getGameUI().updateBag();
                game.getGameUI().updateNumTable();
                game.getAudioManager().playAudio(AudioType.GOLD1);
            }
        }
        else if (itemCmp.item instanceof Weapon) {
            playerCmp.addItem(itemCmp.item);
            game.getAudioManager().playAudio(AudioType.GOLD1);
            item.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
        }
        else if (itemCmp.item instanceof Special) {
            playerCmp.addItem(itemCmp.item);
            game.getAudioManager().playAudio(AudioType.GOLD1);
            item.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
        }
    }

    @Override
    public void playerVSDoor(Entity player, Entity door) {
        final DoorLayerComponent doorLayerComponent = ECSEngine.doorLayerCmpMapper.get(door);
        game.getMapManager().setNextMapType(MapType.valueOf(doorLayerComponent.name));
        game.setScreen(ScreenType.LOAD);
    }

    @Override
    public void playerVSDamageArea(Entity player, Entity damageArea) {
        DamageAreaComponent damageAreaCmp = ECSEngine.damageAreaCmpMapper.get(damageArea);
        PlayerComponent playerCmp = ECSEngine.playerCmpMapper.get(player);
        AnimationComponent aniCmp = ECSEngine.aniCmpMapper.get(player);
        if (((damageAreaCmp.owner & CoreGame.BIT_BOSS) == CoreGame.BIT_BOSS ||
            (damageAreaCmp.owner & CoreGame.BIT_ENEMY) == CoreGame.BIT_ENEMY) &&
                (!playerCmp.vincible)) {
            if (damageAreaCmp.isbullet)
                damageArea.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
            playerCmp.life =  Math.max(playerCmp.life - damageAreaCmp.damage, 0);
            if (damageAreaCmp.damage != 0)
                game.getAudioManager().playAudio(AudioType.HIT);

            aniCmp.isDamaged = true;

            if (playerCmp.life <= 0) {;
                reset(player, playerCmp);
            }
            game.getGameUI().updateHeart();
        }
    }

    @Override
    public void damageAreaVSEnemy(Entity damageArea, Entity enemy) {
        DamageAreaComponent damageAreaCmp = ECSEngine.damageAreaCmpMapper.get(damageArea);
        EnemyComponent enemyCmp = ECSEngine.enemyCmpMapper.get(enemy);
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(enemy);
        AnimationComponent aniCmp = ECSEngine.aniCmpMapper.get(enemy);
        Box2DComponent b2dCmp = ECSEngine.box2dCmpMapper.get(enemy);
        UIComponent uiCmp = ECSEngine.uiCmpMapper.get(enemy);

        if ((damageAreaCmp.owner & CoreGame.BIT_PLAYER) == CoreGame.BIT_PLAYER) {
            aniCmp.isDamaged = true;
            if (damageAreaCmp.type == EffectType.BIG_KUNAI) {
                b2dCmp.light = new PointLight(game.getRayHandler(), 64, new Color(1,1,1,0.5f), 3, b2dCmp.body.getPosition().x, b2dCmp.body.getPosition().y);
                b2dCmp.light.attachToBody(b2dCmp.body);
            }
            game.getAudioManager().playAudio(AudioType.FX);
            if(damageAreaCmp.isbullet) {
                damageArea.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
            }
            if (enemyCmp != null) {
                b2dCmp.body.applyLinearImpulse(-b2dCmp.body.getLinearVelocity().x*b2dCmp.body.getMass(), -b2dCmp.body.getLinearVelocity().y*b2dCmp.body.getMass(), b2dCmp.body.getPosition().x, b2dCmp.body.getPosition().y, true);
                enemyCmp.life -= damageAreaCmp.damage;
                uiCmp.isShow = true;
                if (enemyCmp.life <= 0) {
                    // thêm tạo cửa sổ để them do
                    FoodType foodType = randomFood();
                    Food food = new Food(foodType, b2dCmp.body.getPosition());
                    game.getEcsEngine().getItemArray().add(food);
                    game.getAudioManager().playAudio(AudioType.KILL);

                    enemy.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
                }
            }
            else {
                if (bossCmp.invincible) return;
                bossCmp.life -= damageAreaCmp.damage;
                bossCmp.isHit = true;
                aniCmp.isFinished = false;
                game.getGameUI().updateLifeBar(bossCmp);
                if (bossCmp.life <= 0) {
                    game.getAudioManager().playAudio(AudioType.KILL);
                    game.getMapManager().getCurrentMap().setBossKilled(true);
                    game.getGameUI().destroyLifeBar();
                    switch (bossCmp.type) {
                        case GIANTSPIRIT:
                            Special special = new Special(SpecialType.SCROLLFIRE, b2dCmp.body.getPosition());
                            game.getEcsEngine().getItemArray().add(special);
                            break;
                        case GIANTBLUESAMURAI:
                            game.setScreen(ScreenType.END);
                    }
                    enemy.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
                }
            }
        }
    }

    @Override
    public void damageAreaVSGround(Entity damageArea) {
        DamageAreaComponent damageAreaComponent = ECSEngine.damageAreaCmpMapper.get(damageArea);
        if (damageAreaComponent.isbullet) {
            damageArea.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
        }
    }

    @Override
    public void damageAreaVSWeapon(Entity damageArea) {
        DamageAreaComponent damageAreaComponent = ECSEngine.damageAreaCmpMapper.get(damageArea);
        if (damageAreaComponent.isbullet && damageAreaComponent.owner != CoreGame.BIT_PLAYER) {
            damageArea.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
        }
    }

    public void reset(Entity player, PlayerComponent playerCmp) {
        for (int i=0; i<4; i++) {
            playerCmp.inventory[i] = null;
        }
        playerCmp.indWeapon = 0;
        playerCmp.weaponList.clear();
        game.getGameUI().updateBag();
        game.getGameUI().updateNumTable();
        game.getGameUI().destroyLifeBar();
        for (Map map: game.getMapManager().getMapCache().values()) {
            for (GameObject gameObject : map.getGameObjects()) {
                gameObject.setIsUsed(false);
            }
            map.setBossKilled(false);
        }
        player.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
    }

}
