/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Graph used by HW6 to store location of nodes.
               All nodes are connected to their immediate neighbors with the same weight
 */

import java.util.Collections;
import java.util.LinkedList;

public class GameBoard {
    protected final LinkedList<Node> listOfBugs = new LinkedList<Node>();  // array list of bug nodes
    private final Node[][] graph;                                          // array storing nodes
    private int rows, columns, endCode;                                    // fields to be used
    private Node tron;                                                     // store tron's node
    
    /**
     * Constructor for gameBoard
     * @param height    height of array
     * @param width     width of array
     */
    public GameBoard (int height, int width) {
        graph = new Node [height][width];
        rows = height;
        columns = width;
        endCode = 0;        // default end code
    }
    
    // getter and setter methods
    public int getHeight() { return rows; }
    
    public int getWidth() { return columns; }
    
    public int doneCheck() { return endCode; }
    
    protected void setCode(int c) { endCode = c; }
    
    /**
     * Method to insert a row from input file into array
     * @param index     the index of the row to insert into
     * @param toAdd     elements to insert into row
     */
    public void insertRow(int index, String[] toAdd) {
        for(int i = 0; i < toAdd.length; i++) {                 // for each element in toAdd
            graph[index][i] = new Node (index, i, toAdd[i]);    // create a node for it and insert at position
            
            if (i > 0) {                                            // condition for not first element in row
                graph[index][i].connectRight(graph[index][i - 1]);  // connect nodes horizontally
            }
            if (index > 0) {                                        // condition for not the only column in array
                graph[index][i].connectAbove(graph[index - 1][i]);  // connect nodes vertically
            }
            
            if (toAdd[i].equals("T")) {                                 // if node to add is tron 
                tron = graph[index][i];                                 // store tron's node
            } else if (!toAdd[i].equals("I") && !toAdd[i].equals("#")       // else if its is a bug
                                             && !toAdd[i].equals(" ")) {
                listOfBugs.add(graph[index][i]);                            // add bug node to list of bugs
            } else {
                continue;                                                   // else not bug or Tron continue
            }
        }
        Collections.sort(listOfBugs);                                       // sort bugs alphabetically for turn order
    }
    
    /**
     * Method to update Tron's location on the GameBoard
     * @param move  A string of what direction Tron is tring to move in
     * @return      a boolean saying if tron was able to move in the specified directiion or not
     *              False is tron can't move, True if Tron can
     */
    public boolean moveTron(String move) {
        
        if (!tron.getRight().getElement().equals(" ") && !tron.getRight().getElement().equals("I") 
                && !tron.getLeft().getElement().equals(" ") && !tron.getLeft().getElement().equals("I")
                && !tron.getUp().getElement().equals(" ") && !tron.getUp().getElement().equals("I")
                && !tron.getDown().getElement().equals(" ") && !tron.getDown().getElement().equals("I")) {    // condition tron is trapped
            endCode = 3;        // update end code
            return false;       // return tron could not move
        }
        
        switch (move) {
        case "u" : 
            if (!tron.getUp().getElement().equals(" ") && !tron.getUp().getElement().equals("I")) {  // condition tron's move is invalid
                return false;                                                                        // return false
            }
            tron.setElement(" ");                                                                    // mark current tron node as empty
            tron = tron.getUp();                                                                     // move to new tron node
            if (tron.getElement().equals("I")) {                                                     // if new tron node is tower
                endCode = 1;                                                                         // update end condition
            }
            tron.setElement("T");                                                                    // set new tron nodes element to be T for tron        
            break;                                                                                      
                                                                                                     // repeat above code for each direction tron can move in
        case "d" : 
            if (!tron.getDown().getElement().equals(" ") && !tron.getDown().getElement().equals("I")) {
                return false;
            }
            tron.setElement(" ");
            tron = tron.getDown();
            if (tron.getElement().equals("I")) {
                endCode = 1;
            }
            tron.setElement("T"); 
            break;
            
        case "l" : 
            if (!tron.getLeft().getElement().equals(" ") && !tron.getLeft().getElement().equals("I")) {
                return false;
            }
            tron.setElement(" ");
            tron = tron.getLeft();
            if (tron.getElement().equals("I")) {
                endCode = 1;
            }
            tron.setElement("T"); 
            break;
            
        case "r" : 
            if (!tron.getRight().getElement().equals(" ") && !tron.getRight().getElement().equals("I")) {
                return false;
            }
            tron.setElement(" ");
            tron = tron.getRight();
            if (tron.getElement().equals("I")) {
                endCode = 1;
            }
            tron.setElement("T"); 
            break;
        }
        return true;        // return tron was able to move
    }

