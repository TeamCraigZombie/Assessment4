package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Zombie extends Character {

    private int attackDamage;
    private int hitRange;

    /**
     * Constructor for the Zombie class
     * @param sprite sprite to use for the zombie
     * @param zombieSpawn the coordinates to spawn thw zombie at
     * @param world the Box2D world to add the zombie to
     * @param speed speed attribute of the zombie
     * @param health health attribute of the zombie
     * @param damage attack damage of the zombie
     */
    public Zombie(Sprite sprite, Vector2 zombieSpawn, World world, float speed, int health, int damage) {
        super(sprite, zombieSpawn, world);
        this.speed = speed*Constant.ZOMBIESPEED;
        this.attackDamage = damage*Constant.ZOMBIEDMG;
        hitRange = Constant.ZOMBIERANGE;
        maxhealth = this.health = health*Constant.ZOMBIEMAXHP;
        setCharacterPosition(zombieSpawn);
    }

    /**
     * Attack and damage the player if in range and hit counter refreshed
     * @param player instance of Player class to attack
     * @param delta the time between the start of the previous call and now
     */
    public void attack(Player player, float delta) {
        if (canHitGlobal(player, hitRange) && hitRefresh > Constant.ZOMBIEHITCOOLDOWN) {
            player.takeDamage(attackDamage);
            hitRefresh = 0;
        } else
            hitRefresh += delta;
    }

    /**
     * Method to update positional and action behavior
     * @param delta the time between the start of the previous call and now
     */
    @Override
    public void update(float delta) {
        //move according to velocity
        super.update(delta);

        if (Level.getPlayer().isVisible()) {
            // seek out player using gdx-ai seek functionality
            this.steeringBehavior = SteeringPresets.getSeek(this, Level.getPlayer());
            this.currentMode = SteeringState.SEEK;
            // update direction to face the player
            direction = getDirectionTo(Level.getPlayer().getCenter());
        } else { //player cannot be seen, so wander randomly
            this.steeringBehavior = SteeringPresets.getWander(this);
            this.currentMode = SteeringState.WANDER;
            // update direction to face direction of travel
            direction = -(this.vectorToAngle(this.getLinearVelocity()));
        }
    }
}
