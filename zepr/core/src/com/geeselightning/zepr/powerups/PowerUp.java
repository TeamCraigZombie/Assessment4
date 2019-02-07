package com.geeselightning.zepr.powerups;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.geeselightning.zepr.Level;
import com.geeselightning.zepr.Player;

public class PowerUp extends Sprite {

    public int type;
    Level currentLevel;
    public boolean active;
    public float timeRemaining;
    protected Player player;

    public PowerUp(int type, Texture texture, Level currentLevel, Player player) {
        super(new Sprite(texture));
        this.type = type;
        this.currentLevel = currentLevel;
        if (currentLevel != null)
            // Tests pass a null currentLevel
            setPosition(currentLevel.config.powerSpawn.x, currentLevel.config.powerSpawn.y);
        this.player = player;
    }

    public void activate(){
        active = true;
        this.getTexture().dispose();
    }

    public void deactivate(){
        active = false;
        if (currentLevel != null)
            // Tests pass a null currentLevel
            currentLevel.currentPowerUp = null;
    }

    public boolean overlapsPlayer(){
        Rectangle rectanglePlayer = player.getBoundingRectangle();
        Rectangle rectanglePower = this.getBoundingRectangle();
        return rectanglePlayer.overlaps(rectanglePower);
    }

    public void update(float delta) {
        if (active) {
            timeRemaining -= delta;

            if (timeRemaining < 0)
                deactivate();
        }
    }
}
