/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Class used to input and create a tree with data.
               takes in olympic sports, events, and their winning athletes/countries
               creates a tree connecting all these elements
               then takes in arguments to find data in tree
 */

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.IOException;

public class HW3 {
    @SuppressWarnings("rawtypes")
    public static final Tree TREE = new Tree();
    public static final ArrayList<Node<String>> MEDAL_LIST = new ArrayList<Node<String>>();     // List of medal nodes

    /**
     * Method used to find most medals of an athlete or country
     * @param type          argument for if to check Athlete node(0) or Country node(1)
     * @param medalType     argument for if to find Gold medals or Any
     */
    public static void medalCount (int type, String medalType) {
        ArrayList<String> namesTaken = new ArrayList<String>();                     // list of names of athletes/countries already found
        ArrayList<MedalCounter> numOfMedalsPer = new ArrayList<MedalCounter>();     // array of MeadalCounter, stores names and the num of medals for each
        
        for (int i = 0; i < MEDAL_LIST.size(); i++) {                               // loop through list of medals given
            Node<String> current = MEDAL_LIST.get(i).children.get(type);            // store current medal node
            if (!namesTaken.contains(current.element)) {                            // if the athlete/country is a new entry
                namesTaken.add(current.element);                                    // add to lists
                numOfMedalsPer.add(new MedalCounter(current.element));
            }
            if (medalType.equals("Gold")) {                                         // check if only gold medals are wanted
                if (current.parent.element.equals(medalType)) {                     // if nodes parent is the gold medal node
                    int index = namesTaken.indexOf(current.element);                // use namesTaken list to find index inside numOfMedals
                    numOfMedalsPer.get(index).addMedal();                           // increment athlete's/county's medals by one
                } 
            } else {                                                                // if we don't want only Gold count any medal
                int index = namesTaken.indexOf(current.element);                    // use namesTaken list to find index inside numOfMedals
                numOfMedalsPer.get(index).addMedal();                               // increment athlete's/county's medals by one
            }
        }
        
        printMedal (numOfMedalsPer);                                                // call printMedal to print most medals to screen
    }
    
    /**
     * Method to print athletes/countries and medal counts to screen.
     *        if multiple athletes/countries have the same amount print lexicographically
     * @param numOfMedalsPer  list of athletes/countries with their medal count
     */
    public static void printMedal (ArrayList<MedalCounter> numOfMedalsPer) {
        int max = 0;                                            // set max to zero
        Collections.sort(numOfMedalsPer);                       // sort list of athletes/countries lexicographically for output
        for (MedalCounter toCheck : numOfMedalsPer) {           // loop through athlete/country list
            if (toCheck.count > max) {                          // if athlete/country has most medals
                max = toCheck.count;                            // set new max to most medals
            }
        }
        System.out.printf(" %d", max);                          // output of most medals
        for (MedalCounter name : numOfMedalsPer) {              // loop through athlete/country list
            if (name.count == max) {                            // if an athlete/country has the most medals
                System.out.printf(" %s", name.name);            // output athlete/country
            }
        }
        System.out.println();
    }
    
