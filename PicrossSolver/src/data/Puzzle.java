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
   */
  public Puzzle(ArrayList<ArrayList<Integer>> colNums, ArrayList<ArrayList<Integer>> rowNums) {
    this.colNums = colNums;
    this.rowNums = rowNums;
    cols = colNums.size();
    rows = rowNums.size();
    board = new int[cols * rows];
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
  
}
