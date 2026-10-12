package com.malar.jarcade.games.tictactoe;

import com.malar.jarcade.games.Game;
import com.malar.jarcade.ui.Colors;
import com.malar.jarcade.ui.InputProvider;
import com.malar.jarcade.ui.Theme;

public class TicTacToe implements Game {

    private Theme theme;
    Board board = new Board();
    private int moves = 0;

    private char player1;
    private char player2;
    private char currPlayer;

    public TicTacToe() {}

    @Override
    public String getName() {
        return theme.primary() + "TicTacToe" + Colors.RESET;
    }

    @Override
    public Theme applyTheme() {
        this.theme = new Theme(Colors.CYAN, Colors.MAGENTA);
        return this.theme;
    }

    @Override
    public void rules() {
        System.out.println(theme.secondary() +
                "Tic Tac Toe is a two-player game where players take turns marking spaces in a 3x3 grid. The player who succeeds in placing three of their marks in a horizontal, vertical, or diagonal row wins the game. If all spaces are filled and no player has three in a row, the game is a draw."
                + Colors.RESET);
    }

    private void choice() {
        while (true) {
            System.out.println("Choose your character (X/O): ");
            String input = InputProvider.readLine().toUpperCase();
            if (input.isEmpty()) continue;
            char c = input.charAt(0);
            if (c == 'X' || c == 'O') {
                player1 = c;
                break;
            }
            System.out.println(Colors.RED + "Invalid choice! Please choose X or O." + Colors.RESET);
        }

        player2 = (player1 == 'X') ? 'O' : 'X';

        System.out.println("You chose: " + player1);
        System.out.println("Opponent is: " + player2);

        currPlayer = player1;
    }

    public int row, col;
    private void handleInput() {
        while (true) {
            System.out.println("Enter move (e.g., A1, B2): ");
            String move = InputProvider.readLine().toUpperCase();
            if (move.length() != 2) {
                System.out.println(Colors.RED + "Invalid input format!" + Colors.RESET);
                continue;
            }
            col = move.charAt(0) - 'A';
            row = move.charAt(1) - '1';
            break;
        }
    }

    private boolean won(char x) {
        // sleeping line 
        for (int i = 0; i < 3; i++) {
            if (board.getCell(i, 0) == x &&
                    board.getCell(i, 1) == x &&
                    board.getCell(i, 2) == x)
                return true;
        }

        // standing line
        for (int j = 0; j < 3; j++) {
            if (board.getCell(0, j) == x &&
                    board.getCell(1, j) == x &&
                    board.getCell(2, j) == x)
                return true;
        }

        // primary diagonal 
        if (board.getCell(0, 0) == x &&
                board.getCell(1, 1) == x &&
                board.getCell(2, 2) == x)
            return true;

        // secondary diagonal
        if (board.getCell(0, 2) == x &&
                board.getCell(1, 1) == x &&
                board.getCell(2, 0) == x)
            return true;

        return false;
    }

    private void gameArea() {
        board.render(theme.primary(), theme.secondary());
    }

    @Override
    public void gameLoop() {
        choice();
        gameArea();
        boolean playing = true;
        while (playing) {
            handleInput();

            if (board.updateBoard(row, col, currPlayer)) {
                moves++;
                gameArea();
            } else {
                continue;
            }

            if (won(currPlayer)) {
                System.out.println(Colors.MAGENTA + "Player " + currPlayer + " wins!" + Colors.RESET);
                return;
            }

            if (moves == 9) {
                System.out.println(Colors.YELLOW + "It's a draw!" + Colors.RESET);
                return;
            }

            currPlayer = (currPlayer == player1) ? player2 : player1;
        }
    }
}
