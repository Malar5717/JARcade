package com.malar.jarcade.games.hangman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.malar.jarcade.games.Game;
import com.malar.jarcade.ui.Colors;
import com.malar.jarcade.ui.InputProvider;
import com.malar.jarcade.ui.Theme;
import com.malar.jarcade.resource.WordLoader;

public class Hangman implements Game {

    private Theme theme;
    private List<String> words;

    private record LengthRange(int min, int max) {}
    private final Map<Integer, LengthRange> difficultyRanges = new HashMap<>();
    {
        difficultyRanges.put(0, new LengthRange(5, 6));
        difficultyRanges.put(1, new LengthRange(7, 8));
        difficultyRanges.put(2, new LengthRange(9, 11));
    }

    private String word;
    private int wordLength;
    private int maxAttempts;
    private int attempts;
    private int wrGs = 0;

    WordTracker wordTracker = new WordTracker();
    KeyboardRenderer keyboard = new KeyboardRenderer();
    private Hang Man = new Hang();

    void load(int level) {
        LengthRange range = difficultyRanges.getOrDefault(level, difficultyRanges.get(0));
        words = WordLoader.loadWords("words.txt", range.min(), range.max());
    }

    public Hangman() {
        load(0);
    }

    @Override
    public String getName() {
        return theme.primary() + "Hangman" + Colors.RESET;
    }

    @Override
    public Theme applyTheme() {
        this.theme = new Theme(Colors.YELLOW, Colors.BLUE);
        return this.theme;
    }

    @Override
    public void rules() {
        System.out.println(theme.secondary() + "Hangman is a word guessing game where players try to "
                + "guess a hidden word by suggesting letters within a certain number of guesses. "
                + "Each incorrect guess brings the player closer to being 'hanged'." + Colors.RESET);
    }

    public void difficulty() {
        System.out.println("Choose difficulty (E - easy / M - medium / H - hard): ");
        String diff = InputProvider.readLine().toUpperCase();
        switch (diff) {
            case "E":
                System.out.println(theme.secondary() + "Easy mode selected." + Colors.RESET);
                load(0);
                break;
            case "M":
                System.out.println(theme.secondary() + "Medium mode selected." + Colors.RESET);
                load(1);
                break;
            case "H":
                System.out.println(theme.secondary() + "Hard mode selected." + Colors.RESET);
                load(2);
                break;
            default:
                System.out.println("Invalid choice, defaulting to Easy mode.");
                load(0);
        }
    }

    public void pick() {
        int idx = (int) (Math.random() * words.size());
        word = words.get(idx);
        wordLength = word.length();
        maxAttempts = wordLength + 1;
        attempts = maxAttempts;

        System.out.println("A word has been chosen. Start guessing!");
        wordTracker.init(wordLength);
        wordTracker.render();
    }

    public boolean handleInput() {
        System.out.println();
        System.out.println("Enter your guess: ");
        String guess = InputProvider.readLine().toUpperCase();

        if (guess.length() != 1) {
            System.out.println(Colors.RED + "Invalid input! Please enter a single letter." + Colors.RESET);
            return false;
        }

        char g = guess.charAt(0);
        if (!Character.isLetter(g)) {
            System.out.println(Colors.RED + "Invalid input! Please enter a letter." + Colors.RESET);
            return false;
        }

        validateGuess(g);
        return true;
    }

    private void validateGuess(char g) {
        boolean found = false;

        for (int i = 0; i < wordLength; i++) {
            if (word.charAt(i) == g) {
                wordTracker.updateWord(g, i);
                found = true;
            }
        }

        if (!found) {
            attempts--;
            wrGs++;
            System.out.println(Colors.RED + "Wrong guess!" + Colors.RESET + theme.secondary() + " Attempts left: " + attempts + Colors.RESET);
            keyboard.addWrongGuess(g);
            Man.render(wrGs, maxAttempts);
        } else {
            System.out.println(Colors.GREEN + "Correct guess!" + Colors.RESET + theme.primary() + " Attempts left: " + attempts + Colors.RESET);
            keyboard.addRightGuess(g);
        }
        wordTracker.render();
        keyboard.renderKeyboard();
    }

    @Override
    public void gameLoop() {
        difficulty();
        pick();
        boolean playing = true;
        while (playing) {
            if (!handleInput()) {
                continue;
            }

            if (attempts <= 0) {
                System.out.println(Colors.RED + "Out of attempts!" + Colors.RESET + Colors.BLACK + " The word was: " + word + Colors.RESET);
                return;
            }

            if (wordTracker.isWordComplete()) {
                System.out.println(Colors.GREEN + "Congratulations! You've guessed the word: " + word + Colors.RESET);
                return;
            }
        }
    }

}
