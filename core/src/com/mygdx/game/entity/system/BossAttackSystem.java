package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.boss.BossType;
import com.mygdx.game.character.boss.system.GiantBlueSamurai;
import com.mygdx.game.effect.DamageArea;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.*;
import com.mygdx.game.view.DirectionType;

import static com.mygdx.game.character.boss.system.GiantBlueSamurai.GBS_attack;

public class BossAttackSystem extends IteratingSystem {
    private final CoreGame game;
    private boolean attacking = false;

    public BossAttackSystem(CoreGame game) {
        super(Family.all(AnimationComponent.class, BossComponent.class, Box2DComponent.class).get());
        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        if (bossCmp.type == BossType.GIANTBLUESAMURAI) {
            GBS_attack(game, entity, v);
        }
        if (!bossCmp.isAttack && !bossCmp.isCharge) {
            //huy damage area cho don danh thuong
            destroyArea();
            if (bossCmp.type == BossType.GIANTBLUESAMURAI) {
                GiantBlueSamurai.attacking = false;
            }
            bossCmp.resetState();
        }

    }

    public void destroyArea () {
        ImmutableArray<Entity> areaEntities = game.getEcsEngine().getEntitiesFor(Family.all(DamageAreaComponent.class, Box2DComponent.class).get());
        for (Entity areaEntity: areaEntities) {
            DamageAreaComponent damageAreaCmp = areaEntity.getComponent(DamageAreaComponent.class);
            if (!damageAreaCmp.isbullet && damageAreaCmp.owner == CoreGame.BIT_BOSS)
                areaEntity.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
        }
    }
}
