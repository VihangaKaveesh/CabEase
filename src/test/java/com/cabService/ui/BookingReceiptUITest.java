package com.cabService.ui;

import com.cabService.ui.components.CustomerLoginComponent;
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
    void testReceiptPageLoads() {
        loginComponent.loginAsCustomer("john.doe@example.com", "password123");
         // Redirect to the booking receipt page with a valid booking ID
        driver.get("http://localhost:8080/cab-service/pages/receipt.jsp?bookingID=65");
        
        // Verify if the receipt page loads correctly
        String expectedTitle = "Booking Receipt";
        assertEquals(expectedTitle, driver.getTitle(), "Page title does not match.");
    }

    @Test
    void testReceiptDetailsDisplayed() {
        
        loginComponent.loginAsCustomer("john.doe@example.com", "password123");
         // Redirect to the booking receipt page with a valid booking ID
        driver.get("http://localhost:8080/cab-service/pages/receipt.jsp?bookingID=65");
        
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
