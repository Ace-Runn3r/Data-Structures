/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Class used to store the athlete or country name 
               and medal count of either one.
 */

public class MedalCounter implements Comparable<MedalCounter> {
    String name;
    int count = 0;
    
    /**
     * constructor for athlete or country name
     * @param inputName  name of athlete or country
     */
    public MedalCounter (String inputName) {
       name = inputName; 
    }
    
    protected void addMedal() {
        count++;
    }

    /**
     * Comparable method to set natural ordering to lexicographic order
     * @param other  MedalCounter to be compared to
     * @return        value == 0 if elements are the same.
     *                < 0 if this.element comes first and.
     *                > 0 if other.element comes first.
     */
    @Override
    public int compareTo(MedalCounter other) {
        return this.name.compareTo( other.name);
    }
}
