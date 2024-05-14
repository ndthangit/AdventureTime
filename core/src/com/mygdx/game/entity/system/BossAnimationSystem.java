package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.BossComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.view.AnimationType;
import com.mygdx.game.view.DirectionType;

public class BossAnimationSystem extends IteratingSystem {
    public BossAnimationSystem(CoreGame game) {
        super(Family.all(AnimationComponent.class, BossComponent.class, Box2DComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        Box2DComponent box2DCmp = ECSEngine.box2dCmpMapper.get(entity);
        AnimationComponent animationCmp = ECSEngine.aniCmpMapper.get(entity);
        if (bossCmp.isHit) {
            animationCmp.aniType = AnimationType.B_HIT;
        } else if (bossCmp.isCharge) {
            if (bossCmp.direction == DirectionType.LEFT) {
                animationCmp.aniType = AnimationType.CHARGE_LEFT;
            }
            else {
                animationCmp.aniType = AnimationType.CHARGE_RIGHT;
            }

        } else if (bossCmp.isAttack) {
            if (bossCmp.direction == DirectionType.LEFT) {
                animationCmp.aniType = AnimationType.B_ATTACK_LEFT;
            }
            else {
                animationCmp.aniType = AnimationType.B_ATTACK_RIGHT;
            }
        } else if (bossCmp.isSkill1) {

        } else if (bossCmp.isSkill2) {

        } else {
            if (box2DCmp.body.getLinearVelocity().equals(Vector2.Zero)) {
                animationCmp.aniType = AnimationType.IDLE_DOWN;
            }
            else {
                animationCmp.aniType = AnimationType.DOWN;
            }
        }
    }
}