    /**
     * Method to add medal nodes to event nodes and 
     *        athlete and country nodes to medal nodes
     * @param parent        element used to find Parent node to add medal nodes and athlete/country nodes
     * @param orderList     List of athletes and countries separated by colon (ignore first index: parent element)
     */
    @SuppressWarnings("unchecked")
    public static void medalists (String parent, String[] orderList) {

        Node<String> parentNode = TREE.getNode(parent);         // find node in tree that contains parent 
        Node<String> gold = new Node<String>("Gold");           // create a node for Gold medal
        Node<String> silver = new Node<String>("Silver");       // create a node for Silver medal
        Node<String> bronze = new Node<String>("Bronze");       // create a node for Bronze medal

        TREE.appendChild(parentNode, gold);                     // add Gold medal node to event
        TREE.appendChild(parentNode, silver);                   // add Silver medal node to event
        TREE.appendChild(parentNode, bronze);                   // add Bronze medal node to event

        String name, country;                                                   // stores athlete name and country name
        for (int i = 1; i < orderList.length; i++) {                            // loop through athlete country list
            name = orderList[i].substring(0, orderList[i].indexOf(":"));        // separate name from country and store
            country = orderList[i].substring(orderList[i].indexOf(":") + 1);    // separate country from name and store
            Node<String> nameNode = new Node<String>(name);                     // create a node with athlete name
            Node<String> countryNode = new Node<String>(country);               // create a node with country name
            if (i == 1) {                                           // Gold medalist is first athlete/country
                TREE.appendChild(gold, nameNode);                   // add athlete name node to Gold medal
                TREE.appendChild(gold, countryNode);                // add country node to Gold medal
                MEDAL_LIST.add(gold);                               // add medal to list of given medals
            } else if (i == 2) {                                    // Silver medalist is second athlete/country
                TREE.appendChild(silver, nameNode);                 // repeat above steps
                TREE.appendChild(silver, countryNode);
                MEDAL_LIST.add(silver);
            } else {                                                // Bronze medalist is last athlete/country
                TREE.appendChild(bronze, nameNode);                 // repeat above steps
                TREE.appendChild(bronze, countryNode);
                MEDAL_LIST.add(bronze);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void main (String[] args) throws IOException {

        final Scanner dataInput = new Scanner(Paths.get(args[0]));      // scanner for data input
        final Scanner queryInput = new Scanner(Paths.get(args[1]));     // scanner for query input

        while (dataInput.hasNextLine()) {                               // data input
            final String input = dataInput.nextLine();                  // take in line from input
            final Scanner parse = new Scanner(input);                   // scanner to parse input line
            String parent = parse.next();                               // parent is the first string in line
            if (input.contains(":")) {                                  // condition for athlete/country medalist list
                String[] list = input.split(" ");                           // split up athlete/country into a list
                medalists(parent, list);                                    // add athlete/country medalists to parent event
            } else {
                while (parse.hasNext()) {                               // parse through line for parents children
                    String child = parse.next();                        // store child in line
                    TREE.add(parent, child);                            // add child to parent node in tree
                }
                parse.close();
            }
        }
        dataInput.close();
        
        String sport, event, athlete;                                           // string for storing argument input
        Node<String> node = new Node<String>();                                 // default node used to store nodes
        ArrayList<Node<String>> queryList = new ArrayList<Node<String>>();      // used to store athlete nodes when finding their events 
        while (queryInput.hasNext()) {                                          // query input
            switch (queryInput.next()) {                                        // switch statement for input arguments types
            case "GetEventsBySport" :
                sport = queryInput.next();                                      // store sport to find events of      
                node = TREE.getNode(sport);                                     // get the node containing sport name

                System.out.printf("%s %s ", "GetEventsBySport", sport);         // output
                for (int i = 0; i < node.children.size(); i++) {                // loop to output sport node's children
                    System.out.print(node.children.get(i).element + " ");       // output
                }
                System.out.println();
                break;
            case "GetWinnersAndCountriesBySportAndEvent" :
                sport = queryInput.next();                                      // store sport
                event = queryInput.next();                                      // store event to find winners of
                node = TREE.nodeBySportEvent(sport, event);                     // find event node by sport and event

                System.out.printf("%s %s %s", "GetWinnersAndCountriesBySportAndEvent",sport, event);    // output
                for (Node<String> medal : node.children) {                                              // output each medalist
                    System.out.printf(" %s:%s", medal.children.get(0).element,                          // output name
                                                medal.children.get(1).element);                         // output country
                }
                System.out.println();
                break;
            case "GetGoldMedalistAndCountryBySportAndEvent" :
                sport = queryInput.next();                                      // store sport      
                event = queryInput.next();                                      // store event to find winners of
                node = TREE.nodeBySportEvent(sport, event);                     // find event node by sport and event
                node = node.children.get(0);                                    // grab gold medalist from event node

                System.out.printf("%s %s %s", "GetGoldMedalistAndCountryBySportAndEvent", sport, event);    // output
                System.out.printf(" %s:%s", node.children.get(0).element,  node.children.get(1).element);   // output
                System.out.println();
                break;
            case "GetAthleteWithMostMedals" :
                System.out.print("GetAthleteWithMostMedals");       // output
                medalCount (0, "Any");                              // call medalCount
                break;
            case "GetAthleteWithMostGoldMedals" :
                System.out.print("GetAthleteWithMostGoldMedals");   // output
                medalCount (0, "Gold");                             // call medalCount
                break;
            case "GetCountryWithMostMedals" :
                System.out.print("GetCountryWithMostMedals");       // output
                medalCount (1, "Any");                              // call medalCount
                break;
            case "GetCountryWithMostGoldMedals" :
                System.out.print("GetCountryWithMostGoldMedals");   // output  
                medalCount (1, "Gold");                             // call medalCount
                break;
            case "GetSportAndEventByAthlete" :
                athlete = queryInput.next();                                            // store athlete name
                queryList = TREE.postOrderSearch(athlete, TREE.getRoot(), queryList);   // search tree for nodes with athlete's name

                System.out.printf("%s %s", "GetSportAndEventByAthlete", athlete);       // output
                for (Node<String> events : queryList) {                                 // loops through events athlete is in
                    System.out.printf(" %s:%s", events.parent.parent.parent.element,    // output sport name
                                                       events.parent.parent.element);   // output event name
                }
                System.out.println();
                queryList.clear();                                  // clear list for later queries
                break;
            default:
                break;
            }
        }
        queryInput.close();
    }
}
