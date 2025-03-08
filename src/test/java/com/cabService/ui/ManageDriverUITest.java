package com.cabService.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.support.ui.Select;

public class ManageDriverUITest {

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

    @Test
    public void testManageDriversPage() {
        // Navigate directly to manageDrivers.jsp page
        driver.get("http://localhost:8080/cab-service/pages/manageDrivers.jsp");

        // Ensure the correct page is loaded
        assertTrue(driver.getTitle().contains("Manage Drivers"), "Incorrect page title!");

        // Add a new driver
        WebElement nicField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nic")));
        WebElement nameField = driver.findElement(By.name("name"));
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement phoneField = driver.findElement(By.name("phone"));
        WebElement licenseField = driver.findElement(By.name("licenseNumber"));
        WebElement vehicleTypeDropdown = driver.findElement(By.name("vehicleType"));
        WebElement vehicleModelField = driver.findElement(By.name("vehicleModel"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        nicField.sendKeys("S123456789V");
        nameField.sendKeys("John Doe");
        emailField.sendKeys("johndoe@example.com");
        phoneField.sendKeys("1234567890");
        licenseField.sendKeys("ABC123456");
        Select select = new Select(vehicleTypeDropdown);
        select.selectByVisibleText("Car");
        vehicleModelField.sendKeys("Toyota Corolla");

        submitButton.click();

        try {
            // Wait for and accept the driver addition success alert
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            assertEquals("Driver added successfully!", alert.getText());
            alert.accept();
        } catch (NoAlertPresentException e) {
            fail("Driver added success alert not found: " + e.getMessage());
        }

        // Verify the driver is added (can check for driver list update or success message)
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-message")));
        assertTrue(successMessage.getText().contains("Driver added successfully!"), "Driver addition failed!");

        // Verify the driver appears in the table
        WebElement driverTable = driver.findElement(By.tagName("table"));
        assertTrue(driverTable.getText().contains("John Doe"), "New driver not found in the table!");

        // Optionally, check if delete functionality works
        WebElement deleteButton = driver.findElement(By.xpath("//button[contains(text(),'Delete')]"));
        deleteButton.click();
        Alert deleteAlert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Are you sure you want to delete this driver?", deleteAlert.getText());
        deleteAlert.accept();

        // Wait for driver removal and verify
        wait.until(ExpectedConditions.invisibilityOf(deleteButton));
        assertFalse(driverTable.getText().contains("John Doe"), "Driver was not deleted successfully!");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
