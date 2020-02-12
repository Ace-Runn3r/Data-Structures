/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Object used to store time key and activity name for 
               HW5 skip list
 */

public class Activity implements Comparable<Activity>{
    
    private int time;               // time of activity used as key
    private String activity;        // name of activity
    
    /**
     * Default constructor
     */
    public Activity () {}
    
    /**
     * Constructor for all fields
     * @param t     time of activity
     * @param a     name of activity
     */
    public Activity (int t, String a) {
        time = t;
        activity = a;
    }
    
    // getter methods
    public int getTime() { return time; }
    
    public String getActivity() { return activity; }

    /**
     * compare method returns -1 if first time come first zero if they are the same
     * and 1 if the latter time is larger
     */
    @Override
    public int compareTo(Activity other) {
        if (this.time < other.time) {
            return -1;
        } else if (this.time > other.time) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format(" %d:%s", time, activity); // output format
    }
}
