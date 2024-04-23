package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.EffectComponent;

public class EffectSystem extends IteratingSystem {
    public EffectSystem(CoreGame game) {
        super(Family.all(EffectComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final EffectComponent effectComponent = ECSEngine.effectCmpMapper.get(entity);
        if (effectComponent != null) {
            effectComponent.aniTime += deltaTime;
        }
    }
}
