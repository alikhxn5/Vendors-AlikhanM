/**
 * An item sold by a vendor, with its price, how many are in stock,
 * how many have been sold, and a description customers can read.
 */
class Item{
    static final String DEFAULT_DESCRIPTION = "No description available.";

    double price;
    int stock;
    int purchaseCount;      // total pieces sold so far
    String description;

    Item(double price, int numPieces){
        this(price, numPieces, DEFAULT_DESCRIPTION);
    }

    Item(double price, int numPieces, String description){
        this.price = price;
        this.stock = numPieces;
        this.description = description;
    }

    void restock(int amount){
        this.stock = this.stock + amount;
    }

    /** removes sold pieces from stock and records the sale */
    void purchase(int amount){
        this.stock = this.stock - amount;
        this.purchaseCount = this.purchaseCount + amount;
    }
}