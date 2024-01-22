/* Clarence Gomez
 * Dr. Steinberg
 * COP3503 Fall 2023
 * Programming Assignment 2
 */

import java.io.IOException;
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;

public class MagicMaze {
    private char[][] maze; // 2d array to store maze
    private String mazeNum; // maze number
    private int mRows; // maze rows/cols numbers
    private int mCols;

    // hashmaps to store the magic square locations
    private HashMap<Character, MagicSquare> magicSquares;
    private HashMap<Character, MagicSquare> magicSquaresDest;

    // all the valid moves Kenny can make
    public static final int[] moveX = { -1, 1, 0, 0 };
    public static final int[] moveY = { 0, 0, -1, 1 };

    // maze Constructor to initialize the MagicMaze
    public MagicMaze(String filename, int rows, int cols) {
        this.mazeNum = filename;
        this.mRows = rows;
        this.mCols = cols;
        this.maze = new char[rows][cols];
        this.magicSquares = new HashMap<>();
        this.magicSquaresDest = new HashMap<>();
        MazeReader(mazeNum);
    }

    // Method to read the maze from a text file
    public void MazeReader(String mazeNumber) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(mazeNum));

            for (int i = 0; i < mRows; ++i) {
                String line = reader.readLine();
                for (int j = 0; j < mCols; ++j) {
                    maze[i][j] = line.charAt(j);

                    if (Character.isDigit(maze[i][j])) { // store the coordinates in source and destination hashmaps
                        if (!(magicSquaresDest.containsKey(line.charAt(j)))) {
                            magicSquaresDest.put(maze[i][j], new MagicSquare(i, j));
                        } else {
                            magicSquares.put(maze[i][j], new MagicSquare(i, j));
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            throw null;
        }
    }

    // method to compute the magic square jump within the maze
    private int[] magicSquareJump(char digit, int row, int col, char[][] maze, int visited[][]) {

        int[] newLoc = { row, col };
        MagicSquare newLocation = new MagicSquare(row, col);

        // if the original coordinates match any of the numbers then we jump
        if (magicSquares.containsKey(digit) && newLocation.getRow() == row && newLocation.getCol() == col) {
            newLocation = magicSquaresDest.get(digit);
            newLoc[0] = newLocation.getRow();
            newLoc[1] = newLocation.getCol();
        }
        if (magicSquaresDest.containsKey(digit) && newLocation.getRow() == row && newLocation.getCol() == col) {
            newLocation = magicSquares.get(digit);
            newLoc[0] = newLocation.getRow();
            newLoc[1] = newLocation.getCol();
        }
        return newLoc;
    }

    // driver function
    public boolean solveMagicMaze() {

        int[][] visited = new int[mRows][mCols]; // tracking the visited blocks
        int entryRow = mRows - 1;
        int entryCol = 0; // enter from bottom left

        return solveMagicMazeR(entryRow, entryCol, maze, visited);
    }

    // check if the position is valid
    private boolean positionOk(int row, int col, char[][] maze, int[][] visited) {
        return row >= 0 && row < mRows && col >= 0 && col < mCols && maze[row][col] != '@' && visited[row][col] != 1;
    }

    public boolean solveMagicMazeR(int row, int col, char[][] maze, int[][] visited) {

        if (maze[row][col] == 'X') { // base case
            visited[row][col] = 1;
            return true;
        }

        visited[row][col] = 1;

        // check if kenny stepped on a magic square
        if (Character.isDigit(maze[row][col])) {
            int[] c = magicSquareJump(maze[row][col], row, col, maze, visited);
            row = c[0];
            col = c[1];
        }

        // check all steps
        for (int i = 0; i < 4; ++i) {

            int newX = row + moveX[i];
            int newY = col + moveY[i];

            if (positionOk(newX, newY, maze, visited)) {
                if (solveMagicMazeR(newX, newY, maze, visited))
                    return true;
            }
        }
        // backtrack
        visited[row][col] = 0;
        return false;
    }
}

// helper class
class MagicSquare {
    private int row, col;

    public MagicSquare(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return " " + row + " " + col;
    }
}