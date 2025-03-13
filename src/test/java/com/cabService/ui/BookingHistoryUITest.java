package com.cabService.ui;

import com.cabService.ui.components.CustomerLoginComponent;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingHistoryUITest {

    private WebDriver driver;
    private WebDriverWait wait;
    private CustomerLoginComponent loginComponent;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginComponent = new CustomerLoginComponent(driver);


    }
    @Test
    void testBookingHistoryPageLoads() {
         loginComponent.loginAsCustomer("john.doe@example.com", "password123");
        driver.get("http://localhost:8080/cab-service/pages/bookingHistory.jsp");

        // Verify the page title
        assertEquals("Booking History", driver.getTitle(), "Page title is incorrect");

        // Check if the table is present
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")));
        assertNotNull(table, "Booking history table is not visible");

        // Verify table headers
        List<WebElement> headers = table.findElements(By.tagName("th"));
        assertEquals(7, headers.size(), "Table headers do not match expected columns");

        assertEquals("Pickup Location", headers.get(0).getText());
        assertEquals("Dropoff Location", headers.get(1).getText());
        assertEquals("Date", headers.get(2).getText());
        assertEquals("Vehicle Type", headers.get(3).getText());
        assertEquals("Price", headers.get(4).getText());
        assertEquals("Status", headers.get(5).getText());
        assertEquals("Receipt", headers.get(6).getText());

        // Verify if there are booking records in the table
        List<WebElement> rows = table.findElements(By.xpath("//tr[position()>1]"));
        assertTrue(rows.size() > 0, "No booking history found");

        // Validate each row has 7 columns
        for (WebElement row : rows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            assertEquals(7, columns.size(), "Row does not have expected 7 columns");
        }
    }

//    @Test
//    void testReceiptButtonFunctionality() {
//        driver.get("http://localhost:8080/cab-service/pages/bookingHistory.jsp");
//
//        List<WebElement> receiptButtons = driver.findElements(By.xpath("//form[@action='receipt.jsp']//button"));
//        if (!receiptButtons.isEmpty()) {
//            receiptButtons.get(0).click(); // Click the first receipt button
//            wait.until(ExpectedConditions.urlContains("receipt.jsp"));
//
//            // Verify that the receipt page loads
//            assertTrue(driver.getCurrentUrl().contains("receipt.jsp"), "Receipt page did not open correctly");
//        } else {
//            System.out.println("No bookings with a receipt found.");
//        }
//    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
