/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
//package net.datastructures;

/**
 * A basic doubly linked list implementation.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class DoublyLinkedList<E> {

  //---------------- nested Node class ----------------
  /**
   * Node of a doubly linked list, which stores a reference to its
   * element and to both the previous and next node in the list.
   */
  protected static class Node<E> {

    /** The element stored at this node */
    private E element;               // reference to the element stored at this node

    /** A reference to the preceding node in the list */
    private Node<E> prev;            // reference to the previous node in the list

    /** A reference to the subsequent node in the list */
    private Node<E> next;            // reference to the subsequent node in the list
    
    /** A reference to the above node in the list */
    private Node<E> above;           // reference to the above node in the skip list

    /** A reference to the below node in the list */
    private Node<E> below;           // reference to the below node in the skip list
    
           // reference to the below node in the skip list

    /**
     * Creates a node with the given element and next node.
     *
     * @param e  the element to be stored
     * @param p  reference to a node that should precede the new node
     * @param n  reference to a node that should follow the new node
     */
    public Node(E e, Node<E> p, Node<E> n) {
      element = e;
      prev = p;
      next = n;
    }

    // public accessor methods
    /**
     * Returns the element stored at the node.
     * @return the element stored at the node
     */
    public E getElement() { return element; }

    /**
     * Returns the node that precedes this one (or null if no such node).
     * @return the preceding node
     */
    public Node<E> getPrev() { return prev; }

    /**
     * Returns the node that follows this one (or null if no such node).
     * @return the following node
     */
    public Node<E> getNext() { return next; }
    
    /**
     * Returns the node above this one (or null if no such node).
     * @return the above node
     */
    @SuppressWarnings("unused")
    public Node<E> getAbove() { return above; }
    
    /**
     * Returns the node below this one (or null if no such node).
     * @return the below node
     */
    @SuppressWarnings("unused")
    public Node<E> getBelow() { return below; }
    

    // Update methods
    /**
     * Sets the node's previous reference to point to Node n.
     * @param p    the node that should precede this one
     */
    public void setPrev(Node<E> p) { prev = p; }

    /**
     * Sets the node's next reference to point to Node n.
     * @param n    the node that should follow this one
     */
    public void setNext(Node<E> n) { next = n; }
    
    /**
     * Sets the node's above reference to point to Node a.
     * And the below reference of a to this node
     * @param a
     */
    public void setAbove(Node<E> a) { 
        this.above = a;
        a.below = this;
    }

    @Override
    public String toString() {
        return element.toString();  // outfut formart for node
    }

  } //----------- end of nested Node class -----------

  // instance variables of the DoublyLinkedList
  /** Sentinel node at the beginning of the list */
  private Node<E> header;                    // header sentinel

  /** Sentinel node at the end of the list */
  private Node<E> trailer;                   // trailer sentinel

  /** Number of elements in the list (not including sentinels) */
  private int size = 0;                      // number of elements in the list

  /** Constructs a new empty list. */
  public DoublyLinkedList() {
    header = new Node<>(null, null, null);      // create header
    trailer = new Node<>(null, header, null);   // trailer is preceded by header
    header.setNext(trailer);                    // header is followed by trailer
  }

  // public accessor methods
  /**
   * Returns the number of elements in the linked list.
   * @return number of elements in the linked list
   */
  public int size() { return size; }

  /**
   * Tests whether the linked list is empty.
   * @return true if the linked list is empty, false otherwise
   */
  public boolean isEmpty() { return size == 0; }

  /**
   * Returns (but does not remove) the first element of the list.
   * @return element at the front of the list (or null if empty)
   */
  public E first() {
    if (isEmpty()) return null;
    return header.getNext().getElement();   // first element is beyond header
  }

  /**
   * Returns (but does not remove) the last element of the list.
   * @return element at the end of the list (or null if empty)
   */
  public E last() {
    if (isEmpty()) return null;
    return trailer.getPrev().getElement();    // last element is before trailer
  }

  // public update methods
  /**
   * Adds an element to the front of the list.
   * @param e   the new element to add
   */
  public void addFirst(E e) {
    addBetween(e, header, header.getNext());    // place just after the header
  }

  /**
   * Adds an element to the end of the list.
   * @param e   the new element to add
   */
  public void addLast(E e) {
    addBetween(e, trailer.getPrev(), trailer);  // place just before the trailer
  }
  
  /**
   * Insets a node between two nodes
   * @param e           element to add
   * @param predecessor preceding node
   * @param successor   succeeding node
   */
  public void insertBetween(E e, Node<E> predecessor, Node<E> successor) {
      addBetween(e,  predecessor, successor);   // place between node
  }
  
  /**
   * Removes a node from the list
   * @param node    node to be removed
   */
  public void removeFromList(Node<E> node) {
      remove(node);     // remove from list
  }
  
  /**
   * Connects the layers of the skip list
   * @param lowerNode   upper node to connect to
   * @param upperNode   lower node to connect to
   */
  public void connectLevels(Node<E> lowerNode, Node<E> upperNode) {
      lowerNode.setAbove(upperNode);    // connect nodes
  }

  /**
   * Removes and returns the first element of the list.
   * @return the removed element (or null if empty)
   */
  public E removeFirst() {
    if (isEmpty()) return null;                  // nothing to remove
    return remove(header.getNext());             // first element is beyond header
  }

  /**
   * Removes and returns the last element of the list.
   * @return the removed element (or null if empty)
   */
  public E removeLast() {
    if (isEmpty()) return null;                  // nothing to remove
    return remove(trailer.getPrev());            // last element is before trailer
  }
  
  /**
   * Retrieves a node within the array
   * @param index   position of node to find
   * @return        node at index in array
   */
  public Node<E> get (int index) {
      Node<E> position = new Node<E>(null, null, null);     // create node to store
      position = header.getNext();                          // start at beginning
      for (int i = 0; i < index; i++) {
          position = position.getNext();                    // iterate to index
      }
      return position;                                      // return node
  }
  
  /**
   * Create a sub list of desired nodes
   * @param start   node to start sublist at
   * @param end     node to end sublist at
   * @return    A sublist between start and end nodes
   */
  public DoublyLinkedList<Node<E>> getBetween(Node<E> start, Node<E> end) {
      DoublyLinkedList<Node<E>> listOfNodes = new DoublyLinkedList<Node<E>>();  // create sub list
      Node<E> current = start;                                                  // set current node for iteration
      listOfNodes.addFirst(header.getNext());                                   // add +inf to list
      while (current != end.getNext() && current != trailer) {                  // while between start and end node
          listOfNodes.addLast(current);                                         // add current to list
          current = current.getNext();                                          // iterate
      }
      listOfNodes.addLast(trailer.getPrev());                                   // add -inf to list
      return listOfNodes;
  }
  
  /**
   * Method to remove a node from the list
   * @param position    node to be removed
   */
  public void removeNode(Node<E> position) {
      remove(position);     // remove node
  }

  // private update methods
  /**
   * Adds an element to the linked list in between the given nodes.
   * The given predecessor and successor should be neighboring each
   * other prior to the call.
   *
   * @param predecessor   node just before the location where the new element is inserted
   * @param successor     node just after the location where the new element is inserted
   */
  private void addBetween(E e, Node<E> predecessor, Node<E> successor) {
    // create and link a new node
    Node<E> newest = new Node<>(e, predecessor, successor);
    predecessor.setNext(newest);
    successor.setPrev(newest);
    size++;
  }

  /**
   * Removes the given node from the list and returns its element.
   * @param node    the node to be removed (must not be a sentinel)
   */
  private E remove(Node<E> node) {
    Node<E> predecessor = node.getPrev();
    Node<E> successor = node.getNext();
    predecessor.setNext(successor);
    successor.setPrev(predecessor);
    size--;
    return node.getElement();
  }
  
   /**
   * Produces a string representation of the contents of the list.
   * Modified for HW5 ouput
   * @return    output sting
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    boolean isEmpty = true;                     // boolean to check if list is empty
    Node<E> walk = header.getNext().getNext();  // start at first non special node
    while (walk != trailer.getPrev()) {         // loop through non special nodes
      sb.append(walk.toString());               // append node to stringbuilder
      walk = walk.getNext();                    // iterate
      isEmpty = false;                          // since non special node found list not empty
    }
    if (isEmpty) {                              // special output for empty list
        sb.append(" empty");
    }
    return sb.toString();
  }

} //----------- end of DoublyLinkedList class -----------