package com.cabService.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManageBookingUITest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }
    
    // Helper method to perform login with alert handling
    private void loginAsManager() {
        driver.get("http://localhost:8080/cab-service/pages/login.jsp");

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        // Provide valid credentials
        emailField.sendKeys("jane.smith@example.com");
        passwordField.sendKeys("hashed_password_456");
        loginButton.click();

        try {
            // Wait for and accept login alert
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            assertEquals("Welcome Manager!", alert.getText());
            alert.accept();
        } catch (NoAlertPresentException e) {
            fail("Login success alert not found: " + e.getMessage());
        }

        // Wait for redirection to the customer dashboard
        wait.until(ExpectedConditions.urlContains("managementDashboard.jsp"));
    }
    

    @Test
    void testManageBookingsPageLoads() {
        
        loginAsManager();
         driver.get("http://localhost:8080/cab-service/pages/manageBookings.jsp");

        // Verify the page title
        assertEquals("Manage Bookings", driver.getTitle(), "Page title is incorrect");

        // Check if the table is present
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")));
        assertNotNull(table, "Manage bookings table is not visible");

        // Verify table headers (Updated to match 10 headers)
        List<WebElement> headers = table.findElements(By.tagName("th"));
        assertEquals(10, headers.size(), "Table headers do not match expected columns");

        assertEquals("Booking ID", headers.get(0).getText());
        assertEquals("Customer ID", headers.get(1).getText());
        assertEquals("Pickup Location", headers.get(2).getText());
        assertEquals("Dropoff Location", headers.get(3).getText());
        assertEquals("Driver ID", headers.get(4).getText());
        assertEquals("Package Name", headers.get(5).getText());
        assertEquals("Vehicle Type", headers.get(6).getText());
        assertEquals("Price (LKR)", headers.get(7).getText());
        assertEquals("Status", headers.get(8).getText());
        assertEquals("Actions", headers.get(9).getText());

        // Verify if there are booking records in the table
        List<WebElement> rows = table.findElements(By.xpath("//tr[position()>1]"));
        assertTrue(rows.size() > 0, "No bookings found in the manage bookings table");

        // Validate each row has expected columns (10)
        for (WebElement row : rows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            assertEquals(10, columns.size(), "Row does not have expected 10 columns");
        }
    }


    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
