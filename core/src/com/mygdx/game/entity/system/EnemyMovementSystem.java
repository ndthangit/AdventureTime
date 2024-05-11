package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.EnemyComponent;
import com.mygdx.game.entity.component.PlayerComponent;

public class EnemyMovementSystem extends IteratingSystem {
    public EnemyMovementSystem(CoreGame game) {
        super(Family.all(EnemyComponent.class, Box2DComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        final EnemyComponent enemyCompnent = ECSEngine.enemyCmpMapper.get(entity);
        final Box2DComponent b2dComponent = ECSEngine.box2dCmpMapper.get(entity);

        b2dComponent.body.applyLinearImpulse( 0 - b2dComponent.body.getLinearVelocity().x * b2dComponent.body.getMass(),
                0
                        - b2dComponent.body.getLinearVelocity().y * b2dComponent.body.getMass(),
                b2dComponent.body.getWorldCenter().x,
                b2dComponent.body.getWorldCenter().y, true);
    }
}
