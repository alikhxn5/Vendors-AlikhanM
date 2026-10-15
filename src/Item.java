/**
 * An item sold by a vendor, with its price, how many are in stock,
 * and how many have been sold.
 */
class Item {
    double price;
    int stock;
    int purchaseCount;
    // total pieces sold so far

    Item(double price, int numPieces){
        this.price = price;
        this.stock = numPieces;
    }

    void restock(int amount){
        this.stock = this.stock+amount;
    }

    // removes sold pieces from stock and records the sale
    void purchase(int amount){
        this.stock = this.stock - amount;
        this.purchaseCount = this.purchaseCount + amount;
    }
}