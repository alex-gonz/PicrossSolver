package puzzle;

import java.util.ArrayList;
import java.util.Arrays;

import solver.PuzzleVerifier;

/**
 * Class to hold a representation of the puzzle and current board state
 * 
 * @author coatrain
 * 
 */
public class Puzzle {
  // These represent the series of numbers on the edge of the board denoting the numbers of filled
  // in squares
  public final ArrayList<ArrayList<Integer>> colNums;
  public final ArrayList<ArrayList<Integer>> rowNums;

  public final int rows;
  public final int cols;

  /**
   * Board that the user guesses on. Row determined by pos/cols. Column determined by pos%cols.
   * Integers represent different states: 0 is an unsure / default blank square 1 is a guessed
   * filled in square 2 is a guessed crossed out / guessed blank square
   */
  private int[] board;

  /**
   * Initialize the puzzle with given column and row numbers, and a blank board
   * 
   * @param colNums
   * @param rowNums
   * @throws IllegalStateException throws exception if given puzzle is determined unsolvable
   */
  public Puzzle(ArrayList<ArrayList<Integer>> colNums, ArrayList<ArrayList<Integer>> rowNums)
      throws IllegalStateException {
    this.colNums = colNums;
    this.rowNums = rowNums;
    cols = colNums.size();
    rows = rowNums.size();
    board = new int[cols * rows];
    if (!PuzzleVerifier.isValidPuzzle(this))
      throw new IllegalStateException("Given Board Cannot be solved!");
  }

  /**
   * Gets the entire board
   * 
   * @return the state of the board
   */
  public int[] getBoard() {
    return board;
  }

  /**
   * Sets the current board state to given board state
   * 
   * @param board given board state
   */
  public void setBoard(int[] board) {
    this.board = board;
  }

  /**
   * Sets value at given position on board
   * 
   * @param val value to set in board
   * @param pos position in board array to be set
   * @throws ArrayIndexOutOfBoundsException when given a position larger than board dimensions
   */
  public void setBoard(int val, int pos) throws ArrayIndexOutOfBoundsException {
    if (pos >= rows * cols)
      throw new ArrayIndexOutOfBoundsException("Position past end of board");
    board[pos] = val;
  }



  /**
   * Return the first uncertain square
   * 
   * @return the first uncertain square, or a negative if all full
   */
  public int getFirstBlankPos() {
    for (int pos = 0; pos < board.length; pos++) {
      if (board[pos] == 0)
        return pos;
    }
    return -1;
  }

  /**
   * Returns a new copy of this puzzle. colNums and rowNums will still reference same arraylist, but
   * board will reference a second copy of the board. Allows for manipulating puzzle differently for
   * each copy.
   * 
   * @return a copy of this puzzle
   */
  public Puzzle createCopy() {
    Puzzle p = new Puzzle(colNums, rowNums);
    p.board = Arrays.copyOf(board, board.length);
    return p;
  }

  /**
   * Generates an empty puzzle with same constraints as a given board
   * 
   * @param board given board
   * @param cols number of columns in the puzzle
   * @param rows number of rows in the puzzle
   * @return a puzzle with blank board matching given board, cols and rows. null if cols and rows
   *         don't match board size
   */
  public static Puzzle boardToPuzzle(int[] board, int cols, int rows) {
    if (cols * rows != board.length)
      return null;
    ArrayList<ArrayList<Integer>> colNums = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> rowNums = new ArrayList<ArrayList<Integer>>();

    // Generate rowNums
    for (int row = 0; row < rows; row++) {
      ArrayList<Integer> currentRow = new ArrayList<Integer>();
      int continuousLine = 0;
      for (int col = 0; col < cols; col++) {
        int pos = col + row * cols;
        // If we are ending a line, add it to list
        if (board[pos] != 1 && continuousLine > 0) {
          currentRow.add(continuousLine);
          continuousLine = 0;
        } else if (board[pos] == 1) {
          // Add to current line if square is filled in
          continuousLine++;
        }
      }
      // Add last line if there is one
      if (continuousLine > 0)
        currentRow.add(continuousLine);
      // Add row to row list
      rowNums.add(currentRow);
    }

    // Generate colNums
    // Iterate down column instead of across row
    for (int col = 0; col < cols; col++) {
      ArrayList<Integer> currentCol = new ArrayList<Integer>();
      int continuousLine = 0;
      for (int row = 0; row < rows; row++) {
        int pos = col + row * cols;
        if (board[pos] != 1 && continuousLine > 0) {
          currentCol.add(continuousLine);
          continuousLine = 0;
        } else if (board[pos] == 1) {
          continuousLine++;
        }
      }
      if (continuousLine > 0)
        currentCol.add(continuousLine);
      colNums.add(currentCol);
    }
    return new Puzzle(colNums, rowNums);
  }

  @Override
  /**
   * Generated hashCode method
   */
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(board);
    result = prime * result + ((colNums == null) ? 0 : colNums.hashCode());
    result = prime * result + cols;
    result = prime * result + ((rowNums == null) ? 0 : rowNums.hashCode());
    result = prime * result + rows;
    return result;
  }

  @Override
  /**
   * Generated equals method
   */
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Puzzle other = (Puzzle) obj;
    if (!Arrays.equals(board, other.board))
      return false;
    if (colNums == null) {
      if (other.colNums != null)
        return false;
    } else if (!colNums.equals(other.colNums))
      return false;
    if (cols != other.cols)
      return false;
    if (rowNums == null) {
      if (other.rowNums != null)
        return false;
    } else if (!rowNums.equals(other.rowNums))
      return false;
    if (rows != other.rows)
      return false;
    return true;
  }
}
