package com.snakegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.snakegame.managers.GameScreenManager;

public class Application extends Game {

    // Application Vars

    public static final int APP_FPS = 60;
    public static final String APP_TITLE = "netman's snake";
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 700;
	public static final String saveFileName = "game.save";


    public GameScreenManager gsm;


    public GlyphLayout glyphLayout;
    public Pixmap pixmap;
    public SpriteBatch batch;
    public BitmapFont fontLarge;
    public BitmapFont fontSmall;
    public BitmapFont fontMiddle;

	@Override
	public void create () {
	    pixmap = new Pixmap(Application.WIDTH, Application.HEIGHT, Pixmap.Format.RGBA8888);
        batch = new SpriteBatch();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Typewriter-Regular.otf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        fontSmall = generator.generateFont(parameter);

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        fontMiddle = generator.generateFont(parameter);

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        fontLarge = generator.generateFont(parameter);

        generator.dispose();

        // Setup manager
        gsm = new GameScreenManager(this);

        glyphLayout = new GlyphLayout();
	}

    @Override
    public void dispose () {
	    super.dispose();
        batch.dispose();
        pixmap.dispose();

        gsm.dispose();

        fontLarge.dispose();
        fontMiddle.dispose();
        fontSmall.dispose();
    }

	@Override
	public void render () {
	    super.render();
	}
	

}
