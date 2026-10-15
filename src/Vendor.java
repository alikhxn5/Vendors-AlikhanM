import java.util.HashMap;
import java.util.Map;


/**
 * Class for a Vending Machine.  Contains a hashtable mapping item names to item data, as
 * well as the current balance of money that has been deposited into the machine.
 */
class Vendor {
    // Each vendor has its own inventory. (fixed)
    private final Map<String, Item> stock = new HashMap<>();
    private double balance;

    Vendor(int numCandy, int numGum) {
        stock.put("Candy", new Item(1.25, numCandy));
        stock.put("Gum", new Item(.5, numGum));
        this.balance = 0;
    }

    /**
     * resets the Balance to 0
     */
    void resetBalance() {
        this.balance = 0;
    }

    /**
     * returns the current balance
     */
    double getBalance() {
        return this.balance;
    }

    /**
     * adds money to the machine's balance
     *
     * @param amt how much money to add
     * @throws IllegalArgumentException if amt is zero or negative
     *
     */
    void addMoney(double amt) {
        if (amt <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.balance = this.balance + amt;
    }

    /**
     * Returns how many of the named item are in stock.
     *
     * @param name the item to lookup
     * @return the stock count, or 0 if this vendor does not carry the item
     */
    int getStock(String name) {
        Item item = stock.get(name);
        return item == null ? 0 : item.stock;
    }

    /**
     * attempt to purchase named item. A message is printed if the vendor
     * does not carry the item or the balance isn't enough to cover its cost.
     *
     * @param name The name of the item to purchase ("Candy" or "Gum")
     * @return true if the purchase went through
     */
    boolean select(String name) {
        Item item = stock.get(name);
        if(item == null){
            System.out.println("Sorry, don't know that item.");
            return false;
        }
        if (balance < item.price) {
            System.out.println("Gimme more money.");
            return false;
        }
        item.purchase(1);
        this.balance = this.balance - item.price;
        return true;


    }
}


