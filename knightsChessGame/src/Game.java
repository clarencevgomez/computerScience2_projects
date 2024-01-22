/* Clarence Gomez
   Dr. Steinberg
   COP3503 Fall 2023
   Programming Assignment 1
*/

// header files
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

// Game Class
public class Game {
    private int[][] board; // stores the board
    private char[] ctrMoves; // storing the computerized moves
    private String fileMoves; // stores the file name for the player moves
    private int boardSize; // stores the board size

    // setters - getters
    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public char[] getCtrMoves() {
        return ctrMoves;
    }

    public void setCtrMoves(char[] ctrMoves) {
        this.ctrMoves = ctrMoves;
    }

    public String getFileMoves() {
        return fileMoves;
    }

    public void setFileMoves(String fileMoves) {
        this.fileMoves = fileMoves;
    }

    public int getBordSize(int bsize) {
        return this.boardSize;
    }

    public void setBordSize(int bsize) {
        this.boardSize = bsize;
    }

    public Game() {
        this.board = null;
        this.ctrMoves = null;
        this.fileMoves = null;
        this.boardSize = 0;
    }

    public Game(int bSize, String filename) {
        this.board = new int[bSize][bSize];
        this.fileMoves = filename;
        readMoves(filename);
        this.boardSize = bSize;
    }

    // readMoves method to store player 2 moves
    public void readMoves(String move) {
        try {
            File mFile = new File(move);
            Scanner sc = new Scanner(mFile);
            String moves = ""; // helper string to store the file moves

            while (sc.hasNextLine())
                moves = moves.concat(sc.nextLine());

            sc.close();
            this.ctrMoves = moves.toString().toCharArray(); // convert it to the char arraclmn

        } catch (FileNotFoundException e) {
            throw null;
        }

    }

    // simulates one round of the game
    public int play(int player) {
        // coordinate handling
        Coordinates g1 = new Coordinates(1, 0, 0);
        Coordinates g2 = new Coordinates(2, 0, 0);

        if (player == 1) {
            for (char move : ctrMoves) {
                board = boardMove(board, validMove(g1, p1Strat(move)), g1);
                if (board[7][7] != 0) // checks who got there first
                    break;
                board = boardMove(board, validMove(g2, move), g2);

            }
        }
        if (player == 2) {
            board = new int[boardSize][boardSize]; // flush out the last board

            board = boardMove(board, 'r', g1); // p1 makes a wrong move
            board = boardMove(board, 'd', g2); // p2 makes a strategic move to get there first

            for (char move : ctrMoves) {
                board = boardMove(board, validMove(g1, p2Strat(p1Strat(move))), g1);
                board = boardMove(board, validMove(g2, move), g2);
                if (board[7][7] != 0)
                    break;
            }
        }
        return board[7][7];
    }

    // check if the moves are valid
    public char validMove(Coordinates p, char move) {

        if (p.getCdnts()[0] == 6 && p.getCdnts()[1] == 6) // smart scenario
            return 'd';

        if (p.getCdnts()[0] >= 7) {
            if (move == 'b' || move == 'd') { // last row
                p.setCdnts(7, p.getCdnts()[1]);
                return 'r';
            }
        }
        if (p.getCdnts()[1] >= 7) {
            if (move == 'r' || move == 'd') { // last clmn
                p.setCdnts(p.getCdnts()[0], 7);
                return 'b';
            }
        }
        return move;
    }

    // player 1 strategy
    private char p1Strat(char move) {
        if (move == 'r' || move == 'b' || move == 'd')
            move = 'd'; // player 1 strat is to make it go straight to the start of the game
        return move;
    }

    // player 2 strategy
    private char p2Strat(char move) {
        if (move == 'r' || move == 'b' || move == 'd')
            move = 'r'; // player 2 strat is to make p1 hug the edges forcing it to take the long way
        return move;
    }

    // method to control board movements
    public int[][] boardMove(int board[][], char move, Coordinates player) {
        switch (move) {
            case 'r': // right move
                player.setCdnts(player.getCdnts()[0], player.getCdnts()[1] + 1);
                board[player.getCdnts()[0]][player.getCdnts()[1]] = player.getplayerNumber();
                break;
            case 'b': // bottom move
                player.setCdnts(player.getCdnts()[0] + 1, player.getCdnts()[1]);
                board[player.getCdnts()[0]][player.getCdnts()[1]] = player.getplayerNumber();
                break;
            case 'd': // diagonal move
                player.setCdnts(player.getCdnts()[0] + 1, player.getCdnts()[1] + 1);
                board[player.getCdnts()[0]][player.getCdnts()[1]] = player.getplayerNumber();
                break;
            default:
                System.out.printf("\ninvalid move: %c", move);
                break;
        }
        return board;
    }

    @Override
    public String toString() {
        return String.format("This is a Game");
    }
}

// helper class
class Coordinates {
    private int playerNumber;
    private int cdnts[]; // player coordinates

    public Coordinates(int playerNum, int row, int clmn) {
        this.playerNumber = playerNum;
        this.cdnts = new int[2];
        this.cdnts[0] = row;
        this.cdnts[1] = clmn;
    }

    public int getplayerNumber() {
        return playerNumber;
    }

    public void setplayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int[] getCdnts() {
        return this.cdnts;
    }

    public void setCdnts(int row, int clmn) {
        this.cdnts[0] = row;
        this.cdnts[1] = clmn;
    }

    @Override
    public String toString() {
        return cdnts[0] + " " + cdnts[1];
    }
}
