package com.geeselightning.zepr.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Level;
import com.geeselightning.zepr.Player;

public class PowerUpSpeed extends PowerUp {

    /**
     * Constructor for the speed power up
     * @param currentLevel - level to spawn the power up in
     * @param player - player to monitor for pick up event and to apply the effect to
     */
    public PowerUpSpeed(Level currentLevel, Player player) {
        super(new Texture("speed.png"), currentLevel, player, Constant.SPEEDUPTIME);
    }

    /**
     * Increase player speed attribute value
     */
    @Override
    public void activate() {
        super.activate();
        player.speed += Constant.SPEEDUP;
    }

    /**
     * Reset player speed attribute value
     */
    @Override
    public void deactivate() {
        super.deactivate();
        player.speed -= Constant.SPEEDUP;
    }
}
