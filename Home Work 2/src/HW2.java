/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: A program that finds all possible class schedules
               using recursion based on course name and section
               times. Preference is given to courses higher up on
               the list and section time closer to the list in
               event of conflict
 */

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

public final class HW2 {
    private HW2 () {}

    public static final LinkedList<Object> POSSIBLE = new LinkedList<Object>();     // List to store combinations of class schedules

    /**
     * Recursive method that will create every combination of class and section times possible
     * @param courses   Linked List of Course objects that contain names and times of classes
     * @param schedule  Linked list to place possible schedules for cloning into POSSIBLE list
     * @param i         used to keep track of the index of for courses array
     */
    public static void bruteForce (final LinkedList<Course> courses,
                                   final LinkedList<Course> schedule, final int i) {
        if (i == courses.size()) {                                                  // base case if index has hit the bottom of a recursion tree
            POSSIBLE.add(schedule.clone());                                         // Add possible schedule to list
        } else {
            for (int k = 0; k < courses.get(i).listOfTimes.size(); k++) {           // parse through list of times for course [i] in array
                schedule.add(new Course (courses.get(i).courseName,                 // create a Course object based on
                                         courses.get(i).listOfTimes.get(k)));
                bruteForce (courses, schedule, i + 1);                              // moves down recursive tree
                schedule.removeLast();                                              // moves back up recursive tree after base case
            }
        }
    }

    /**
     * Method to search the list of POSSIBLE list and store the schedule allowing the most classes with preference in mind
     * @param courses   list of courses with sections times in preference order to compare schedule to
     * @param schedule  list used to store best schedule for display method
     */
    @SuppressWarnings("unchecked")
    public static void bestSchedule (final LinkedList<Course> courses,
                                           LinkedList<Course> schedule) {
        int numOfCourses = 0;                                           // current schedule's number of courses
        int numOfConflicts = 0;                                         // current schedule's number of conflicts
        int leastConflicts = Integer.MAX_VALUE;                         // least conflicts
        int mostCourses = 0;                                            // most Courses
        int qualityPoints = 0;                                          // Score for how close schedule is to preference
        int mostPoints = 0;                                             // highest score so far
        LinkedList<Course> tempSchedule;                                // temporary schedule for testing how good it is

        for (int i = 0; i < POSSIBLE.size(); i++) {                     // keep track of position inside list of possible schedules
            tempSchedule = (LinkedList<Course>) POSSIBLE.get(i);        // store schedule
            for (int k = 0; k < courses.size(); k++) {                                      // keep track of course inside schedule
                if (!tempSchedule.get(k).sectionTime.equals("")) {                          // check if there is a section time
                    numOfCourses++;
                }
                for (int j = 0; j < courses.size(); j++) {                                                      // parse and check for time conflicts
                    if ((tempSchedule.get(k).sectionTime.equals(tempSchedule.get(j).sectionTime)) && (j != k)   // check if current course time being tested
                                                            && !tempSchedule.get(j).sectionTime.equals("")) {   //     conflicts with any other courses
                        numOfConflicts++;
                    }
                }
            }
            if ((numOfCourses >= mostCourses) && (numOfConflicts <= leastConflicts)) {      // first condition for best schedule
                qualityPoints = compare(courses, tempSchedule);                             // check how close schedule is to preference
                if (qualityPoints > mostPoints || numOfCourses > mostCourses) {             // second condition for best schedule
                    schedule = (LinkedList<Course>) POSSIBLE.get(i);                        // save new best schedule
                    mostPoints = qualityPoints;                                             // save most points
                }
                leastConflicts = numOfConflicts;                                            // set new least conflict value
                mostCourses = numOfCourses;                                                 // set new most courses value
            }
            numOfCourses = 0;                                                               // reset all values for next test
            numOfConflicts = 0;
            qualityPoints = 0;
        }

        display(courses, schedule);                                     // display schedule and conflicts in output format
    }

    /**
     * Method for displaying formatted output to screen
     * @param shedule      The list of courses and the times that allow for the most classes with preference
     * @param courses      The list of courses used to get times for classes missing from schedule
     *                     Classes not in schedule have conflicts
     */
    public static void display (final LinkedList<Course> courses,
                                final LinkedList<Course> schedule) {
        System.out.println("---Course schedule---");
        for (final Course s: schedule) {                                          // output for final schedule
            if (!s.sectionTime.equals("")) {                                      // if class has no time it's a conflict
                System.out.printf("%s %s%n", s.courseName, s.sectionTime);
            }
        }

        System.out.println("---Courses with a time conflict---");
        for (int i = 0; i < schedule.size(); i++) {                               // output for conflicts
            if (schedule.get(i).sectionTime.equals("")) {                         // condition for conflicting course
                System.out.print(courses.get(i).courseName);
                for (int k = 1; k < courses.get(i).listOfTimes.size(); k++) {     // loop through class section times
                    System.out.print(" " + courses.get(i).listOfTimes.get(k));    // output each time
                }
                System.out.println();
            }
        }
    }


    /**
     * Method used to compare a schedule to the list of courses that are sorted by preference
     * Higher number of points means schedule is closer to preference order
     * @param courses   list of class and times sorted by preference
     * @param schedule  schedule to be tested
     * @return          the number of quality points
     */
    public static int compare (final LinkedList<Course> courses,
                               final LinkedList<Course> schedule) {
        int points = 0;
        for (int i = 0; i < courses.size(); i++) {                          // for loop to keep track of class inside courses and schedule
            final int size = courses.get(i).listOfTimes.size();             // the number of section times for current class
            for (int k = 1; k < size; k++) {                                                    // for loop checking section times
                if (schedule.get(i).sectionTime.equals(courses.get(i).listOfTimes.get(k))) {    // checks where schedules's section time is inside courses
                    points = points + ((courses.size() - i) * (size - k + 1));                  // add points based on distance from preference
                }                                                                               // Math: section times closer to the left add more points
            }                                                                                   //       multiply by how high up in courses the class is
        }                                                                                       //       classes further up have larger multiplier
        return points;                                                      // return number of quality points
    }

    public static void main (final String[] args) throws IOException {

        final LinkedList<Course> courses = new LinkedList<Course>();        // Linked List to store courses and
        final LinkedList<Course> schedule = new LinkedList<Course>();       // Linked List to store possible schedule temporarily

        final Scanner fileInput = new Scanner(Paths.get(args[0]));          // Scanner for input from file
        int index = 0;                                                      // used to keep track of course to add the section times to
        final int start = 0;                                                // Becomes i variable in bruteForce

        while (fileInput.hasNextLine()) {                                   // loop to take in all input
            final String input = fileInput.nextLine();
            final Scanner parse = new Scanner(input);                       // Scanner to parse line taken in from file
            courses.add(new Course (parse.next()));                         // creates and adds a Course object to Courses
            courses.get(index).addTime("");                                 // add a blank time to list of times to test schedule without time
            while (parse.hasNext()) {                                           // scan input line and add sections time for a course
                courses.get(index).addTime(parse.next());                       // adds section time
            }
            index++;                                                        // moves onto next course in file
            parse.close();
        }

        bruteForce(courses, schedule, start);                               // creates combinations of schedules
        bestSchedule(courses, schedule);                                    // finds schedule with most classes that fits input file preference

        fileInput.close();
    }
}
