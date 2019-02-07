package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.geeselightning.zepr.pathfinding.GraphGenerator;
import com.geeselightning.zepr.pathfinding.GraphImp;
import com.geeselightning.zepr.powerups.*;
import com.geeselightning.zepr.screens.TextScreen;
import java.util.ArrayList;


public class Level implements Screen {

    private Zepr parent;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private static Player player;
    private ArrayList<Zombie> aliveZombies;
    private ZeprInputProcessor inputProcessor;
    private boolean isPaused;
    private Stage stage;
    private Table table;
    private Skin skin;
    private int currentWave = 0;
    public int zombiesRemaining; // the number of zombies left to kill to complete the wave
    public int zombiesToSpawn; // the number of zombies that are left to be spawned this wave
    static Texture blank;
    public PowerUp currentPowerUp = null;
    private Box2DDebugRenderer debugRenderer;
    private static float worldScale = 1.f;
    static float physicsDensity = 100.f;
    public LevelConfig config;
    private World world;

    public static int lvlTileWidth;
    public static int lvlTileHeight;
    public static int lvlPixelWidth;
    public static int lvlPixelHeight;
    public static int tilePixelWidth;
    public static int tilePixelHeight;
    public static GraphImp graph;

    Label progressLabel, healthLabel, powerupLabel;

    public Level(Zepr zepr, LevelConfig config) {
   
    	this.world = zepr.getWorld();
    	parent = zepr;
    	this.config = config;
        blank = new Texture("blank.png");


        player = new Player(new Sprite(new Texture("player01.png")), new Vector2(300, 300), world);
        
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        aliveZombies = new ArrayList<Zombie>();
        inputProcessor = new ZeprInputProcessor();
        
        progressLabel = new Label("", skin);
        healthLabel = new Label("", skin);
        powerupLabel = new Label("", skin);

        // Set up data for first wave of zombies
        this.zombiesRemaining = config.waves[0];
        this.zombiesToSpawn = zombiesRemaining;

        // Creating a new libgdx stage to contain the pause menu and in game UI
        this.stage = new Stage(new ScreenViewport());

        // Creating a table to hold the UI and pause menu
        this.table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        
        // Loads the testmap.tmx file as map.
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load(config.mapLocation);

        // get level width/height in both tiles and pixels and hang on to the values
        MapProperties properties = map.getProperties();
        lvlTileWidth = properties.get("width", Integer.class);
        lvlTileHeight = properties.get("height", Integer.class);
        tilePixelWidth = properties.get("tilewidth", Integer.class);
        tilePixelHeight = properties.get("tileheight", Integer.class);
        lvlPixelWidth = lvlTileWidth * tilePixelWidth;
        lvlPixelHeight = lvlTileHeight * tilePixelHeight;
        // graph for indexed a star search for zombie pathfinding
        graph = GraphGenerator.generateGraph(map);

        // renderer renders the .tmx map as an orthogonal (top-down) map.
        renderer = new OrthogonalTiledMapRenderer(map, Constant.worldScale);
           
        debugRenderer = new Box2DDebugRenderer();
        
        MapBodyBuilder.buildShapes(map, Constant.physicsDensity / Constant.worldScale, world);

        
        // It is only possible to view the render of the map through an orthographic camera.
        camera = new OrthographicCamera();

        //reset player instance
        player.respawn(config.playerSpawn);

        Gdx.input.setInputProcessor(inputProcessor);

        resumeGame();
    }
    
    public static Player getPlayer() {
    	return player;
    }
    
    public ArrayList<Zombie> getAliveZombiesList() {
    	return aliveZombies;
    }

    /**
     * Called once the stage is complete to update the game progress
     */
    protected void complete() {
        // implemented in subclasses
    }
    
    
    public World getBox2DWorld() {
    	return world;
    }
    static {
        GdxNativesLoader.load();
    }


    /**
     * Called when the player's health <= 0 to end the stage.
     */
    public void gameOver() {
        isPaused = true;
        parent.setScreen(new TextScreen(parent, "You died."));
    }


