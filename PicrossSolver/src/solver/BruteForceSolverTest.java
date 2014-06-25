package solver;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import puzzle.Puzzle;
import ui.FileHandler;
/**
 * 
 * @author coatrain
 *
 */
public class BruteForceSolverTest {

  Puzzle empty = FileHandler.getFilePuzzle("testPuzzles/emptyTest.txt");
  Puzzle singleSquare = FileHandler.getFilePuzzle("testPuzzles/baseTest.txt");
  Puzzle small = FileHandler.getFilePuzzle("testPuzzles/5x5.txt");
  Puzzle multi2x2 = FileHandler.getFilePuzzle("testPuzzles/multi2x2.txt");
  Puzzle multiLarge = FileHandler.getFilePuzzle("testPuzzles/multiLarge.txt");
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
  
  int[] multiSol2 = {
      2,1,
      1,2};
  
  
  /**
   * Test method for {@link solver.BruteForceSolver#findSolution(puzzle.Puzzle)}.
   */
  @Test
  public void testFindSolution() {
    assertTrue(Arrays.equals(BruteForceSolver.findSolution(empty).getBoard(), emptySol));
    assertTrue(Arrays.equals(BruteForceSolver.findSolution(small).getBoard(), smallSol));
  }

  /**
   * Test method for {@link solver.BruteForceSolver#findSolutions(puzzle.Puzzle)}.
   */
  @Test
  public void testFindSolutions() {
    //Find for a single solution puzzle
    ArrayList<Puzzle> sols = BruteForceSolver.findSolutions(small);
    assertTrue(sols.size() == 1);
    assertTrue(Arrays.equals(sols.get(0).getBoard(), smallSol));
    
    //Find for a small multi-solution puzzle
    sols = BruteForceSolver.findSolutions(multi2x2);
    assertTrue(sols.size() == 2);
    assertTrue(Arrays.equals(sols.get(0).getBoard(), multiSol1) ||
        Arrays.equals(sols.get(0).getBoard(), multiSol2));
    assertTrue(Arrays.equals(sols.get(1).getBoard(), multiSol1) ||
        Arrays.equals(sols.get(1).getBoard(), multiSol2));
    
    //Count solutions for a many-solution puzzle
    sols = BruteForceSolver.findSolutions(multiLarge);
    //Solutions are included below the problem in the same text file
    assertTrue(sols.size() == 8);
  }

}