    /**
     * Method to update bugs locations
     * @return  a string of the bugs moves as well as its direction moved and distance to tron
     */
    public String updateBugs () {
        Node destination, bugMove, bug;     // fields
        String output = "";                 // initialize out put string
        String moveDirection;               // used store direction bug moved
        
        for (int i = 0; i < listOfBugs.size(); i++) {   // for each bug
            StringBuilder sb = new StringBuilder();
            bug = listOfBugs.get(i);
            destination = breathFirst(bug);             // find shortest path to Tron
            if (destination == null) {                  // if no path was found
                output = output.concat(String.format("Bug %s: has no path to Tron.%n",  bug.getElement())); // error output
                continue;                                                                                   // let next bug move
            }
            
            bugMove = destination;                      // store path from Tron to bug
            moveDirection = findDirection(destination); // calculate which direction bug moved
            output = output.concat(String.format("Bug %s: %s %d", bug.getElement(), moveDirection, destination.getDistance())); // format output
            sb.append(String.format(")%d,%d( ", bugMove.getColumn(), bugMove.getRow()));    // format output
            
            while (bugMove.getParentInPath() != bug) {                                       // for each node in path add its location to ouput
                bugMove = bugMove.getParentInPath();
                sb.append(String.format(")%d,%d( ", bugMove.getColumn(), bugMove.getRow())); // add path movement to output
            }
            sb.append(String.format(")%d,%d( ", bug.getColumn(), bug.getRow()));             // output format
            output = output.concat(sb.reverse().toString() + "\n");                          // new line on output
            
            bugMove.setElement(bug.getElement());   // move bugs element to node it moved to
            bug.setElement(" ");                    // set old bugs node to be empty
            listOfBugs.remove(i);                   // remove old bug node from array
            listOfBugs.add(i, bugMove);             // add new bug node in its place
            if (destination.getDistance() == 1) {   // check for if bug ate Tron
                endCode = 2;                        // update end condition
                return output;                      // game over return
            }
        }
        return output;                              // return output
    }
    
    /**
     * Breath first graph search method used to find shortest path from bug to tron
     * @param start     node of bug to start at
     * @return          the node of tron with parent connections to starting bug
     *                  or Null if no path to T is found / is blocked
     */
    public Node breathFirst(Node start) {
        LinkedList<Node> path = new LinkedList<Node>();  // queue of nodes
        Node current;                                    // current node being operated on
        
        resetDiscoveries();                              // reset parent pointers and discoveries
        start.setDistance(0);                            // set distance from start node
        path.addLast(start);                             // add node to queue
        start.discovered();                              // discover node
        while (!path.isEmpty()) {                           // while the queue is not empty
            current = path.pop();                           // grab first node in queue
            if (current.getElement().equals("T")) {         // check if it is the node we want
                return current;                             // return the node if it is
            }
            if (!current.getUp().isDiscovered() && current.getUp() != null && (current.getUp().getElement().equals(" ")   // condition for valid bug/path move
                                                                            || current.getUp().getElement().equals("T")))  {
                current.getUp().setParentInPath(current);                       // set next nodes parent to current so we know how we got there
                path.addLast(current.getUp());                                  // add next node to queue
                current.getUp().discovered();                                   // mark next node as discovered
                current.getUp().setDistance(current.getDistance() + 1);         // update next nodes distance from start
            }                                                                   // repeat above code for each direction bug can move
            if (!current.getDown().isDiscovered() && current.getDown() != null && (current.getDown().getElement().equals(" ") 
                                                                                || current.getDown().getElement().equals("T"))) {
                current.getDown().setParentInPath(current);
                path.addLast(current.getDown());
                current.getDown().discovered();
                current.getDown().setDistance(current.getDistance() + 1);
            }
            if (!current.getLeft().isDiscovered() && current.getLeft() != null && (current.getLeft().getElement().equals(" ") 
                                                                                || current.getLeft().getElement().equals("T"))) {
                current.getLeft().setParentInPath(current);
                path.addLast(current.getLeft());
                current.getLeft().discovered();;
                current.getLeft().setDistance(current.getDistance() + 1);
            }
            if (!current.getRight().isDiscovered() && current.getRight() != null && (current.getRight().getElement().equals(" ") 
                                                                                  || current.getRight().getElement().equals("T"))) {
                current.getRight().setParentInPath(current);
                path.addLast(current.getRight());
                current.getRight().discovered();
                current.getRight().setDistance(current.getDistance() + 1);
            }
        }    
        return null;    // no path found to T
    }
    
    /**
     * Method used to figure out which direction the bug moved
     * @param bugPath   Tron node with links to parent bug
     * @return          a string telling which direction the bug moved
     */
    public String findDirection(Node bugPath) {
        
        while (bugPath.getParentInPath().getParentInPath() != null) { // loop to node just before bug
            bugPath = bugPath.getParentInPath();
        }
        
        int rowDisplace = bugPath.getRow() - bugPath.getParentInPath().getRow();;         // find difference in bugs row movement
        int columnDisplace = bugPath.getColumn() - bugPath.getParentInPath().getColumn(); // find difference in bugs column movement
        
        if (columnDisplace == 1) {          // if bug moved right
            return "r";
        } else if (columnDisplace == -1) {  // if bug moved left
            return "l";
        } else if (rowDisplace == 1) {      // if bug moved down
            return "d";
        } else {                            // else bug moved up
            return "u";
        }
    }
    
    /**
     * Method to reset nodes parents and discovery status for next search
     */
    public void resetDiscoveries() {
        for (int i = 0; i < graph.length; i++) {
            for (int k = 0; k < graph[i].length; k++) {
                graph[i][k].resetDiscovery();           // mark node as not discovered
                graph[i][k].resetParent();              // reset pointer to parent in path
            }
        }
    }
    
    /**
     * MNethod used for HW6 output to print one row at a time
     * @param rowIndex     row to print out
     * @return             a string of the row in the correct format 
     */
    public String printRow(int rowIndex) {
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < columns; i++) {
            output.append(graph[rowIndex][i]);
        }
        return output.toString();               // return output
    }
}
