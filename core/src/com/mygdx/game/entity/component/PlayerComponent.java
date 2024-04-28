package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.mygdx.game.character.player.PlayerType;
import com.mygdx.game.items.weapon.Weapon;
import com.mygdx.game.view.AnimationType;

public class PlayerComponent extends EntityComponent {
	public PlayerType aniType;
	// chỗ này sẽ cho thêm vi tri der them inventory de sau co the su dung

	//tam tao vu khi tai day
	public Weapon weapon;
	public boolean isAttack;
	public boolean isSkill;

	public float timeAttackUp;
	public float timeSpeedUp;

	@Override
	public void reset() {
		super.reset();
		aniType = null;
		weapon = null;
		isSkill = false;
		isAttack = false;
		timeAttackUp = 0;
		timeSpeedUp = 0;
	}

}
