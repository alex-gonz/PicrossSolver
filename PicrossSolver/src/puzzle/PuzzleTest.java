package puzzle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ui.FileHandler;

/**
 * @author coatrain
 *
 */
public class PuzzleTest {

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
  public void setUp() throws Exception { }

  /**
   * Test method for {@link puzzle.Puzzle#Puzzle(java.util.ArrayList, java.util.ArrayList)}.
   */
  @Test(expected = IllegalStateException.class)
  public void testPuzzle()  {
    //Init arraylists for puzzle
    ArrayList<ArrayList<Integer>> row = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> col = new ArrayList<ArrayList<Integer>>();
    ArrayList<Integer> a = new ArrayList<Integer>();
    a.add(1);
    a.add(2);
    
    row.add(new ArrayList<Integer>());
    row.add(a);
    row.add(new ArrayList<Integer>());
    row.add(a);
    row.add(a);
    
    col.add(a);
    col.add(new ArrayList<Integer>());
    col.add(a);
    col.add(a);
    //Create Puzzle and test
    Puzzle p = null;
    try {
      p = new Puzzle(col, row);
    } catch (IllegalStateException e) {
      fail("Shouldn't catch on a solvable puzzle");
    }
    assertTrue(p.cols == col.size());
    assertTrue(p.rows == row.size());
    assertTrue(row.equals(p.rowNums));
    assertTrue(col.equals(p.colNums));
    assertTrue(p.getBoard().length == row.size()*col.size());
    for(Integer i: p.getBoard()) {
      assertTrue(i == 0);
    }
    
    //Change so that this becomes an unsolvable puzzle
    a.add(1);
    p = new Puzzle(col, row);
    fail("Didn't catch exception");
  }

  /**
   * Test method for {@link puzzle.Puzzle#getBoard()}.
   */
  @Test
  public void testGetBoard() {
    empty.setBoard(emptySol);
    assertTrue(empty.getBoard().equals(emptySol));
    small.setBoard(smallSol);
    assertTrue(small.getBoard().equals(smallSol));
  }

  /**
   * Test method for {@link puzzle.Puzzle#setBoard(int[])}.
   */
  @Test
  public void testSetBoardIntArray() {
    //Test with 1x1 boards
    empty.setBoard(emptySol);
    assertTrue(empty.getBoard().equals(emptySol));
    singleSquare.setBoard(singleSquareSol);
    assertTrue(singleSquare.getBoard().equals(singleSquareSol));
    
    //Test with 5x5 board
    small.setBoard(smallSol);
    assertTrue(small.getBoard().equals(smallSol));
  }

  /**
   * Test method for {@link puzzle.Puzzle#setBoard(int, int)}.
   */
  @Test
  public void testSetBoardIntInt() {
    empty.setBoard(2, 0);
    assertTrue(Arrays.equals(emptySol, empty.getBoard()));
    small.setBoard(Arrays.copyOf(smallSol, smallSol.length));
    smallSol[20] = 0;
    small.setBoard(0, 20);
    assertTrue(Arrays.equals(smallSol, small.getBoard()));
  }

  /**
   * Test method for {@link puzzle.Puzzle#getFirstBlankPos()}.
   */
  @Test
  public void testGetFirstBlankPos() {
    int[] fourthUnguessed = {
        1,2,1,2,0,
        0,2,2,2,0,
        0,2,1,2,0,
        1,2,1,2,0,
        1,2,0,2,1,};
    int[] centerUnguessed = {
        1,2,1,2,2,
        1,2,2,2,1,
        2,2,0,2,2,
        1,2,1,2,1,
        1,2,1,2,2,};
    
    //Test with unguessed boards
    assertTrue(empty.getFirstBlankPos() == 0);
    assertTrue(small.getFirstBlankPos() == 0);
    
    //Test with solved boards for negative result
    empty.setBoard(emptySol);
    small.setBoard(smallSol);
    assertTrue(empty.getFirstBlankPos() == -1);
    assertTrue(small.getFirstBlankPos() == -1);
    
    //Test with other boards
    small.setBoard(fourthUnguessed);
    assertTrue(small.getFirstBlankPos() == 4);
    small.setBoard(centerUnguessed);
    assertTrue(small.getFirstBlankPos() == 12);
  }

  /**
   * Test method for {@link puzzle.Puzzle#createCopy()}.
   */
  @Test
  public void testCreateCopy() {
    small.setBoard(smallSol);
    Puzzle other = small.createCopy();
    assertFalse(other == small);
    assertFalse(other.getBoard() == small.getBoard());
    assertTrue(other.colNums.equals(small.colNums));
    assertTrue(other.rowNums.equals(small.rowNums));
    assertTrue(other.rows == small.rows);
    assertTrue(other.cols == small.cols);
    assertTrue(Arrays.equals(other.getBoard(), small.getBoard()));
  }

  /**
   * Test method for {@link puzzle.Puzzle#boardToPuzzle(int[], int, int)}.
   */
  @Test
  public void testBoardToPuzzle() {
    //Test that rows and cols must match to given board's size
    assertEquals(null, Puzzle.boardToPuzzle(smallSol, 4, 6));
    
    //Test for matching
    assertTrue(singleSquare.equals(Puzzle.boardToPuzzle(singleSquareSol, 1, 1)));
    assertTrue(small.equals(Puzzle.boardToPuzzle(smallSol, 5, 5)));
    assertFalse(small.equals(Puzzle.boardToPuzzle(smallWrong, 5, 5)));
  }
}
