package data;

import java.util.ArrayList;

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
}
