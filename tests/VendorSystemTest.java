import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class VendorSystemTest {

    private VendorSystem system;

    // Fresh system before every test: 5 vendors, each with 10 Candy and 5 Gum
    @BeforeEach
    void setUp() {
        system = new VendorSystem(10, 5);
    }

    @Test
    void systemManagesFiveVendors() {
        assertEquals(5, system.getVendorCount());
        for (int i = 1; i <= 5; i++) {
            assertEquals("Vendor " + i, system.getVendor(i).getName());
        }
    }

    @Test
    void vendorsInSystemHaveSeparateInv() {
        system.getVendor(1).restock("Candy", 5);
        assertEquals(15, system.getVendor(1).getStock("Candy"));
        assertEquals(10, system.getVendor(2).getStock("Candy"));
    }

    @Test
    void invalidVendorNumberIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> system.getVendor(0));
        assertThrows(IllegalArgumentException.class, () -> system.getVendor(6));
    }

    @Test
    void invReportListsAllVendorsAndItems() {
        String report = system.getInventoryReport();
        for (int i = 1; i <= 5; i++) {
            assertTrue(report.contains("=== Vendor " + i + " ==="));
        }
        assertTrue(report.contains("Candy: 10 in stock @ $1.25"));
        assertTrue(report.contains("Gum: 5 in stock @ $0.50"));
    }

    @Test
    void invReportShowsChangesToOneVendorOnly() {
        system.getVendor(3).restock("Chips", 4, 1.50);
        assertTrue(system.getVendor(3).getInventoryReport().contains("Chips: 4 in stock @ $1.50"));
        assertFalse(system.getVendor(4).getInventoryReport().contains("Chips"));
    }

    @Test
    void printInvWritesReportToConsole() {
        // Temporarily capture System.out so the printed text can be checked
        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
        try {
            system.printInventory();
        } finally {
            System.setOut(originalOut);
        }
        assertEquals(system.getInventoryReport(), captured.toString());
    }
}