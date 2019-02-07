package com.geeselightning.zepr.tests;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.geeselightning.zepr.Character;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class CharacterTest {

    private final String charTexturePath = "player01.png";

    @Test
    // Test 1.1.1
    public void charactersWithSamePositionShouldCollide() {
        World world = new World(new Vector2(0, 0), true);
        Character character = new Character(new Sprite(new Texture(charTexturePath)), new Vector2(0,0), world);
        assertTrue("A Character should collide with itself.", character.collidesWith(character));
        character.dispose();
        world.dispose();
    }

    @Test
    // Test 1.1.2
    public void touchingCharactersShouldCollide() {
        World world = new World(new Vector2(0, 0), true);
        Character anotherCharacter = new Character(new Sprite(new Texture(charTexturePath)), new Vector2(0,10), world);
        Character character = new Character(new Sprite(new Texture(charTexturePath)), new Vector2(0,0), world);
        assertTrue("Characters that touch should collide.", character.collidesWith(anotherCharacter));
        anotherCharacter.dispose();
        character.dispose();
        world.dispose();
    }

    @Test
    // Test 1.1.3
    public void nonTouchingCharactersShouldNotCollide() {
        World world = new World(new Vector2(0, 0), true);
        Character anotherCharacter = new Character(new Sprite(new Texture(charTexturePath)), new Vector2(200,200), world);
        Character character = new Character(new Sprite(new Texture(charTexturePath)), new Vector2(0,0), world);
        assertFalse("Characters that don't touch should not collide.", character.collidesWith(anotherCharacter));
        anotherCharacter.dispose();
        character.dispose();
        world.dispose();
    }


    @Test
    // Test 1.2.1
    public void getCenterOnCharacterWithPositivePosition() {
        World world = new World(new Vector2(0, 0), true);
        Character character = new Character(new Sprite(new Texture(charTexturePath)), new Vector2(50,20), world);
        assertEquals("Testing center calculation with positive position.", new Vector2(50, 20),
                character.getCenter());
        character.dispose();
        world.dispose();
    }

    @Test
    // Test 1.2.2
    public void getCenterOnCharacterWithNegativePosition() {
        World world = new World(new Vector2(0, 0), true);
        Character character = new Character(new Sprite(new Texture(charTexturePath)), new Vector2(-50,-20), world);
        assertEquals("Testing center calculation with negative position.", new Vector2(-50, -20),
                character.getCenter());
        character.dispose();
        world.dispose();
    }

    @Test
    // Test 1.3.1
    public void getDirectionInTopRightQuadrant() {
        World world = new World(new Vector2(0, 0), true);
        Vector2 center1 = new Vector2(-16,-16);
        Vector2 center2 = new Vector2(1,1);
        Character character = new Character(new Sprite(new Texture(charTexturePath)), center1, world);

        double angle = Math.atan2(center1.y-center2.y, center1.x-center2.x)+Math.PI*2;

        assertEquals("North-East direction should be correct.", angle, character.getDirectionTo(center2), angle);
        character.dispose();
        world.dispose();
    }

    @Test
    // Test 1.3.2
    public void getDirectionInBottomRightQuadrant() {
        World world = new World(new Vector2(0, 0), true);
        Vector2 center1 = new Vector2(-16,-16);
        Vector2 center2 = new Vector2(1,-1);
        Character character = new Character(new Sprite(new Texture(charTexturePath)), center1, world);

        double angle = Math.atan2(center1.y-center2.y, center1.x-center2.x)+Math.PI*2;

        assertEquals("South-East direction should be correct.", angle, character.getDirectionTo(center2), angle);
        character.dispose();
        world.dispose();
    }

    @Test
    // Test 1.3.3
    public void getDirectionInBottomLeftQuadrant() {
        World world = new World(new Vector2(0, 0), true);
        Vector2 center1 = new Vector2(-16,-16);
        Vector2 center2 = new Vector2(-1,-1);
        Character character = new Character(new Sprite(new Texture(charTexturePath)), center1, world);

        double angle = Math.atan2(center1.y-center2.y, center1.x-center2.x)+Math.PI*2;

        assertEquals("South-West direction should be correct.", angle, character.getDirectionTo(center2), angle);
        character.dispose();
        world.dispose();
    }

    @Test
    // Test 1.3.4
    public void getDirectionInTopLeftQuadrant() {
        World world = new World(new Vector2(0, 0), true);
        Vector2 center1 = new Vector2(-16,-16);
        Vector2 center2 = new Vector2(-1,1);
        Character character = new Character(new Sprite(new Texture(charTexturePath)), center1, world);

        double angle = Math.atan2(center1.y-center2.y, center1.x-center2.x)+Math.PI*2;

        assertEquals("North-West direction should be correct.", angle, character.getDirectionTo(center2), angle);
        character.dispose();
        world.dispose();
    }

    @Test
    // Test 1.4
    public void charactersTakeSpecifiedDamage() {
        World world = new World(new Vector2(0, 0), true);
        Character character = new Character(new Sprite(new Texture(charTexturePath)), new Vector2(0, 0), world);
        double originalHealth = character.getHealth();
        character.takeDamage(50);
        assertEquals("Character's health should be reduced by 50 when takeDamage(50).", originalHealth - 50,
                character.getHealth(), 0.1);
        character.dispose();
        world.dispose();
    }

    @Test
    // Test 1.5.1
    public void getDirNormVectorToNegativePosition() {
        World world = new World(new Vector2(0, 0), true);
        Character character = new Character(new Sprite(new Texture(charTexturePath)), new Vector2(-16,-16), world);
        Vector2 position = new Vector2(-10, -30);

        Vector2 normalizedDirection = character.getDirNormVector(position);

        Vector2 center = character.getCenter();
        double len = Math.sqrt(Math.pow(center.x-position.x,2)+Math.pow(center.y-position.y,2));
        double expectedX = (position.x-center.x) / len;
        double expectedY = (position.y-center.y) / len;

        assertEquals("Correct x component for player with center at origin to (-10, -30).", expectedX, normalizedDirection.x, 0.1);
        assertEquals("Correct y component for player with center at origin to (-10, -30).", expectedY, normalizedDirection.y, 0.1);
        character.dispose();
        world.dispose();
    }

    @Test
    // Test 1.5.2
    public void getDirNormVectorToPositivePosition() {
        World world = new World(new Vector2(0, 0), true);
        Character character = new Character(new Sprite(new Texture(charTexturePath)), new Vector2(-16,-16), world);
        Vector2 position = new Vector2(-47, -20);

        Vector2 normalizedDirection = character.getDirNormVector(position);

        Vector2 center = character.getCenter();
        double len = Math.sqrt(Math.pow(center.x-position.x,2)+Math.pow(center.y-position.y,2));
        double expectedX = (position.x-center.x) / len;
        double expectedY = (position.y-center.y) / len;

        assertEquals("Correct x component for player with center at origin to (-10, -30).", expectedX, normalizedDirection.x, 0.1);
        assertEquals("Correct y component for player with center at origin to (-10, -30).", expectedY, normalizedDirection.y, 0.1);
        character.dispose();
        world.dispose();
    }
}