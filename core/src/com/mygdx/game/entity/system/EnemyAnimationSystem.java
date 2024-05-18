package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.EnemyComponent;
import com.mygdx.game.view.AnimationType;
import com.mygdx.game.view.DirectionType;

public class EnemyAnimationSystem extends IteratingSystem {
    public EnemyAnimationSystem(CoreGame game) {
        super(Family.all(EnemyComponent.class, Box2DComponent.class, AnimationComponent.class).get());

    }

    @Override
    protected void processEntity(Entity entity, float v) {
        Box2DComponent box2DComponent = ECSEngine.box2dCmpMapper.get(entity);
        AnimationComponent animationComponent= ECSEngine.aniCmpMapper.get(entity);
        EnemyComponent enemyComponent = ECSEngine.enemyCmpMapper.get(entity);
        if (box2DComponent.body.getLinearVelocity().isZero()) {
            animationComponent.aniTime = 0;
        }
        if (Math.abs(box2DComponent.body.getLinearVelocityFromLocalPoint(box2DComponent.renderPosition).x) > Math.abs(box2DComponent.body.getLinearVelocityFromLocalPoint(box2DComponent.renderPosition).y)) {
            if (box2DComponent.body.getLinearVelocity().x > 0) {
                animationComponent.aniType = AnimationType.RIGHT;
                enemyComponent.direction = DirectionType.RIGHT;
            }
            else {
                animationComponent.aniType = AnimationType.LEFT;
                enemyComponent.direction = DirectionType.LEFT;
            }
        }
        else {
            if (box2DComponent.body.getLinearVelocity().y > 0) {
                animationComponent.aniType = AnimationType.UP;
                enemyComponent.direction = DirectionType.UP;
            }
            else {
                animationComponent.aniType = AnimationType.DOWN;
                enemyComponent.direction = DirectionType.DOWN;
            }
        }
    }
}
