/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Key object for HW4 heaps
 */

public class Key implements Comparable<Key> {
    private Double price;  // price of coin
    private Integer time;   // time order placed
    
    /**
     * Default constructor
     */
    Key () {}
    
    /**
     * Constructor for every field
     * @param inputPrice
     * @param timeStamp
     */
    Key (Double inputPrice, Integer timeStamp) {
        price = inputPrice;
        time = timeStamp;
    }

    /**
     * Method used to compare price and time to find priority
     */
    public int compareTo(Key other) {
        if (this.price.compareTo(other.price) == 0) { // if price is the same sort by time
            return this.time.compareTo(other.time);   // sort by time
        } else {
            return this.price.compareTo(other.price); // sort by price
        }
    }

}
