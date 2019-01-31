package com.geeselightning.zepr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.geeselightning.zepr.Zepr;

public class StoryScreen implements Screen {

    private Zepr parent;
    private Stage stage;

    public StoryScreen(Zepr zepr) {
        // Constructor builds the gui of the menu screen.
        // parent allows the StoryScreen to reference the MyGdxGame class.
        parent = zepr;

        // The stage is the controller which will react to inputs from the user.
        this.stage = new Stage(new ScreenViewport());
    }
    @Override
    public void show() {
        // Send any input from the user to the stage.
        Gdx.input.setInputProcessor(stage);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        // table.setDebug(true); // Adds borders for the table.
        stage.addActor(table);

        // Importing the necessary assets for the button textures.
        Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        // Writing the story.
        Label line1 = new Label( "Story goes here", skin, "subtitle");
        line1.setWrap(true);
        Label line2 = new Label("After a hard night of partying after the dreaded POPL exam,", skin, "subtitle");
        line2.setWrap(true);
        Label line3 = new Label("you wake up to find yourself in the middle of town," , skin, "subtitle");
        line3.setWrap(true);
        Label line4 = new Label("your friends nowhere to be found.", skin, "subtitle");
        line4.setWrap(true);
        line1.setAlignment(Align.center);
        line2.setAlignment(Align.center);
        line3.setAlignment(Align.center);
        line4.setAlignment(Align.center);

        // Creating buttons.
        TextButton cont = new TextButton("Continue", skin);

        // Adding content to the table (screen).
        table.add(line1).colspan(3);
        table.row().pad(10, 40, 10, 40);
        table.add(line2).colspan(3);
        table.row().pad(10, 40, 10, 40);
        table.add(line3).colspan(3);
        table.row().pad(10, 40, 10, 40);
        table.add(line4).colspan(3);
        table.row().pad(10, 40, 10, 40);
        table.add(cont);

        // Defining actions for the start button.
        cont.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Zepr.SELECT);
            }
        });
    }

    @Override
    public void render(float delta) {
        // Clears the screen to black.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draws the stage.
        this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Update the screen when the window resolution is changed.
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        // Dispose of assets when they are no longer needed.
        stage.dispose();
    }
}