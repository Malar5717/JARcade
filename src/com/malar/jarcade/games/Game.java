package com.malar.jarcade.games;

import com.malar.jarcade.ui.InputProvider;
import com.malar.jarcade.ui.Theme;

public interface Game {
    String getName();
    Theme applyTheme();
    void rules();
    void gameLoop();

    default void start() {
        Theme theme = applyTheme();
        System.out.println("Welcome to " + getName() + " game!");
        rules();
        if (confirmProceed()) {
            gameLoop();
        } else {
            System.out.println("Returning to menu...");
        }
    }

    static boolean confirmProceed() {
        System.out.print("Ready? (y/n): ");
        return InputProvider.readLine().equalsIgnoreCase("Y");
    }
}
