import java.util.ArrayList;
import java.util.List;

/**
 * Manages a group of five NPC vendors and prints their inventories.
 */
class VendorSystem{
    static final int NUM_VENDORS = 5;

    private final List<Vendor> vendors = new ArrayList<>();

    /**
     * Creates five vendors, named "Vendor 1" through "Vendor 5",
     * each stocked with the given amounts of Candy and Gum.
     */
    VendorSystem(int candyEach, int gumEach){
        for (int i = 1; i <= NUM_VENDORS; i++){
            vendors.add(new Vendor("Vendor " + i, candyEach, gumEach));
        }
    }

    /** returns how many vendors the system manages */
    int getVendorCount(){
        return vendors.size();
    }

    /**
     * Returns a vendor by its number.
     *
     * @param number the vendor number, from 1 to 5
     * @throws IllegalArgumentException if there is no vendor with that number
     */
    Vendor getVendor(int number){
        if (number < 1 || number > vendors.size()){
            throw new IllegalArgumentException("Vendor number must be between 1 and " + vendors.size());
        }
        return vendors.get(number - 1);
    }

    /** builds one report that lists every vendor's inventory */
    String getInventoryReport(){
        StringBuilder report = new StringBuilder();
        for (Vendor vendor : vendors){
            report.append(vendor.getInventoryReport()).append(System.lineSeparator());
        }
        return report.toString();
    }

    /** prints every vendor's inventory to the console */
    void printInventory(){
        System.out.print(getInventoryReport());
    }
}