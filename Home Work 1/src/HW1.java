/*

  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: A program that manages the inventory of products
               for sellers.

 */

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.text.DecimalFormat;

public class HW1 {
    // Create Lists to store sellers and products
    public static final SinglyLinkedList<Seller> SELLERS
                  = new SinglyLinkedList<Seller>();
    public static final SinglyLinkedList<Product> PRODUCTS
                  = new SinglyLinkedList<Product>();

    public static class Seller {
        // Creates an Object for each seller
        private String name;
        private double minFreeShipping;
        private double shippingFee;
        
        /** 
         * Constructor for Seller object
         * @param sellName  name of the seller
         * @param shipFee   shipping fee
         * @param minFree   minimum required to spend for free shipping
         */
        Seller (final String sellName, final double shipFee,  final double minFree) {
            name = sellName;
            minFreeShipping = minFree;
            shippingFee = shipFee;
        }
    }

    public static class Product {
        //Creates an Object for each product
        private String productName;
        private String sellerName;
        private double price;
        private double shipping;
        private double totalPrice;
        private int inventory = 0;

        public double getTotalPrice() { return totalPrice; }
        
        /** Constructor for products
         *  
         * @param product = product name
         * @param sellName = seller name
         * @param cost = price of product
         */
        Product (final String product, final String sellName, final double cost) {
            productName = product;
            sellerName = sellName;
            price = cost;
            // calculates if minimum price of product is above sellers minimum for free shipping
            for (int i = 0; i < SELLERS.size(); i++) {
                if (SELLERS.get(i).name.equals(sellerName)) {               // if seller name matches a seller in the SELLERS list
                    shipping = SELLERS.get(i).shippingFee;                  // store shipping for this product based on sellers shipping fee
                    if (price < SELLERS.get(i).minFreeShipping) {           // check to see if price of product is greater than 
                        totalPrice = price + shipping;                      // sellers minimum for free shipping and set total price
                    } else {
                        totalPrice = price;
                        shipping = 0; // free shipping
                    }
                    break;
                }
            }
        }
        
        public final void addInv (final int num) {
            inventory = inventory + num;
        }
        public final boolean purchase (final int num) {
            //checks to see if inventory would run out
            if (inventory - num >= 0) {
                inventory = inventory - num;                                //if not perform operation
                return true;
            }
            return false;
        }
    }
    
    /** 
     * Method for displaying product table based on name of product
     * @param product   name of product to find and display
     */
    public static void display (final String product) {                         //output
        System.out.println("DisplaySellerList " + product);                     //output
        System.out.printf("%10s%14s%14s%11s %n", "seller", "productPrice",
                                                "shippingCost", "totalCost");
        // Search PRODUCTS for products and print
        for (int i = 0; i < PRODUCTS.size(); i++) {
            if ((PRODUCTS.get(i).productName.equals(product))
                            && (PRODUCTS.get(i).inventory != 0)) {              // If the product name matches print information
                System.out.printf("%10s %,13.2f %,13.2f %,10.2f %n",            // output
                        PRODUCTS.get(i).sellerName, PRODUCTS.get(i).price,
                        PRODUCTS.get(i).shipping, PRODUCTS.get(i).totalPrice);
            }
        }
    }
    
    /** Takes in a product and seller name and searches the PRODUCTS list 
     * for a seller that sell the product
     * @param product = name of product to find
     * @param seller = name of seller to find
     * @return the index of the product in PRODUCTS list
     */
    public static int searchProducts (final String product, final String seller) {
        for (int i = 0; i < PRODUCTS.size(); i++) {                         // looks for a product from a seller
            if ((PRODUCTS.get(i).productName.equals(product))
              && (PRODUCTS.get(i).sellerName.equals(seller))) {
                return i;
            }
        }
        return -1; //returns if not found
    }

    /** 
     * Find index where new product will go in list
     * @param price = the total price of the item
     * @return the index where the new products will go in the 
     *         PRODUCTS list based off total price
     */
    public static int sortPosition (final double price) {
        if (PRODUCTS.size() == 0) { // condition for empty list
            return 0;
        }
        if (PRODUCTS.size() == 1) { // condition for list of size one. new item can only go before or after
            if (price < PRODUCTS.get(0).totalPrice) { 
                return 0;
            } else {
                return 1;
            }
        } else {
            if (price > PRODUCTS.get(PRODUCTS.size() - 1).totalPrice) { // condition for if new product goes to the end of the list
                return PRODUCTS.size() - 1;
            }
            for (int i = 0; i < PRODUCTS.size() - 1; i++) {                 //loop for if the position is in-between two nodes
                if ((price >= PRODUCTS.get(i).totalPrice)
                        && (price < PRODUCTS.get(i + 1).totalPrice)) {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    /**
     * Main method that takes in name of input file and
     * performs operations based on input strings
     * @param args  name of input file to find
     * @throws IOException
     */
    public static void main (final String[] args) throws IOException {
        final Scanner fileInput = new Scanner(Paths.get(args[0]));
        int index, num;
        String sellerName;
        final DecimalFormat df = new DecimalFormat("#.##"); // Format for decimal output

        while (fileInput.hasNext()) {
            switch (fileInput.next()) {                                             //test for each input case
            case "SetShippingCost" :                                                /** For creating sellers shipping information */
                //store input values
                sellerName = fileInput.next();
                final double shipFee = fileInput.nextDouble();
                final double shipFreeMin = fileInput.nextDouble();
                SELLERS.addLast(new Seller(sellerName, shipFee, shipFreeMin));      //create and add to list new seller object
                System.out.printf("%s %s %s %s%n", "SetShippingCost", sellerName,   //output
                        df.format(shipFee), df.format(shipFreeMin));
                break;
            case "SetProductPrice" :                                                /** For creating products and price information */
                //store input
                final String productName = fileInput.next();
                sellerName = fileInput.next();
                final double price = fileInput.nextDouble();
                final Product product = new Product(productName, sellerName, price); // create Product object and add to list
                index = sortPosition(product.totalPrice);                            // find position in array for new product
                PRODUCTS.insert(product, index);                                     // insert it into array
                System.out.printf("%s %s %s %s%n", "SetProductPrice", productName,   // output
                        sellerName, df.format(price));
                break;
            case "IncreaseInventory" :                                               /** For product inventory increase */
                index = searchProducts(fileInput.next(), fileInput.next());          // take in seller and product name and search for the product
                num = fileInput.nextInt();                                           // amount of inventory to add
                PRODUCTS.get(index).addInv(num);                                     // add to inventory
                System.out.printf("%s %s %s %d %d%n", "IncreaseInventory",           // output
                        PRODUCTS.get(index).productName, PRODUCTS.get(index).sellerName,
                        num, PRODUCTS.get(index).inventory);
                break;
            case "CustomerPurchase" :                                               /** For product purchase by customer */
                //position of product by seller in products list
                index = searchProducts(fileInput.next(), fileInput.next());         // take in seller and product name and search for the product
                num = fileInput.nextInt();                                          // number of purchased items
                final String result;                                                // for outcome of purchase
                if (PRODUCTS.get(index).purchase(num)) {
                    result = Integer.toString(PRODUCTS.get(index).inventory);       // output
                } else {
                    result = "NotEnoughInventoryError";                             // output for error
                }
                System.out.printf("%s %s %s %d %s%n", "CustomerPurchase",           //output
                        PRODUCTS.get(index).productName,
                        PRODUCTS.get(index).sellerName, num, result);
                break;
            case "DisplaySellerList" :                                              /** to display sellers of product from input */
                display(fileInput.next());
                break;
            default :
                break;
            }
        }
        fileInput.close();
    }
}
