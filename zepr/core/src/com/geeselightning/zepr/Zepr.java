package com.geeselightning.zepr;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.geeselightning.zepr.screens.LoadingScreen;
import com.geeselightning.zepr.screens.MenuScreen;
import com.geeselightning.zepr.screens.SelectLevelScreen;
import com.geeselightning.zepr.screens.StoryScreen;

public class Zepr extends Game {

	private LoadingScreen loadingScreen;
	private MenuScreen menuScreen;
	private StoryScreen storyScreen;
	private Level level;
	private MiniGame minigame;
	private SelectLevelScreen selectLevelScreen;
	private World world;
	
	public static enum location { MENU, STORY, SELECT, TOWN, HALIFAX, COURTYARD, COMPLETE, MINIGAME };

	// The progress is the integer representing the last level completed. i.e. 3 for Town
	public static location progress = location.TOWN;

	

	public void changeScreen(final location screen) {
		LevelConfig config;
		switch(screen) {
			case MENU:
				if (menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
            case STORY:
                storyScreen = new StoryScreen(this);
                this.setScreen(storyScreen);
                break;
			case SELECT:
				selectLevelScreen = new SelectLevelScreen(this);
				this.setScreen(selectLevelScreen);
				break;
			case TOWN:
				config = new LevelConfig() {{
					mapLocation = "maps/townmap.tmx";
					playerSpawn = new Vector2(530, 430);
					powerSpawn = new Vector2(300, 300);
					zombieSpawnPoints = new ArrayList<Vector2>(
				            Arrays.asList(new Vector2(200,200), new Vector2(700,700),
				                    new Vector2(200,700), new Vector2(700,200)));
					waves = new int[]{5, 10, 15};
					location = screen;
					isFinal = false;
				}};						 
				level = new Level(this, config);
				this.setScreen(level);
				break;
			case HALIFAX:
				config = new LevelConfig() {{
					mapLocation = "maps/halifaxmap.tmx";
					playerSpawn = new Vector2(300, 300);
					powerSpawn = new Vector2(200, 200);
					zombieSpawnPoints = new ArrayList<Vector2>(
							Arrays.asList(new Vector2(600,100), new Vector2(100,200),
				                    new Vector2(600,500), new Vector2(100,600)));
					waves = new int[]{10, 15, 20};
					location = screen;
					isFinal = false;
				}};						 
				level = new Level(this, config);
				this.setScreen(level);
				break;
			case COURTYARD:
				config = new LevelConfig() {{
					mapLocation = "maps/courtyard.tmx";
					playerSpawn = new Vector2(300, 300);
					powerSpawn = new Vector2(250, 250);
					zombieSpawnPoints = new ArrayList<Vector2>(
							 Arrays.asList(new Vector2(120,100), new Vector2(630,600),
					                   new Vector2(630,100), new Vector2(120,500)));
					waves = new int[]{13, 17, 1};
					location = screen;
					isFinal = true;
				}};						 
				level = new Level(this, config);
				this.setScreen(level);
				break;
			case MINIGAME:
				minigame = new MiniGame(this);
				this.setScreen(minigame);
				break;
		}
	}
	
	public World getWorld() {
		return world;
	}

	@Override
	public void create() {
		
        //Initialise Box2D physics engine
        world = new World(new Vector2(0, 0), true);
		
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}
}