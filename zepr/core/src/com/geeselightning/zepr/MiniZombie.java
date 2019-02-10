package com.geeselightning.zepr;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MiniZombie {
	
	private static long timer;
	private long last;
	private long collisionTimer;
	Sprite zombie;
	private int y = Gdx.graphics.getHeight()/2;
	private int width = Gdx.graphics.getWidth();
	private double direction = 10;
	private int inc = 0;
	private boolean collision = false;
	private float zombieWidth;
	private float zombieHeight;
	private float zombieX;
	private float zombieY;
	private int spawnX;
	private int mouseX;
	private int mouseY;
	private float initialWidth;
	private BitmapFont font;
	private int distance = 30;
	private double rand;
	
	public MiniZombie(String texture) {
		
	
		zombie = new Sprite(new Texture(texture));
		font = new BitmapFont();
		initialWidth = zombie.getWidth();
		this.spawn();		
	}
		
	public Sprite move() {
		
		zombieX = zombie.getX();
		zombieY = zombie.getY();
		zombieWidth = zombie.getWidth();
		zombieHeight = zombie.getHeight();
		
		if(timer > collisionTimer + 1.5) {
			collision = false;
		}
		
		if (timer > last + 1) {	
			last = timer;
			distance -= 2;
			if(!collision) {
				direction = Math.random();
			}
			last = timer;	
			
		}
		
		if(direction > 0.5 && direction < 1) {
			if(this.collision()) {
				inc--;
			} else {
				inc++;
			}		
		} 
		else if(direction < 0.5) {
			if(this.collision()) {
				inc++;
			} else {
				inc--;
			}
		}
	
		if(distance <= 0) {
			MiniGame.playerDeath(true);
		}
		
		zombie.setSize(zombieWidth += 0.3, zombieHeight += 0.3);		
		zombie.setPosition(spawnX+inc, y-zombieWidth/2);	
		return zombie;
	}
	
	
	public boolean collision() {
		

		if(zombieX <= 150 || zombieX+(zombieWidth) >= width-150) {	
			collisionTimer = timer;
			this.collision = true;
		}
		return this.collision;
	}
	
	
	public void setVisibleWidth(float width) {
		this.zombieWidth = width;
	}
	
	public void setVisibleX(float x) {
		this.zombieX = x;
	}
	
	public boolean getDamage() {
		
		mouseX = Gdx.input.getX();
		mouseY = -(Gdx.input.getY()-720);
	
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			MiniGame.trigger = MiniGame.timer;
			if(aimingAt()) {
				return true;
			} else { 
				return false;
			}
		} else {
			return false;
		}		
	}
	
	public void spawn() {
		
		rand = Math.random();
		
		System.out.println(rand);
		
		if(rand < 0.3){
			spawnX = 230;
		}
		else if(rand >= 0.3 && rand <= 0.6){
			spawnX = 610;
		}
		else if(rand > 0.6) {
			spawnX = 1000;
		}
		
		last = timer+1;
		zombie.setPosition(spawnX, y);	
		zombie.setSize(initialWidth, initialWidth);		
	}
	
	public void render(SpriteBatch spriteBatch, int zCount) {
		
        this.move().draw(spriteBatch);
        font.draw(spriteBatch, Integer.toString(distance) + " meters", zombieX, zombieY+zombieHeight);
       
	}
	
	public boolean aimingAt() {
		
		if(mouseX >= zombieX && mouseX <= zombieX+(zombieWidth) && 
				mouseY <= y+(zombieHeight) && mouseY >= zombieY) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
	public static long timer() {	
		timer = System.nanoTime()/1000000000;		
		return timer;
	}
	
}

