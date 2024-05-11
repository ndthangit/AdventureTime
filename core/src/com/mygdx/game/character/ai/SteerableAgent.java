//Steerable Agent
package com.mygdx.game.character.ai;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class SteerableAgent implements Steerable<Vector2> {
    Body body;
    Boolean tagged;
    float boundingRadius;
    float maxLinearSpeed, maxAngularSpeed;
    float maxAngularAcceleration, maxLinearAcceleration;

    public SteerableAgent(Body body, float boundingRadius) {
        this.body = body;
        this.boundingRadius = boundingRadius;
        this.maxLinearSpeed = 2000;  // Thử tăng giá trị này
        this.maxLinearAcceleration = 500;  // Và giá trị này
        this.maxAngularSpeed = 120;
        this.maxAngularAcceleration = 20;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float v) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float v) {
        this.maxLinearSpeed = v;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float v) {
        this.maxLinearAcceleration = v;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float v) {
        this.maxAngularSpeed = v;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return getMaxLinearAcceleration();
    }

    @Override
    public void setMaxAngularAcceleration(float v) {
        this.maxAngularAcceleration = v;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public void setOrientation(float v) {
        body.setTransform(getPosition(), v);
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Location<Vector2>() {
            private Vector2 position = new Vector2();

            @Override
            public Vector2 getPosition() {
                return position;
            }

            @Override
            public float getOrientation() {
                return 0;
            }

            @Override
            public void setOrientation(float orientation) {
                // Not implemented for this simple example
            }

            @Override
            public Location<Vector2> newLocation() {
                return this;
            }

            @Override
            public float vectorToAngle(Vector2 vector) {
                return (float) Math.atan2(-vector.x, vector.y);
            }

            @Override
            public Vector2 angleToVector(Vector2 outVector, float angle) {
                outVector.x = -(float) Math.sin(angle);
                outVector.y = (float) Math.cos(angle);
                return outVector;
            }
        };
    }
}