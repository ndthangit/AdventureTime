package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.DamageAreaComponent;
import com.mygdx.game.entity.component.EffectComponent;
import com.mygdx.game.entity.component.RemoveComponent;

public class DamageAreaSystem extends IteratingSystem {
    public DamageAreaSystem(CoreGame game) {
        super(Family.all(DamageAreaComponent.class, Box2DComponent.class).get());

    }

    @Override
    protected void processEntity(Entity entity, float v) {
        DamageAreaComponent damageAreaCmp = ECSEngine.damageAreaCmpMapper.get(entity);
        Box2DComponent box2DCmp = ECSEngine.box2dCmpMapper.get(entity);
        if (damageAreaCmp.isbullet) {
            Vector2 velocity = Vector2.Zero;
            switch (damageAreaCmp.direction) {
                case LEFT:
                    velocity = new Vector2(-damageAreaCmp.bulletSpeed, 0);
                    break;
                case RIGHT:
                    velocity = new Vector2(damageAreaCmp.bulletSpeed, 0);
                    break;
                case UP:
                    velocity = new Vector2(0, damageAreaCmp.bulletSpeed);
                    break;
                default:
                    velocity = new Vector2(0, -damageAreaCmp.bulletSpeed);
                    break;
            }
            box2DCmp.body.setLinearVelocity(velocity);

        }
    }
}
