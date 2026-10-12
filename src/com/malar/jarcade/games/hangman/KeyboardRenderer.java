package com.malar.jarcade.games.hangman;

import java.util.HashSet;
import java.util.Set;

import com.malar.jarcade.ui.Colors;

public class KeyboardRenderer {
    private final StringBuilder kb1 = new StringBuilder("Q W E R T Y U I O P");
    private final StringBuilder kb2 = new StringBuilder("A S D F G H J K L");
    private final StringBuilder kb3 = new StringBuilder("Z X C V B N M");

    private final Set<Character> wrongGuesses = new HashSet<>();
    private final Set<Character> rightGuesses = new HashSet<>();

    public void addWrongGuess(char g) {
        wrongGuesses.add(Character.toUpperCase(g));
    }

    public void addRightGuess(char g) {
        rightGuesses.add(Character.toUpperCase(g));
    }

    public void renderKeyboard() {
        System.out.println("Keyboard:");

        for (int i = 0; i < kb1.length(); i++) {
            char c = kb1.charAt(i);
            if (wrongGuesses.contains(c)) {
                System.out.print(Colors.RED + c + Colors.RESET + " ");
            } else if (rightGuesses.contains(c)) {
                System.out.print(Colors.GREEN + c + Colors.RESET + " ");
            } else {
                System.out.print(c + " ");
            }
        }
        System.out.println();

        for (int i = 0; i < kb2.length(); i++) {
            char c = kb2.charAt(i);
            if (wrongGuesses.contains(c)) {
                System.out.print(Colors.RED + c + Colors.RESET + " ");
            } else if (rightGuesses.contains(c)) {
                System.out.print(Colors.GREEN + c + Colors.RESET + " ");
            } else {
                System.out.print(c + " ");
            }
        }
        System.out.println();

        for (int i = 0; i < kb3.length(); i++) {
            char c = kb3.charAt(i);
            if (wrongGuesses.contains(c)) {
                System.out.print(Colors.RED + c + Colors.RESET + " ");
            } else if (rightGuesses.contains(c)) {
                System.out.print(Colors.GREEN + c + Colors.RESET + " ");
            } else {
                System.out.print(c + " ");
            }
        }
        System.out.println();
    }
}
