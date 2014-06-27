package solver;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;

import puzzle.Puzzle;
import ui.FileHandler;

/**
 * @author rippe_000
 * 
 */
public class DeductiveSolverTest {
  Puzzle empty = FileHandler.getFilePuzzle("testPuzzles/emptyTest.txt");
  Puzzle singleSquare = FileHandler.getFilePuzzle("testPuzzles/baseTest.txt");
  Puzzle small = FileHandler.getFilePuzzle("testPuzzles/5x5.txt");
  Puzzle multi2x2 = FileHandler.getFilePuzzle("testPuzzles/multi2x2.txt");
  Puzzle partial = FileHandler.getFilePuzzle("testPuzzles/partial.txt");
  int[] emptySol = {2};
  int[] singleSquareSol = {1};
  int[] smallSol = {
      1,1,1,2,2,
      1,2,1,1,1,
      2,2,2,1,1,
      1,2,2,2,1,
      1,1,2,2,2};

  int[] smallWrong = {
      1,1,1,2,2,
      1,2,1,1,1,
      2,2,1,1,2,
      1,2,1,2,2,
      1,1,2,2,2};

  int[] multiSol1 = {
      1,2,
      2,1};

  int[] partialDeductiveSol = {
      1,1,
      2,2,
      0,0,
      0,0};

  /**
   * Test method for {@link solver.DeductiveSolver#findSolution(puzzle.Puzzle)}.
   */
  @Test
  public void testFindSolution() {
    Puzzle output;

    // Can solve entire puzzle by deduction
    output = DeductiveSolver.findSolution(small.createCopy());
    // Small should be blank, but output should have solution
    assertTrue(Arrays.equals(small.getBoard(), new int[25]));
    small.setBoard(smallSol);
    assertTrue(small.equals(output));

    // Can't deduce any positions
    output = DeductiveSolver.findSolution(multi2x2.createCopy());
    assertTrue(Arrays.equals(output.getBoard(), new int[4]));

    // Partial solution can be found
    output = DeductiveSolver.findSolution(partial.createCopy());
    assertTrue(Arrays.equals(output.getBoard(), partialDeductiveSol));
  }


  /**
   * Test method for {@link solver.DeductiveSolver#deduceFilledColumns(puzzle.Puzzle)}.
   */
  @Test
  public void testDeduceFilledColumns() {
    fail("Not yet implemented.");
  }

  /**
   * Test method for {@link solver.DeductiveSolver#deduceFilledRows(puzzle.Puzzle)}.
   */
  @Test
  public void testDeduceFilledRows() {
    // Trivial case
    singleSquare = DeductiveSolver.deduceFilledRows(singleSquare);
    assertTrue(Arrays.equals(singleSquare.getBoard(), singleSquareSol));

    // More complex case
    partial = DeductiveSolver.deduceFilledRows(partial);
    int[] expected = {
        1,1,
        0,0,
        0,0,
        0,0};
    assertTrue(Arrays.equals(partial.getBoard(), expected));

    // Test full solve
    int input[] = {
        0,0,
        0,0,
        1,0,
        0,0};
    int sol[] = {
        1,1,
        0,0,
        1,0,
        0,1};
    partial.setBoard(input);
    partial = DeductiveSolver.deduceFilledRows(partial);
    assertTrue(Arrays.equals(partial.getBoard(), sol));
  }

  /**
   * Test method for {@link solver.DeductiveSolver#findLeftmostFillable(int[], int, int)}.
   */
  @Test
  public void testFindLeftmostFillable() {
    // Test base cases
    int[] base = {0};
    assertTrue(DeductiveSolver.findLeftmostFillable(base, 1, 0) == 0);
    int[] full = {2};
    assertTrue(DeductiveSolver.findLeftmostFillable(full, 1, 0) == -1);

    // Test just barely fitting
    int[] twoWide = {0,2,0,0,2,0,0,0};
    assertTrue(DeductiveSolver.findLeftmostFillable(twoWide, 2, 0) == 2);

    // Test too large
    assertTrue(DeductiveSolver.findLeftmostFillable(twoWide, 4, 0) == -1);
    // Test that moving position later works
    assertTrue(DeductiveSolver.findLeftmostFillable(twoWide, 2, 3) == 5);
    assertTrue(DeductiveSolver.findLeftmostFillable(twoWide, 2, 6) == 6);

    // Test that ending next to a full square pushes found position to end of the line
    int[] toEnd = {0,1,2,0,0,1,1,0};
    assertTrue(DeductiveSolver.findLeftmostFillable(toEnd, 1, 0) == 1);
    assertTrue(DeductiveSolver.findLeftmostFillable(toEnd, 2, 1) == 5);
    assertTrue(DeductiveSolver.findLeftmostFillable(toEnd, 2, 6) == 6);
  }

  /**
   * Test method for {@link solver.DeductiveSolver#findRightmostFillable(int[], int, int)}.
   */
  @Test
  public void testFindRightmostFillable() {
    // TODO create test
    // Test base cases
    int[] base = {0};
    assertTrue(DeductiveSolver.findRightmostFillable(base, 1, 0) == 0);
    int[] full = {2};
    assertTrue(DeductiveSolver.findRightmostFillable(full, 1, 0) == -1);

    // Test just barely fitting
    int[] twoWide = {0,0,0,2,0,0,2,0};
    assertTrue(DeductiveSolver.findRightmostFillable(twoWide, 2, twoWide.length - 1) == 5);

    // Test too large
    assertTrue(DeductiveSolver.findRightmostFillable(twoWide, 4, twoWide.length - 1) == -1);
    // Test that moving position later works
    assertTrue(DeductiveSolver.findRightmostFillable(twoWide, 2, 4) == 2);
    assertTrue(DeductiveSolver.findRightmostFillable(twoWide, 2, 1) == 1);

    // Test that ending next to a full square pushes found position to end of the line
    int[] toEnd = {0,1,1,0,0,2,1,0};
    assertTrue(DeductiveSolver.findRightmostFillable(toEnd, 1, toEnd.length - 1) == 6);
    assertTrue(DeductiveSolver.findRightmostFillable(toEnd, 2, toEnd.length - 2) == 2);
    assertTrue(DeductiveSolver.findRightmostFillable(toEnd, 2, 1) == 1);
  }
}
