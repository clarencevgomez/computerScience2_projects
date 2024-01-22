/* Clarence Gomez
   Dr. Steinberg
   COP3503 Fall 2023
   Programming Assignment 4
*/

// sequence alignment class
public class SequenceAlignment {
    private String wordX, wordY; // two sequencws
    private int[][] costSeq, optSeq; // cost and helper array
    private int n, m; // sequence lengths

    // overloaded constructor method
    public SequenceAlignment(String wordX, String wordY) {
        this.wordX = wordX;
        this.wordY = wordY;
        this.m = wordX.length(); // rows
        this.n = wordY.length(); // columns
        this.costSeq = new int[m + 1][n + 1]; // cost matrix
        this.optSeq = new int[m + 1][n + 1]; // helper matrix
    }

    // invoke the dynamic programming solution
    public void computeAlignment(int delta) {
        int i, j, cost;
        char c1, c2;
        initialize(delta);

        // dynamic programming
        for (j = 1; j <= n; j++) {
            for (i = 1; i <= m; i++) {

                c1 = wordX.charAt(i - 1);
                c2 = wordY.charAt(j - 1);

                if (c1 == c2) // Same symbol costs is 0
                    cost = 0;
                else {
                    if (isVowel(c1) && isVowel(c2))
                        cost = 1; // Vowel and different vowel will cost 1
                    else if (!isVowel(c1) && !isVowel(c2))
                        cost = 1; // Constant and different constant will cost 1
                    else
                        cost = 3; // Vowel and constant will cost 3
                } // inserting the minimum cost value to the cost matrix
                costSeq[i][j] = minimum(cost + costSeq[i - 1][j - 1], delta + costSeq[i - 1][j],
                        delta + costSeq[i][j - 1], i, j);
            }
        }
    }

    // initializes tables
    public void initialize(int delta) {
        int i, j;
        for (i = 1; i <= m; i++) {
            costSeq[i][0] = i * delta;
            optSeq[i][0] = 2;
        }
        for (j = 0; j <= n; j++) {
            costSeq[0][j] = j * delta;
            optSeq[0][j] = 3;
        }
    }

    // method that will return both alignments as one string
    public String getAlignment() { // using StringBuilder to build the sequences
        StringBuilder str1 = new StringBuilder();
        StringBuilder str2 = new StringBuilder();

        optAlign(str1, str2, m, n); // recursive method to build alignment
        return str1 + " " + str2;
    }

    // isVowel method
    private boolean isVowel(char letter) {
        return letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u';
    }

    // minimum number method
    public int minimum(int cost1, int cost2, int cost3, int x, int y) {
        int minNum = Math.min(Math.min(cost1, cost2), cost3);

        // fill helper matrix
        if (minNum == cost1)
            optSeq[x][y] = 1; // diagonal movement
        else if (minNum == cost2)
            optSeq[x][y] = 2; // vertical movement
        else if (minNum == cost3)
            optSeq[x][y] = 3; // horizontal movement

        return minNum;
    }

    // append the letter to each sequence
    public void sequenceBuilder(StringBuilder seq1, StringBuilder seq2, int i, int j, int offX, int offY) {
        optAlign(seq1, seq2, i, j);

        if (offX == 0 && offY == 0) {
            seq1.append(wordX.charAt(i));
            seq2.append(wordY.charAt(j));
        } else if (offX == 1) {
            seq1.append('-');
            seq2.append(wordY.charAt(j));
        } else if (offY == 1) {
            seq1.append(wordX.charAt(i));
            seq2.append('-');
        }
    }

    // optimal alignment builder method
    public void optAlign(StringBuilder seq1, StringBuilder seq2, int i, int j) {
        if (i <= 0 || j <= 0)
            return;

        switch (optSeq[i][j]) {
            case 1: // diagonal
                sequenceBuilder(seq1, seq2, i - 1, j - 1, 0, 0);
                break;
            case 2: // vertical
                sequenceBuilder(seq1, seq2, i - 1, j, 0, 1); // inserting gap in y sequence
                break;
            case 3:// horizontal
                sequenceBuilder(seq1, seq2, i, j - 1, 1, 0); // inserting gap in x sequence
                break;
        }
    }

    // toString method
    @Override
    public String toString() {
        return wordX + ' ' + wordY;
    }
}