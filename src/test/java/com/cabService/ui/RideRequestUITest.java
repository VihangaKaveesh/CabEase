package com.cabService.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class RideRequestUITest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().driverVersion("134.0.6998.36").setup();
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

        // Provide valid credentials (change accordingly)
        emailField.sendKeys("customer@example.com");
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
    public void testRideRequestFormSubmission() {
        // Perform login before accessing the page
        loginAsCustomer();

        // Navigate to rideRequest.jsp after login
        driver.get("http://localhost:8080/cab-service/pages/rideRequest.jsp");

        // Ensure the correct page is loaded
        assertTrue(driver.getTitle().contains("Request a Ride"), "Incorrect page title!");

        // Fill the form
        WebElement pickupField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pickupLocation")));
        WebElement dropoffField = driver.findElement(By.name("dropoffLocation"));
        WebElement packageDropdown = driver.findElement(By.name("packageId"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        pickupField.sendKeys("123 Main Street");
        dropoffField.sendKeys("456 Elm Street");

        // Use Select class to choose an option
        Select select = new Select(packageDropdown);
        select.selectByIndex(0);

        submitButton.click();

        try {
            // Wait for and accept the ride request success alert
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            assertEquals("Ride request submitted successfully!", alert.getText());
            alert.accept();
        } catch (NoAlertPresentException e) {
            fail("Ride request success alert not found: " + e.getMessage());
        }

        // Wait for redirection after submission
        wait.until(ExpectedConditions.urlContains("customerDashboard.jsp"));

        // Verify successful redirection
        assertTrue(driver.getCurrentUrl().contains("pages/customerDashboard.jsp"), "Ride request submission failed!");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
