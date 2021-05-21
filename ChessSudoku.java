package finalproject;

import  java.util.*;
import java.io.*;
import java.util.HashSet;


public class ChessSudoku
{
    /* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For
     * a standard Sudoku puzzle, SIZE is 3 and N is 9.
     */
    public int SIZE, N;

    /* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
     * not yet been revealed are stored as 0.
     */
    public int grid[][];

    /* Booleans indicating whether of not one or more of the chess rules should be
     * applied to this Sudoku.
     */
    public boolean knightRule;
    public boolean kingRule;
    public boolean queenRule;


    // Field that stores the same Sudoku puzzle solved in all possible ways
    public HashSet<ChessSudoku> solutions = new HashSet<ChessSudoku>();


    /* The solve() method should remove all the unknown characters ('x') in the grid
     * and replace them with the numbers in the correct range that satisfy the constraints
     * of the Sudoku puzzle. If true is provided as input, the method should find finds ALL
     * possible solutions and store them in the field named solutions. */
    public void solve(boolean allSolutions) {

        SudokuSolve(0,0);
        print();
    }

    private boolean SudokuSolve(int row, int column){

        if (row==N-1 && column==N) { return true; } // if we are at the end, then it is solved

        if (column==N) { // we will iterate through the puzzle by column
            row++;
            column=0;
        }

        if (grid[row][column] > 0) {return SudokuSolve(row, column + 1);}

        for (int i=1; i<=N; i++ ) { // the possible we wish to print

            if (is_we_good(column,row,i) ) {

                grid[row][column]=i;

                if (SudokuSolve(row,column+1)) {return true;} // recall the method using the next column
            }
            grid[row][column]=0;
        }
        return false;
    }

    private boolean row_rule( int row, int number) {

        for (int i=0; i < N; i++) {
            if (grid[row][i] == number) {return true;} // checking for a row

        }
        return false;
    }

    private boolean column_rule(int column, int number) {

        for (int i = 0; i < N; i++) {
            if (grid[i][column] == number) { return true; } // checking for a column
        }
        return false;
    }


    private boolean box_rule(int column, int row, int number ) {

        int box_row = row - row % SIZE; // find the start of the box for both column and row
        int box_column = column - column % SIZE;

        for (int i=box_row; i < box_row + SIZE; i++) {
            for (int j=box_column; j<box_column +SIZE;j++){
                if (grid[i][j]==number) {return true;}
            }
        }
        return false;
    }

    private boolean KingRule (int column, int row, int number) {

        if ((column + 1 <=  N-1 ) && ( row + 1 <= N-1 ) && grid[row+1][column+1]==number) { return true;} // top right
        if ((column - 1 >= 0 ) && ( row + 1 <= N-1 ) && grid[row+1][column-1]==number ) {return true;} //bottom right
        if ((column -1 >= 0) && (row -1 >= 0) && grid[row-1][column-1]==number ) {return true;} //bottom left
        if ((column + 1 <= N-1 ) && (row - 1 >= 0) && grid[row-1][column+1]==number ) {return true;} //  top left

        return false;

    }

    private boolean QueenRule(int column, int row, int number) {

        if (number!=N){return false;}

        int temp_column=column;
        int temp_row=row;

        while (temp_column<N-1 && temp_row<N-1) { // top right
            temp_column++;
            temp_row++;
            if (grid[temp_row][temp_column]==N){ return true; }
        }
        temp_column=column;
        temp_row=row;

        while (temp_column>0 && temp_row<N-1) {
            temp_column--;
            temp_row++;
            if (grid[temp_row][temp_column]==N){ return true; }
        }
        temp_column=column;
        temp_row=row;
        while (temp_column>0 && temp_row>0) {
            temp_column--;
            temp_row--;
            if (grid[temp_row][temp_column]==N){ return true; }
        }
        temp_column=column;
        temp_row=row;
        while (temp_column<N-1 && temp_row>0) {
            temp_column++;
            temp_row--;
            if (grid[temp_row][temp_column]==N){ return true; }
        }
        return false;
    }

    private boolean KnightRule(int column, int row, int number) {

        int[] xOffsets = {-2, -1, 1, 2, 2, 1, -1, -2};
        int[] yOffsets = {1, 2, 2, 1, -1, -2, -2, -1};

        for(int i = 0; i < 8; i++) {
            if ( (row+xOffsets[i] >= 0) && (row+xOffsets[i] <=N-1) &&  (column+yOffsets[i] >= 0) && (column + yOffsets[i] <= N-1) ) {
                if (grid[row + xOffsets[i]][column + yOffsets[i]]==number) {return true;}
            }
        }
        return false;
    }

    private boolean is_we_good (int column, int row, int number) {

        if (kingRule) {
            if (KingRule(column,row,number)) {return false;} // if king rules return true, we not good
        }
        if (queenRule) {
            if (QueenRule(column,row,number)) {return false;} // if the queen rules return true, we not good
        }
        if (knightRule) {
            if (KnightRule(column,row,number)){return false;}
        }
        return (!(column_rule(column,number) || row_rule(row, number) || box_rule(column,row,number))); // if none of these return true, we is not good
        // thus return true

    }

