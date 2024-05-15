package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.BulletComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.RemoveComponent;

public class BulletMovementSystem extends IteratingSystem {
    private static final float BULLET_SPEED = 2.0f; // Tốc độ di chuyển của đạn

    public BulletMovementSystem() {
        super(Family.all(BulletComponent.class, Box2DComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BulletComponent bulletComponent = ECSEngine.bulletCmpMapper.get(entity);
        Box2DComponent box2DComponent = ECSEngine.box2dCmpMapper.get(entity);

        // Kiểm tra xem đạn đã di chuyển quãng đường 5 đơn vị từ vị trí bắt đầu hay chưa
        if (box2DComponent.body.getPosition().dst(bulletComponent.start) >= 5) {
            // Nếu có, loại bỏ đạn khỏi hệ thống
            entity.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
        } else {
            // Nếu không, di chuyển đạn
            box2DComponent.body.setLinearVelocity(bulletComponent.dir);
        }
    }
}
