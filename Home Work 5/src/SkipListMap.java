/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Skip list used to store time and activities
               for easier searching
 */

import java.util.ArrayList;

public class SkipListMap {
    
    private static final ArrayList<DoublyLinkedList<Activity>> LEVELS    // array list of skip list levels
                   = new ArrayList<DoublyLinkedList<Activity>>();
    FakeRandomHeight randHeight = new FakeRandomHeight();   // classed used to give heights for node towers
    private int listHeight;                                 // number of lists in skip list
    private static final int posInf = 99999999;             // special right end node
    private static final int negInf = 0;                    // special left end node
    
    /**
     * Constructor
     */
    SkipListMap () {
        listHeight = -1;            // set height to -1 since no lists yet
        LEVELS.add(addLevel());     // add list to skip list for at least one empty list
    }
    
    /**
     * Method to get activity in skip list
     * Returns null if not found
     * @param time  time to look for in list
     * @return  null if activity is not found
     *          or Activity
     */
    public Activity get(int time) {
        DoublyLinkedList.Node<Activity> node = search(time);            // search for node with key equal to time
        if ((node == null) || (node.getElement().getTime() != time)) {  // check if times match
            return null;                                                // not found
        } else {
            return search(time).getElement();                           // return found activity
        }
    }
    
    /**
     * Method to insert node and create tower of "random" height
     * @param key           time of activity used as key
     * @param activity      name of activity used as value
     * @return              null if node is added
     *                      or activity for error output if node 
     *                      exists that already contains key
     */
    public Activity put (int key, String activity) {
        DoublyLinkedList.Node<Activity> entryPos = search(key);     // find position to add new node
        if (entryPos.getElement().getTime() == key) {               // check if key is already used
            return entryPos.getElement();                           // return node for error output
        }
        Activity entry = new Activity(key, activity);               // create activity to add to list
        DoublyLinkedList.Node<Activity> lowerNode = null;           // create a temp variable used  to connect levels if needed
        int height = randHeight.get();                              // grab a pseudo random height to use for tower height
        int currentHeight = 0;                                      // current height of tower
        while ((currentHeight) <= height) {
            if (currentHeight >= listHeight) {                      // check if skip list needs a new empty list at top level
                LEVELS.add(addLevel());                             // add new level if needed
            }
            LEVELS.get(currentHeight).insertBetween(entry, entryPos, entryPos.getNext());   // insert node into current list level
            if (currentHeight > 0) {                                                        // if more than one level
                LEVELS.get(currentHeight).connectLevels(lowerNode, entryPos.getNext());     // connect upper tower node to lower
            }
            lowerNode = entryPos.getNext();                                                 // store lower tower node or new lower tower node
            while (!entryPos.getElement().getActivity().equals("negInf")                    // while the current pos has no node above it
                 && entryPos.getAbove() == null) {                                          // move back to find one
                entryPos = entryPos.getPrev();                                              // move back in list
            }
            entryPos = entryPos.getAbove();                                                 // move up next skip list level
            currentHeight++;                                                                // move up to next skip list level for node tower
        }
        return null;
    }
    
    /**
     * Method to remove a node and tower from skip list
     * @param time      time of activity to remove
     * @return          null if there is no activity with time
     *                  or the activity that was removed
     */
    public Activity remove (int time) {
        DoublyLinkedList.Node<Activity> position = search(time);    // position of node to be removed
        if (position.getElement().getTime() != time) {              // check if nodes time matches time being searched for
            return null;                                            // not found
        } else {
            Activity removedEntry = position.getElement();          // store removed activity four return
            int listIndex = 0;                                      // list level to operate on
            while (position.getAbove() != null) {                   // while node has a node about it in list
                LEVELS.get(listIndex).removeNode(position);         // remove node from list
                position = position.getAbove();                     // iterate up list
                listIndex++;                                        // operate on next list level
            }
            LEVELS.get(listIndex).removeNode(position);             // remove highest node in tower
            while ((listHeight >= 1) && (LEVELS.get(listHeight).size() == 2 && LEVELS.get(listHeight - 1).size() == 2)) {  // condition for removing extra empty list at top
                LEVELS.remove(listHeight);                          // remove highest level;
                listHeight--;                                       // lower higher variable
            }
            return removedEntry;                                    // return removed activity
        }
    }
    
    /**
     * Method to return a submap between two specified time keys
     * @param time1     lower bound of time keys
     * @param time2     upper bound of time keys
     * @return          list of nodes that contain keys between specified keys
     *                  or null if none found
     */
    public DoublyLinkedList<DoublyLinkedList.Node<Activity>> subMap (int time1, int time2) {
        DoublyLinkedList.Node<Activity> startPosition = search(time1);      // find node with key closed to time1 without going over
        DoublyLinkedList.Node<Activity> endPosition = search(time2);        // find node with key closed to time2 without going over
        if (startPosition.getElement().getTime() < time1) {                 // special condition if start position would grab a node before start time
            startPosition = startPosition.getNext();
        } if (time1 == 0) {                                                 // special condition for if negInf special node is grabbed
            startPosition = startPosition.getNext();
        }
        DoublyLinkedList<DoublyLinkedList.Node<Activity>> subMap            // create list of nodes between keys
                    = LEVELS.get(0).getBetween(startPosition, endPosition);
        if (subMap.size() == 2) {                                           // if map only contains special nodes it is empty
            return null;
        } else {
            return subMap;
        }
    }
    
    /**
     * Method to print out skip list levels and nodes
     */
    public void printSkipList() {
        for (int i = listHeight; i >= 0; i--) {
            System.out.printf("(S%d)%s%n", i, LEVELS.get(i));   // output list. Uses doublelinkedlist toString()
        }
    }
    
    /**
     * Method to create a new level for the skip list
     * @return      new empty level to add to skip list LEVELS
     */
    private DoublyLinkedList<Activity> addLevel() {
        DoublyLinkedList<Activity> newLevel = new DoublyLinkedList<Activity>();     // create list for new level
        Activity node = new Activity(negInf, "negInf");                             // create left end special node
        newLevel.addFirst(node);                                                    // add left end special node
        node = new Activity(posInf, "posInf");                                      // create right end special node
        newLevel.addLast(node);                                                     // add right end special node
        if (listHeight != -1) {                                                                     // if there is more than one level
            LEVELS.get(listHeight).connectLevels(LEVELS.get(listHeight).get(0), newLevel.get(0));   // connect left and right end special nodes
            LEVELS.get(listHeight).connectLevels(LEVELS.get(listHeight).get(1), newLevel.get(1));   // on current and lower level
        }
        listHeight++;                                                               // increment height of skip list
        return newLevel;                                                            // return new list to add
    }
    
    /**
     * Method to find node that contains key
     * @param key     key to find in skip list
     * @return        node on lowest level of skip list with key
     *                or closest node with value smaller than key
     */
    private DoublyLinkedList.Node<Activity> search(int key) {
        DoublyLinkedList.Node<Activity> current = LEVELS.get(listHeight).get(0);    // current node being checked
        while (current.getBelow() != null) {            // while there is a node below current
            current = current.getBelow();               // go down
            while (current.getNext().getElement().getTime() <= key)  {  // while next key is =< key 
                current = current.getNext();                           // go to next key
            }
        }
        return current; // exit when can't go down and next key would be larger than key being searched for and return current
    }
}
