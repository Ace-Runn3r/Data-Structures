/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Class used to create nodes for Tree.java
 */

import java.util.ArrayList;

public class Node<E extends Comparable<E>> {

    protected Node<E> parent;       // Stores pointer to parent node
    protected E element;
    protected ArrayList<Node<E>> children = new ArrayList<Node<E>>();   // list of pointer to child nodes

    public Node() { }   // default constructor

    /**
     * Constructor for element input
     * @param e   element to be stored in node
     */
    public Node(final E e) {
        element = e;
     }

    /**
     * Compares the elements of two nodes for sorting.
     * Uses String compareTo inside method
     * @param other   node to be compared to
     * @return        value == 0 if elements are the same.
     *                < 0 if this.element comes first and.
     *                > 0 if other.element comes first.
     */
    public int compareTo (Node<E> other) {
        return this.element.compareTo(other.element);
    }
}
