class Item {
    double price;
    int stock;

    Item(double price, int numPieces) {
        this.price = price;
        this.stock = numPieces;
    }

    void restock(int amount) {
        this.stock = this.stock + amount;
    }

    void purchase(int amount) {
        this.stock = this.stock - amount;
    }
}