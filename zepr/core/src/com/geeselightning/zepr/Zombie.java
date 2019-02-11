package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Zombie extends Character {

    int attackDamage = Constant.ZOMBIEDMG;
    public int hitRange = Constant.ZOMBIERANGE;
    public final float hitCooldown = Constant.ZOMBIEHITCOOLDOWN;

    public Zombie(Sprite sprite, Vector2 zombieSpawn, World world, float speed, int health) {
        super(sprite, zombieSpawn, world);
        this.speed = speed;
        maxhealth = this.health = health;
    }

    public void attack(Player player, float delta) {
        if (canHitGlobal(player, hitRange) && hitRefresh > hitCooldown) {
            player.takeDamage(attackDamage);
            hitRefresh = 0;
        } else
            hitRefresh += delta;
    }

    @Override
    public void update(float delta) {
        //move according to velocity
        super.update(delta);

        if (Level.getPlayer().canBeSeen) {
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
