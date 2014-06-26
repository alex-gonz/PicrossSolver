package solver;

import static org.junit.Assert.assertTrue;

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
   * Test method for {@link solver.DeductiveSolver#deduceFilledLines(puzzle.Puzzle)}.
   */
  @Test
  public void testDeduceFilledLines() {
    // Trivial case
    singleSquare = DeductiveSolver.deduceFilledLines(singleSquare);
    assertTrue(Arrays.equals(singleSquare.getBoard(), singleSquareSol));

    // More complex case
    partial = DeductiveSolver.deduceFilledLines(partial);
    int[] expected = {
        1,1,
        0,0,
        0,0,
        0,0};
    assertTrue(Arrays.equals(partial.getBoard(), expected));
  }
}
