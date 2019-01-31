package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpHeal extends PowerUp {

    public PowerUpHeal(Level currentLevel) {
        super(1, new Texture("heal.png"), currentLevel);
    }

    @Override
    public void activate() {
        super.activate();

        //Health cannot be more than max health
        if(Level.getPlayer().health+Constant.HEALUP <= (int)(Level.getPlayer().HPMult * Constant.PLAYERMAXHP)) {

        	Level.getPlayer().health += Constant.HEALUP;

        } else {

        	Level.getPlayer().health = (int)(Level.getPlayer().HPMult * Constant.PLAYERMAXHP);
        }
    }

    @Override
    public void update(float delta) {
        if (active) {
            super.deactivate();
            this.getTexture().dispose();
        }
    }

}
