package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.boss.system.GiantBlueSamurai;
import com.mygdx.game.character.boss.system.GiantSpirit;
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
        switch (bossCmp.type) {
            case GIANTBLUESAMURAI:
                GiantBlueSamurai.GBS_animation(bossCmp, animationCmp, box2DCmp);
                break;
            case GIANTSPIRIT:
                GiantSpirit.GS_animation(bossCmp, animationCmp, box2DCmp);
                break;
        }

    }
}
