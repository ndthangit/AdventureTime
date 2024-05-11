package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.mygdx.game.character.player.PlayerType;
import com.mygdx.game.items.Item;
import com.mygdx.game.items.food.Food;
import com.mygdx.game.items.weapon.Weapon;
import com.mygdx.game.view.AnimationType;

public class PlayerComponent extends EntityComponent {
	public PlayerType aniType;
	// chỗ này sẽ cho thêm vi tri der them inventory de sau co the su dung

	public Item[] inventory = new Item[5];
	private int index = 0;
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

	public void useItem(Item item) {
		if (item instanceof Weapon) {
			Weapon weapon = (Weapon) item;
			// tam thoi chua lam gi
		} else {
			Food food = (Food) item;

			life += food.type.getHeal();
			if (life > maxLife) {
				life = maxLife;
			}
		}
	}

	public boolean addItem(Item newItem) {
		for (Item item : inventory) {
			if (item != null && item.key.equals(newItem.key)) {
				item.quatity += 1;
				return true;
			}
		}
		if (index < 4) {
			inventory[index++] = newItem;
			newItem.quatity = 1;
			return true;
		}
		return false;
	}
}
