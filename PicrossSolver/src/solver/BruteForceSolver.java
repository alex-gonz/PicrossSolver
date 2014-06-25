package solver;

import java.util.ArrayList;
import java.util.Stack;

import puzzle.Puzzle;

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
    if(p == null || !PuzzleVerifier.matchesFull(p))
      return null;
    int pos = p.getFirstBlankPos();
    if(pos < 0)
      return p; //puzzle is solved
    //Copy the puzzle so we can modify it
    Puzzle p1 = p.createCopy();
    //Guess that next empty square is filled in
    p.setBoard(1, pos);
    //Recurse and see if we can find a solution from here
    p = findSolution(p);
    if(p != null)
      return p;
    //If we didn't find a solution, try setting next empty square as blank
    p1.setBoard(2, pos);
    return findSolution(p1);
  }
  
  /**
   * Finds all solutions to a given puzzle by brute force and returns them
   * @param p given puzzle
   * @return all possible solutions to the given puzzle, empty list if none found
   */
  public static ArrayList<Puzzle> findSolutions(Puzzle p) {
    //TODO
    ArrayList<Puzzle> ret = new ArrayList<Puzzle>();
    //Use a stack to hold all puzzles that may be solvable
    Stack<Puzzle> st = new Stack<Puzzle>();
    //Return if empty list if p is null or we know it can't be solved
    if(!PuzzleVerifier.matchesFull(p))
      return ret;
    //Otherwise, add it to the stack
    st.push(p);
    while(!st.empty()) {
      //Insert opposite values into candidate puzzle
      Puzzle top = st.pop();
      Puzzle other = top.createCopy();
      int firstEmpty = top.getFirstBlankPos();
      top.setBoard(2, firstEmpty);
      other.setBoard(1, firstEmpty);
      //Only continue with top if it can be or is already solved
      if(PuzzleVerifier.matchesFull(top))
        //If it's not solved, add it to the stack
        if(top.getFirstBlankPos() != -1)
          st.push(top);
        else
          //Add to solutions if there's no blank spot left
          ret.add(top);
      //Repeat with opposite value puzzle
      if(PuzzleVerifier.matchesFull(other))
        if(top.getFirstBlankPos() != -1)
          st.push(other);
        else
          ret.add(other);
    }
    return ret;
  }
}
