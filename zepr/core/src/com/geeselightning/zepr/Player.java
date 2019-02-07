package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Character {

    public int attackDamage = Constant.PLAYERDMG;
    int hitRange = Constant.PLAYERRANGE;
    final float hitCooldown =  Constant.PLAYERHITCOOLDOWN;
    Texture mainTexture;
    Texture attackTexture;
    public boolean attack = false;
    public float HPMult;
    float dmgMult;
    float speedMult;
    static String playertype;
    public boolean isImmune;
    public boolean canBeSeen = true;


    public Player(Sprite sprite, Vector2 playerSpawn, World world) {
        super(sprite, playerSpawn, world);
        setCharacterPosition(playerSpawn);
        refreshAttributes();
    }

    public static void setType(String playertype){
        Player.playertype = playertype;
    }

    public void refreshAttributes() {
        if (playertype == "nerdy") {
            dmgMult = Constant.NERDYDMGMULT;
            HPMult = Constant.NERDYHPMULT;
            speedMult = Constant.NERDYSPEEDMULT;
            mainTexture = new Texture("player01.png");
            attackTexture = new Texture("player01_attack.png");
        } else if (playertype == "sporty") {
            dmgMult = Constant.SPORTYDMGMULT;
            HPMult = Constant.SPORTYHPMULT;
            speedMult = Constant.SPORTYSPEEDMULT;
            mainTexture = new Texture("player02.png");
            attackTexture = new Texture("player02_attack.png");
        }
        else {
            dmgMult = Constant.ARTSYDMGMULT;
            HPMult = Constant.ARTSYHPMULT;
            speedMult = Constant.ARTSYSPEEDMULT;
            mainTexture = new Texture("player03.png");
            attackTexture = new Texture("player03_attack.png");
        }

        setTexture(mainTexture);

        health = maxhealth = (int) HPMult * Constant.PLAYERMAXHP;
        attackDamage = (int)(Constant.PLAYERDMG * dmgMult);
        speed = Constant.PLAYERSPEED * speedMult;
    }

    public void attack(Zombie zombie, float delta) {
        if (canHitGlobal(zombie, hitRange) && hitRefresh > hitCooldown) {
            zombie.takeDamage(attackDamage);
            hitRefresh = 0;
        } else
            hitRefresh += delta;
    }

    public void respawn(Vector2 playerSpawn){

        body.setTransform(playerSpawn.x / Constant.physicsDensity, playerSpawn.y / Constant.physicsDensity, 0);
        health = maxhealth;
    }
    
    public void look(Vector2 mouseCoordinates) {
     	// Update the direction the player is facing.
        direction = getDirectionTo(mouseCoordinates);
    }

    @Override
    public void updateSprite() {
        super.updateSprite();
        
        control();

       
        // Gives the player the attack texture for 0.1s after an attack.
        //if (hitRefresh <= 0.1 && getTexture() != attackTexture) {
        if (attack)
            setTexture(attackTexture);
        else 
        // Changes the texture back to the main one after 0.1s.
        //if (hitRefresh > 0.1 && getTexture() == attackTexture) {
            setTexture(mainTexture);
    }
    
    public void control() {	
    	   	
    	Vector2 playerPosition = body.getPosition();
    	
    	if (Gdx.input.isKeyPressed(Keys.W))
			body.applyLinearImpulse(new Vector2(0, speed), playerPosition, true);
		else if (Gdx.input.isKeyPressed(Keys.S))
			body.applyLinearImpulse(new Vector2(0, -speed), playerPosition, true);

		if (Gdx.input.isKeyPressed(Keys.A))
			body.applyLinearImpulse(new Vector2(-speed, 0), playerPosition, true);
		else if (Gdx.input.isKeyPressed(Keys.D))
			body.applyLinearImpulse(new Vector2(speed, 0), playerPosition, true);
    }

    @Override
    public void takeDamage(int dmg){
        if(!isImmune)
            //If powerUpImmunity is activated
            health -= dmg;
    }

}
