package com.malar.jarcade.games.tictactoe;

import com.malar.jarcade.ui.Colors;

public class Board {
    private char[][] grid = new char[3][3];

    public Board(){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                grid[i][j] = ' ';
            }
        }
    }

    public char getCell(int r, int c) {
        return grid[r][c];
    }

    public void setCell(int r, int c, char val) {
        grid[r][c] = val;
    }

    public void render(String xColor, String oColor) {
        System.out.println("   A   B   C");

        for (int i = 0; i < 3; i++) {
            System.out.print((i + 1) + " ");

            for (int j = 0; j < 3; j++) {
                char val = getCell(i, j);
                if (val == 'X') {
                    System.out.print(xColor + val + Colors.RESET + " ");
                } else if (val == 'O') {
                    System.out.print(oColor + val + Colors.RESET + " ");
                } else {
                    System.out.print(". ");
                }

                if (j < 2) System.out.print(Colors.BLACK + " | " + Colors.RESET);
            }

            System.out.println();

            if (i < 2) System.out.println(Colors.BLACK + "  ---|----|---" + Colors.RESET);
        }
    }

    public boolean updateBoard(int row, int col, char currPlayer) {
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            System.out.println(Colors.RED + "Invalid position!" + Colors.RESET);
            return false;
        }
        if (this.getCell(row, col) != ' ') {
            System.out.println(Colors.RED + "Cell already taken!" + Colors.RESET);
            return false;
        }
        this.setCell(row, col, currPlayer);
        return true;
    }
}
