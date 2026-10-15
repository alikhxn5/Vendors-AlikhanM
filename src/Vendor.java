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

    // returns true if this vendor carries the named item
     boolean hasItem(String name){
        return stock.containsKey(name);
     }

    /**
     * Returns the price of the named item
     *
     * @throws IllegalArgumentException if the vendor does not carry the item
     */
    double getPrice(String name){
        Item item = stock.get(name);
        if(item == null){
            throw new IllegalArgumentException("Unknown item: " + name);
        }
        return item.price;
    }

    /**
     * attempt to purchase named item. A message is printed if the vendor
     * does not carry the item or the balance isn't enough to cover its cost.
     *
     * @param name The name of the item to purchase ("Candy" or "Gum")
     * @return true if the purchase went through, false otherwise
     */
    boolean select(String name) {
        Item item = stock.get(name);
        if(item == null){
            System.out.println("Sorry, don't know that item.");
            return false;
        }
        if(item.stock <= 0){
            System.out.println("Sorry " + name + " is sold out");
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
    /**
     * Adds more pieces of an item the vendor already carries.
     *
     * @param name the item to restock
     * @param amount how many pieces to add (must be positive)
     * @return true if the item was restocked, false if the vendor does not carry it
     * @throws IllegalArgumentException if amount is zero or negative
     */
    boolean restock(String name, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Restock amount must be positive");
        }
        Item item = stock.get(name);
        if (item == null) {
            return false;
        }
        item.restock(amount);
        return true;
    }

    /**
     * Renames an item. Its stock, price, and other data move with it to the new name.
     *
     * @param oldName the item's current name
     * @param newName the name to use from now on
     * @return true if renamed; false if the old name is unknown, or the new name is blank
     * or already used by another item
     */
    boolean renameItem(String oldName, String newName){
        if(newName == null || newName.isBlank()){
            return false;
        }

        // Refuse overwriting of item that already has the new name
        if(!stock.containsKey(oldName) || stock.containsKey(newName)){
            return false;
        }
        stock.put(newName, stock.remove(oldName));
        return true;
    }

    /**
     * Restocks an item, first adding it to the inventory if the vendor
     * does not carry it yet. This lets the inventory change over time.
     *
     * @param name the item to restock
     * @param amount how many pieces to add (must be positive)
     * @param price the price for a new item (an existing item keeps its current price)
     * @throws IllegalArgumentException if the name is blank, or amount or price is not positive
     */
    void restock(String name, int amount, double price) {
        // Validate everything before changing the inventory
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name must not be blank");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Restock amount must be positive");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (!stock.containsKey(name)) {
            stock.put(name, new Item(price, 0));
        }
        stock.get(name).restock(amount);
    }
}



