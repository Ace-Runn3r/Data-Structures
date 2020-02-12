/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Node class used by GameBoard to connect graph
 */

public class Node implements Comparable<Node>{
    private Node up = null;                 // pointer to above node
    private Node down = null;               // pointer to below node
    private Node left = null;               // pointer to left node
    private Node right = null;              // pointer to right node
    private Node parentInPath = null;       // parent node when finding path
    private int row;                        // row in 2D array
    private int column;                     // column in 2D array
    private int distance = 0;               // distance from root node in path
    private String element;                 // element of node
    private boolean discovered = false;     // status of node discovery
    
    /**
     * Constructor for node
     * @param r     row index in array
     * @param c     column index in array
     * @param e     element of node
     */
    public Node (int r, int c, String e) {
        row = r;
        column = c;
        element = e;
    }
    
    // Getter and setter methods
    public int getRow() { return row; }
    
    public int getColumn() { return column; }
    
    public Node getUp() { return up; }

    public void setUp(Node up) { this.up = up; }

    public Node getDown() { return down; }

    public void setDown(Node down) { this.down = down; }

    public Node getLeft() { return left; }

    public void setLeft(Node left) { this.left = left; }

    public Node getRight() { return right; }

    public void setRight(Node right) { this.right = right; }
    
    public Node getParentInPath() { return parentInPath; }

    public void setParentInPath(Node parentInPath) { this.parentInPath = parentInPath; }
    
    public String getElement() { return element; }
    
    public void setElement (String e) { element = e; }
    
    public int getDistance() { return distance; }

    public void setDistance(int distance) { this.distance = distance; }

    public boolean isDiscovered() { return discovered; }
    
    public void discovered() { discovered = true; }
    
    public void resetDiscovery() { discovered = false; }
    
    public void resetParent() { parentInPath = null; }
    
    /**
     * Connects two nodes horizontally
     * @param other     the node to connect this one to
     */
    public void connectRight(Node other) {
        this.setLeft(other);
        other.setRight(this);
    }
    
    /**
     * Connects two nodes vertically
     * @param other     the node to connect this one to
     */
    public void connectAbove(Node other) {
        this.setUp(other);
        other.setDown(this);
    }

    @Override
    public String toString() {
        return element;
    }

    @Override
    public int compareTo(Node other) {
        return this.getElement().compareTo(other.getElement());
    }
}
