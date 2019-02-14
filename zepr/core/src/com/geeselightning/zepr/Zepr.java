package com.geeselightning.zepr;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.screens.LoadingScreen;
import com.geeselightning.zepr.screens.MenuScreen;
import com.geeselightning.zepr.screens.SelectLevelScreen;
import com.geeselightning.zepr.screens.StoryScreen;

public class Zepr extends Game {

	private MenuScreen menuScreen;
	
	public enum Location { MENU, STORY, SELECT, TOWN, HALIFAX, COURTYARD, CENTRALHALL, 
		GLASSHOUSE, CONSTANTINE, COMPLETE, MINIGAME, TEST }
	

	// The progress is the integer representing the last level completed. i.e. 3 for Town
	public static Location progress;

	/**
	 * Method to change the currently active screen
	 * @param screen the Location to set as active
	 */
	public void changeScreen(final Location screen) {
		LevelConfig config;
		Level level;
		switch(screen) {
			case MENU:
				if (menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
            case STORY:
				StoryScreen storyScreen = new StoryScreen(this);
                this.setScreen(storyScreen);
                break;
			case SELECT:
				SelectLevelScreen selectLevelScreen = new SelectLevelScreen(this);
				this.setScreen(selectLevelScreen);
				break;
			case TOWN:
				config = new LevelConfig() {{
					mapLocation = "maps/townmap.tmx";
					playerSpawn = new Vector2(530, 600);
					powerSpawn = new Vector2(300, 300);
					zombieSpawnPoints = new ArrayList<>(
				            Arrays.asList(new Vector2(200,200), new Vector2(700,700),
				                    new Vector2(200,700), new Vector2(700,200)));
					waves = new int[]{5, 10, 15, 1};
					location = screen;
					
					boss1 = false;
					boss2 = true;
					isTeleporting = false;
				}};
				level = new Level(this, config);
				this.setScreen(level);
				break;
			case HALIFAX:
				config = new LevelConfig() {{
					mapLocation = "maps/halifaxmap.tmx";
					playerSpawn = new Vector2(300, 300);
					powerSpawn = new Vector2(200, 200);
					zombieSpawnPoints = new ArrayList<>(
							Arrays.asList(new Vector2(600,100), new Vector2(100,200),
				                    new Vector2(600,500), new Vector2(100,600)));
					waves = new int[]{10, 15, 20};
					location = screen;
					boss1 = false;
					boss2 = false;
					isTeleporting = false;
				}};						 
				level = new Level(this, config);
				this.setScreen(level);
				break;
			case COURTYARD:
				config = new LevelConfig() {{
					mapLocation = "maps/courtyardmap.tmx";
					playerSpawn = new Vector2(300, 300);
					powerSpawn = new Vector2(250, 250);
					zombieSpawnPoints = new ArrayList<>(
							 Arrays.asList(new Vector2(120,100), new Vector2(630,600),
					                   new Vector2(630,100), new Vector2(120,500)));
					waves = new int[]{13, 17, 1};
					location = screen;
					boss1 = true;
					boss2 = false;
					isTeleporting = false;
					
				}};						 
				level = new Level(this, config);
				this.setScreen(level);
				break;
			case CENTRALHALL:
				config = new LevelConfig() {{
					mapLocation = "maps/centralhallmap.tmx";
					playerSpawn = new Vector2(50, 900);
					powerSpawn = new Vector2(250, 250);
					zombieSpawnPoints = new ArrayList<Vector2>(
							 Arrays.asList(new Vector2(120,100), new Vector2(630,600),
					                   new Vector2(630,100), new Vector2(120,500)));
					waves = new int[]{13, 17, 1};
					location = screen;
					boss1 = true;
					boss2 = false;
					isTeleporting = false;
				}};						 
				level = new Level(this, config);
				this.setScreen(level);
				break;
			case GLASSHOUSE:
				config = new LevelConfig() {{
					mapLocation = "maps/glasshousemap.tmx";
					playerSpawn = new Vector2(400, 70);
					powerSpawn = new Vector2(250, 250);
					zombieSpawnPoints = new ArrayList<Vector2>(
							 Arrays.asList(new Vector2(120,100), new Vector2(630,600),
					                   new Vector2(630,100), new Vector2(120,500)));
					waves = new int[]{13, 17, 1};
					location = screen;
					boss1 = true;
					boss2 = false;
					isTeleporting = false;
				}};
			case CONSTANTINE:
				config = new LevelConfig() {{
					mapLocation = "maps/constantinemap.tmx";
					playerSpawn = new Vector2(300, 300);
					powerSpawn = new Vector2(250, 250);
					zombieSpawnPoints = new ArrayList<Vector2>(
							 Arrays.asList(new Vector2(120,100), new Vector2(630,600),
					                   new Vector2(630,100), new Vector2(120,500)));
					waves = new int[]{13, 17, 1};
					location = screen;
					boss1 = true;
					boss2 = false;
					isTeleporting = false;
				}};	
				level = new Level(this, config);
				this.setScreen(level);
				break;
			case MINIGAME:
				MiniGame minigame = new MiniGame(this);
				this.setScreen(minigame);
				break;
		}
	}

	/**
	 * Create event run when the class is constructed, loading save data if it exists
	 */
	@Override
	public void create() {

		LoadingScreen loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);

		//Attempt to load save data file
		File f = new File("saveData.txt");
		if(f.isFile()) {
			System.out.println(f.getName() + " exists.");
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(f));
				String st;
				  while ((st = br.readLine()) != null) {
				    System.out.println("Player is on stage:" + st);
				    Zepr.progress = Location.values()[Integer.parseInt(st)];
				  }
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(f.getName() + " does not exist. Creating now...");
			//Create a new save data file if one cannot be found
			try {
				boolean b = f.createNewFile();
				System.out.println(f.getName() + " has been created? ... " + b);
				FileOutputStream edit = new FileOutputStream(f);
				byte[] lvl = ("3").getBytes();
				edit.write(lvl);
				edit.close();
				progress = Location.TOWN;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}