package com.malar.jarcade.games.wordle;

import java.util.List;

import com.malar.jarcade.games.Game;
import com.malar.jarcade.ui.Colors;
import com.malar.jarcade.ui.InputProvider;
import com.malar.jarcade.ui.Theme;
import com.malar.jarcade.resource.WordLoader;

public class Wordle implements Game {

    private Theme theme;
    private int attempt = 0;
    private int MAX_ATTEMPTS = 6;

    private List<String> answers;
    private List<String> options;

    private String answer;

    private final Blanks blanks = new Blanks();

    void loadWords() {
        if (answers != null && options != null) return;

        answers = WordLoader.loadWords("answers.txt", 5, 5);
        options = WordLoader.loadWords("options.txt", 5, 5);
        options.addAll(answers);
    }

    public Wordle() {
        loadWords();
    }

    @Override
    public String getName() {
        return theme.primary() + "Wordle" + Colors.RESET;
    }

    @Override
    public Theme applyTheme() {
        this.theme = new Theme(Colors.GREEN, Colors.YELLOW);
        return this.theme;
    }

    @Override
    public void rules() {
        System.out.println(Colors.YELLOW
                + "Wordle is a word guessing game where players have six attempts to guess a five-letter word. After each guess, the game provides feedback in the form of colored tiles: green for correct letters in the correct position, yellow for correct letters in the wrong position, and gray for incorrect letters. The objective is to guess the word within the allotted attempts."
                + Colors.RESET);
    }

    public void pick() {
        int idx = (int) (Math.random() * answers.size());
        answer = answers.get(idx);

        System.out.println("A word has been chosen. Start guessing!");
        blanks.renderEmpty();
    }

    public void gameArea() {
        System.out.println("Attempts left: " + (MAX_ATTEMPTS - attempt));
    }

    private String guess;
    public boolean handleInput() {
        System.out.println("Enter your guess: ");
        guess = InputProvider.readLine().toUpperCase();

        if (guess.length() != 5) {
            System.out.println(Colors.RED + "Invalid input! Please enter a 5-letter word." + Colors.RESET);
            return false;
        }

        if (!options.contains(guess)) {
            System.out.println(Colors.RED + "Word not in list! Try again." + Colors.RESET);
            return false;
        }

        return true;
    }

    boolean won() {
        return guess.equals(answer);
    }

    @Override
    public void gameLoop() {
        pick();
        boolean playing = true;
        while (playing) {
            if (!handleInput()) {
                continue;
            }

            if (blanks.updateBlanks(guess, answer, attempt, MAX_ATTEMPTS)) {
                attempt++;
            } else {
                continue;
            }

            if (won()) {
                System.out.println("You guessed the word! The word was: " + answer);
                return;
            }

            if (attempt == MAX_ATTEMPTS) {
                System.out.println(Colors.YELLOW + "Out of attempts! The word was: " + answer + Colors.RESET);
                return;
            }
        }
    }
}
