package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.enemy.EnemySkillType;
import com.mygdx.game.character.enemy.system.ProjectileSystem;
import com.mygdx.game.effect.DamageArea;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.*;
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
            if (enemyCom.type.getSkillType() == EnemySkillType.PROJECTILE) {
                ProjectileSystem.Projectile(game, enemyCom, enemyPos, playerPos, b2dComponent);
            }
        }

        if(enemyCom.isAttack)
            if(!enemyCom.isAttacking) {//Đánh thường
                b2dComponent.body.applyLinearImpulse(-b2dComponent.body.getLinearVelocity().x*b2dComponent.body.getMass(),
                        -b2dComponent.body.getLinearVelocity().y*b2dComponent.body.getMass(),
                        b2dComponent.body.getPosition().x,
                        b2dComponent.body.getPosition().y, true);
                b2dComponent.body.applyForceToCenter(Vector2.Zero, true);

                float localPositionX = enemyCom.direction == DirectionType.RIGHT ?  b2dComponent.width/4: -b2dComponent.width/4;
                Vector2 position = new Vector2(b2dComponent.renderPosition.x + localPositionX, b2dComponent.renderPosition.y);
                DamageArea area = new DamageArea(position, CoreGame.BIT_ENEMY, enemyCom.direction, 16, 22, enemyCom.attack, EffectType.NONE, false, 0);
                game.getEcsEngine().createDamageArea(area);
                enemyCom.isAttacking=true;
            }
        if(enemyCom.isAttack && enemyCom.time>=enemyCom.reload){
            destroyArea();
            enemyCom.isAttack = false;
            enemyCom.isAttacking = false;
            enemyCom.time = 0;
        }

    }

    public Entity getPlayerEntity() {
        ImmutableArray<Entity> entities = game.getEcsEngine().getEntitiesFor(Family.all(PlayerComponent.class).get());
        if (entities.size() > 0) {
            return entities.first();
        }
        return null;
    }
    public void destroyArea () {
        ImmutableArray<Entity> areaEntities = game.getEcsEngine().getEntitiesFor(Family.all(DamageAreaComponent.class, Box2DComponent.class).get());
        for (Entity areaEntity: areaEntities) {
            DamageAreaComponent damageAreaCmp = areaEntity.getComponent(DamageAreaComponent.class);
            if (damageAreaCmp.owner == CoreGame.BIT_ENEMY)
                areaEntity.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
        }
    }
}
