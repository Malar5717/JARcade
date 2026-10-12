package com.malar.jarcade.games;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.malar.jarcade.games.hangman.Hangman;
import com.malar.jarcade.games.tictactoe.TicTacToe;
import com.malar.jarcade.games.wordle.Wordle;

public final class GameRegistry {
    private static final List<Supplier<Game>> registry = new ArrayList<>();

    // instructions to create the respective game objects
    static {
        register(TicTacToe::new);
        register(Wordle::new);
        register(Hangman::new);
        // Create the object when get() is called.
    }

    private GameRegistry() {}

    private static void register(Supplier<Game> factory) {
        registry.add(factory);
    }

    public static List<Supplier<Game>> getFactories() {
        return registry;
    }
}
