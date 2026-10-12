package com.malar.jarcade.games.wordle;

import java.util.HashMap;
import java.util.Map;

import com.malar.jarcade.ui.Colors;

public class Blanks {
    private final char[] blanks = new char[5];

    public Blanks() {
        for (int i = 0; i < 5; i++) {
            blanks[i] = '_';
        }
    }

    public void renderEmpty() {
        System.out.println("_ _ _ _ _");
    }

    public boolean updateBlanks(String guess, String answer, int attempt, int maxAttempts) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : answer.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        char[] result = new char[5];
        for (int i = 0; i < 5; i++) {
            result[i] = ' ';
        }

        for (int i = 0; i < 5; i++) {
            char g = guess.charAt(i);
            if (g == answer.charAt(i)) {
                freq.put(g, freq.getOrDefault(g, 0) - 1);
                result[i] = 'G';
            }
        }

        for (int i = 0; i < 5; i++) {
            char g = guess.charAt(i);
            if (result[i] == 'G') {
                continue;
            }

            if (freq.getOrDefault(g, 0) > 0) {
                freq.put(g, freq.getOrDefault(g, 0) - 1);
                result[i] = 'Y';
            } else {
                result[i] = 'B';
            }
        }

        StringBuilder feedback = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            char g = guess.charAt(i);
            if (result[i] == 'G') {
                feedback.append('[').append(Colors.GREEN).append(g).append(Colors.RESET).append(']').append(" ");
            } else if (result[i] == 'Y') {
                feedback.append('[').append(Colors.YELLOW).append(g).append(Colors.RESET).append(']').append(" ");
            } else {
                feedback.append('[').append(Colors.GRAY).append(g).append(Colors.RESET).append(']').append(" ");
            }
        }

        System.out.println(feedback.toString());
        System.out.println("Attempts left: " + (maxAttempts - attempt - 1));
        return true;
    }
    
}
