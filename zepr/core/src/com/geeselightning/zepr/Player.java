package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Character {

    int attackDamage = Constant.PLAYERDMG;
    int hitRange = Constant.PLAYERRANGE;
    final float hitCooldown =  Constant.PLAYERHITCOOLDOWN;
    Texture mainTexture;
    Texture attackTexture;
    public boolean attack = false;
    float HPMult;
    float dmgMult;
    float speedMult;
    static String playertype;
    public boolean isImmune;
    private static Level level;
    public boolean canBeSeen = true;


    public Player(Sprite sprite, Vector2 playerSpawn) {
        super(sprite, playerSpawn, level);
    }
    
    public static void setLevel(Level currentLevel) {
    	level = currentLevel;
    }

    public static void setType(String playertype){
        Player.playertype = playertype;
    }

    public void attack(Zombie zombie, float delta) {
        if (canHitGlobal(zombie, hitRange) && hitRefresh > hitCooldown) {
            zombie.takeDamage(attackDamage);
            hitRefresh = 0;
        } else {
            hitRefresh += delta;
        }
    }

    public void respawn(Vector2 playerSpawn, Level level){
        setPosition(playerSpawn.x, playerSpawn.y);
        if (playertype == "nerdy"){
            dmgMult = Constant.NERDYDMGMULT;
            HPMult = Constant.NERDYHPMULT;
            speedMult = Constant.NERDYSPEEDMULT;
        }
        else if (playertype == "sporty"){
            dmgMult = Constant.SPORTYDMGMULT;
            HPMult = Constant.SPORTYHPMULT;
            speedMult = Constant.SPORTYSPEEDMULT;
        }
        else if (playertype == "Artsy"){
            dmgMult = Constant.ARTSYDMGMULT;
            HPMult = Constant.ARTSYHPMULT;
            speedMult = Constant.ARTSYSPEEDMULT;
        }
        else if (playertype == null){
            dmgMult =1;
            HPMult = 1;
            speedMult = 1;
        }
        this.attackDamage = (int)(Constant.PLAYERDMG * dmgMult);
        this.speed = (int)(Constant.PLAYERSPEED * speedMult);
        this.health = (int)(HPMult * Constant.PLAYERMAXHP);
        this.currentLevel = level;

        if (playertype == "nerdy") {
            mainTexture = new Texture("player01.png");
            attackTexture = new Texture("player01_attack.png");
            this.setTexture(mainTexture);
        } else if (playertype == "sporty") {
            // playertype == sporty
            mainTexture = new Texture("player02.png");
            attackTexture = new Texture("player02_attack.png");
            this.setTexture(mainTexture);
        }
        else {
        	mainTexture = new Texture("player03.png");
            attackTexture = new Texture("player03_attack.png");
            this.setTexture(mainTexture);
        }
    }

    @Override
    public void update() {
        super.update();
        
        control();

        // Update the direction the player is facing.
        direction = getDirectionTo(currentLevel.getMouseWorldCoordinates());

        // When you die, end the level.
        if (health <= 0) {
            currentLevel.gameOver();
        }

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
			body.applyLinearImpulse(new Vector2(0, speedMult), playerPosition, true);
		else if (Gdx.input.isKeyPressed(Keys.S))
			body.applyLinearImpulse(new Vector2(0, -speedMult), playerPosition, true);

		if (Gdx.input.isKeyPressed(Keys.A))
			body.applyLinearImpulse(new Vector2(-speedMult, 0), playerPosition, true);
		else if (Gdx.input.isKeyPressed(Keys.D))
			body.applyLinearImpulse(new Vector2(speedMult, 0), playerPosition, true);
    }

    @Override
    public void takeDamage(int dmg){
        if(!isImmune){
            //If powerUpImmunity is activated
            health -= dmg;
        }
    }

}
