package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Zombie extends Character {

    int attackDamage = Constant.ZOMBIEDMG;
    public int hitRange = Constant.ZOMBIERANGE;
    public final float hitCooldown = Constant.ZOMBIEHITCOOLDOWN;


    public Zombie(Sprite sprite, Vector2 zombieSpawn, Level currentLevel) {
        super(sprite, zombieSpawn, currentLevel);
        this.speed = Constant.ZOMBIESPEED;
        this.health = Constant.ZOMBIEMAXHP;      
    }

    public void attack(Player player, float delta) {
        if (canHitGlobal(player, hitRange) && hitRefresh > hitCooldown) {
            player.takeDamage(attackDamage);
            hitRefresh = 0;
        } else
            hitRefresh += delta;
    }

    @Override
    public void update() {
        //move according to velocity
        super.update();

        if (Level.getPlayer().canBeSeen) {
            // update velocity to move towards player
            // Vector2.scl scales the vector
            velocity = getDirNormVector(Level.getPlayer().getPixelPosition()).scl(speed);
            
            body.applyLinearImpulse(velocity, body.getPosition() , true);

            // update direction to face the player
            direction = getDirectionTo(Level.getPlayer().getCenter());
            

            if (health <= 0) {
                currentLevel.zombiesRemaining--;
                currentLevel.getAliveZombiesList().remove(this);
                dispose();
            }
        }
    }
}
