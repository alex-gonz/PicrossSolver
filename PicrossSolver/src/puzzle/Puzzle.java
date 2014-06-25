package puzzle;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class to hold a representation of the puzzle and current board state
 * @author coatrain
 *
 */
public class Puzzle {
  //These represent the series of numbers on the edge of the board denoting the numbers of filled in squares
  public final ArrayList<ArrayList<Integer>> colNums;
  public final ArrayList<ArrayList<Integer>> rowNums;

  public final int rows;
  public final int cols;

  /**
   * Board that the user guesses on. Row determined by pos/cols. Column determined by pos%cols. 
   * Integers represent different states:
   * 0 is an unsure / default blank square
   * 1 is a guessed filled in square
   * 2 is a guessed crossed out / guessed blank square
   */
  private int[] board;

  /**
   * Initialize the puzzle with given column and row numbers, and a blank board 
   * @param colNums
   * @param rowNums
   * @throws IllegalStateException throws exception if given puzzle is determined unsolvable
   */
  public Puzzle(ArrayList<ArrayList<Integer>> colNums, ArrayList<ArrayList<Integer>> rowNums) throws IllegalStateException{
    this.colNums = colNums;
    this.rowNums = rowNums;
    cols = colNums.size();
    rows = rowNums.size();
    board = new int[cols * rows];
    if(!isValidPuzzle())
      throw new IllegalStateException("Given Board Cannot be solved!");
  }

  /**
   * Interprets a puzzle given a file name and path
   * @param file the given file name and path
   * @return a puzzle matching description in the file
   */
  public static Puzzle interpretPuzzle(String file) {
    //TODO
    return null;
  }

  /**
   * Gets the entire board
   * @return the state of the board
   */
  public int[] getBoard() {
    return board;
  }

  /**
   * Sets the current board state to given board state
   * @param board given board state
   */
  public void setBoard(int[] board) {
    this.board = board;
  }

  /**
   * Sets value at given position on board
   * @param val value to set in board
   * @param pos position in board array to be set
   * @throws ArrayIndexOutOfBoundsException when given a position larger than board dimensions
   */
  public void setBoard(int val, int pos) throws ArrayIndexOutOfBoundsException {
    if(pos >= rows * cols)
      throw new ArrayIndexOutOfBoundsException("Position past end of board");
    board[pos]= val;
  }

  /**
   * Determines if a board could have solutions based on its dimensions, colNums and rowNums
   * @return false if the board is determined impossible to solve, otherwise true
   */
  public boolean isValidPuzzle() {
    boolean ret = true;

    ret = ret && sameNumSquares();
    ret = ret && squaresFit();

    return ret;
  }

  /**
   * Returns true if all squares of the columns and rows could fit in the board
   * @return true if each column and row could contain specified squares
   */
  private boolean squaresFit() {
    for(ArrayList<Integer> list: colNums) {
      int lineTotal = 0;
      for(Integer i: list) {
        //Add i+1 to account for extra space between numbers
        lineTotal += i+1;
      }
      //Remove extra space
      lineTotal--;
      //Compare to spaces in column
      if(lineTotal > cols)
        return false;
    }

    for(ArrayList<Integer> list: rowNums) {
      int lineTotal = 0;
      for(Integer i: list) {
        //Add i+1 to account for extra space between numbers
        lineTotal += i+1;
      }
      //Remove extra space
      lineTotal--;
      //Compare to spaces in row
      if(lineTotal > rows)
        return false;
    }
    return true;
  }

  /**
   * Checks if there are the same number of required squares in columns as in rows.
   * If false, puzzle cannot be solved.
   * @return True if number of required squares are same for both columns and rows.
   */
  private boolean sameNumSquares() {
    int total = 0;
    for(ArrayList<Integer> a: colNums) {
      for(Integer i: a) {
        total += i;
      }
    }
    for(ArrayList<Integer> a: rowNums) {
      for(Integer i: a) {
        total -= i;
      }
    }
    return total == 0;
  }

  /**
   * Returns false if there must be an incorrect mark on the board
   * @return false if there is an incorrect mark on the board, otherwise true
   */
  public boolean isSolvable() {
    boolean ret = true;
    ret = ret && matchesFull();
    return ret;
  }

  /**
   * Determine if fully filled rows and columns of board match the numbers on edges 
   * @return true if all full rows and columns match, false otherwise
   */
  private boolean matchesFull() {
    // TODO Test
    for(int i = 0; i< cols; i++) {
      ArrayList<Integer> currentRow = new ArrayList<Integer>();
      int continuousFilled = 0;
      boolean skip = false;
      //Iterate through row i of board
      for(int j = i*cols; j < (i+1)*cols; j++) {
        //Break if row isn't full of guessed values
        if(board[j] == 0) {
          skip = true; //Set so that we know we broke the loop
          break;
        }

        //Add to list if next is blank and last square was not blank
        if(board[j] == 2 && continuousFilled != 0) {
          currentRow.add(continuousFilled);
          continuousFilled = 0;
          //Add to the number of square in a row if square is filled in
        } else if (board[j] == 1) {
          continuousFilled++;
        }
      }
      //Skip line if skip flag is true
      if(skip)
        continue;
      //Add the last continuous patch of squares
      currentRow.add(continuousFilled);
      //Check if row matches what we counted
      if(!currentRow.equals(rowNums.get(i)))
        return false;
    }
    
    //TODO do same for columns
    for(int k = 0; k< rows; k++) {
      ArrayList<Integer> currentCol = new ArrayList<Integer>();
      int continuousFilled = 0;
      boolean skip = false;
      //Iterate through column k of board
      for(int l = 0; l < rows; l++) {
        int pos = k+l*cols;
        if(board[pos] == 0) {
          skip = true;
          break;
        }

        if(board[pos] == 2 && continuousFilled != 0) {
          currentCol.add(continuousFilled);
          continuousFilled = 0;
        } else if (board[pos] == 1) {
          continuousFilled++;
        }
      }
      if(skip)
        continue;
      currentCol.add(continuousFilled);
      if(!currentCol.equals(colNums.get(k)))
        return false;
    }
    return true;
  }

  /**
   * Return the first uncertain square
   * @return the first uncertain square, or a negative if all full
   */
  public int getFirstBlankPos() {
    // TODO Test
    for(int pos = 0; pos < board.length; pos++) {
      if(board[pos] == 0)
        return pos;
    }
    return -1;
  }

  /**
   * Returns a new copy of this puzzle
   * @return a copy of this puzzle
   */
  public Puzzle createCopy() {
    // TODO Test
    Puzzle p = new Puzzle(colNums, rowNums);
    p.board = Arrays.copyOf(board, board.length);
    return p;
  }
}