    /**
     * Spawns multiple zombies cycling through spawnPoints until the given amount have been spawned.
     *
     * @param spawnPoints locations where zombies should be spawned on this stage
     * @param numberToSpawn number of zombies to spawn
     */
    public void spawnZombies(int numberToSpawn, ArrayList<Vector2> spawnPoints, boolean boss1, boolean boss2) {
    	if (boss2 == true && numberToSpawn == 1) {
        	Zombie zombie = new Zombie(new Sprite(new Texture("GeeseLightingBoss.png")),
                    spawnPoints.get(0), world, player,Constant.ZOMBIESPEED * 15, Constant.ZOMBIEMAXHP * 5);
            aliveZombies.add(zombie);
        }
    	else if (boss1 == true && numberToSpawn == 1) {
        	Zombie zombie = new Zombie(new Sprite(new Texture("GeeseLightingBoss.png")),
                    spawnPoints.get(0), world, player,Constant.ZOMBIESPEED * 15, Constant.ZOMBIEMAXHP * 5);
            aliveZombies.add(zombie);
        }
    	else {
    	
    	for (int i = 0; i < numberToSpawn/3; i++) {
	
	            Zombie zombie = new Zombie(new Sprite(new Texture("zombie01.png")),
	                    spawnPoints.get(i % spawnPoints.size()), world, player, Constant.ZOMBIESPEED, Constant.ZOMBIEMAXHP);
	            aliveZombies.add(zombie);
	        }
	        for (int i = 0; i < numberToSpawn/3; i++) {
	
	            Zombie zombie = new Zombie(new Sprite(new Texture("player01.png")),
	                    spawnPoints.get(i % spawnPoints.size()), world, player, Constant.ZOMBIESPEED * 1.5f, Constant.ZOMBIEMAXHP);
	            aliveZombies.add(zombie);
	        }
	        for (int i = 0; i < (numberToSpawn - (2*(numberToSpawn/3))); i++) {
	
	            Zombie zombie = new Zombie(new Sprite(new Texture("player02.png")),
	                    spawnPoints.get(i % spawnPoints.size()), world, player, Constant.ZOMBIESPEED, Constant.ZOMBIEMAXHP * 2);
	            aliveZombies.add(zombie);
        
    	}
    }
   }


    /**
     * Converts the mousePosition which is a Vector2 representing the coordinates of the mouse within the game window
     * to a Vector2 of the equivalent coordinates in the world.
     *
     * @return Vector2 of the mouse position in the world.
     */
    public Vector2 getMouseWorldCoordinates() {
        // Must first convert to 3D vector as camera.unproject() does not take 2D vectors.
        Vector3 screenCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 worldCoords3 = camera.unproject(screenCoordinates);

        return new Vector2(worldCoords3.x, worldCoords3.y);
    }

    @Override
    public void show() {
    }

