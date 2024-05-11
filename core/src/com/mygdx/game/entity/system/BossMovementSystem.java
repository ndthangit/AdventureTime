package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.BossComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.PlayerComponent;

public class BossMovementSystem extends IteratingSystem {
    public BossMovementSystem(CoreGame game) {
        super(Family.all(AnimationComponent.class, BossComponent.class, Box2DComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        if (bossCmp.isAttack) {

        } else if (bossCmp.isSkill1) {

        } else if (bossCmp.isSkill2) {

        } else {

        }
    }
}
