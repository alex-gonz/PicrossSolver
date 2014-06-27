package ui;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import puzzle.Puzzle;
import solver.BruteForceSolver;

/**
 * Handles file io for puzzles When reading a puzzle, First line of file should have number of rows,
 * a space, then number of columns Following lines should have the rows then columns. The numbers
 * which specify the squares to be filled in should be given left to right, or up to down and space
 * separated. If there are no squares to be filled in (line is blank, 0 normally on edge), the line
 * should be left blank in the file.
 * 
 * @author coatrain
 * 
 */
public class FileHandler {

  public static final String inputLocation = "testPuzzles/5x5.txt";
  public static final String outputLocation = "output.txt";
  public static final Charset utf8 = StandardCharsets.UTF_8;

  public static void main(String[] args) {
    // Parse a puzzle from file
    Puzzle p = getFilePuzzle(inputLocation);
    // Quit if we don't have a puzzle
    if (p == null) {
      System.err.println("Not given a valid puzzle.");
      return;
    }
    // Find solution through brute force
    p = BruteForceSolver.findSolution(p);

    // Output to file
    printBoardToFile(p, outputLocation);

  }

  /**
   * Writes a board of a puzzle to given location, creating or overwriting at the location. If
   * puzzle is null, creates an empty file
   * 
   * @param p a given puzzle
   * @param fileLocation given file location
   */
  public static void printBoardToFile(Puzzle p, String fileLocation) {
    // Generate lines to write
    List<String> lines = new ArrayList<String>();
    // Only get board if p isn't null
    if (p != null) {
      // Create an initialize a stringbuilder to hold each line
      StringBuilder sb = new StringBuilder(p.cols);
      // Fill lines by rows of board
      for (int i = 0; i < p.getBoard().length; i++) {
        sb.append(translate(p.getBoard()[i]));
        // Check if we've reached the end of the current row
        if ((i + 1) % p.cols == 0) {
          lines.add(sb.toString());
          sb = new StringBuilder(p.cols);
        }
      }
    }
    // sb should be equivalent to a new stringbuilder at this point

    try {
      // Write lines to file, overwriting or creating a file at given location
      Files.write(Paths.get(fileLocation), lines, utf8);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Translates integers in board to a visual representation
   * 
   * @param i given integer
   * @return dark block if it's filled in, space if it's not, ? if it doesn't match any
   */
  public static char translate(int i) {
    if (i == 1)
      return '\u2588';
    if (i == 2 || i == 0)
      return ' ';
    return '?';
  }

  /**
   * Translates characters in board to integer representation
   * 
   * @param c given character
   * @return dark block becomes 1, space becomes 2, ? becomes 0, -1 if it doesn't match either
   */
  public static int translate(char c) {
    if (c == '\u2588')
      return 1;
    if (c == ' ')
      return 2;
    if (c == '?')
      return 0;
    return -1;
  }

  /**
   * Writes a puzzle to a given file location. Will create or overwrite any file a given location
   * 
   * @param p given puzzle
   * @param fileLocation given file location
   */
  public static void printPuzzleToFile(Puzzle p, String fileLocation) {
    // Generate lines to write to file
    List<String> lines = new ArrayList<String>();
    // Add row and col nums
    lines.add(p.rows + " " + p.cols);
    // Break row arraylist into lines and space separated integers
    for (ArrayList<Integer> list : p.rowNums) {
      // Instantiate with first number
      StringBuilder sb = new StringBuilder(list.get(0).toString());
      for (int i = 1; i < list.size(); i++) {
        // Add on each number with a space to separate
        sb.append(" " + list.get(i));
      }
      // Add row to list of lines to write
      lines.add(sb.toString());
    }

    // Do the same with columns
    for (ArrayList<Integer> list : p.colNums) {
      StringBuilder sb = new StringBuilder(list.get(0).toString());
      for (int i = 1; i < list.size(); i++) {
        sb.append(" " + list.get(i));
      }
      lines.add(sb.toString());
    }

    try {
      // Write lines to file, overwriting or creating a file at given location
      Files.write(Paths.get(fileLocation), lines, utf8);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Parses a puzzle from given text file First line of file should have number of rows, a space,
   * then number of columns Following lines should have rows then columns. The numbers which specify
   * the squares to be filled in should be given left to right, or up to down and space separated.
   * 
   * @param fileLocation location of the file
   * @return a puzzle with a blank board matching the file, or null if cannot be parsed to a puzzle
   */
  public static Puzzle getFilePuzzle(String fileLocation) {
    try {
      List<String> lines = Files.readAllLines(Paths.get(fileLocation), utf8);
      // First line says how many rows, then columns
      String[] line = lines.get(0).split("\\s");
      int rows = Integer.parseInt(line[0]);
      int cols = Integer.parseInt(line[1]);
      ArrayList<ArrayList<Integer>> rowNums = new ArrayList<ArrayList<Integer>>(rows);
      ArrayList<ArrayList<Integer>> colNums = new ArrayList<ArrayList<Integer>>(cols);

      // Next rows lines specify numbers on row for puzzle
      for (int i = 1; i < rows + 1; i++) {
        // Create row list for numbers
        ArrayList<Integer> currentRow = new ArrayList<Integer>();
        // Split line into individual numbers
        // Handle case where consecutive last lines are empty
        if (lines.size() < i + 1) {
          rowNums.add(currentRow);
          continue;
        }
        line = lines.get(i).split("\\s");
        // Add numbers to row
        for (String s : line) {
          // Handle empty line (no filled-in squares on line)
          if (!s.equals("")) {
            // Only add if number > 0
            int found = Integer.parseInt(s);
            if (found > 0)
              currentRow.add(found);
          }
        }
        // Add row to row list
        rowNums.add(currentRow);
      }

      // then next cols lines specify numbers on the columns
      for (int i = rows + 1; i < rows + cols + 1; i++) {
        // Do the same for columns
        ArrayList<Integer> currentCol = new ArrayList<Integer>();
        if (lines.size() < i + 1) {
          colNums.add(currentCol);
          continue;
        }
        line = lines.get(i).split("\\s");

        for (String s : line) {
          if (!s.equals(""))
            currentCol.add(Integer.parseInt(s));
        }
        colNums.add(currentCol);
      }

      return new Puzzle(colNums, rowNums);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Gets a board from file. Board must be rectangular
   * 
   * @param fileLocation given location
   * @return a given board as array from file location, null if none found at location
   */
  public static int[] getFileBoard(String fileLocation) {
    int[] ret = null;
    try {
      // Get board as lines
      List<String> lines = Files.readAllLines(Paths.get(fileLocation), utf8);

      // Determine size of ret, assume a rectangular board
      ret = new int[lines.get(0).length() * lines.size()];

      // Iterate through all lines, adding each character to the board
      int nextEmpty = 0;
      for (String line : lines) {
        for (int i = 0; i < line.length(); i++) {
          char c = line.charAt(i);
          // Add character as number if it is one
          if (Character.isDigit(c))
            ret[nextEmpty] = Character.getNumericValue(c);
          else
            // Otherwise, translate it if possible
            ret[nextEmpty] = translate(c);
          nextEmpty++;
        }
      }

    } catch (IOException e) {
      // Error if we can't find file
      e.printStackTrace();
    }
    return ret;
  }

}
