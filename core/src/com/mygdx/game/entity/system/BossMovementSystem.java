package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.ai.SteerableAgent;
import com.mygdx.game.character.boss.BossType;
import com.mygdx.game.character.boss.system.GiantBlueSamurai;
import com.mygdx.game.character.boss.system.GiantSpirit;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.*;
import com.mygdx.game.view.AnimationType;
import com.mygdx.game.view.DirectionType;


public class BossMovementSystem extends IteratingSystem {

    private final CoreGame game;

    public BossMovementSystem(CoreGame game) {
        super(Family.all(AnimationComponent.class, BossComponent.class, Box2DComponent.class).get());
        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        Box2DComponent b2dComponent = ECSEngine.box2dCmpMapper.get(entity);
        AnimationComponent aniCmp = ECSEngine.aniCmpMapper.get(entity);
        // Đuổi theo Player

        if (getPlayerEntity() == null) return;
        Box2DComponent b2dPlayer = ECSEngine.box2dCmpMapper.get(getPlayerEntity());

        Vector2 playerPos = b2dPlayer.renderPosition;
        Vector2 bossPos = b2dComponent.body.getPosition();

        bossCmp.time += deltaTime;
        if (playerPos.x < bossPos.x) {
            bossCmp.direction = DirectionType.LEFT;
        }
        else if (playerPos.x > bossPos.x) {
            bossCmp.direction = DirectionType.RIGHT;
        }

        switch (bossCmp.type){
            case GIANTBLUESAMURAI:
                GiantBlueSamurai.GBS_movement(game, entity, getPlayerEntity(), deltaTime);
                break;
            case GIANTSPIRIT:
                GiantSpirit.GS_movement(game, entity, getPlayerEntity(), deltaTime);
                break;
        }
    }
    public Entity getPlayerEntity() {
        ImmutableArray<Entity> entities = game.getEcsEngine().getEntitiesFor(Family.all(PlayerComponent.class).get());
        if (entities.size() > 0) {
            return entities.first();
        }
        return null;
    }

}
