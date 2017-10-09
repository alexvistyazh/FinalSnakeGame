package com.snakegame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.snakegame.Application;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen extends AbstractScreen implements Serializable {

    static class CellCoord implements Serializable {
        int x, y;

        private CellCoord() {}

        CellCoord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (!(obj instanceof CellCoord)) return false;
            CellCoord c = (CellCoord)obj;
            return this.x == c.x && this.y == c.y;
        }
    }

    static class SnakeInside implements Serializable {
        ArrayList<CellCoord> snake;
        int dx, dy;
        boolean isDirectionSet;
        int n, m;

        private SnakeInside() {}

        SnakeInside(int n, int m) {
            this.n = n;
            this.m = m;
            isDirectionSet = false;
            snake = new ArrayList<CellCoord>();
            snake.add(new CellCoord(n / 2, m / 2));
            dx = -2;
            dy = -2;
            isDirectionSet = false;
        }

        boolean setDirection(int dx, int dy) {
            if (snake.size() == 1) {
                this.dx = dx;
                this.dy = dy;
                isDirectionSet = true;
                return true;
            }
            CellCoord head = getHead();
            CellCoord preHead = snake.get(snake.size() - 2);
            int nx = preHead.x - head.x;
            int ny = preHead.y - head.y;
            if (dx == nx && dy == ny) {
                return false;
            }
            this.dx = dx;
            this.dy = dy;
            isDirectionSet = true;
            return true;
        }

        boolean setDirection(CellCoord c) {
            return setDirection(c.x, c.y);
        }

        List<CellCoord> getSnake() {
            return snake;
        }

        CellCoord getHead() {
            return snake.get(snake.size() - 1);
        }

        void removeTail() {
            snake.remove(0);
        }

        boolean move() {
            if (!isDirectionSet) {
                return false;
            }
            CellCoord head = getHead();
            CellCoord newHead = new CellCoord(head.x + dx, head.y + dy);
            snake.add(newHead);
            return true;
        }
    }

    private static final int CELL_PX_SIZE = 32;
    private static final int FRAMES_PER_MOVE = 10;
    private static final int FRAMES_PER_SAVE = 300;
    private static final Random RNG = new Random();

    private SnakeInside snakeInside;
    private CellCoord fruit;
    private ArrayList<CellCoord> walls;
    private int offsetX, offsetY;
    private int cnt, targetLength;

    private static final CellCoord LEFT_DIR = new CellCoord(-1, 0);
    private static final CellCoord RIGHT_DIR = new CellCoord(1, 0);
    private static final CellCoord DOWN_DIR = new CellCoord(0, 1);
    private static final CellCoord UP_DIR = new CellCoord(0, -1);

    private void generateFruit() {
        do {
            fruit = new CellCoord(RNG.nextInt(snakeInside.n), RNG.nextInt(snakeInside.m));
        } while (snakeInside.getSnake().contains(fruit) || walls.contains(fruit));
    }

    private GameScreen() {}

    GameScreen(Application app) {
        super(app);
        int n = (Application.WIDTH - 7) / CELL_PX_SIZE;
        int m = (Application.HEIGHT - 25) / CELL_PX_SIZE;
        targetLength = n * m / 3;
        this.offsetX = 3;
        this.offsetY = 3;
        this.cnt = 0;
        snakeInside = new SnakeInside(n, m);
        walls = new ArrayList<CellCoord>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == 0 || j == 0 || i + 1 == n || j + 1 == m) {
                    walls.add(new CellCoord(i, j));
                }
            }
        }
        generateFruit();
    }

    void gameOver() {
        app.gsm.backScreen();
        app.gsm.setScreen(new TextScreen(app, "GAME OVER!\n" +
                "length is " + snakeInside.getSnake().size() + "\n" +
                "target is " + targetLength));
    }

    private void saveTo(String filename) {
        try {
            FileOutputStream fout = new FileOutputStream(filename);
            ObjectOutputStream objOut = new ObjectOutputStream(fout);
            objOut.writeObject(this);
            objOut.close();
            fout.close();
        } catch (IOException e) {
            // do nothing, if unsuccessful
        }
        System.err.println("saved!");
    }

    static GameScreen loadFrom(String filename, Application app) {
        try {
            FileInputStream fin = new FileInputStream(filename);
            ObjectInputStream objIn = new ObjectInputStream(fin);
            GameScreen res;
            res = (GameScreen) objIn.readObject();
            res.app = app;
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void win() {
        app.gsm.backScreen();
        app.gsm.setScreen(new TextScreen(app, "YOU ARE WIN!"));
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            snakeInside.isDirectionSet = false;
            app.gsm.setScreen(new MainMenuScreen(app, true));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            snakeInside.setDirection(DOWN_DIR);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            snakeInside.setDirection(UP_DIR);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            snakeInside.setDirection(LEFT_DIR);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            snakeInside.setDirection(RIGHT_DIR);
        }

        cnt++;

        if (cnt % FRAMES_PER_SAVE == 0) {
            saveTo(Application.saveFileName);
        }

        if (cnt % FRAMES_PER_MOVE == 0) {
            if (!snakeInside.move()) {
                return;
            }
            if (walls.contains(snakeInside.getHead())) {
                gameOver();
            }
            if (snakeInside.getSnake().indexOf(snakeInside.getHead()) < snakeInside.getSnake().size() - 1) {
                gameOver();
            }
            if (!snakeInside.getHead().equals(fruit)) {
                snakeInside.removeTail();
            } else {
                generateFruit();
            }
        }

        if (snakeInside.getSnake().size() == targetLength) {
            win();
        }
    }

    private void drawCell(CellCoord c) {
        app.pixmap.fillRectangle(offsetX + c.x * CELL_PX_SIZE, offsetY + c.y * CELL_PX_SIZE, CELL_PX_SIZE, CELL_PX_SIZE);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        app.batch.begin();

        app.pixmap.setColor(Color.WHITE);
        app.pixmap.fill();

        app.pixmap.setColor(Color.RED);
        for (CellCoord c : snakeInside.getSnake()) {
            drawCell(c);
        }

        app.pixmap.setColor(Color.GOLD);
        drawCell(snakeInside.getHead());

        app.pixmap.setColor(Color.BLACK);
        for (CellCoord c : walls) {
            drawCell(c);
        }

        app.pixmap.setColor(Color.GREEN);
        drawCell(fruit);

        app.pixmap.setColor(Color.BLACK);
        for (int i = 0; i <= snakeInside.n; i++) {
            app.pixmap.drawLine(i * CELL_PX_SIZE + offsetX, 0, i * CELL_PX_SIZE + offsetX, offsetY + snakeInside.m * CELL_PX_SIZE);
        }
        for (int j = 0; j <= snakeInside.m; j++) {
            app.pixmap.drawLine(0, j * CELL_PX_SIZE + offsetY, Application.WIDTH, j * CELL_PX_SIZE + offsetY);
        }

        Texture t = new Texture(app.pixmap);

        app.batch.draw(t, 0, 0);

        app.fontSmall.setColor(Color.BLACK);
        String text = "snake length: " + snakeInside.getSnake().size() + ", target: " + targetLength;
        app.glyphLayout.setText(app.fontSmall, text);
        float height = app.glyphLayout.height;
        app.fontSmall.draw(app.batch, text, 3, height + 5);

        app.batch.end();
    }

}
