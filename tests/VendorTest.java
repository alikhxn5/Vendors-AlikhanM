import org.junit.
        jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class VendorTest {

    private static final double DELTA = 0.001;

    private Vendor vendor;

    @BeforeEach
    void setUp(){
        vendor = new Vendor(10, 5);
    }

    @Test
    void newVenStartStock(){
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
}