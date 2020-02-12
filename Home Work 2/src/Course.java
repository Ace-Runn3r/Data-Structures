/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Class file for creating course Objects
 */

import java.util.LinkedList;

public final class Course {
    protected final String courseName;                                            // Name of the course
    protected LinkedList<String> listOfTimes = new LinkedList<String>();          // Array of section times
    protected final String sectionTime;                                           // field for there is only one known time

    /**
     * Constructor for class name only
     * @param courseInput   the name of the course
     */
    Course (final String courseInput) {
        courseName = courseInput;
        sectionTime = null;
    }

    /**
     * Constructor for brute force method that grabs a single section time
     *      and the name of the course to add to the schedule list for
     *      possible schedules
     * @param courseInput   name of the course
     * @param time  the time of the section
     */
    Course (final String courseInput, final String time) {
        courseName = courseInput;
        sectionTime = time;
    }

    /**
     * Method for adding section times to the list of times
     * Used in the main method for file input
     * @param time  section time to add to list of times
     */
    public void addTime (final String time) {
        listOfTimes.add(time);
    }
}
