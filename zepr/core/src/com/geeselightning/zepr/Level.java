package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.geeselightning.zepr.screens.TextScreen;

import java.util.ArrayList;


public class Level implements Screen {

    private Zepr parent;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private static Player player;
    private ArrayList<Zombie> aliveZombies;
    public ArrayList<Vector2> zombieSpawnPoints;
    private ZeprInputProcessor inputProcessor;
    private boolean isPaused;
    private Stage stage;
    private Table table;
    private Skin skin;
    private int[] waves;
    private int currentWave = 1;
    public int zombiesRemaining; // the number of zombies left to kill to complete the wave
    public int zombiesToSpawn; // the number of zombies that are left to be spawned this wave
    private boolean pauseButton;
    Texture blank;
    Vector2 powerSpawn;
    PowerUp currentPowerUp = null;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private static float worldScale = 1.f;
    static float physicsDensity = 100.f;

    Label progressLabel, healthLabel, powerupLabel;

    public Level(Zepr zepr, String mapLocation, Vector2 playerSpawn, ArrayList<Vector2> zombieSpawnPoints, int[] waves, Vector2 powerSpawn) {
   
    	parent = zepr;
        this.zombieSpawnPoints = zombieSpawnPoints;
        this.isPaused = false;
        this.blank = new Texture("blank.png");
        this.powerSpawn = powerSpawn;
        
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        aliveZombies = new ArrayList<Zombie>();
        inputProcessor = new ZeprInputProcessor();
        pauseButton = false;
        
        progressLabel = new Label("", skin);
        healthLabel = new Label("", skin);
        powerupLabel = new Label("", skin);

        // Set up data for first wave of zombies
        this.waves = waves;
        this.zombiesRemaining = waves[0];
        this.zombiesToSpawn = zombiesRemaining;

        // Creating a new libgdx stage to contain the pause menu and in game UI
        this.stage = new Stage(new ScreenViewport());

        // Creating a table to hold the UI and pause menu
        this.table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        
        // Loads the testmap.tmx file as map.
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load(mapLocation);

        // renderer renders the .tmx map as an orthogonal (top-down) map.
        renderer = new OrthogonalTiledMapRenderer(map, worldScale);
           
        //Initialise Box2D physics engine
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();
        
        MapBodyBuilder.buildShapes(map, physicsDensity / worldScale, world);
        
        Player.setLevel(this);
        player = new Player(new Sprite(new Texture("player01.png")), new Vector2(300, 300));
        
        // It is only possible to view the render of the map through an orthographic camera.
        camera = new OrthographicCamera();

        //reset player instance
        player.respawn(playerSpawn, this);

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
    public void spawnZombies(int numberToSpawn, ArrayList<Vector2> spawnPoints) {

        for (int i = 0; i < numberToSpawn; i++) {

            Zombie zombie = (new Zombie(new Sprite(new Texture("zombie01.png")),
                    spawnPoints.get(i % spawnPoints.size()), this));       
            aliveZombies.add(zombie);
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
        // Start the stage unpaused.
        isPaused = false;
    }

    private void pauseGame() {
        // Input processor has to be changed back once unpaused.
        Gdx.input.setInputProcessor(stage);

        TextButton resume = new TextButton("Resume", skin);
        TextButton exit = new TextButton("Exit", skin);

        if (!pauseButton) {

            table.clear();
            table.center();
            table.add(resume).pad(10);
            table.row();
            table.add(exit);
            pauseButton = true;
        }

        // Defining actions for the resume button.
        resume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isPaused = false;
                // Change input processor back
                Gdx.input.setInputProcessor(inputProcessor);
                pauseButton = false;
            }
        });

        // Defining actions for the exit button.
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Zepr.SELECT);
            }
        });
    }

    private void resumeGame() {
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
        
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
          	isPaused = !isPaused;
          	
          	if(isPaused) {
                pauseGame();
          	}
          	else {
                resumeGame();
          	}
        }   
        
        if (!isPaused){
    
            update(delta);

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
                if(!currentPowerUp.active) {
                    if (currentPowerUp.overlapsPlayer())
                        currentPowerUp.activate();
                    currentPowerUp.draw(batch);
                }
                currentPowerUp.update(delta);
            }

            batch.end();
                    
            debugRenderer.render(world, camera.combined.scl(physicsDensity));
        }
        
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }
    
    public void update(float delta) {
    	  	       
    	world.step(1/60f, 6, 2);

    	player.update();

    	for(int i = 0; i < aliveZombies.size(); i++)
            aliveZombies.get(i).update();

        zombiesRemaining = aliveZombies.size();

        if (zombiesRemaining == 0) {

            // Spawn a power up and the end of a wave, if there isn't already a powerUp on the level
            if (currentPowerUp == null) {

                int random = (int )(Math.random() * 5 + 1);
                if (random == 1)
                    currentPowerUp = new PowerUpHeal(this);
                else if (random == 2)
                    currentPowerUp = new PowerUpSpeed(this);
                else if (random == 3)
                    currentPowerUp = new PowerUpImmunity(this);
                else if (random == 4)
                    currentPowerUp = new PowerUpInstaKill(this);
                else //random == 5
                    currentPowerUp = new PowerUpInvisibility(this);
            }

       	 	// Spawn all zombies in the stage
            spawnZombies(zombiesToSpawn, zombieSpawnPoints);
            // Wave complete, increment wave number
            currentWave++;
            if (currentWave > waves.length) {
                // Level completed, back to select screen and complete stage.
                // If stage is being replayed complete() will stop progress being incremented.
                isPaused = true;
                complete();
                if (parent.progress == Zepr.COMPLETE)
                    parent.setScreen(new TextScreen(parent, "Game completed."));
                else
                    parent.setScreen(new TextScreen(parent, "Level completed."));
            } else {
                // Update zombiesRemaining with the number of zombies of the new wave
                zombiesRemaining = waves[currentWave - 1];
                zombiesToSpawn = zombiesRemaining;
            }
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
        player.getTexture().dispose();
        for (Zombie zombie : aliveZombies)
            zombie.getTexture().dispose();
    }
}
