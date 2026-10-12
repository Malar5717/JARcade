package com.malar.jarcade.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

// all parts of JARcade use one shared Scanner through clean methods, instead of every game creating and managing its own Scanner

public final class InputProvider {
    private static final Scanner SCANNER = new Scanner(System.in);

    private InputProvider() {}

    public static String readLine() {
        return SCANNER.nextLine();
    }

    public static int readInt() {
        try {
            int value = SCANNER.nextInt();
            SCANNER.nextLine();
            return value;
        } catch (InputMismatchException e) {
            // nextInt() does not consume the offending token, so it would be
            // read again on the next call. Discard the rest of the line before
            // rethrowing so the caller's recover-and-continue loop can progress.
            if (SCANNER.hasNextLine()) {
                SCANNER.nextLine();
            }
            throw e;
        }
    }
}
