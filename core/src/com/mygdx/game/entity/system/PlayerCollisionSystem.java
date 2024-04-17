package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.CoreGame;
import com.mygdx.game.WorldContactListener.PlayerCollisionListener;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.GameObjectComponent;
import com.mygdx.game.entity.component.RemoveComponent;
import com.mygdx.game.map.GameObjectType;

public class PlayerCollisionSystem extends IteratingSystem implements PlayerCollisionListener {

    public PlayerCollisionSystem(final CoreGame game) {
        super(Family.all(RemoveComponent.class).get());

        game.getWorldContactListener().addPlayerCollisionListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        getEngine().removeEntity(entity);
    }

    @Override
    public void playerCollision(Entity player, Entity gameObj) {
        final GameObjectComponent gameObjCmp = ECSEngine.gameObjCmpMapper.get(gameObj);
        if (gameObjCmp.type == GameObjectType.TRAP) {
        	
        }
        if (gameObjCmp.type == GameObjectType.TRAP) {
            // decrease life
            gameObj.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));;
        }
    }
}
