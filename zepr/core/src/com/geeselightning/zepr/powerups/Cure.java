package com.geeselightning.zepr.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Level;
import com.geeselightning.zepr.Player;

public class Cure extends PowerUp {
    int radius = Constant.CURERADIUS;

    /**
     * Constructor for the cure item
     * @param currentLevel level to spawn the power up in
     * @param player player to monitor for pick up event and to apply the effect to
     */
    public Cure(Level currentLevel, Player player) {
        super(new Texture("cure.png"), currentLevel, player, 0, "Cure Collected");
    }

    /**
     * Turns nearby zombies into normal characters
     */
    @Override
    public void activate() {
        super.activate();
        currentLevel.cure(radius);
    }
}
