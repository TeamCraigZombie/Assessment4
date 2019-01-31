package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class ZeprInputProcessor implements InputProcessor {

    protected Vector2 mousePosition = new Vector2(0, 0);

    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    	Level.getPlayer().attack = true;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	Level.getPlayer().attack = false;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
