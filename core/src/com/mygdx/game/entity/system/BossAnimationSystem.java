package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.BossComponent;
import com.mygdx.game.entity.component.Box2DComponent;

public class BossAnimationSystem extends IteratingSystem {
    public BossAnimationSystem(CoreGame game) {
        super(Family.all(AnimationComponent.class, BossComponent.class, Box2DComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {

    }
}
