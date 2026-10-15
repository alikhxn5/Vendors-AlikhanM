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

    /** resets the Balance to 0 */
    void resetBalance () {
        this.balance = 0;
    }

    /** returns the current balance */
    double getBalance () {
        return this.balance;
    }

    /** adds money to the machine's balance
     * @param amt how much money to add
     * @throws IllegalArgumentException if amt is zero or negative
     * */
    void addMoney (double amt) {
        if(amt <= 0){
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
    int getStock(String name){
        Item item = stock.get(name);
        return item == null ? 0 : item.stock;
    }

    /** attempt to purchase named item.  Message returned if
     * the balance isn't sufficient to cover the item cost.
     *
     * @param name The name of the item to purchase ("Candy" or "Gum")
     */
    void select (String name) {
        if (stock.containsKey(name)) {
            Item item = stock.get(name);
            if (balance >= item.price) {
                item.purchase(1);
                this.balance = this.balance - item.price;
            }
            else
                System.out.println("Gimme more money");
        }
        else System.out.println("Sorry, don't know that item");
    }

}

class Examples {
}

