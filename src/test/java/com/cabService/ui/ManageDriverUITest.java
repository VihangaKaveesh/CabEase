package com.cabService.ui;

import com.cabService.ui.components.ManagerLoginComponent;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Ensures order of execution
public class ManageDriverUITest {
    private WebDriver driver;
    private WebDriverWait wait;
    private ManagerLoginComponent loginComponent;
    private final String BASE_URL = "http://localhost:8080/cab-service/pages/manageDrivers.jsp";

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginComponent = new ManagerLoginComponent(driver);
    }

    @Test
    @Order(1)
void testAddDriverWithDummyData() {
     loginComponent.loginAsManager("jane.smith@example.com", "hashed_password_456");
    // Open the manage drivers page
    driver.get("http://localhost:8080/cab-service/pages/manageDrivers.jsp");

    // Wait for the email field to be visible
    WebElement nicField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nic")));
    WebElement nameField = driver.findElement(By.name("name"));
    WebElement emailField = driver.findElement(By.name("email"));
    WebElement phoneField = driver.findElement(By.name("phone"));
    WebElement licenseField = driver.findElement(By.name("licenseNumber"));
    WebElement vehicleTypeField = driver.findElement(By.name("vehicleType"));
    WebElement vehicleModelField = driver.findElement(By.name("vehicleModel"));
    WebElement addButton = driver.findElement(By.xpath("//button[text()='Add Driver']"));

    // Provide dummy driver details
    nicField.sendKeys("987654321V");
    nameField.sendKeys("Jane Smith");
    emailField.sendKeys("jane.smith@example.com");
    phoneField.sendKeys("0723456789");
    licenseField.sendKeys("LIC67890");
    vehicleTypeField.sendKeys("SUV");
    vehicleModelField.sendKeys("Honda CR-V");

    // Click Add Driver button
    addButton.click();

    // Handle the alert
    Alert alert = wait.until(ExpectedConditions.alertIsPresent());
    String alertMessage = alert.getText();
    assertTrue(alertMessage.contains("Driver added"), "Driver added alert not displayed");
    alert.accept();

    // Verify driver in table
    WebElement driverTable = driver.findElement(By.tagName("table"));
    assertTrue(driverTable.getText().contains("Jane Smith"), "Driver not added to the table");
}

    @Test
    @Order(2)
public void testDeleteDriver() {
     loginComponent.loginAsManager("jane.smith@example.com", "hashed_password_456");
    // Open the manage drivers page
    driver.get("http://localhost:8080/cab-service/pages/manageDrivers.jsp");

    // Wait for the table to be visible and ensure it contains the driver "Jane Smith"
    WebElement driverTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")));
    assertTrue(driverTable.getText().contains("Jane Smith"), "Driver 'Jane Smith' not found in the table");

    // Find the delete button for the driver "Jane Smith" and click it
    WebElement deleteButton = driver.findElement(By.xpath("//td[contains(text(),'Jane Smith')]/following-sibling::td/form/button"));
    deleteButton.click();

    // Confirm the deletion in the alert
    Alert deleteAlert = wait.until(ExpectedConditions.alertIsPresent());
    String alertMessage = deleteAlert.getText();
    assertTrue(alertMessage.contains("Are you sure you want to delete this driver?"), "Delete confirmation alert missing");
    deleteAlert.accept();

    // Verify success message alert after deletion
    Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
    assertTrue(successAlert.getText().contains("Driver deleted successfully"), "Driver deletion success alert not displayed");
    successAlert.accept();

    // Ensure driver is removed from table (Check that "Jane Smith" is no longer in the table)
    driverTable = driver.findElement(By.tagName("table"));
    assertFalse(driverTable.getText().contains("Jane Smith"), "Driver 'Jane Smith' was not removed from the table after deletion");
}

@AfterEach
public void tearDown() {
    driver.quit();
}

}
