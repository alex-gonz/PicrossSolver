package ui;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import puzzle.Puzzle;

/**
 * @author rippe_000
 *
 */
public class FileHandlerTest {



  /**
   * Test method for {@link ui.FileHandler#printBoardToFile(puzzle.Puzzle, java.lang.String)}.
   * @throws IOException throws if no test files found
   */
  @Test
  public void testPrintBoardToFile() throws IOException {
    Puzzle small = FileHandler.getFilePuzzle("testPuzzles/5x5.txt");
    int[] smallSol = {
        1,1,1,2,2,
        1,2,1,1,1,
        2,2,2,1,1,
        1,2,2,2,1,
        1,1,2,2,2};
    small.setBoard(smallSol);
    FileHandler.printBoardToFile(small, "output.txt");
    byte[] outputLines = Files.readAllBytes(Paths.get("output.txt"));
    byte[] expected = Files.readAllBytes(Paths.get("testPuzzles/5x5Solution.txt"));
    //Same except for new line at end of output
    assertTrue(outputLines.length-2 == expected.length);
    for(int i = 0; i < expected.length; i++) {
      assertTrue(outputLines[i] == expected[i]);
    }

  }

  /**
   * Test method for {@link ui.FileHandler#translate(int)}.
   */
  @Test
  public void testTranslateInt() {
    assertTrue(FileHandler.translate(0) == ' ');
    assertTrue(FileHandler.translate(1) == '\u2588');
    assertTrue(FileHandler.translate(2) == ' ');
    assertTrue(FileHandler.translate(3) == '?');
    assertTrue(FileHandler.translate(-1)== '?');
  }
  
  /**
   * Test method for {@link ui.FileHandler#translate(char)}.
   */
  @Test
  public void testTranslateChar() {
    assertTrue(FileHandler.translate('\u2588') == 1);
    assertTrue(FileHandler.translate(' ') == 2);
    assertTrue(FileHandler.translate('?') == 0);
    assertTrue(FileHandler.translate('a') == -1);
    assertTrue(FileHandler.translate('X') == -1);
  }

  /**
   * Test method for {@link ui.FileHandler#printPuzzleToFile(puzzle.Puzzle, java.lang.String)}.
   * @throws IOException if test files not printed or found
   */
  @Test
  public void testPrintPuzzleToFile() throws IOException {
    Puzzle small = FileHandler.getFilePuzzle("testPuzzles/5x5.txt");
    FileHandler.printPuzzleToFile(small, "output.txt");
    byte[] outputLines = Files.readAllBytes(Paths.get("output.txt"));
    byte[] expected = Files.readAllBytes(Paths.get("testPuzzles/5x5.txt"));
    //Same except for new line at end of output
    assertTrue(outputLines.length-2 == expected.length);
    for(int i = 0; i < expected.length; i++) {
      assertTrue(outputLines[i] == expected[i]);
    }
  }

  /**
   * Test method for {@link ui.FileHandler#getFilePuzzle(java.lang.String)}.
   */
  @Test
  public void testGetFilePuzzle() {
    //Test base puzzle
    Puzzle fromFile = FileHandler.getFilePuzzle("testPuzzles/baseTest.txt");
    ArrayList<ArrayList<Integer>> a = new ArrayList<ArrayList<Integer>>();
    ArrayList<Integer> one = new ArrayList<Integer>();
    one.add(1);
    a.add(one);
    assertTrue(new Puzzle(a,a).equals(fromFile));

    //Test with larger puzzle
    fromFile = FileHandler.getFilePuzzle("testPuzzles/5x5.txt");
    ArrayList<ArrayList<Integer>> row = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> col = new ArrayList<ArrayList<Integer>>();
    ArrayList<Integer> twoTwos = new ArrayList<Integer>();
    twoTwos.add(2);
    twoTwos.add(2);
    ArrayList<Integer> twoOnes = new ArrayList<Integer>();
    twoOnes.add(1);
    twoOnes.add(1);
    ArrayList<Integer> two = new ArrayList<Integer>();
    two.add(2);
    ArrayList<Integer> three = new ArrayList<Integer>();
    three.add(3);
    ArrayList<Integer> oneThenThree = new ArrayList<Integer>();
    oneThenThree.add(1);
    oneThenThree.add(3);
    row.add(three);
    row.add(oneThenThree);
    row.add(two);
    row.add(twoOnes);
    row.add(two);

    col.add(twoTwos);
    col.add(twoOnes);
    col.add(two);
    col.add(two);
    col.add(three);

    assertTrue(new Puzzle(col,row).equals(fromFile));
  }



  /**
   * Test method for {@link ui.FileHandler#getFileBoard(java.lang.String)}.
   */
  @Test
  public void testGetFileBoard() {
    //Test basic case
    int[] input = FileHandler.getFileBoard("testPuzzles/baseSolution.txt");
    int[] expected = {1};
    assertTrue(Arrays.equals(expected, input));

    //Test with unicode characters
    input = FileHandler.getFileBoard("testPuzzles/5x5Solution.txt");
    int[] smallSol = {
        1,1,1,2,2,
        1,2,1,1,1,
        2,2,2,1,1,
        1,2,2,2,1,
        1,1,2,2,2};
    assertTrue(Arrays.equals(smallSol, input));

    //Test with numbers
    input = FileHandler.getFileBoard("testPuzzles/5x5NumberSolution.txt");
    assertTrue(Arrays.equals(smallSol, input));
  }
}

