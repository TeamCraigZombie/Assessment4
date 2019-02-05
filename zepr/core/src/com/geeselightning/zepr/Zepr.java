package com.geeselightning.zepr;

import com.badlogic.gdx.Game;
import com.geeselightning.zepr.screens.LoadingScreen;
import com.geeselightning.zepr.screens.MenuScreen;
import com.geeselightning.zepr.screens.SelectLevelScreen;
import com.geeselightning.zepr.screens.StoryScreen;

public class Zepr extends Game {

	private LoadingScreen loadingScreen;
	private MenuScreen menuScreen;
	private StoryScreen storyScreen;
	private Level level;
	private SelectLevelScreen selectLevelScreen;
	private MiniGame MiniGame;

	// The progress is the integer representing the last level completed. i.e. 3 for Town
	public static int progress = 3;

	public final static int MENU = 0;
	public final static int STORY = 1;
	public final static int SELECT = 2;
	public final static int TOWN = 3;
	public final static int HALIFAX = 4;
	public final static int COURTYARD = 5;
	public final static int COMPLETE = 6;
	public final static int MINIGAME = 7;


	public void changeScreen(int screen) {
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
				level = new TownLevel(this);
				this.setScreen(level);
				break;
			case HALIFAX:
				level = new HalifaxLevel(this);
				this.setScreen(level);
				break;
			case COURTYARD:
				level = new CourtyardLevel(this);
				this.setScreen(level);
				break;
			case MINIGAME:
				MiniGame = new MiniGame(this);
				this.setScreen(MiniGame);
				break;
		}
	}

	@Override
	public void create() {
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}
}