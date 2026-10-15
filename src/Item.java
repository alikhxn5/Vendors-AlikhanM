/**
 * An item sold by a vendor, with its price, how many are in stock,how many have been sold, a description customers can
 * read, and a category and discount used for sales and promotions.
 */
class Item{
    static final String DEFAULT_DESCRIPTION = "No description available.";
    static final String DEFAULT_CATEGORY = "General";
    // full price, before any discount
    double price;
    int stock;

    // total pieces sold so far
    int purchaseCount;
    String description;
    String category;
    int discountPercent;


    // 0 means no discount, 25 means 25% off
    Item(double price, int numPieces){
        this(price, numPieces, DEFAULT_DESCRIPTION);
    }

    Item(double price, int numPieces, String description){
        this(price, numPieces, description, DEFAULT_CATEGORY);
    }

    Item(double price, int numPieces, String description, String category){
        this.price = price;
        this.stock = numPieces;
        this.description = description;
        this.category = category;
    }

    //returns the price a customer pays right now, after any discount
    double getSalePrice(){
        return price * (100 - discountPercent) / 100.0;
    }

    void restock(int amount){
        this.stock = this.stock + amount;
    }

    // removes sold pieces from stock and records the sale
    void purchase(int amount){
        this.stock = this.stock - amount;
        this.purchaseCount = this.purchaseCount + amount;
    }
}