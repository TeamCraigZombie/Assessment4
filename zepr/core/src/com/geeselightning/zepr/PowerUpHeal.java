package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpHeal extends PowerUp {

    public PowerUpHeal(Level currentLevel, Player player) {
        super(1, new Texture("heal.png"), currentLevel, player);
    }

    @Override
    public void activate() {
        super.activate();

        //Health cannot be more than max health
        if(player.health+Constant.HEALUP <= (int)(player.HPMult * Constant.PLAYERMAXHP)) {

        	player.health += Constant.HEALUP;

        } else {

        	player.health = (int)(player.HPMult * Constant.PLAYERMAXHP);
        }
    }
}
