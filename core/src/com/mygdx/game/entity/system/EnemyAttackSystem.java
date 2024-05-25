package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.enemy.EnemySkillType;
import com.mygdx.game.character.enemy.system.AttackSystem;
import com.mygdx.game.character.enemy.system.ProjectileSystem;
import com.mygdx.game.character.enemy.system.ProtectSystem;
import com.mygdx.game.effect.DamageArea;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.BossComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.EnemyComponent;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.view.DirectionType;

public class EnemyAttackSystem extends IteratingSystem {

    private final CoreGame game;
    public EnemyAttackSystem(CoreGame game) {
        super(Family.all(EnemyComponent.class, Box2DComponent.class).get());
        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        if (getPlayerEntity() == null) return;
        Box2DComponent playerCom = ECSEngine.box2dCmpMapper.get(getPlayerEntity());
        EnemyComponent enemyCom = ECSEngine.enemyCmpMapper.get(entity);
        Box2DComponent b2dComponent = ECSEngine.box2dCmpMapper.get(entity);

        Vector2 playerPos = playerCom.renderPosition;
        Vector2 enemyPos = b2dComponent.body.getPosition();
        float distance = playerPos.dst(enemyPos);
        if (!enemyCom.stop && enemyCom.focus) {
            switch (enemyCom.type.getSkillType()) {
                case PROJECTILE:
                    ProjectileSystem.Projectile(game, enemyCom, enemyPos, playerPos, b2dComponent);
                    break;
                case ATTACK:
                    AttackSystem.attackSystem(game, enemyCom, b2dComponent);
                    break;
                case PROTECT:
                    Entity bossEntity = getBossEntity();
                    ProtectSystem.protect(game, enemyCom, bossEntity, v);
                    break;
            }
        }



    } // am thanh khi dung ki nang

    public Entity getPlayerEntity() {
        ImmutableArray<Entity> entities = game.getEcsEngine().getEntitiesFor(Family.all(PlayerComponent.class).get());
        if (entities.size() > 0) {
            return entities.first();
        }
        return null;
    }

    public Entity getBossEntity() {
        ImmutableArray<Entity> entities = game.getEcsEngine().getEntitiesFor(Family.all(BossComponent.class).get());
        if (entities.size() > 0) {
            return entities.first();
        }
        return null;
    }
}
