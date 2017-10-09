package com.snakegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.snakegame.Application;

public class TextScreen extends AbstractScreen {

    final String text;

    public TextScreen(Application app, String text) {
        super(app);
        this.text = text;
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            app.gsm.backScreen();
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        app.glyphLayout.setText(app.fontLarge, text);

        app.batch.begin();

        app.fontLarge.setColor(Color.BLACK);
        app.fontLarge.draw(app.batch, text, (Application.WIDTH - app.glyphLayout.width) / 2.0f, (Application.HEIGHT + app.glyphLayout.height) / 2.0f);

        app.batch.end();

    }
}
