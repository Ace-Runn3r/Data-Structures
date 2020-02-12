/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Program that takes in an activity and time and sort
               the activity based on time inside a skip list.
 */

import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Paths;

public class HW5 {
    
    private static final SkipListMap SkipList = new SkipListMap();      // the skip list storing input for inquiry
    
    /**
     * Method to insert an activity into skiplist
     * @param time          time of activity to insert
     * @param activity      activity to insert
     */
    public static void insertActivity (int time, String activity) {
        Activity entry = SkipList.put(time, activity);                                  // insert activity into list
        System.out.printf("AddActivity %d %s", time, activity);                         // output
        if (entry != null) {                                                            // if time already has an activity
            System.out.printf(" [ExistingActivityError:%s]%n", entry.getActivity());    // output error
        } else {
            System.out.println();                                                       // output
        }
    }
    
    /**
     * Method to find and print an activity from the list
     * output none if no activity at time
     * @param time  time of activity to get
     */
    public static void getActivity (int time) {
        Activity entry = SkipList.get(time);                // get activity from list
        System.out.printf("GetActivity %d", time);          // output
        if (entry == null) {                                // no activity at time
            System.out.println(" none");                    // output
        } else {
            System.out.println(" " + entry.getActivity());  // output
        }
    }
    
    /**
     * Method to remove and print removed activity from the list
     * Error output if no activity at time
     * @param time  time of activity to remove
     */
    public static void removeActivity (int time) {
        Activity entry = SkipList.remove(time);                 // remove activity from list
        System.out.printf("RemoveActivity %d", time);           // output
        if (entry == null) {                                    // no activity
            System.out.println(" NoActivityError");             // output error
        } else {
            System.out.println(" " + entry.getActivity());      // output
        }
    }
    
    /**
     * Method to retrieve and print list of activities on a specific date
     * or none if no activities
     * @param date  date to look for activities on
     */
    public static void getActivityByDay (int date) {
        date = date * 10000;            // add zeros for HHmm
        int endOfDay = date + 2359;     // create time for end of day
        DoublyLinkedList<DoublyLinkedList.Node<Activity>> subMap  // create list of activities on day
                                = SkipList.subMap(date, endOfDay);
        System.out.printf("GetActivitiesForOneDay %04d", date/10000);   // output
        if (subMap == null) {               // no activities
            System.out.println(" none");    // output
        } else {
            System.out.println(subMap);     // output based on linkedlist toString();
        }
    }
  
    /**
     * Method to retrieve and print activities between start of day 
     * and specified time or none if there are activities
     * @param time  time to look for activities before
     */
    public static void getEarlierActivity (int time) {
        int lowerCutOff = (time / 10000) * 10000;   // Truncate time to just date then fill HHmm with zeros
        DoublyLinkedList<DoublyLinkedList.Node<Activity>> subMap     // get activities between beginning of day and time
                              = SkipList.subMap(lowerCutOff, time); 
        System.out.printf("GetActivitiesFromEarlierInTheDay %08d", time);
        if (subMap == null) {               // no activities
            System.out.println(" none");    // output
        } else {
            System.out.println(subMap);     // output based on linkedlist toString();
        }
    }
    
    /**
     * Method to retrieve and print activities between times or 
     * none if there are activities
     * @param startTime     start time of activity range
     * @param endTime       end time of range
     */
    public static void activityBetween (int startTime, int endTime) {
        DoublyLinkedList<DoublyLinkedList.Node<Activity>> subMap = SkipList.subMap(startTime, endTime);     // create list of activities between times
        System.out.printf("GetActivitiesBetweenTimes %08d %08d", startTime, endTime);                       // output
        if (subMap == null) {               // no activities
            System.out.println(" none");    // output
        } else {
            System.out.println(subMap);     // output based on linkedlist toString();
        }
    }
    
    /**
     * Method to output skiplist
     */
    public static void printSkipList () {
        System.out.println("PrintSkipList");    // output
        SkipList.printSkipList();               // output formatted through toString();
    }
    
    public static void main (String[] args) throws IOException {
        
        final Scanner fileInput = new Scanner(Paths.get(args[0]));  // scanner for file
        int date, time, endTime;                                    // variables to be used
        String activity;
        
        while (fileInput.hasNext()) {
            switch(fileInput.next()) {
            case "AddActivity" :
                time = fileInput.nextInt();         // store time
                activity = fileInput.next();        // store activity
                insertActivity(time, activity);     // add activity to list
                break;
            case "RemoveActivity" :
                time = fileInput.nextInt();         // store time
                removeActivity(time);               // remove activity from list
                break;
            case "GetActivity" :
                time = fileInput.nextInt();         // store time
                getActivity(time);                  // get activity with matching time
                break;
            case "GetActivitiesBetweenTimes" :
                time = fileInput.nextInt();         // store start time
                endTime = fileInput.nextInt();      // store end time
                activityBetween(time, endTime);     // get activities between two times
                break;
            case "GetActivitiesForOneDay" :
                date = fileInput.nextInt();         // store date
                getActivityByDay(date);             // get activities from date
                break;
            case "GetActivitiesFromEarlierInTheDay" :
                time = fileInput.nextInt();         // store time
                getEarlierActivity(time);           // get activities from date before HHmm time
                break;
            case "PrintSkipList" :
                printSkipList();                    // output skiplist
                break;
            }
        }
        fileInput.close();
    }
}
