package com.snakegame.managers;

import com.snakegame.Application;
import com.snakegame.screens.AbstractScreen;
import com.snakegame.screens.GameScreen;
import com.snakegame.screens.MainMenuScreen;

import java.util.HashMap;
import java.util.Stack;

public class GameScreenManager {

    public final Application app;

    Stack<AbstractScreen> screenStack;

    public GameScreenManager(final Application app) {
        this.app = app;

        initGameScreens();
        setScreen(new MainMenuScreen(app, false));
    }

    public void initGameScreens() {
        screenStack = new Stack<AbstractScreen>();
    }

    public void setScreen(AbstractScreen nextScreen) {
        screenStack.add(nextScreen);
        app.setScreen(nextScreen);
    }

    public void backScreen() {
        AbstractScreen lastScreen = screenStack.pop();
        lastScreen.dispose();
        app.setScreen(screenStack.peek());
    }

    public void dispose() {
        while (!screenStack.isEmpty()) {
            AbstractScreen screen = screenStack.pop();
            screen.dispose();
        }
    }
}
