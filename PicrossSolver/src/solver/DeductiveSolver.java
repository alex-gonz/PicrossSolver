package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import puzzle.Puzzle;

/**
 * Picross solver that uses only deduction to try to solve
 * 
 * @author rippe_000
 * 
 */
public class DeductiveSolver {

  /**
   * Solves using other deductive methods. Assumes any guessed squares (filled in or blank) are
   * correct.
   * 
   * @param p given puzzle
   * @return a solved puzzle, or one that is solved as fully as possible
   */
  public static Puzzle findSolution(Puzzle p) {
    // Solve by iteratively guessing. Keep looping iff we haven't reached a fixed point.
    boolean hasChanged = true;
    // TODO implement better method to detect change
    while (hasChanged) {
      int[] startingBoard = Arrays.copyOf(p.getBoard(), p.getBoard().length);
      p = deduceFilledRows(p);
      p = deduceFilledColumns(p);
      if (Arrays.equals(startingBoard, p.getBoard()))
        hasChanged = false;
    }
    return p;
  }

  /**
   * Checks columns for squares that must be filled in, and fills them in
   * 
   * @param p given puzzle to check
   * @return a puzzle with deduced squares filled in. null if puzzle is null.
   */
  public static Puzzle deduceFilledColumns(Puzzle p) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Checks rows for squares that must be filled in, and fills them in
   * 
   * @param p given puzzle to check
   * @return a puzzle with deduced squares filled in. null if puzzle is null.
   */
  public static Puzzle deduceFilledRows(Puzzle p) {
    // Return null if puzzle is null
    if (p == null)
      return p;

    // Go through rows of puzzle
    for (int row = 0; row < p.rows; row++) {
      ArrayList<Integer> currentRowNums = p.rowNums.get(row);
      // Skip rows that must be empty
      if (currentRowNums.size() == 0)
        continue;

      // Fill in row if it must be full
      if (currentRowNums.get(0) == p.cols) {
        for (int pos = row * p.cols; pos < (row + 1) * p.cols; pos++) {
          p.setBoard(1, pos);
        }
      } else {
        for (int i = 0; i < currentRowNums.size(); i++) {
          // Get rowNums to left and right of ith
          List<Integer> left = currentRowNums.subList(0, i);
          List<Integer> right = currentRowNums.subList(i + 1, currentRowNums.size());
          int current = currentRowNums.get(i);

          // Get current row from board
          int[] currentRow = Arrays.copyOf(p.getBoard(), p.cols);

          // Find pos of ith rowNum where all lines are as left as possible
          int pos = 0;
          for (int rowNum : left) {
            // Search for the leftmost spot for each rowNum, incrementing pos until one is found
            int foundPos = findLeftmostFillable(currentRow, rowNum, pos);

            // Throw exception if puzzle cannot be solved (since all lines won't fit
            if (foundPos == -1)
              throw new IllegalStateException("Puzzle cannot be solved!");

            // If it fits, increment pos by rowNum+1 for line + a blank space
            // Move on to the next rowNum
            pos = foundPos + rowNum + 1;
          }

          // Add ith on left and blank all other squares
          int[] leftAligned = new int[p.cols];
          Arrays.fill(currentRow, pos, pos + current, 1);

          // Find pos of ith rowNum where all lines are as right as possible
          pos = p.cols - 1;
          for (int j = right.size() - 1; j >= 0; j--) {
            int rowNum = right.get(j);
            int foundPos = findRightmostFillable(currentRow, rowNum, pos);
            if (foundPos == -1)
              throw new IllegalStateException("Puzzle cannot be solved!");
            // If it fits, decrement pos by rowNum+1 for line + a blank space
            pos = foundPos - rowNum - 1;
          }

          // Add ith on right and blank all other squares
          int[] rightAligned = new int[p.cols];
          Arrays.fill(currentRow, pos - current + 1, pos, 1);

          // Find overlap between left and right
          int first = -1;
          int length = 0;
          for (int k = 0; k < currentRow.length; k++) {
            if (leftAligned[k] == 1 && rightAligned[k] == 1) {
              // Set first as the first of overlap if it's not set
              if (first < 0)
                first = k;
              // Increment the length
              length++;
            } else if (first >= 0) {
              // Break loop if we've gone past the overlap
              break;
            }
          }

          // Add overlap to board
          if (first >= 0) {
            int[] board = p.getBoard();
            Arrays.fill(board, first, first + length, 1);
          }
        }
      }



    }

    return p;
  }

  /**
   * Finds the rightmost spot that is >= to given position for the row number line
   * 
   * @param currentRow row to be searched
   * @param rowNum number of consecutive squares needed
   * @param pos position start from
   * @return first available rightmost spot for rowNum consecutive squares, negative if none found
   *         or given invalid rowNum or position
   */
  public static int findRightmostFillable(int[] currentRow, int rowNum, int pos) {
    // Return -1 if given given invalid rowNum, pos or currentRow
    if (rowNum < 1 || pos < 0 || currentRow == null || currentRow.length - 1 < pos)
      return -1;
    for (; pos >= rowNum - 1; pos--) {
      // Check if rowNum fits starting at pos
      boolean fits = true;
      for (int j = 0; j < rowNum; j++) {
        // If square can't be blank, break loop to next rowNum and increase pos
        if (currentRow[pos - j] == 2) {
          fits = false;
          break;
        }
      }
      if (fits) {
        // Pick the end of the current sequence of filled if the next square is filled
        // End at end of sequence or end of row
        while (pos - rowNum > 0 && currentRow[pos - rowNum] == 1) {
          pos--;
        }
        return pos;
      }
    }
    return -1;
  }

  /**
   * Finds the leftmost spot that is >= to given position for the row number line
   * 
   * @param currentRow row to be searched
   * @param rowNum number of consecutive squares needed
   * @param pos position start from
   * @return first available leftmost spot for rowNum consecutive squares, negative if none found
   */
  public static int findLeftmostFillable(int[] currentRow, int rowNum, int pos) {
    // Return -1 if given given invalid rowNum, pos or currentRow
    if (rowNum < 1 || pos < 0 || currentRow == null || currentRow.length - 1 < pos)
      return -1;
    for (; pos < currentRow.length + 1 - rowNum; pos++) {
      // Check if rowNum fits starting at pos
      boolean fits = true;
      for (int j = 0; j < rowNum; j++) {
        // If square can't be blank, break loop to next rowNum and increase pos
        if (currentRow[pos + j] == 2) {
          fits = false;
          break;
        }
      }
      if (fits) {
        // Pick the end of the current sequence of filled if the next square is filled
        // End at end of sequence or end of row
        while (pos + rowNum < currentRow.length && currentRow[pos + rowNum] == 1) {
          pos++;
        }
        return pos;
      }
    }
    return -1;
  }
}
