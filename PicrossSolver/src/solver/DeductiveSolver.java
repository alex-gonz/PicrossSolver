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
      p = deduceFilledLines(p);

      if (Arrays.equals(startingBoard, p.getBoard()))
        hasChanged = false;
    }
    return p;
  }

  /**
   * Checks columns and rows for squares that must be filled in, and fills them in
   * 
   * @param p given puzzle to check
   * @return a puzzle with deduced squares filled in
   */
  public static Puzzle deduceFilledLines(Puzzle p) {
    // TODO Auto-generated method stub
    // Go through rows of puzzle
    for (int row = 0; row < p.rows; row++) {
      ArrayList<Integer> currentRowNums = p.rowNums.get(row);
      // Fill in row if it must be full
      if (currentRowNums.get(0) == p.cols) {
        for (int pos = row * p.cols; pos < (row + 1) * p.cols; pos++) {
          p.setBoard(1, pos);
        }
      } else {
        for (int i = 0; i < currentRowNums.size(); i++) {
          List<Integer> left = currentRowNums.subList(0, i);
          List<Integer> right = currentRowNums.subList(i, currentRowNums.size());
        }
      }



    }

    return p;
  }
}
