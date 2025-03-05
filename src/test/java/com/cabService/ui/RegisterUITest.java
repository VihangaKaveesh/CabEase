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

public class RegisterUITest {

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
    void testRegisterCustomer_Failure() {
        // Open the registration page with an error message query parameter
        driver.get("http://localhost:8080/cab-service/pages/register.jsp?error=Registration failed. Try again.");

        // Wait for the error message to appear
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@style='color: red;']")));

        // Verify the error message
        assertNotNull(errorMessage);
        assertEquals("Registration failed. Try again.", errorMessage.getText());
    }

    @Test
    void testUIElements() {
        // Open the registration page
        driver.get("http://localhost:8080/cab-service/pages/register.jsp");

        // Check if the required elements are present
        WebElement nicInput = driver.findElement(By.name("nic"));
        WebElement nameInput = driver.findElement(By.name("name"));
        WebElement emailInput = driver.findElement(By.name("email"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement phoneInput = driver.findElement(By.name("phone"));
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));

        // Assert the elements are visible
        assertTrue(nicInput.isDisplayed());
        assertTrue(nameInput.isDisplayed());
        assertTrue(emailInput.isDisplayed());
        assertTrue(passwordInput.isDisplayed());
        assertTrue(phoneInput.isDisplayed());
        assertTrue(submitButton.isDisplayed());
    }

    @Test
void testMessagePopup() {
    // Open the registration page with a success message query parameter
    driver.get("http://localhost:8080/cab-service/pages/login.jsp?message=Registration-successful!-You can now log in.");

    try {
        // Increase wait time to 15 seconds
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        Alert alert = longWait.until(ExpectedConditions.alertIsPresent());

        assertEquals("Registration-successful!-You can now log in.", alert.getText());
        alert.accept(); // Close the alert

    } catch (Exception e) {
        fail("Alert not found: " + e.getMessage()); // Fail the test if alert doesn't appear
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
