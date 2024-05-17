package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.CoreGame;
import com.mygdx.game.WorldContactListener.CollisionListener;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.*;
import com.mygdx.game.items.food.Food;
import com.mygdx.game.items.food.FoodType;
import com.mygdx.game.items.weapon.Weapon;
import com.mygdx.game.map.GameObjectType;
import com.mygdx.game.map.MapType;
import com.mygdx.game.screens.ScreenType;

import static com.mygdx.game.items.RandomItem.randomFood;

public class CollisionSystem extends IteratingSystem implements CollisionListener {
    CoreGame game;

    public CollisionSystem(final CoreGame game) {
        super(Family.all(RemoveComponent.class).get());
        this.game = game;
        game.getWorldContactListener().addPlayerCollisionListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
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
//            Effect effect = new Effect();
//            game.getEcsEngine().createEffect(effect);
            gameObj.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
        }
    }

    @Override
    public void playerVSEnemy(Entity player, Entity enemy) {
        final PlayerComponent playerCmp = ECSEngine.playerCmpMapper.get(player);
        final EnemyComponent enemyCmp = ECSEngine.enemyCmpMapper.get(enemy);
        final BossComponent bossCmp = ECSEngine.bossCmpMapper.get(enemy);
        if (playerCmp.vincible) return;
        int damage = enemyCmp != null ? enemyCmp.attack : bossCmp.attack;
        playerCmp.life =  Math.max(playerCmp.life - damage, 0);
        game.getAudioManager().playAudio(AudioType.HIT);
        if (playerCmp.life <= 0) {
            player.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
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
        Gdx.app.debug("Boss" , "ok");
        if (enemyCmp != null) {
            box2dEnCmp.body.applyLinearImpulse(-box2dEnCmp.body.getLinearVelocity().x*box2dEnCmp.body.getMass(), -box2dEnCmp.body.getLinearVelocity().y*box2dEnCmp.body.getMass(), box2dEnCmp.body.getPosition().x, box2dEnCmp.body.getPosition().y, true);
            enemyCmp.stop = true;
            enemyCmp.life -= weaponCmp.attack;
            if (enemyCmp.life <= 0) {
                // thêm tạo cửa sổ để them do
                FoodType foodType = randomFood();
                Food food = new Food(foodType, box2dEnCmp.body.getPosition());
                game.getEcsEngine().getItemArray().add(food);
                enemy.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
            }
        }
        else {
            bossCmp.life -= weaponCmp.attack;
            bossCmp.isHit = true;
            aniCmp.isFinished = false;
            if (bossCmp.life <= 0) {
                game.getAudioManager().playAudio(AudioType.KILL);
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
        if (((damageAreaCmp.owner & CoreGame.BIT_BOSS) == CoreGame.BIT_BOSS ||
            (damageAreaCmp.owner & CoreGame.BIT_ENEMY) == CoreGame.BIT_ENEMY) &&
                (!playerCmp.vincible)) {
            damageArea.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
            playerCmp.life =  Math.max(playerCmp.life - damageAreaCmp.damage, 0);
            game.getAudioManager().playAudio(AudioType.HIT);
            if (playerCmp.life <= 0) {
                player.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
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
        if ((damageAreaCmp.owner & CoreGame.BIT_PLAYER) == CoreGame.BIT_PLAYER) {
            damageArea.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
            if (enemyCmp != null) {
                b2dCmp.body.applyLinearImpulse(-b2dCmp.body.getLinearVelocity().x*b2dCmp.body.getMass(), -b2dCmp.body.getLinearVelocity().y*b2dCmp.body.getMass(), b2dCmp.body.getPosition().x, b2dCmp.body.getPosition().y, true);
                enemyCmp.life -= damageAreaCmp.damage;
                enemyCmp.stop = true;
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
                bossCmp.life -= damageAreaCmp.damage;
                bossCmp.isHit = true;
                aniCmp.isFinished = false;
                if (bossCmp.life <= 0) {
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


}
