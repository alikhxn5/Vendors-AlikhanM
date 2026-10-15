import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class VendorTest {

    private static final double DELTA = 0.001;

    private Vendor vendor;

    @BeforeEach
    void setUp() {
        vendor = new Vendor(10, 5);
    }

    @Test
    void newVenStartStock() {
        assertEquals(10, vendor.getStock("Candy"));
        assertEquals(5, vendor.getStock("Gum"));
    }

    @Test
    void vendorsHaveSeparateInventories() {
        Vendor other = new Vendor(1, 1);
        // Creating a second vendor must not change the first vendor's stock
        assertEquals(10, vendor.getStock("Candy"));
        assertEquals(1, other.getStock("Candy"));
    }

    @Test
    void addMoneyIncreasesBalance() {
        vendor.addMoney(2.00);
        assertEquals(2.00, vendor.getBalance(), DELTA);
    }

    @Test
    void addMoneyAccumulatesAcrossDeposits() {
        vendor.addMoney(1.00);
        vendor.addMoney(0.75);
        assertEquals(1.75, vendor.getBalance(), DELTA);
    }

    @Test
    void addMoneyRejectsZeroOrNegativeAmounts() {
        assertThrows(IllegalArgumentException.class, () -> vendor.addMoney(0));
        assertThrows(IllegalArgumentException.class, () -> vendor.addMoney(-5));
        // A rejected deposit must not change the balance
        assertEquals(0, vendor.getBalance(), DELTA);
    }

    @Test
    void resetBalanceSetsBalanceBackToZero() {
        vendor.addMoney(3.00);
        vendor.resetBalance();
        assertEquals(0, vendor.getBalance(), DELTA);
    }

    @Test
    void buyingItemReducesStockAndBalance() {
        vendor.addMoney(2.00);
        assertTrue(vendor.select("Candy"));
        assertEquals(9, vendor.getStock("Candy"));
        assertEquals(0.75, vendor.getBalance(), DELTA);   // $2.00 - $1.25
    }

    @Test
    void buyingWithExactChangeLeavesZeroBalance() {
        vendor.addMoney(0.50);
        assertTrue(vendor.select("Gum"));
        assertEquals(4, vendor.getStock("Gum"));
        assertEquals(0, vendor.getBalance(), DELTA);
    }

    @Test
    void buyingWithoutEnoughMoneyFails() {
        vendor.addMoney(1.00);                  // Candy costs $1.25
        assertFalse(vendor.select("Candy"));
        assertEquals(10, vendor.getStock("Candy"));
        assertEquals(1.00, vendor.getBalance(), DELTA);
    }

    @Test
    void buyingUnknownItemFails() {
        vendor.addMoney(5.00);
        assertFalse(vendor.select("Chips"));
        assertEquals(5.00, vendor.getBalance(), DELTA);
    }

    @Test
    void buyingEveryItemEmptiesInventory() {
        vendor.addMoney(10 * 1.25 + 5 * 0.50);  // exactly enough for everything
        for (int i = 0; i < 10; i++) {
            assertTrue(vendor.select("Candy"));
        }
        for (int i = 0; i < 5; i++) {
            assertTrue(vendor.select("Gum"));
        }
        assertEquals(0, vendor.getStock("Candy"));
        assertEquals(0, vendor.getStock("Gum"));
        assertEquals(0, vendor.getBalance(), DELTA);
    }

    @Test
    void cantBuyItemSoldOut() {
        vendor.addMoney(20.00);
        for (int i = 0; i < 5; i++) {
            vendor.select("Gum");
        }
        //6th Gum: none left....
        assertFalse(vendor.select("Gum"));

        //stock never goes negative
        assertEquals(0, vendor.getStock("Gum"));

        // only charged for 5 gum
        assertEquals(17.50, vendor.getBalance(), DELTA);
    }

    @Test
    void emptyVendorRefusesAllPurchases() {
        Vendor empty = new Vendor(0, 0);
        empty.addMoney(5.00);
        assertFalse(empty.select("Candy"));
        assertFalse(empty.select("Gum"));
        assertEquals(5.00, empty.getBalance(), DELTA);
    }

    @Test
    void restockAddsToExistingItem() {
        assertTrue(vendor.restock("Gum", 20));
        assertEquals(25, vendor.getStock("Gum"));
    }

    @Test
    void restockedItemBuyPostSellOut() {
        Vendor empty = new Vendor(0, 0);
        empty.addMoney(1.25);
        // sold out
        assertFalse(empty.select("Candy"));

        empty.restock("Candy", 3);
        // available again
        assertTrue(empty.select("Candy"));
        assertEquals(2, empty.getStock("Candy"));
    }

    @Test
    void restockRejectZeroNegativeAmt() {
        assertThrows(IllegalArgumentException.class, () -> vendor.restock("Candy", 0));
        assertThrows(IllegalArgumentException.class, () -> vendor.restock("Candy", -4));
        assertEquals(10, vendor.getStock("Candy"));
    }

    @Test
    void restockUnknownItemWithoutPriceFails() {
        assertFalse(vendor.restock("Chips", 5));
        assertEquals(0, vendor.getStock("Chips"));
    }

    // Story: restocking adds unavailable items
    @Test
    void restockAddsNewItemToInventory() {
        assertFalse(vendor.hasItem("Chips"));
        vendor.restock("Chips", 8, 1.50);
        assertTrue(vendor.hasItem("Chips"));
        assertEquals(8, vendor.getStock("Chips"));
        assertEquals(1.50, vendor.getPrice("Chips"), DELTA);
    }

    @Test
    void newlyStockedItemPurchasable() {
        vendor.restock("Chips", 2, 1.50);
        vendor.addMoney(1.50);
        assertTrue(vendor.select("Chips"));
        assertEquals(1, vendor.getStock("Chips"));
    }

    @Test
    void restockWPriceOnExistingItemKeepsOGPrice() {
        vendor.restock("Candy", 5, 9.99);
        assertEquals(15, vendor.getStock("Candy"));
        assertEquals(1.25, vendor.getPrice("Candy"), DELTA);

    }

    @Test
    void restockNewItemRejectInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> vendor.restock("Chips", 5, 0));
        assertThrows(IllegalArgumentException.class, () -> vendor.restock("Chips", 0, 1.50));
        assertThrows(IllegalArgumentException.class, () -> vendor.restock(" ", 5, 1.50));
        assertFalse(vendor.hasItem("Chips"));   // nothing was added
    }

    @Test
    void getPriceOfUnknownItemThrows() {
        assertThrows(IllegalArgumentException.class, () -> vendor.getPrice("Chips"));
    }

    // story: rename an item
    @Test
    void renameItemKeepsDetails() {
        assertTrue(vendor.renameItem("Candy", "Chocolate Bar"));
        assertFalse(vendor.hasItem("Candy"));
        assertTrue(vendor.hasItem("Chocolate Bar"));
        assertEquals(10, vendor.getStock("Chocolate Bar"));
        assertEquals(1.25, vendor.getPrice("Chocolate Bar"), DELTA);
    }

    @Test
    void renamedBoughtbyName() {
        vendor.renameItem("Gum", "Mint Gum");
        vendor.addMoney(1.00);
        assertFalse(vendor.select("Gum"));
        assertTrue(vendor.select("Mint Gum"));
    }

    @Test
    void renamedUnknownFail() {
        assertFalse(vendor.renameItem("Chips", "Crisps"));
        assertFalse(vendor.hasItem("Crisps"));
    }

    @Test
    void renameToExistingNameFailsNoOverwriting() {
        assertFalse(vendor.renameItem("Candy", "Gum"));
        assertEquals(10, vendor.getStock("Candy"));
        assertEquals(5, vendor.getStock("Gum"));
    }

    @Test
    void renameToBlankFails() {
        assertFalse(vendor.renameItem("Candy", "   "));
        assertTrue(vendor.hasItem("Candy"));
    }

    // story: manage and print vendors
    @Test
    void vendorKeepsName() {
        Vendor named = new Vendor("Vendor 7", 1, 1);
        assertEquals("Vendor 7", named.getName());
    }

    @Test
    void inventoryReportListsAtoB() {
        vendor.restock("Apple", 3, 0.75);
        String report = vendor.getInventoryReport();
        assertTrue(report.indexOf("Apple") < report.indexOf("Candy"));
        assertTrue(report.indexOf("Candy") < report.indexOf("Gum"));
    }

    // story: remove discontinued items
    @Test
    void removeItemDeletesItFromInv() {
        assertTrue(vendor.removeItem("Gum"));
        assertFalse(vendor.hasItem("Gum"));
        assertEquals(0, vendor.getStock("Gum"));
    }

    @Test
    void removedItemCantBePurchased() {
        vendor.removeItem("Candy");
        vendor.addMoney(5.00);
        assertFalse(vendor.select("Candy"));
        assertEquals(5.00, vendor.getBalance(), DELTA);
    }

    @Test
    void removeUnkownItemsReturnsF() {
        assertFalse(vendor.removeItem("Chips"));
    }


    @Test
    void removedItemNotInReport(){
        vendor.removeItem("Gum");
        String report = vendor.getInventoryReport();
        assertFalse(report.contains("Gum"));
        assertTrue(report.contains("Candy"));
    }

    @Test
    void removingEveryItemShowsEmptyInv(){
        vendor.removeItem("Candy");
        vendor.removeItem("Gum");
        assertTrue(vendor.getInventoryReport().contains("(no items)"));
    }

}


