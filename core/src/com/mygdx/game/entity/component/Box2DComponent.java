package com.mygdx.game.entity.component;

import box2dLight.Light;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Box2DComponent implements Component, Poolable{
	public Body body;
	public float width;
	public float height;
	public Light light;
	public Vector2 renderPosition = new Vector2();
	@Override
	public void reset() {
		if(light != null){
			light.remove(true);
			light = null;
		}
		if (body != null) {
			body.getWorld().destroyBody(body);
			body = null;
		}
		
		width = height = 0;
		renderPosition.set(0, 0);
	}
	

}
