package com.malar.jarcade.games.hangman;

public class WordTracker {
    private final StringBuilder emptyTrack = new StringBuilder();
    private int wordLength;

    public boolean isWordComplete() {
        return emptyTrack.indexOf("[ ]") == -1;
    }

    public void init(int wordLength) {
        this.wordLength = wordLength;
        emptyTrack.setLength(0);
        for (int i = 0; i < wordLength; i++) {
            emptyTrack.append("[ ] ");
        }
    }

    public void updateWord(char g, int index) {
        int charIndex = (index * 4) + 1;
        if (charIndex >= 0 && charIndex < emptyTrack.length()) {
            emptyTrack.setCharAt(charIndex, g);
        }
    }

    public void render() {
        System.out.println(emptyTrack.toString());
    }
}
