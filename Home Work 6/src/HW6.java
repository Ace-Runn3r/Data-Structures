/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Main method to create a game board using a graph and run a game of tron
               Runs for one turn
 */

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class HW6 {

    /**
     * Method to print board to screen in correct format
     * @param playField     GameBoard to print
     */
    private static void printBoard(GameBoard playField) {
        System.out.print(" ");                                      // create column headings
        for (int i = 0; i < playField.getWidth(); i++) {
            System.out.print(i);
        }
        System.out.println();
        for (int i = 0; i < playField.getHeight(); i++) {           // print each row with heading
            System.out.printf("%d%s%n", i, playField.printRow(i));
        }
    }
    
    public static void main (String[] args) throws IOException {
        Scanner fileInput = new Scanner(Paths.get(args[0]));        // scanner for file input
        GameBoard playField = new GameBoard(fileInput.nextInt(), fileInput.nextInt());
        String fromFile;
        
        fileInput.nextLine();                                       // insert file elements into game board
        for (int i = 0; i < playField.getHeight(); i++) {
            fromFile = fileInput.nextLine();
            playField.insertRow(i, fromFile.split(""));
        }
        fileInput.close();
        
        Scanner player = new Scanner(System.in);                     // scanner for user input
        boolean updateSuccess = false;                               // check for valid move by Tron
        int endCode = 0;                                             // used to report what end condition was met 0 = not over,
        printBoard(playField);                                       // 1 = Tron reached tower, 2 = bog ate tron, 3 = tron is trapped                                     
       
        while (!updateSuccess) {
            System.out.print("Please enter your move [u(p), d(own), l(elf), or r(ight)]: ");
            String playerInput = player.next();                      // store player input
        
            if (!playerInput.equals("u") && !playerInput.equals("d")                                    // check for correct input
                                         && !playerInput.equals("l") && !playerInput.equals("r"))  {
                System.out.println("Incorrent input. Please try again");                                // error output
            }
        
            updateSuccess = playField.moveTron(playerInput);                        // move tron and check if move was valid
            if (playField.doneCheck() == 1) {                                       // if Tron reaches I/O tower
                break;                                                              // game is over leave loop
            }
            if (playField.doneCheck() == 3) {                                       // check end code of board
                System.out.println("Tron is trapped with no moves. Bugs turn.");    // tron has no valid move so let bugs move
            } else if (!updateSuccess) {
                System.out.println("Adjacent cell is not empty. Please try again"); // tron's move was invalid
            }
        }
        
        printBoard(playField);                          // output
        if (playField.doneCheck() != 1) {               // if Tron has not reached I/O tower
            System.out.print(playField.updateBugs());   // update bugs and print their moves                                                           // skip bugs moves
        }

        endCode = playField.doneCheck();                // check if game is over
        
        switch (endCode) {
        case 1 :                                            // end code check for final output
            System.out.println("Tron reaches I/O Tower.");
            break;
        case 2 :
            System.out.println("A bug is not hungry any more!");
            break;
        }
        
        player.close();
    }
}
