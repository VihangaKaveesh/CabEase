package com.cabService.ui;

import com.cabService.ui.components.ManagerLoginComponent;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EditDriverUITest {

    private WebDriver driver;
    private WebDriverWait wait;
    private ManagerLoginComponent loginComponent;

    @BeforeAll
    void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
          loginComponent = new ManagerLoginComponent(driver);
    }

    @Test
    void testUIElements() {
        loginComponent.loginAsManager("jane.smith@example.com", "hashed_password_456");

        driver.get("http://localhost:8080/cab-service/pages/editDrivers.jsp?driverID=13");

        WebElement nameInput = driver.findElement(By.name("name"));
        WebElement emailInput = driver.findElement(By.name("email"));
        WebElement phoneInput = driver.findElement(By.name("phone"));
        WebElement vehicleModelInput = driver.findElement(By.name("vehicleModel"));
        WebElement statusDropdown = driver.findElement(By.name("status"));
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));

        assertTrue(nameInput.isDisplayed());
        assertTrue(emailInput.isDisplayed());
        assertTrue(phoneInput.isDisplayed());
        assertTrue(vehicleModelInput.isDisplayed());
        assertTrue(statusDropdown.isDisplayed());
        assertTrue(submitButton.isDisplayed());
    }

   @Test
    void testEditDriver_Success() {
        loginComponent.loginAsManager("jane.smith@example.com", "hashed_password_456");

        driver.get("http://localhost:8080/cab-service/pages/editDrivers.jsp?driverID=13");

        WebElement nameInput = driver.findElement(By.name("name"));
        WebElement emailInput = driver.findElement(By.name("email"));
        WebElement phoneInput = driver.findElement(By.name("phone"));
        WebElement vehicleModelInput = driver.findElement(By.name("vehicleModel"));
        WebElement statusDropdown = driver.findElement(By.name("status"));
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));

        // Clear and enter new values
        nameInput.clear();
        nameInput.sendKeys("Updated Name");

        emailInput.clear();
        emailInput.sendKeys("updated@example.com");

        phoneInput.clear();
        phoneInput.sendKeys("0712345679");

        vehicleModelInput.clear();
        vehicleModelInput.sendKeys("Updated Model");

        statusDropdown.sendKeys("Assigned");

        submitButton.click();

         // Handle the alert
    Alert alert = wait.until(ExpectedConditions.alertIsPresent());
    String alertMessage = alert.getText();
    assertTrue(alertMessage.contains("Driver edited successfully"), "Driver added alert not displayed");
    alert.accept();

        // Verify redirection to manageDrivers.jsp with success message
        wait.until(ExpectedConditions.urlContains("manageDrivers.jsp?message="));
    }

    @Test
    void testEditDriver_Failure() {
        loginComponent.loginAsManager("jane.smith@example.com", "hashed_password_456");

        driver.get("http://localhost:8080/cab-service/pages/editDrivers.jsp?driverID=999");

        WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//p[contains(text(), 'Driver not found')]")
        ));

        assertNotNull(errorMessage);
        assertEquals("Driver not found.", errorMessage.getText());
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
