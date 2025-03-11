package com.cabService.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BookingReceiptUITest {
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
    private void loginAsCustomer() {
        driver.get("http://localhost:8080/cab-service/pages/login.jsp");

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        // Provide valid credentials
        emailField.sendKeys("john.doe@example.com");
        passwordField.sendKeys("password123");
        loginButton.click();

        try {
            // Wait for and accept login alert
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            assertEquals("Welcome Customer!", alert.getText());
            alert.accept();
        } catch (NoAlertPresentException e) {
            fail("Login success alert not found: " + e.getMessage());
        }

        // Wait for redirection to the customer dashboard
        wait.until(ExpectedConditions.urlContains("customerDashboard.jsp"));
    }
       
    

    @Test
    void testReceiptPageLoads() {
        loginAsCustomer();
         // Redirect to the booking receipt page with a valid booking ID
        driver.get("http://localhost:8080/cab-service/pages/receipt.jsp?bookingID=63");
        
        // Verify if the receipt page loads correctly
        String expectedTitle = "Booking Receipt";
        assertEquals(expectedTitle, driver.getTitle(), "Page title does not match.");
    }

    @Test
    void testReceiptDetailsDisplayed() {
        
        loginAsCustomer();
         // Redirect to the booking receipt page with a valid booking ID
        driver.get("http://localhost:8080/cab-service/pages/receipt.jsp?bookingID=63");
        
        // Check if the table is displayed
        WebElement receiptTable = driver.findElement(By.tagName("table"));
        assertNotNull(receiptTable, "Receipt table not found on page.");

        // Validate if key receipt details are present
        assertTrue(driver.getPageSource().contains("Pickup Location"), "Pickup Location missing");
        assertTrue(driver.getPageSource().contains("Dropoff Location"), "Dropoff Location missing");
        assertTrue(driver.getPageSource().contains("Vehicle Type"), "Vehicle Type missing");
        assertTrue(driver.getPageSource().contains("Driver Name"), "Driver Name missing");
    }
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
