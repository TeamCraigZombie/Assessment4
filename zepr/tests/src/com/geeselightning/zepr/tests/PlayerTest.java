package com.geeselightning.zepr.tests;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Player;
import com.geeselightning.zepr.Zombie;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class PlayerTest {

    @Test
    // Test 2.1
    public void playerPositionResetsWhenRespawned() {
        Player player = new Player(new Sprite(new Texture("player01.png")), new Vector2(0, 0));
        Vector2 originalPosition = new Vector2(player.getX(), player.getY());
        player.setPosition(10, 10);
        player.respawn(new Vector2(0, 0));
        assertEquals("Position should reset when the player is respawned.", originalPosition, new Vector2(player.getX(), player.getY()));
    }

    @Test
    // Test 2.2.1
    public void playerDoesNoDamageToZombieWhenAtMaxRange() {
        Player player = new Player(new Sprite(new Texture("player01.png")), new Vector2(0, 0));

        Zombie zombie = new Zombie(new Sprite(), new Vector2(player.getCenter().x, player.getCenter().y + Constant.PLAYERRANGE), null);
        double originalHealth = zombie.getHealth();
        player.attack(zombie, 0);

        assertEquals("Zombie on the edge of range should not take damage when the player attacks.",
                zombie.getHealth(), originalHealth, 0.1);
    }

    @Test
    // Test 2.2.2
    public void playerDoesDamageToZombieWhenInRange() {
        Player player = new Player(new Sprite(new Texture("player01.png")), new Vector2(0, 0));

        Zombie zombie = new Zombie(new Sprite(), new Vector2(player.getCenter().x, player.getCenter().y + Constant.PLAYERRANGE - 10), null);
        double originalHealth = zombie.getHealth();
        player.attack(zombie, 0);

        assertNotEquals("Zombie within range should take damage when the player attacks.",
                zombie.getHealth(), originalHealth, 0.1);
    }

    @Test
    //Test 2.2.3
    public void playerDoesNoDamageToZombieOutOfRange() {
        Player player = new Player(new Sprite(new Texture("player01.png")), new Vector2(0, 0));

        Zombie zombie = new Zombie(new Sprite(), new Vector2(player.getCenter().x, player.getCenter().y +100), null);
        double originalHealth = zombie.getHealth();
        player.attack(zombie, 0);

        assertEquals("Zombie outside of range should not take damage when the player attacks.",
                zombie.getHealth(), originalHealth, 0.1);
    }

    @Test
    // Test 2.3.1
    public void playerTypesHaveDifferentHealth() {
        Player player = new Player(new Sprite(new Texture("player01.png")), new Vector2(0, 0));
        Player.setType("nerdy");
        player.respawn(Constant.ORIGIN);
        double nerdyHealth = player.getHealth();
        Player.setType("sporty");
        player.respawn(Constant.ORIGIN);
        assertNotEquals("Sporty and nerdy students should have a different amount of hit points.",
                nerdyHealth, player.getHealth(), 0.1);
    }

    @Test
    // Test 2.3.2
    public void playerTypesHaveDifferentSpeed() {
        Player player = new Player(new Sprite(new Texture("player01.png")), new Vector2(0, 0));
        Player.setType("nerdy");
        player.respawn(Constant.ORIGIN);
        double nerdySpeed = player.speed;
        Player.setType("sporty");
        player.respawn(Constant.ORIGIN);
        assertNotEquals("Sporty and nerdy students should have a different amount of hit points.",
                nerdySpeed, player.speed);
    }

}