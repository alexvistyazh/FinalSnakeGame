package com.snakegame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.snakegame.Application;

import java.util.ArrayList;
import java.util.List;

public class MainMenuScreen extends AbstractScreen {

    static interface Action {
        public void act();
    }

    static class ListItem {
        String text;
        Action action;
        ListItem(String text, Action action) {
            this.text = text;
            this.action = action;
        }
    }


    List<ListItem> lst;
    int curLstIdx;

    public MainMenuScreen(final Application app, boolean continueAvailable) {
        super(app);
        curLstIdx = 0;
        lst = new ArrayList<ListItem>();
        if (continueAvailable) {
            lst.add(new ListItem("continue", new Action() {
                @Override
                public void act() {
                    app.gsm.backScreen();
                }
            }));
        } else if (!continueAvailable) {
            final GameScreen gameScreen = GameScreen.loadFrom(Application.saveFileName, this.app);
            if (gameScreen != null) {
                lst.add(new ListItem("load last game", new Action() {
                    @Override
                    public void act() {
                        app.gsm.setScreen(gameScreen);
                    }
                }));
            }
        }
        lst.add(new ListItem("new game", new Action() {
            @Override
            public void act() {
                app.gsm.setScreen(new GameScreen(app));
            }
        }));
        lst.add(new ListItem("exit", new Action() {
            @Override
            public void act() {
                Gdx.app.exit();
            }
        }));
    }



    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            curLstIdx = (curLstIdx + 1) % lst.size();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            curLstIdx = (curLstIdx - 1 + lst.size()) % lst.size();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            lst.get(curLstIdx).action.act();
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        app.batch.begin();

        BitmapFont curFont = app.fontMiddle;

        for (int i = 0; i < lst.size(); i++) {
            if (i == curLstIdx) {
                curFont.setColor(Color.RED);
            } else {
                curFont.setColor(Color.BLACK);
            }
            ListItem item = lst.get(i);

            app.glyphLayout.setText(curFont, item.text);

            curFont.draw(app.batch, item.text, (Application.WIDTH - app.glyphLayout.width) / 2, Application.HEIGHT - 100 - i * curFont.getLineHeight());
        }

        app.batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
