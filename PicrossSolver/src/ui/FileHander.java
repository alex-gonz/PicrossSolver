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
 * Takes in a file at given location and will output solution to output
 * @author coatrain
 *
 */
public class FileHander {

  public static final String inputLocation = "input.txt";
  public static final String outputLocation = "output.txt";
  public static final Charset utf8 = StandardCharsets.UTF_8;
  
  public static void main(String[] args) {
    //Parse a puzzle from file
    Puzzle p = getStringPuzzle(inputLocation);
    //Quit if we don't have a puzzle
    if(p == null) {
      System.err.println("Not given a valid puzzle.");
      return;
    }
    //Find solution through brute force
    p = BruteForceSolver.findSolution(p);
    
    //Output to file
    printBoardToFile(p, outputLocation);

  }

  /**
   * Writes a board of a puzzle to given location, creating or overwriting at the location
   * @param p a given puzzle
   * @param fileLocation given file location
   */
  public static void printBoardToFile(Puzzle p, String fileLocation) {
    // TODO Test
    //Generate lines to write
    List<String> lines = new ArrayList<String>();
    //Create an initialize a stringbuilder to hold each line
    StringBuilder sb = new StringBuilder(p.cols);
    //Fill lines by rows of board
    for(int i=0; i<p.getBoard().length; i++) {
      sb.append(translate(p.getBoard()[i]));
      //Check if we've reached the end of the current row
      if((i+1)%p.cols == 0) {
        lines.add(sb.toString());
        sb = new StringBuilder(p.cols);
      }
    }
    //sb should be equivalent to a new stringbuilder at this point
   
    try {
      //Write lines to file, overwriting or creating a file at given location
      Files.write(Paths.get(fileLocation), lines, utf8);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Translates integers in board to a visual representation
   * @param i given integer
   * @return dark block if it's filled in, space if it's not, null if it doesn't match
   */
  public static String translate(int i) {
    // TODO Test
    if(i == 1)
      return "█";
    if(i == 2 || i == 0)
      return " ";
    return null;
  }

  /**
   * Writes a puzzle to a given file location. Will create or overwrite any file a given location
   * @param p given puzzle
   * @param fileLocation given file location
   */
  public static void printPuzzleToFile(Puzzle p, String fileLocation) {
    // TODO test
    //Generate lines to write to file
    List<String> lines = new ArrayList<String>();
    //Add row and col nums
    lines.add(p.rows + " " + p.cols);
    //Break row arraylist into lines and space separated integers
    for(ArrayList<Integer> list: p.rowNums) {
      //Instantiate with first number
      StringBuilder sb = new StringBuilder(list.get(0).toString());
      for(int i = 1; i<list.size(); i++) {
        //Add on each number with a space to separate
        sb.append(" " + list.get(i));
      }
      //Add row to list of lines to write
      lines.add(sb.toString());
    }
    
    //Do the same with columns
    for(ArrayList<Integer> list: p.colNums) {
      StringBuilder sb = new StringBuilder(list.get(0).toString());
      for(int i = 1; i<list.size(); i++) {
        sb.append(" " + list.get(i));
      }
      lines.add(sb.toString());
    }

    try {
      //Write lines to file, overwriting or creating a file at given location
      Files.write(Paths.get(fileLocation), lines, utf8);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Parses a puzzle from given text file
   * First line of file should have number of rows, a space, then number of columns
   * Following lines should have rows then columns. The numbers which specify the squares to be
   * filled in should be given left to right, or up to down and space separated.
   * 
   * @param fileLocation location of the file
   * @return a puzzle with a blank board matching the file, or null if cannot be parsed to a puzzle
   */
  //TODO test
  public static Puzzle getStringPuzzle(String fileLocation) {
    try {
      List<String> lines = Files.readAllLines(Paths.get(fileLocation), utf8);
      //First line says how many rows, then columns
      String[] line = lines.get(0).split("\\s");
      int rows = Integer.parseInt(line[0]);
      int cols = Integer.parseInt(line[1]);
      
      //Next rows lines specify numbers on row for puzzle
      ArrayList<ArrayList<Integer>> rowNums = new ArrayList<ArrayList<Integer>>();
      for(int i = 1; i<rows+1; i++) {
        //Split line into individual numbers
        line = lines.get(i).split("\\s");
        //Create row list for numbers
        ArrayList<Integer> currentRow = new ArrayList<Integer>();
        //Add numbers to row
        for(String s: line) {
          currentRow.add(Integer.parseInt(s));
        }
        //Add row to row list
        rowNums.add(currentRow);
      }
      
      //then next cols lines specify numbers on the columsn
      ArrayList<ArrayList<Integer>> colNums = new ArrayList<ArrayList<Integer>>();
      for(int i = rows+1; i<cols+1; i++) {
        //Do the same for columns
        line = lines.get(i).split("\\s");
        ArrayList<Integer> currentCol = new ArrayList<Integer>();
        for(String s: line) {
          currentCol.add(Integer.parseInt(s));
        }
        rowNums.add(currentCol);
      }
      
      return new Puzzle(rowNums, colNums);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
