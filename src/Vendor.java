import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Class for an NPC vendor (a vending machine). Contains a map from item names to
 * item data, as well as the current balance of money that has been deposited into the machine.
 */
class Vendor{
    // Each vendor has its own inventory. In the starter code this map was static,
    // so every vendor shared (and overwrote) the same stock.
    // A TreeMap keeps the items sorted by name, so printed inventories are predictable.
    private final Map<String, Item> stock = new TreeMap<>();
    private final String name;
    private double balance;

    /**
     * creates a vendor named "Vendor" with the given amounts of Candy and Gum
     */
    Vendor(int numCandy, int numGum){
        this("Vendor", numCandy, numGum);
    }

    /**
     * creates a named vendor with the given amounts of Candy and Gum
     */
    Vendor(String name, int numCandy, int numGum){
        this.name = name;
        stock.put("Candy", new Item(1.25, numCandy, "A sweet chocolate treat.", "Snacks"));
        stock.put("Gum", new Item(.5, numGum, "Chewy mint gum that freshens your breath.", "Snacks"));
        this.balance = 0;
    }

    /**
     * returns this vendor's name
     */
    String getName(){
        return name;
    }

    /**
     * resets the Balance to 0
     */
    void resetBalance(){
        this.balance = 0;
    }

    /**
     * returns the current balance
     */
    double getBalance(){
        return this.balance;
    }

