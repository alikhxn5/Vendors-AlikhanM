public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Amazing Bruin Shop!");

        // Five vendors, each starting with 10 Candy and 5 Gum
        VendorSystem system = new VendorSystem(10, 5);
        system.printInventory();

        System.out.println("Maintained by Alikhan Makhamadaliev");
    }
}