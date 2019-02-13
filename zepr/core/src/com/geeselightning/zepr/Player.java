package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Character {

    private int attackDamage;
    private Texture mainTexture;
    private Texture attackTexture;
    private boolean attack = false;
    private float HPMult;
    private static String playerType;
    private boolean isImmune;
    private boolean canBeSeen = true;
    private int boostDamage;

    /**
     * Constructor for the player class
     * @param sprite the sprite to use for the player
     * @param playerSpawn coordinates to spawn the player at
     * @param world the Box2D world to spawn the player in
     */
    public Player(Sprite sprite, Vector2 playerSpawn, World world) {
        super(sprite, playerSpawn, world);
        setCharacterPosition(playerSpawn);
        refreshAttributes();
    }

    /**
     * Set the player type attribute
     * @param playerType the new player type value
     */
    public static void setType(String playerType){
        Player.playerType = playerType;
    }

    /**
     * Update the attributes based on the player type
     * Call this after changing the player type attribute
     */
    public void refreshAttributes() {
        float dmgMult, speedMult;
        if (playerType == "nerdy") {
            dmgMult = Constant.NERDYDMGMULT;
            HPMult = Constant.NERDYHPMULT;
            speedMult = Constant.NERDYSPEEDMULT;
            mainTexture = new Texture("player01.png");
            attackTexture = new Texture("player01_attack.png");
        } else if (playerType == "sporty") {
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

        health = maxhealth = (int) (HPMult * 100);
        attackDamage = (int)(Constant.PLAYERDMG * dmgMult);
        boostDamage = 1;
        speed = Constant.PLAYERSPEED * speedMult;
    }

    /**
     * Routine to perform an attack move, damaging nearby enemies
     * @param zombie the zombie to test for promiximity and to damage
     * @param delta the time between the start of the previous call and now
     */
    public void attack(Zombie zombie, float delta) {
        if (canHitGlobal(zombie, Constant.PLAYERRANGE) && hitRefresh > Constant.PLAYERHITCOOLDOWN) {
            zombie.takeDamage(attackDamage*boostDamage);
            hitRefresh = 0;
        } else
            hitRefresh += delta;
    }

    /**
     * Respawn the player, resetting the health attribute
     * @param playerSpawn the position to respawn in
     */
    public void respawn(Vector2 playerSpawn){

        setCharacterPosition(playerSpawn);
        health = maxhealth;
    }


    /**
     * Routine to set the sprite direction to look at the mouse
     * @param mouseCoordinates coordinates of the mouse
     */
    void look(Vector2 mouseCoordinates) {
     	// Update the direction the player is facing.
        direction = getDirectionTo(mouseCoordinates);
    }

    /**
     * Update method to process control processing and attacking action
     * @param delta the time between the start of the previous call and now
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        
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

    /**
     * Handle player keyboard controls
     */
    private void control() {
    	   	
    	Vector2 playerPosition = body.getPosition();

    	//Apply Box2D body impulses in specific direction when keys pressed
    	if (Gdx.input.isKeyPressed(Keys.W))
			body.applyLinearImpulse(new Vector2(0, speed), playerPosition, true);
		else if (Gdx.input.isKeyPressed(Keys.S))
			body.applyLinearImpulse(new Vector2(0, -speed), playerPosition, true);

		if (Gdx.input.isKeyPressed(Keys.A))
			body.applyLinearImpulse(new Vector2(-speed, 0), playerPosition, true);
		else if (Gdx.input.isKeyPressed(Keys.D))
			body.applyLinearImpulse(new Vector2(speed, 0), playerPosition, true);
    }

    /**
     * Reduce the player health if not immune
     * @param dmg the amount to reduce the health by
     */
    @Override
    public void takeDamage(int dmg){
        if(!isImmune)
            //If powerUpImmunity is activated
            health -= dmg;
    }

    public float getHPMult() {
        return HPMult;
    }

    boolean isVisible() {
        return canBeSeen;
    }

    public void setVisible(boolean visible) {
        canBeSeen = visible;
    }

    public void setImmune(boolean immune) {
        this.isImmune = immune;
    }

    public void setBoostDamage(int boostDamage) {
        this.boostDamage = boostDamage;
    }

    void setAttack(boolean attack) {
        this.attack = attack;
    }

    boolean isAttacking() {
        return attack;
    }
}
