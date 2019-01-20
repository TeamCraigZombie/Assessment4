package com.geeselightning.zepr.tests;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Player;
import com.geeselightning.zepr.Zombie;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class ZombieTest {

    Player player = Player.getInstance();

    @Test
    // Test 3.1.1
    public void zombieDoesNoDamageToPlayerWhenAtMaxRange() {
        player.respawn(Constant.ORIGIN, null);

        Zombie zombie = new Zombie(new Sprite(), new Vector2(player.getCenter().x, player.getCenter().y - Constant.ZOMBIERANGE), null);
        double originalHealth = player.getHealth();
        zombie.attack(player, 0);

        assertEquals("Player on the edge of range should not take damage when the zombie attacks.",
                player.getHealth(), originalHealth, 0.1);
    }

    @Test
    // Test 3.1.2
    public void zombieDoesDamageToPlayerWhenInRange() {
        player.respawn(Constant.ORIGIN, null);

        Zombie zombie = new Zombie(new Sprite(), new Vector2(player.getCenter().x, player.getCenter().y - Constant.ZOMBIERANGE + 5), null);
        double originalHealth = player.getHealth();
        zombie.attack(player, 0);

        assertNotEquals("Player within range should take damage when the zombie attacks.",
                player.getHealth(), originalHealth, 0.1);
    }


    @Test
    // Test 3.1.3
    public void zombieDoesNoDamageToPlayerOutOfRange() {
        player.respawn(Constant.ORIGIN, null);

        Zombie zombie = new Zombie(new Sprite(), new Vector2(player.getCenter().x, player.getCenter().y - 100), null);
        double originalHealth = player.getHealth();
        zombie.attack(player, 0);

        assertEquals("Player outside of range should not take damage when the zombie attacks.",
                player.getHealth(), originalHealth, 0.1);
    }
}
