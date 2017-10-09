package com.snakegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.snakegame.Application;
import com.snakegame.managers.GameScreenManager;

import java.io.Serializable;

public abstract class AbstractScreen implements Screen {

    transient Application app;

    AbstractScreen(final Application app) {
        this.app = app;
    }

    AbstractScreen() {}

    public abstract void update(float delta);

    @Override
    public void render(float delta) {
        update(delta);

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void hide() {}

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
    }
}
