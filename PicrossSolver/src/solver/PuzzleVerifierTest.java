/**
 * 
 */
package solver;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import puzzle.Puzzle;
import ui.FileHandler;

/**
 * @author rippe_000
 *
 */
public class PuzzleVerifierTest {
  
  Puzzle empty = FileHandler.getFilePuzzle("testPuzzles/emptyTest.txt");
  Puzzle singleSquare = FileHandler.getFilePuzzle("testPuzzles/baseTest.txt");
  Puzzle small = FileHandler.getFilePuzzle("testPuzzles/5x5.txt");
  
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
  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {}

  /**
   * Test method for {@link solver.PuzzleVerifier#isValidPuzzle(puzzle.Puzzle)}.
   */
  @Test(expected = IllegalStateException.class)
  public void testIsValidPuzzle() {
    //Init arraylists for puzzle
    ArrayList<ArrayList<Integer>> row = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> col = new ArrayList<ArrayList<Integer>>();
    ArrayList<Integer> a = new ArrayList<Integer>();
    a.add(2);
    a.add(1);
    
    row.add(new ArrayList<Integer>());
    row.add(a);
    row.add(a);
    row.add(new ArrayList<Integer>());
    row.add(a);
    
    col.add(a);
    col.add(a);
    col.add(new ArrayList<Integer>());
    col.add(a);
    //Create Puzzle and test
    Puzzle p = null;
    try {
      p = new Puzzle(col, row);
    } catch (IllegalStateException e) {
      fail("Shouldn't catch on a solvable puzzle");
    }
    
    //Change so that this becomes an unsolvable puzzle
    a.add(1);
    p = new Puzzle(col, row);
    fail("Didn't catch exception");
  }
  
  /**
   * Test method for {@link solver.PuzzleVerifier#matchesFull(puzzle.Puzzle)}.
   */
  @Test
  public void testMatchesFull() {
  //No 0 in center means it can check the center row and column to see it's wrong
    int[] smallUnfilledWrong = {
        0,1,1,1,1,
        1,0,1,1,1,
        1,1,2,1,1,
        1,1,1,0,1,
        1,1,1,1,0};
    int[] smallNoFull = {
        0,1,1,1,1,
        1,0,1,1,1,
        1,1,0,1,1,
        1,1,1,0,1,
        1,1,1,1,0};
    
    //Test that unguessed boards return true
    assertTrue(PuzzleVerifier.matchesFull(empty));
    assertTrue(PuzzleVerifier.matchesFull(singleSquare));
    assertTrue(PuzzleVerifier.matchesFull(small));
    
    //Test that we don't match unfilled rows or columns
    small.setBoard(smallNoFull);
    assertTrue(PuzzleVerifier.matchesFull(small));
    small.setBoard(smallUnfilledWrong);
    assertFalse(PuzzleVerifier.matchesFull(small));
    
    //Test some boards that should fail
    empty.setBoard(singleSquareSol);
    singleSquare.setBoard(emptySol);
    small.setBoard(smallWrong);
    assertFalse(PuzzleVerifier.matchesFull(empty));
    assertFalse(PuzzleVerifier.matchesFull(singleSquare));
    assertFalse(PuzzleVerifier.matchesFull(small));
    
    //Test that solved boards return true
    empty.setBoard(emptySol);
    singleSquare.setBoard(singleSquareSol);
    small.setBoard(smallSol);
    assertTrue(PuzzleVerifier.matchesFull(empty));
    assertTrue(PuzzleVerifier.matchesFull(singleSquare));
    assertTrue(PuzzleVerifier.matchesFull(small));
  }
}