    /**
     * adds money to the machine's balance
     *
     * @param amt how much money to add
     * @throws IllegalArgumentException if amt is zero or negative
     *
     */
    void addMoney(double amt){
        if (amt <= 0){
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

    // returns true if this vendor carries the named item
    boolean hasItem(String name){
        return stock.containsKey(name);
    }

    /**
     * Returns the price a customer pays for the named item, after any discount.
     *
     * @throws IllegalArgumentException if the vendor does not carry the item
     */
    double getPrice(String name){
        return findItem(name).getSalePrice();
    }

    /**
     * Returns the named item's description, so a customer can check it before purchasing.
     *
     * @throws IllegalArgumentException if the vendor doesn't carry the item
     */
    String getDescription(String name){
        return findItem(name).description;
    }

    /**
     * Changes the named item's description
     */
    boolean setDescription(String name, String description){
        Item item = stock.get(name);
        if (item == null){
            return false;
        }
        item.description = description;
        return true;
    }

    /**
     * Returns a one-line summary of an item for a customer to read before purchasing.
     * e.g. "Candy - $1.25 - 10 in stock - A sweet chocolate treat."
     * @throws IllegalArgumentException if the vendor doesn't carry the item.
     */
    String getItemDetails(String name){
        Item item = findItem(name);
        return String.format("%s - $%.2f - %d in stock - %s", name, item.getSalePrice(), item.stock, item.description);
    }

    /**
     * Returns the named item's category
     *
     * @throws IllegalArgumentException if the vendor doesn't carry the item.
     */
    String getCategory(String name){
        return findItem(name).category;
    }

    /**
     * Puts the named item in a category, such as 'snacks' or 'drinks'
     *
     * @return true if updated, false if the vendor doesn't carry the item.
     */
    boolean setCategory(String name, String category){
        Item item = stock.get(name);
        if(item==null){
            return false;
        }
        item.category = category;
        return true;
    }

    /**
     * Puts one item on sale. A discount replaces any earlier one.
     *
     * @param name the item to discount
     * @param percent how much to take off, from 0-100.
     * @return true if the discount was applied, false if the vendor does not carry that item.
     * @throws IllegalArgumentException if the percent is outside 0 to 100.
     */
    boolean applyDiscount(String name, int percent){
        checkPercent(percent);
        Item item = stock.get(name);
        if(item==null){
            return false;
        }
        item.discountPercent = percent;
        return true;
    }

    /**
     * Puts every item in a category on sale.
     *
     * @param category the category to dicount
     * @param percent how much to take off, from 0-100.
     * @return how many items were discounted
     * @throws IllegalArgumentException if the percent is outside 0-100.
     */
    int applyCategoryDiscount(String category, int percent){
        checkPercent(percent);
        int count = 0;
        for(Item item : stock.values()){
            if(item.category.equals(category)){
                item.discountPercent = percent;
                count++;
            }
        }
        return count;
    }

    /**
     * Ends the sale on an item so it goes back to full price
     *
     * @return true if updated, false if the vendor does not carry the item.
     */
    boolean removeDiscount(String name){
        return applyDiscount(name, 0);
    }

    /**
     * Marks an item as a Bestseller
     *
     * @return true if marked, false if the vendor doesn't carry the item
     */
    boolean markBestseller(String name){
        return setBestseller(name, true);
    }

    /**
     * Removes the Bestseller mark from an item
     *
     * @return true if updated, false if the vendor doesn't carry the item.
     */
    boolean unmarkBestseller(String name){
        return setBestseller(name, false);
    }

    // returns true if the named item is marked as a Bestseller (false if not carried by the vendor).
    boolean isBestseller(String name){
        Item item = stock.get(name);
        return item != null && item.bestseller;
    }

     // Returns the names of all bestsellers in a-b order, giving quick access to the most commonly purchased items.
    List<String>getBestsellers(){
        // was stuck on this for a minute, ended up using google along with claude ai to help out with logic.
        List<String> bestsellers = new ArrayList<>();
        for (Map.Entry<String, Item> entry: stock.entrySet()){
            if(entry.getValue().bestseller){
                bestsellers.add(entry.getKey());
            }
        }
        return bestsellers;
    }

    /**
     * Uses the purchase history to mark the top-selling items as bestsellers.
     * Items that have never sold are skipped and existing marks are kept.
     *
     * @param howMany how many of the top sellers to mark
     */
    void markTopSellers(int howMany){
        List<String> byPopularity = getItemsByPop();
        for(int i = 0; i < howMany && i< byPopularity.size(); i++){
            Item item = stock.get(byPopularity.get(i));
            if(item.purchaseCount == 0){
                // bc the rest of the list has no sales either
                break;
            }
            item.bestseller = true;
        }
    }

    // sets or clears the bestseller mark
    // returns false if the item is not carried by the vendor.
    private boolean setBestseller(String name, boolean value){
        Item item = stock.get(name);
        if(item == null){
            return false;
        }
        item.bestseller = value;
        return true;
    }

    // throws an exception if a discount percentage isn't b/w 0-100.
    private void checkPercent(int percent){
        if(percent < 0 || percent > 100){
            throw new IllegalArgumentException("Discount must be between 0 and 100 percent");
        }
    }

    // looks up an item, throwing an exception if the vendor doesn't carry the item.
    private Item findItem(String name){
        Item item = stock.get(name);
        if (item == null){
            throw new IllegalArgumentException("Unknown item: " + name);
        }
        return item;
    }


    /**
     * Attempts to purchase the named item. A message is printed if the
     * vendor does not carry the item, the item is sold out, or the balance isn't enough to cover its cost.
     *
     * @param name the name of the item to purchase (e.g. "Candy" or "Gum")
     * @return true if the purchase went through, false otherwise
     */
    boolean select(String name){
        Item item = stock.get(name);
        if (item == null){
            System.out.println("Sorry, don't know that item");
            return false;
        }
        // Without this check, stock could go negative on an empty vendor
        if (item.stock <= 0){
            System.out.println("Sorry, " + name + " is sold out");
            return false;
        }
        // includes any discount
        double cost = item.getSalePrice();
        if (balance < cost){
            System.out.println("Gimme more money");
            return false;
        }
        item.purchase(1);
        this.balance = this.balance - cost;
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
    boolean restock(String name, int amount){
        if (amount <= 0){
            throw new IllegalArgumentException("Restock amount must be positive");
        }
        Item item = stock.get(name);
        if (item == null){
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
     * Removes a discontinued item from the inventory
     *
     * @param name the item to remove
     * @return true if the item was removed, false if the vendor did not carry it.
     */
    boolean removeItem(String name){
        return stock.remove(name)!=null;
    }

    /**
     * Returns how many pieces of the named item have been sold.
     *
     * @return the number sold, or 0 if this vendor doesn't carry the item
     */
    int getPurchaseCount(String name){
        Item item = stock.get(name);
        return item == null ? 0: item.purchaseCount;
    }

    // returns the total number of pieces sold across all items.
    int getTotalPurchases(){
        int total = 0;
        for(Item item : stock.values()){
            total = total+item.purchaseCount;
        }
        return total;
    }

    /**
     * Returns the item that has sold the most pieces.
     * If two items are tied, the one that comes first alphabetically wins.
     *
     * @return the most popular item's name, or null if nothing has sold yet
     */
    String getMostPopularItem(){
        String mostPopular = null;
        int highestCount = 0;
        for(Map.Entry<String, Item> entry : stock.entrySet()){
            if(entry.getValue().purchaseCount > highestCount){
                mostPopular = entry.getKey();
                highestCount = entry.getValue().purchaseCount;
            }
        }
        return mostPopular;
    }


    /**
     * Returns every item ordered from the most sold to the least (showing trending items).
     * Items with the same count stay in alphabetical order.
     *
     */
    List<String> getItemsByPop(){
        List<String> names = new ArrayList<>(stock.keySet());
        //starts alphabetically

        // List.sort keeps the ties alphabetical order
        names.sort((a, b) -> Integer.compare(stock.get(b).purchaseCount, stock.get(a).purchaseCount));
        return names;
    }


    // Builds a printable list of this vendor's items, sorted by name.
    String getInventoryReport(){
        StringBuilder report = new StringBuilder("=== " + name + " ===" + System.lineSeparator());
        if (stock.isEmpty()){
            report.append("(no items)").append(System.lineSeparator());
        }
        for (Map.Entry<String, Item> entry : stock.entrySet()){
            Item item = entry.getValue();
            report.append(String.format("%s: %d in stock @ $%.2f",
                    entry.getKey(), item.stock, item.getSalePrice()));
            if (item.discountPercent > 0){
                report.append(" (").append(item.discountPercent).append("% off)");
            }
            if(item.bestseller){
                report.append(" [Bestseller]");
            }
            report.append(System.lineSeparator());
        }
        return report.toString();
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
    void restock(String name, int amount, double price){
        // Validate everything before changing the inventory
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Item name must not be blank");
        }
        if (amount <= 0){
            throw new IllegalArgumentException("Restock amount must be positive");
        }
        if (price <= 0){
            throw new IllegalArgumentException("Price must be positive");
        }
        if (!stock.containsKey(name)){
            stock.put(name, new Item(price, 0));
        }
        stock.get(name).restock(amount);
    }
}



