/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Program that takes in buyers and sellers, stores them in heaps,
               and performs operations on the heaps.
 */

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class HW4 {

    private static final LinkedList<String> BUYERS_SELLERS                      // list of names of people already in heaps
                                    = new LinkedList<String>();
    
    private static final HeapAdaptablePriorityQueue<Key, Order> SELLERS         // heap of sellers
                               = new HeapAdaptablePriorityQueue<Key, Order>();
    private static final HeapAdaptablePriorityQueue<Key, Order> BUYERS          // heap of buyers
                               = new HeapAdaptablePriorityQueue<Key, Order>();
    private static final DecimalFormat df = new DecimalFormat("#.##"); // Format for decimal output
    
    /**
     * Method used to check if a sale will be made
     */
    public static void checkForSale () {
        boolean done = false; // flag for no more sales
        while (!done) {       // while there are more sales
            if ((SELLERS.size() > 0 && BUYERS.size() > 0) &&        // check if there is a buyer and seller to compare
                    (SELLERS.min().getValue().getPrice() <=  -BUYERS.min().getValue().getPrice())) { // check if seller price is <= highest buyer price
                Order seller = SELLERS.min().getValue();                        // store seller
                Order buyer = BUYERS.min().getValue();                          // store buyer
                int index, numSold;                                   // variable declaration
                double pricePer;
                int sellerQuantity = seller.getQuantity();                      // store sellers quantity
                int buyerQuantity = buyer.getQuantity();                        // store buyers quantity
                if (-buyer.getPrice() > seller.getPrice()) {                     // if the seller price is lower than buyer price special case
                    pricePer = (-buyer.getPrice() + seller.getPrice()) / 2.0;          // set price per to the average of the two prices for output 
                } else {
                    pricePer = seller.getPrice();                               // else price is the seller and buyer price
                }
                if (sellerQuantity >= buyerQuantity) {                     // if the seller has more than the buyer wants
                    seller.changeQuantity(sellerQuantity -= buyerQuantity);     // set seller quantity to difference between quantities
                    numSold = buyerQuantity;                                    // num sold is the amount the buyer had
                    buyer.changeQuantity(0);                                    // set buyers quantity to zero
                    BUYERS.removeMin();                                         // remove buyer from heap
                    index = BUYERS_SELLERS.indexOf(buyer.getname());            // find buyers name from list of names
                    BUYERS_SELLERS.remove(index);                               // remove buyers name from list of names
                    if (seller.getQuantity() == 0) {
                        SELLERS.removeMin();
                        index = BUYERS_SELLERS.indexOf(seller.getname());           // find sellers name from list of names
                        BUYERS_SELLERS.remove(index);                               // remove sellers name from list of names
                    }
                } else {                                                    // else buyer wants more than what seller has
                    buyer.changeQuantity(buyerQuantity -= sellerQuantity);      // set buyer quantity to difference between quantities
                    numSold = sellerQuantity;                                   // num sold is the amount the seller had
                    seller.changeQuantity(0);                                   // set sellers quantity to zero
                    SELLERS.removeMin();                                        // remove seller from heap
                    index = BUYERS_SELLERS.indexOf(seller.getname());           // find sellers name from list of names
                    BUYERS_SELLERS.remove(index);                               // remove sellers name from list of names
                }
                System.out.printf("ExecuteBuySellOrders %s %s%nBuyer: %s %d%nSeller: %s %d%n", df.format(pricePer), numSold,               // output
                                                 buyer.getname(), buyer.getQuantity(), seller.getname(), seller.getQuantity()); // output
            } else {
                done = true;    // no more sales
            }
        }
    }
    
    /**
     * Method to amend sellers information
     * @param timeStamp     time stamp of sellers information
     * @param name          name of seller to find
     * @param newPrice      new price seller wants to buy at
     * @param newQuantity   new quantity seller wants
     */
    public static void amendSeller (int timeStamp, String name, double newPrice, int newQuantity) {
        for (int i = 0; i < SELLERS.heap.size(); i++) {                    // find buyer based on name
            if (SELLERS.heap.get(i).getValue().getname().equals(name)) {   // if found
                Entry<Key, Order> entry = SELLERS.heap.get(i);         // store entry of buyer in variable
                Key newKey = new Key(newPrice, timeStamp);               // create a new key
                SELLERS.replaceKey(entry, newKey);                       // update key which is price
                entry.getValue().changeTime(timeStamp);                    // set new time stamp
                entry.getValue().changePrice(newPrice);                    // set new price
                entry.getValue().changeQuantity(newQuantity);              // set new quantity
                break;
            }
        }
    }
    
    /**
     * Method to amend buyers information
     * @param timeStamp     time stamp of buyer information
     * @param name          name of buyer to find
     * @param newPrice      new price of coin from buyer
     * @param newQuantity   new quantity of buyer
     */
    public static void amendBuyer (int timeStamp, String name, double newPrice, int newQuantity) {
        for (int i = 0; i < BUYERS.size(); i++) {                         // find buyer based on name
            if (BUYERS.heap.get(i).getValue().getname().equals(name)) {   // if found
                Entry<Key, Order> entry = BUYERS.heap.get(i);         // store entry of buyer in variable
                Key newKey = new Key(newPrice, timeStamp);               // create a new key
                BUYERS.replaceKey(entry, newKey);                       // update key which is price
                entry.getValue().changeTime(timeStamp);                   // set new time stamp
                entry.getValue().changePrice(newPrice);                   // set new price
                entry.getValue().changeQuantity(newQuantity);             // set new quantity
                break;
            }
        }
    }
    
    /**
     * Method that takes in a sellers name and find them inside sellers heap
     *          and removes them
     * @param name  the name of the seller to find
     */
    public static void cancelSell (String name) {
        for (int i = 0; i < SELLERS.size(); i++) {                          // search sellers heap from seller
            if (SELLERS.heap.get(i).getValue().getname().equals(name)) {    // if found
                SELLERS.remove(SELLERS.heap.get(i));                        // remove entry from sellers heap
                int index = BUYERS_SELLERS.indexOf(name);                   // find name within list of names
                BUYERS_SELLERS.remove(index);                               // remove name since no longer a seller
                break;
            }
        }
    }
    
    /**
     * Method that takes in a buyers name and find them inside buyers heap
     *          and removes them
     * @param name  the name of the buyer to find
     */
    public static void cancelBuy (String name) {
        for (int i = 0; i < BUYERS.size(); i++) {                         // search buyers hap for buyer
            if (BUYERS.heap.get(i).getValue().getname().equals(name)) {   // if found
                BUYERS.remove(BUYERS.heap.get(i));                        // remove entry from buyers heap
                int index = BUYERS_SELLERS.indexOf(name);                 // find name within list of names
                BUYERS_SELLERS.remove(index);                             // remove name since no longer a buyer
                break;
            }
        }
    }
    
    /**
     * Method to retrieve the lowest seller from sellers heap and format its output
     * @return The output string for lowest seller case
     */
    public static String lowestSell() {
        if (SELLERS.isEmpty()) {    // if there is no seller return an empty string
            return "";
        }
        Order person = SELLERS.min().getValue(); // store seller in object
        return String.format("%s %06d %s %s", person.getname(), person.getTime(), df.format(person.getPrice()), person.getQuantity()); // output format
    }
    
    /**
     * Method to retrieve the highest buyer from buyers heap and format its output
     * @return The output string for highest buyer case
     */
    public static String highestBuy() {
        if (BUYERS.isEmpty()) {     // if there is no buyer return an empty string
            return "";
        }
        Order person = BUYERS.min().getValue(); // store buyer in object
        return String.format("%s %06d %s %s", person.getname(), person.getTime(), df.format(-person.getPrice()), person.getQuantity()); //output format
    }
    
    public static void main (String[] args) throws IOException{
        
        final Scanner fileInput = new Scanner(Paths.get(args[0]));   // Scanner for input file
        Order person = new Order();           // Order object used to store things in switch statement
        Key key = new Key();                  // Key object used for heap key
        int timeStamp, quantity;       // variables to store information in switch statement
        double price;
        String name;                          // variable to store information in switch statement
        
        while(fileInput.hasNext()) {
            switch (fileInput.next()) { 
            case "EnterBuyOrder" :
                timeStamp = fileInput.nextInt();        // store time
                name = fileInput.next();                // store name
                price = -fileInput.nextDouble();           // store price
                quantity = fileInput.nextInt();         // store quantity
                key = new Key (price, timeStamp);       // create object for key
                person = new Order(name, price, quantity, timeStamp);   // create object for buyer
                System.out.printf("EnterBuyOrder %06d %s ", (int)timeStamp, person);    // output
                if (BUYERS_SELLERS.contains(name)) {            // check if buyer already exists in heap
                    System.out.print("ExistingBuyerError");     // error output
                } else {                                        // else new person
                    BUYERS_SELLERS.add(name);                   // add name to list of names
                    BUYERS.insert(key, person);                 // add buyer to BUYERS heap
                }                                               // price is key and Order object is the value
                break;

            case "CancelBuyOrder" :
                timeStamp = fileInput.nextInt();        // store time
                name = fileInput.next();                // store name to find
                System.out.printf("CancelBuyOrder %06d %s ", (int)timeStamp, name);     //output
                if (!BUYERS_SELLERS.contains(name)) {   // check if name is in list
                    System.out.print("noBuyerError");   // error output
                } else {
                    cancelBuy(name);                    // cancel the buy order
                }
                break;

            case "ChangeBuyOrder" :
                timeStamp = fileInput.nextInt();        // store time
                name = fileInput.next();                // store name
                price = -fileInput.nextDouble();           // store new price
                quantity = fileInput.nextInt();         // store new quantity
                System.out.printf("ChangeBuyOrder %06d %s %s %s ", (int)timeStamp, name, df.format(-price), quantity);  //output
                if (!BUYERS_SELLERS.contains(name)) {   // check if person already exists in heap
                    System.out.print("noBuyerError");   // error output
                } else {
                    amendBuyer(timeStamp, name, price, quantity);   // change the buyer information
                }
                break;

            case "EnterSellOrder" :
                timeStamp = fileInput.nextInt();        // store time
                name = fileInput.next();                // store name
                price = fileInput.nextInt();            // store price
                quantity = fileInput.nextInt();         // store quantity
                key = new Key (price, timeStamp);       // create object for key
                person = new Order(name, price, quantity, timeStamp);   // create an object for seller
                System.out.printf("EnterSellOrder %06d %s ", (int)timeStamp, person);   // output
                if (BUYERS_SELLERS.contains(name)) {            // check if seller already exists in heap
                    System.out.print("ExistingSellerError");    // error output
                } else {                                        // else new seller
                    BUYERS_SELLERS.add(name);                   // add name to list of names
                    SELLERS.insert(key, person);                // add seller to heap of sellers and add entry to locations
                }                                               // price is key and Order object is the value
                break;
 
            case "CancelSellOrder" :
                timeStamp = fileInput.nextInt();        // store time
                name = fileInput.next();                // store name to find
                System.out.printf("CancelSellOrder %06d %s ", (int)timeStamp, name);  // output
                if (!BUYERS_SELLERS.contains(name)) {   // check if seller is in list
                    System.out.print("noSellerError");  // if not output error
                } else {
                    cancelSell(name);                   // else remove seller from heap
                }
                break;


            case "ChangeSellOrder" :
                timeStamp = fileInput.nextInt();    // store time
                name = fileInput.next();            // store name
                price = fileInput.nextInt();        // store new price
                quantity = fileInput.nextInt();     // store new quantity
                System.out.printf("ChangeSellOrder %06d %s %s %s ", (int)timeStamp, name, df.format(price), quantity); // output
                if (!BUYERS_SELLERS.contains(name)) {   // if the seller does not exist
                    System.out.print("noSellerError");  // output error
                } else {
                    amendSeller(timeStamp, name, price, quantity); // else change seller information
                }
                break;

            case "DisplayHighestBuyOrder" :
                timeStamp = fileInput.nextInt();    //store time
                System.out.printf("DisplayHighestBuyOrder %06d %s", (int)timeStamp, highestBuy()); // output and method call to find highest buyer
                break;

            case "DisplayLowestSellOrder" :
                timeStamp = fileInput.nextInt();    // store time
                System.out.printf("DisplayLowestSellOrder %06d %s", (int)timeStamp, lowestSell()); // output and method call to find lowest seller
                break;
            }
            System.out.println();       // to maintain output
            checkForSale();             // after case is complete check is a sale can happen
        }
        fileInput.close();
    }
}
