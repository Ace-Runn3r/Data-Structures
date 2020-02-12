import java.text.DecimalFormat;

/*
  Author: Cole Clements
  Email: cclements2016@my.fit.edu
  Course: Data Structures and Algorithms
  Section: E2
  Description: Class used by HW4 to store information of buyers
               and sellers
 */


public class Order {
    private String name;     // name of seller  or buyer
    private int quantity;    // quantity selling or buying
    private double price;       // price to sell or buy at
    private int orderTime;   // time of order
    
    
    // default constructor
    public Order () {};
    
    /**
     * Constructor to fill all fields
     * @param personsName    buyer/seller name
     * @param pricePer       price selling/buying at
     * @param numberOfCoins  number of coins desired
     * @param timePlaced     time the order is placed
     */
    public Order (String personsName, double pricePer, int numberOfCoins, int timePlaced) {
        name = personsName;
        price = pricePer;
        quantity = numberOfCoins;
        orderTime = timePlaced;
    }
    
    // Getter methods for fields
    public String getname () { return name; };
    
    public double getPrice () { return price; };
    
    public int getQuantity () { return quantity; };
    
    public int getTime () { return orderTime; };
    
    // Setter methods for fields
    public void changePrice (double newPrice) { price = newPrice; };
    
    public void changeTime (int newTime) { orderTime = newTime; };
    
    public void changeQuantity (int newQuantity) { quantity = newQuantity; }

    // toString method for object output
    @Override
    public String toString() {
        final DecimalFormat df = new DecimalFormat("#.##"); // Format for decimal output
        double outputPrice = Math.abs(price);
        return String.format("%s %s %d", name, df.format(outputPrice), quantity);
    };
}
