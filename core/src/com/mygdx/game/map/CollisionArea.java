package com.mygdx.game.map;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;

public class CollisionArea {
	private final float x;
	private final float y;

	private final float[] vertices;
	
	public CollisionArea (final float x, final float y,final float[] vertices) {
		this.x = x * CoreGame.UNIT_SCALE;
		this.y = y * CoreGame.UNIT_SCALE;
		this.vertices = vertices;
		for (int i=0; i<vertices.length; i += 2) {
			vertices[i] = vertices[i] * CoreGame.UNIT_SCALE;
			vertices[i+1] = vertices[i+1] * CoreGame.UNIT_SCALE;

		}
	}


	public float getX() {
		return x;
	}


	public float getY() {
		return y;
	}


	public float[] getVertices() {
		return vertices;
	}
}
