package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.CoreGame;
import com.mygdx.game.WorldContactListener.PlayerCollisionListener;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.GameObjectComponent;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.entity.component.RemoveComponent;
import com.mygdx.game.map.GameObjectType;

public class PlayerCollisionSystem extends IteratingSystem implements PlayerCollisionListener {
    CoreGame game;
    public PlayerCollisionSystem(final CoreGame game) {
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
        if (gameObjCmp.type == GameObjectType.TRAP) {
            // decrease life
            if (playerCmp.life > 0) {
                playerCmp.life -= 1;
                game.getGameUI().updateHeart();
            }
            gameObj.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));;
        }
    }

    @Override
    public void weaponCollision(Entity weapon, Entity gameObj) {
        final GameObjectComponent gameObjCmp = ECSEngine.gameObjCmpMapper.get(gameObj);
        if (gameObjCmp.type == GameObjectType.TRAP) {
            gameObj.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));;
        }
    }
}
