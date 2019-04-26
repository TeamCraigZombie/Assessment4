package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Human extends Character {
    private Level currentLevel;

    /**
     * Constructor for human class
     * @param spawnPos the coordinates to spawn the human at
     * @param world the Box2D world to add the zombie to
     */
    public Human(Vector2 spawnPos, World world, Level currentLevel) {
        super(world);
        this.currentLevel = currentLevel;
        maxhealth = Constant.ZOMBIEMAXHP;
        set(new Sprite(new Texture("player01.png")));
        health = maxhealth;
        body.setFixedRotation(true);
        body.setLinearDamping(50f);
        setCharacterPosition(spawnPos);

        if (Zepr.zombieMode) {speed = Constant.HUMANSPEEDZMODE; }
        else {speed = Constant.ZOMBIESPEED; }
    }

    @Override
    public void update(float delta) {
        //move according to velocity
        super.update(delta);

        Character target;

        if (!Zepr.zombieMode) {target = currentLevel.getClosestZombie(getCenter()); }
        else {target = Level.getPlayer(); }

        if (target != null) {
            this.steeringBehavior = SteeringPresets.getEvade(this, target);
            this.currentMode = SteeringState.EVADE;
            direction = getDirectionTo(target.getCenter());

        }
    }
}
