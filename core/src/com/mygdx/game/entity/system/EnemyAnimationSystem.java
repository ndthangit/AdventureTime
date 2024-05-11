package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class EnemyAnimationSystem extends IteratingSystem {
    public EnemyAnimationSystem(Family family) {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float v) {

    }
}
