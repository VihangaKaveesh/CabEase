package com.cabService.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;

public class BookingHistoryUITest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().driverVersion("134.0.6998.36").setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
void testLoginSuccess_Customer() {
    // Open the login page
    driver.get("http://localhost:8080/cab-service/pages/login.jsp");

    // Find the email and password fields
    WebElement emailInput = driver.findElement(By.name("email"));
    WebElement passwordInput = driver.findElement(By.name("password"));
    WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));

    // Enter valid credentials for customer
    emailInput.sendKeys("john.doe@example.com");
    passwordInput.sendKeys("password123");

    // Submit the form
    submitButton.click();

    try {
        // Wait for the alert to appear
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        Alert alert = longWait.until(ExpectedConditions.alertIsPresent());

        // Verify the alert text and accept the alert
        assertEquals("Welcome Customer!", alert.getText());
        alert.accept(); // Close the alert
    } catch (NoAlertPresentException e) {
        fail("Alert not found: " + e.getMessage()); // Fail the test if the alert doesn't appear
    }
}

    @AfterEach
    void tearDown() {
        // Close the browser after the test
        if (driver != null) {
            driver.quit();
        }
    }
}
