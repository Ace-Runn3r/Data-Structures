/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: A program that finds the best class schedules
               without recursion based on course name and section
               times. Preference is given to courses higher up on
               the list and section time closer to the list in
               event of conflict
 */

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

public final class HW2Extra {
    private HW2Extra () {}

    /**
     * Method to find the best possible schedule based on classes and time in courses list
     * @param courses   Linked list of all courses and times
     * @param schedule  Linked list of best schedule
     * @return          The best possible schedule by preference
     */
    private static LinkedList<Course> bestSchedule (final LinkedList<Course> courses,
            LinkedList<Course> schedule) {
        String check;                                                   // the time to test for conflicts
        Boolean conflict = false;                                       // boolean for if there is a conflict with check
        Boolean sameCourse = false;                                     // condition for overriding old section time if there is a better one
        Boolean filled = false;                                         // boolean for if a course has found a section time
        LinkedList<String> takenTimes = new LinkedList<String>();       // list to keep track of time already taken by a course

        for (int i = 0; i < courses.size(); i++) {                              // parse through course list
            for (int k = 0; k < courses.get(i).listOfTimes.size(); k++) {                       // parse through a courses times
                check = courses.get(i).listOfTimes.get(k);                                      // store current time to check
                conflict = checkForConflict(check, courses, i + 1);                             // test for conflict
                if ((!conflict && !takenTimes.contains(check) && (!filled || i == 0))           // {no conflict and time is not taken and (course had no time in schedule OR first item in courses list)} 
                                 || (conflict && !takenTimes.contains(check) && !filled)) {     // {has conflict and time is not taken and is not filled} add to schedule
                    if (sameCourse) {                                                           // if a better section time is found for a course [i]
                        schedule.removeFirst();                                                 // remove older time from schedule
                        takenTimes.removeLast();                                                // remove older time form list of taken times
                    }
                    schedule.add(new Course(courses.get(i).courseName,                          // add time to schedule
                                                 courses.get(i).listOfTimes.get(k)));
                    takenTimes.add(courses.get(i).listOfTimes.get(k));                          // add time to list of taken times
                    sameCourse = true;                                                          // marks course [i] as having a section time that works so far
                    filled = true;                                                              // mark course as [i] having a section time for it
                }
            }
            if (!filled) {                                                      // if course was not added to the schedule it must conflict
                courses.get(i).listOfTimes.addFirst("conflict");                // add conflict tag to that course
            }
            filled = false;                                                     // reset boolean tests
            sameCourse = false;                                                 // reset boolean tests
        }
        return schedule;                                                // return best schedule
    }

    /**
     * Method to parse courses and check for a time conflict with the time that is passed in
     * @param check     The current time being tested for conflicts
     * @param courses   List of courses to compare to for conflicts
     * @param start     Stops method from checking earlier classes in list by skipping their index
     * @return          True if course conflicts. False otherwise
     */
    private static Boolean checkForConflict (final String check, LinkedList<Course> courses, int start) {
        for (int i = start; i < courses.size(); i++) {      // track course being tested
            for (int k = 0; k < courses.get(i).listOfTimes.size(); k++) {       // track time being test
                if (check.equals(courses.get(i).listOfTimes.get(k))) {          // if time has a conflict
                    return true;                                                // return conflict found
                }
            }
        }
        return false;                                                           // return no conflict
    }

    /**
     * Method for displaying correct output format
     * @param schedule  The best schedule that allows for the most classes with preference
     * @param courses   The list of courses used to get times for classes missing from schedule
     */
    private static void display (LinkedList<Course> schedule, LinkedList<Course> courses) {
        System.out.println("---Course schedule---");
        for (int i = 0; i < courses.size(); i++) {              // keeps track of preferred output order
            for (int k = 0; k < schedule.size(); k++) {                         // parse through schedule
                if (courses.get(i).courseName.equals(schedule.get(k).courseName)) {           // if course and schedule have same class
                       System.out.printf("%s %s%n",                                           // output
                               schedule.get(k).courseName, schedule.get(k).sectionTime);
                }
            }
        }

        System.out.println("---Courses with a time conflict---");
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).listOfTimes.getFirst().equals("conflict")) {       // if a course is marked with conflict print
                System.out.print(courses.get(i).courseName);
                for (int k = 1; k < courses.get(i).listOfTimes.size(); k++) {     // loop through class section times
                    System.out.print(" " + courses.get(i).listOfTimes.get(k));    // output each time
                }
                System.out.println();
            }
        }

    }

    public static void main (final String[] args) throws IOException {

        final LinkedList<Course> courses = new LinkedList<Course>();        // Linked List to store courses and
        LinkedList<Course> schedule = new LinkedList<Course>();             // Linked List to store possible schedule

        final Scanner fileInput = new Scanner(Paths.get(args[0]));          // Scanner for input from file
        int index = 0;                                                      // used to keep track of course to add the section times to
        while (fileInput.hasNextLine()) {                                   // loop to take in all input
            final String input = fileInput.nextLine();
            final Scanner parse = new Scanner(input);                       // Scanner to parse line taken in from file
            courses.add(new Course (parse.next()));                         // creates and adds a Course object to Courses
            while (parse.hasNext()) {                                           // scan input line and add sections time for a course
                courses.get(index).addTime(parse.next());                       // adds section time
            }
            index++;                                                        // moves onto next course in file
            parse.close();
        }

        schedule = bestSchedule(courses, schedule);                         // find best schedule
        display(schedule, courses);                                         // print to screen

        fileInput.close();
    }
}