    private void pauseGame() {
        isPaused = true;
        // Input processor has to be changed back once unpaused.
        Gdx.input.setInputProcessor(stage);

        TextButton resume = new TextButton("Resume", skin);
        TextButton exit = new TextButton("Exit", skin);

        table.clear();
        table.center();
        table.add(resume).pad(10);
        table.row();
        table.add(exit);
        // Defining actions for the resume button.
        resume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Change input processor back
                Gdx.input.setInputProcessor(inputProcessor);
                resumeGame();
            }
        });

        // Defining actions for the exit button.
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Zepr.location.SELECT);
            }
        });
    }

    private void resumeGame() {
        isPaused = false;
        table.clear();
        table.top().left();
        table.add(progressLabel).pad(10);
        table.row().pad(10);
        table.add(healthLabel).pad(10).left();
        table.row();
        table.add(powerupLabel);
    }

    @Override
    public void render(float delta) {
    	
    	 // Clears the screen to black.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!isPaused){

            update(delta);

            if (!isPaused) {

                // Keep the player central in the screen.
                camera.position.set(player.getCenter().x, player.getCenter().y, 0);
                camera.update();

                renderer.setView(camera);
                renderer.render();

                Batch batch = renderer.getBatch();
                batch.begin();

                player.draw(batch);

                // Draw zombies
                for (Zombie zombie : aliveZombies)
                    zombie.draw(batch);

                if (currentPowerUp != null) {
                    // Activate the powerup up if the player moves over it and it's not already active
                    // Only render the powerup if it is not active, otherwise it disappears
                    if (!currentPowerUp.active) {
                        if (currentPowerUp.overlapsPlayer())
                            currentPowerUp.activate();
                        currentPowerUp.draw(batch);
                    }
                    currentPowerUp.update(delta);
                }

                batch.end();

                debugRenderer.render(world, camera.combined.scl(Constant.physicsDensity));
            }
        }
        
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        if (Gdx.input.isKeyPressed(Keys.ESCAPE))
            pauseGame();
    }
    
    public void update(float delta) {
    	  	       
    	world.step(1/60f, 6, 2);

    	player.updateSprite();
    	player.look(getMouseWorldCoordinates());
    	
    	// When you die, end the level.
        if (player.health <= 0)
           gameOver();

    	for(int i = 0; i < aliveZombies.size(); i++) {
            Zombie zomb = aliveZombies.get(i);
            zomb.update();
                        
            if (zomb.getHealth() <= 0) {
                zombiesRemaining--;
                aliveZombies.remove(zomb);
                zomb.dispose();
            }
        }

        zombiesRemaining = aliveZombies.size();

        if (zombiesRemaining == 0) {

            // Spawn a power up and the end of a wave, if there isn't already a powerUp on the level
            if (currentPowerUp == null) {

                int random = (int)(Math.random() * 5 + 1);
                switch(random) {
	                case 1:
	                	currentPowerUp = new PowerUpHeal(this, player);
	                	break;
	                case 2:
	                	currentPowerUp = new PowerUpSpeed(this, player);
	                	break;
	                case 3:
	                	currentPowerUp = new PowerUpImmunity(this, player);
	                	break;
	                case 4:
	                	currentPowerUp = new PowerUpInstaKill(this, player);
	                	break;
	                case 5:
	                	currentPowerUp = new PowerUpInvisibility(this, player);
	                	break;
                }
            }

       	 	// Spawn all zombies in the stage
            spawnZombies(zombiesToSpawn, config.zombieSpawnPoints,config.boss1, config.boss2);
            // Wave complete, increment wave number
            currentWave++;
            if (currentWave > config.waves.length) {
                // Level completed, back to select screen and complete stage.
                // If stage is being replayed complete() will stop progress being incremented.
                isPaused = true;
                complete();
                if (Zepr.progress == Zepr.location.COMPLETE)
                    parent.setScreen(new TextScreen(parent, "Game completed."));
                else {
                    parent.setScreen(new TextScreen(parent, "Level completed."));
	                if(Zepr.progress == config.location)
	                	Zepr.progress = Zepr.location.values()[Zepr.progress.ordinal() + 1];
                }
            } else {
            	
            	// Update zombiesRemaining with the number of zombies of the new wave
                if (currentWave < config.waves.length)
                	{
                		zombiesRemaining = config.waves[currentWave];
                	}
                else
                {
                	zombiesRemaining =0;
                }
                zombiesToSpawn = zombiesRemaining;           }
        }
    	
    	 // Resolve all possible attacks
        for (Zombie zombie : aliveZombies) {
            // Zombies will only attack if they are in range, the attack has cooled down, and they are
            // facing a player.
            // Player will only attack in the reverse situation but player.attack must also be true. This is
            //controlled by the ZeprInputProcessor. So the player will only attack when the user clicks.
            if (player.attack)
                player.attack(zombie, delta);
            zombie.attack(player, delta);          
        }    
        
        String progressString = ("Wave " + Integer.toString(currentWave) + ", " + Integer.toString(zombiesRemaining) + " zombies remaining.");
        String healthString = ("Health: " + Integer.toString(player.health) + "HP");

        progressLabel.setText(progressString);
        healthLabel.setText(healthString);
    }

    @Override
    public void resize(int width, int height) {
    	// Resize the camera depending the size of the window.
        camera.viewportHeight = height;
        camera.viewportWidth = width;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
        map.dispose();
        renderer.dispose();
        debugRenderer.dispose();
        if (currentPowerUp != null)
            currentPowerUp.getTexture().dispose();
        for (Zombie zombie : aliveZombies)
            zombie.dispose();
        player.dispose();
        
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        for(Body body : bodies)
        	world.destroyBody(body);
    }
}