    private boolean is_solved(){
        for (int i=0; i<this.N; i++){ // check to see
            for (int j=0; j<this.N; j++){
                if (grid[i][j]==0) {return false;}
            }
        }
        return true;
    }

    




    /*****************************************************************************/
    /* NOTE: YOU SHOULD NOT HAVE TO MODIFY ANY OF THE METHODS BELOW THIS LINE. */
    /*****************************************************************************/

    /* Default constructor.  This will initialize all positions to the default 0
     * value.  Use the read() function to load the Sudoku puzzle from a file or
     * the standard input. */
    public ChessSudoku( int size ) {
        SIZE = size;
        N = size*size;

        grid = new int[N][N];
        for( int i = 0; i < N; i++ )
            for( int j = 0; j < N; j++ )
                grid[i][j] = 0;
    }


    /* readInteger is a helper function for the reading of the input file.  It reads
     * words until it finds one that represents an integer. For convenience, it will also
     * recognize the string "x" as equivalent to "0". */
    static int readInteger( InputStream in ) throws Exception {
        int result = 0;
        boolean success = false;

        while( !success ) {
            String word = readWord( in );

            try {
                result = Integer.parseInt( word );
                success = true;
            } catch( Exception e ) {
                // Convert 'x' words into 0's
                if( word.compareTo("x") == 0 ) {
                    result = 0;
                    success = true;
                }
                // Ignore all other words that are not integers
            }
        }

        return result;
    }


    /* readWord is a helper function that reads a word separated by white space. */
    static String readWord( InputStream in ) throws Exception {
        StringBuffer result = new StringBuffer();
        int currentChar = in.read();
        String whiteSpace = " \t\r\n";
        // Ignore any leading white space
        while( whiteSpace.indexOf(currentChar) > -1 ) {
            currentChar = in.read();
        }

        // Read all characters until you reach white space
        while( whiteSpace.indexOf(currentChar) == -1 ) {
            result.append( (char) currentChar );
            currentChar = in.read();
        }
        return result.toString();
    }


    /* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
     * grid is filled in one row at at time, from left to right.  All non-valid
     * characters are ignored by this function and may be used in the Sudoku file
     * to increase its legibility. */
    public void read( InputStream in ) throws Exception {
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                grid[i][j] = readInteger( in );
            }
        }
    }


    /* Helper function for the printing of Sudoku puzzle.  This function will print
     * out text, preceded by enough ' ' characters to make sure that the printint out
     * takes at least width characters.  */
    void printFixedWidth( String text, int width ) {
        for( int i = 0; i < width - text.length(); i++ )
            System.out.print( " " );
        System.out.print( text );
    }


    /* The print() function outputs the Sudoku grid to the standard output, using
     * a bit of extra formatting to make the result clearly readable. */
    public void print() {
        // Compute the number of digits necessary to print out each number in the Sudoku puzzle
        int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

        // Create a dashed line to separate the boxes
        int lineLength = (digits + 1) * N + 2 * SIZE - 3;
        StringBuffer line = new StringBuffer();
        for( int lineInit = 0; lineInit < lineLength; lineInit++ )
            line.append('-');

        // Go through the grid, printing out its values separated by spaces
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                printFixedWidth( String.valueOf( grid[i][j] ), digits );
                // Print the vertical lines between boxes
                if( (j < N-1) && ((j+1) % SIZE == 0) )
                    System.out.print( " |" );
                System.out.print( " " );
            }
            System.out.println();

            // Print the horizontal line between boxes
            if( (i < N-1) && ((i+1) % SIZE == 0) )
                System.out.println( line.toString() );
        }
    }


    /* The main function reads in a Sudoku puzzle from the standard input,
     * unless a file name is provided as a run-time argument, in which case the
     * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
     * outputs the completed puzzle to the standard output. */
    public static void main( String args[] ) throws Exception {
        InputStream in = new FileInputStream("queenSudokuEasy3x3.txt");

        // The first number in all Sudoku files must represent the size of the puzzle.  See
        // the example files for the file format.
        int puzzleSize = readInteger( in );
        if( puzzleSize > 100 || puzzleSize < 1 ) {
            System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
            System.exit(-1);
        }

        ChessSudoku s = new ChessSudoku( puzzleSize );

        // You can modify these to add rules to your sudoku
        s.knightRule = false;
        s.kingRule = false;
        s.queenRule = true;

        // read the rest of the Sudoku puzzle
        s.read( in );

        System.out.println("Before the solve:");
        s.print();
        System.out.println();

        // Solve the puzzle by finding one solution.
        s.solve(false);

        // Print out the (hopefully completed!) puzzle
        System.out.println("After the solve:");
        s.print();
    }
}