package solver;

import java.util.ArrayList;

import data.Puzzle;

/**
 * Solve the puzzle by brute force
 * @author coatrain
 *
 */
public class BruteForceSolver {

  /**
   * Solves a given puzzle by brute force
   * @param p given puzzle
   * @return the solved puzzle if one is found
   */
  public static Puzzle findSolution(Puzzle p) {
    //TODO Test
    if(p == null || !p.isSolvable())
      return null;
    int pos = p.getFirstBlankPos();
    if(pos < 0)
      return p; //puzzle is solved
    //Copy the puzzle so we can modify it
    Puzzle p1 = p.createCopy();
    //Guess that next empty square is filled in
    p.setBoard(1, pos);
    //Recurse and see if we can find a solution from here
    if(findSolution(p) != null)
      return p;
    //If we didn't find a solution, try setting next empty square as blank
    p1.setBoard(2, pos);
    return findSolution(p1);
  }
  
  /**
   * Finds all solutions to a given puzzle by brute force and returns them
   * @param p given puzzle
   * @return all possible solutions to the given puzzle
   */
  public static ArrayList<Puzzle> findSolutions(Puzzle p) {
    //TODO
    return null;
  }
}
