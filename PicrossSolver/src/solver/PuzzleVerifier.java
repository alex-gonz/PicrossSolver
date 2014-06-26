package solver;

import java.util.ArrayList;

import puzzle.Puzzle;

/**
 * Puzzle verifier methods for solvers
 * 
 * @author rippe_000
 * 
 */
public class PuzzleVerifier {
  /**
   * Determines if a board could have solutions based on its dimensions, colNums and rowNums
   * 
   * @param p given puzzle
   * @return false if the board is determined impossible to solve, otherwise true
   */
  public static boolean isValidPuzzle(Puzzle p) {
    boolean ret = true;

    ret = ret && sameNumSquares(p);
    ret = ret && squaresFit(p);

    return ret;
  }

  /**
   * Returns true if all squares of the columns and p.rows could fit in the board
   * 
   * @param p given puzzle
   * @return true if each column and row could contain specified squares. False if they don't, or
   *         given null puzzle
   */
  private static boolean squaresFit(Puzzle p) {
    if (p == null)
      return false;
    for (ArrayList<Integer> list : p.colNums) {
      int lineTotal = 0;
      for (Integer i : list) {
        // Add i+1 to account for extra space between numbers
        lineTotal += i + 1;
      }
      // Remove extra space
      lineTotal--;
      // Compare to spaces in column
      if (lineTotal > p.rows)
        return false;
    }

    for (ArrayList<Integer> list : p.rowNums) {
      int lineTotal = 0;
      for (Integer i : list) {
        // Add i+1 to account for extra space between numbers
        lineTotal += i + 1;
      }
      // Remove extra space
      lineTotal--;
      // Compare to spaces in row
      if (lineTotal > p.cols)
        return false;
    }
    return true;
  }

  /**
   * Checks if there are the same number of required squares in columns as in p.rows. If false,
   * puzzle cannot be solved.
   * 
   * @param p given puzzle
   * @return True if number of required squares are same for both columns and p.rows. False if they
   *         don't match or puzzle is null
   */
  private static boolean sameNumSquares(Puzzle p) {
    if (p == null)
      return false;
    int total = 0;
    for (ArrayList<Integer> a : p.colNums) {
      for (Integer i : a) {
        total += i;
      }
    }
    for (ArrayList<Integer> a : p.rowNums) {
      for (Integer i : a) {
        total -= i;
      }
    }
    return total == 0;
  }

  /**
   * Determine if fully filled p.rows and columns of board match the numbers on edges
   * 
   * @param p given puzzle
   * @return true if all full p.rows and columns match, false otherwise
   */
  public static boolean matchesFull(Puzzle p) {
    for (int i = 0; i < p.cols; i++) {
      ArrayList<Integer> currentRow = new ArrayList<Integer>();
      int continuousFilled = 0;
      boolean skip = false;
      // Iterate through row i of board
      for (int j = 0; j < p.rows; j++) {
        int pos = j + i * p.cols;
        // Break if row isn't full of guessed values
        if (p.getBoard()[pos] == 0) {
          skip = true; // Set so that we know we broke the loop
          break;
        }

        // Add to list if next is blank and last square was not blank
        if (p.getBoard()[pos] == 2 && continuousFilled != 0) {
          currentRow.add(continuousFilled);
          continuousFilled = 0;
          // Add to the number of square in a row if square is filled in
        } else if (p.getBoard()[pos] == 1) {
          continuousFilled++;
        }
      }
      // Skip line if skip flag is true
      if (skip)
        continue;
      // Add the last continuous patch of squares if there are any
      if (continuousFilled > 0)
        currentRow.add(continuousFilled);
      // Check if row matches what we counted
      if (!currentRow.equals(p.rowNums.get(i)))
        return false;
    }

    for (int k = 0; k < p.rows; k++) {
      ArrayList<Integer> currentCol = new ArrayList<Integer>();
      int continuousFilled = 0;
      boolean skip = false;
      // Iterate through column k of board
      for (int l = 0; l < p.rows; l++) {
        int pos = k + l * p.cols;
        if (p.getBoard()[pos] == 0) {
          skip = true;
          break;
        }

        if (p.getBoard()[pos] == 2 && continuousFilled != 0) {
          currentCol.add(continuousFilled);
          continuousFilled = 0;
        } else if (p.getBoard()[pos] == 1) {
          continuousFilled++;
        }
      }
      if (skip)
        continue;
      if (continuousFilled > 0)
        currentCol.add(continuousFilled);
      if (!currentCol.equals(p.colNums.get(k)))
        return false;
    }
    return true;
  }
}
