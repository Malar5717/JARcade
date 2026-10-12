package com.malar.jarcade;

import java.util.*;
import java.util.function.Supplier;

import com.malar.jarcade.games.Game;
import com.malar.jarcade.games.GameRegistry;
import com.malar.jarcade.ui.InputProvider;

public class JARcade {
    public static void main(String[] args) {
        List<Supplier<Game>> factories = GameRegistry.getFactories();

        List<String> names = List.of(
                    "TicTacToe",
                    "Wordle",
                    "Hangman");

        System.out.println("\nWELCOME TO JARCADE!\n");

        while (true) {

            System.out.println("Choose: ");
            System.out.println("0. Exit");
            for (int i = 0; i < factories.size(); i++) {
                System.out.println((i + 1) + ". " + names.get(i));
            }

            int choice;
            try {
                choice = InputProvider.readInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }
            if (choice == 0) {
                System.out.println("Thanks for playing!");
                break;
            }
            if (choice < 1 || choice > factories.size()) {
                System.out.println("Invalid choice! Please select 0-" + factories.size() + ".");
                continue;
            }

            Supplier<Game> selectedFactory = factories.get(choice - 1);
            Game selectedGame = selectedFactory.get();
            selectedGame.start();

            System.out.println("Game ended.\n\n\nPlay again?");
        }
    }
}
