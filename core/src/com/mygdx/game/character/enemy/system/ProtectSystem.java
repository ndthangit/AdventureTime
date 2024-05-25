package com.mygdx.game.character.enemy.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.BossComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.EffectComponent;
import com.mygdx.game.entity.component.EnemyComponent;

public class ProtectSystem {
    public static void protect(CoreGame game, EnemyComponent enemyCom, Entity boss , float deltaTime) {
        if (boss == null) return;
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(boss);
        EffectComponent effectCmp = ECSEngine.effectCmpMapper.get(boss);
        enemyCom.reload += deltaTime;
        if (enemyCom.reload > 5) {
            enemyCom.reload = 0;
            bossCmp.invincible = true;
            effectCmp.type = EffectType.SHIELD;
        }
    }
}
