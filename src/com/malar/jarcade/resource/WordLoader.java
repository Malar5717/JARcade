package com.malar.jarcade.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class WordLoader {

    public static List<String> loadWords(String resourcePath, int wordLengthMin, int wordLengthMax) {
        if (wordLengthMin < 1 || wordLengthMax < 1 || wordLengthMin > wordLengthMax) {
            throw new IllegalArgumentException("Invalid word length range: " + wordLengthMin + " to " + wordLengthMax);
        }

        InputStream in = WordLoader.class.getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IllegalStateException("Resource not found: " + resourcePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return new ArrayList<>(reader.lines()
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .filter(w -> w.length() >= wordLengthMin && w.length() <= wordLengthMax)
                    .toList());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read resource: " + resourcePath, e);
        }
    }
}