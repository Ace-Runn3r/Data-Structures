/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Class to create a tree with no limit
               on children.
 */

import java.util.ArrayList;
import java.util.LinkedList;

public class Tree<E extends Comparable<E>> {
    private Node<E> root = null;                                // root node                     

    /**
     * Getter method for root node
     * @return  root node
     */
    public Node<E> getRoot () { 
        return root;
    }
    
    /**
     * Method to get children nodes of a node
     * @param parent    parent node of whose children we want
     * @return          the array of parent's children
     */
    public ArrayList<Node<E>> getChildren (Node<E> parent) {
        return parent.children;
    }
    
    /**
     * Method to get parent node of a node
     * @param child    child node of whose parent we want
     * @return         parent node
     */
    public Node<E> getParent (Node<E> child) {
        return child.parent;
    }

    /**
     * Method to create nodes from a generic types and add them to tree
     * @param parent    parent of child node to add
     * @param child     child node to add to tree
     */
    public void add (E parent, E child) {
        if (root == null) {                                         // case for first node added to tree
            Node<E> newParent = new Node<>(parent);                 // create a node for the parent
            root = newParent;                                       // set root as first parent added
        }
        Node<E> newChild = new Node<>(child);                       // create node for child
        Node<E> parentNode = getNode(parent);                       // find the parent node containing parent argument
        insertChild(parentNode, newChild);                          // call to insertChild method
    }

    /**
     * Method to add child to parent in lexicographic order with
     *      respect to parent's children
     * @param parent         parent node to add child node too
     * @param childToAdd     child node to add to tree
     */
    public void insertChild (Node<E> parent, Node<E> childToAdd) {
        childToAdd.parent = parent;                                 // link child to parent
        for (int i = 0; i < parent.children.size(); i++) {          // loop through parent's children
            if (childToAdd.compareTo(parent.children.get(i)) < 0) {     // check if child comes before current child lexicographically
                parent.children.add(i, childToAdd);                     // if it does add it to list at current position
                return;                                                 // exit method because child was inserted
            }
        }
        parent.children.add(childToAdd);                            // if child does not fit in from of anything in
    }                                                               // parent's children list add to end

    /**
     * Method to add child to parent's list of children in non 
     *      lexicographic order
     * @param parent         parent node to add child node too
     * @param childToAdd     child node to add to tree
     */
    public void appendChild (Node<E> parent, Node<E> childToAdd) {
        childToAdd.parent = parent;                                 // link child to parent
        parent.children.add(childToAdd);                            // add child to list of parent's children
    }
       
    /**
     * Method to find a node based on a generic input using level order traversal
     * @param nodeToFind    the primitive type to find in a node inside the tree
     * @return              node containing primitive type
     */
    public Node<E> getNode (E nodeToFind){
        LinkedList<Node<E>> queue = new LinkedList<Node<E>>();      // create queue to add nodes to
        queue.add(root);
        while(!queue.isEmpty()) {
            Node<E> current = queue.remove(0);                      // dequeue and store value in current
            if (current.element.equals(nodeToFind)) {               // check if current is the node needed
                return current;                                     // if yes return node
            }
            queue.addAll(current.children);                         // else add children of current to queue
        }
        return null;                                                // element not found
    }
    
    /**
     * Method to use recursive post order search to find nodes containing an element
     * @param elementToFind    Athlete name to look for
     * @param node             Node to start the search from
     * @param queryList        Stores nodes containing athlete name
     * @return                 returns queryListt containing nodes containing athlete name
     */
    public ArrayList<Node<E>> postOrderSearch (E elementToFind, Node<E> node, ArrayList<Node<E>> queryList) {
        if (node.children.size() == 0) {                            // base case if leaf found
            return queryList;
        }
        for (int i = 0; i < node.children.size(); i++) {            // go to every inner node in tree
            postOrderSearch (elementToFind, node.children.get(i), queryList);   // recur down each possible child of node
            if (node.children.get(i).element.equals(elementToFind)) {           // check if node contains element being looked for
                queryList.add(node.children.get(i));                            // if so add to query list
            }
        }
        return queryList;
    }
    
    /**
     * Method to find a node based on sport and event nodes
     * @param sport     name of sport node
     * @param event     name of event node inside sport node
     * @return          node of event being looked for
     */
    @SuppressWarnings("unused")
    public Node<E> nodeBySportEvent (String sport, String event) {
        Node<E> current = root;                                     // current node stores node being checked
        for (Node<E> node : root.children) {                        // loops through sports nodes
            if (node.element.equals(sport)) {                       // if nodes contains sport being looked for
                current = node;                                     // set current to node containing sport
                for (Node<E> nextNode : node.children) {                // loops through sport nodes children
                    if (nextNode.element.equals(event)) {               // if child node contains event name
                        return current = nextNode;                      // return found node
                    }
                }
            }
        }
        return null;                                                // node not found
    }
}
