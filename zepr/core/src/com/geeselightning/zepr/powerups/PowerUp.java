package com.geeselightning.zepr.powerups;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.geeselightning.zepr.Level;
import com.geeselightning.zepr.Player;

public class PowerUp extends Sprite {

    private Level currentLevel;
    private boolean active;
    private float timeRemaining;
    private float effectDuration;
    protected Player player;

    /**
     * Constructor for the generic power up class
     * @param texture the texture to display for the pick up
     * @param currentLevel the instance of Level to spawn the power up in
     * @param player player instance to pick up and apply the power up to
     */
    PowerUp(Texture texture, Level currentLevel, Player player, float effectDuration) {
        super(new Sprite(texture));
        this.currentLevel = currentLevel;
        this.effectDuration = effectDuration;
        // Tests pass a null currentLevel
        if (currentLevel != null)
            setPosition(currentLevel.getConfig().powerSpawn.x, currentLevel.getConfig().powerSpawn.y);
        this.player = player;
    }

    /**
     * Apply the power up effect to the player, removing the power up texture
     */
    public void activate(){
        timeRemaining = effectDuration;
        active = true;
        this.getTexture().dispose();
    }

    /**
     * Remove the power up effect from the player
     */
    public void deactivate(){
        active = false;
        if (currentLevel != null)
            // Tests pass a null currentLevel
            currentLevel.setCurrentPowerUp(null);
    }

    /**
     * Check whether the player instance is overlapping the power up
     * @return true if the player is overlapping the power up
     */
    public boolean overlapsPlayer(){
        Rectangle rectanglePlayer = player.getBoundingRectangle();
        Rectangle rectanglePower = this.getBoundingRectangle();
        return rectanglePlayer.overlaps(rectanglePower);
    }

    /**
     * Update method to advance the effect duration timer and deactivate if expired
     * @param delta the time between the start of the previous render() call and now
     */
    public void update(float delta) {
        if (active) {
            timeRemaining -= delta;

            if (timeRemaining < 0)
                deactivate();
        }
    }

    public boolean isActive() {
        return active;
    }
}
