package com.cabService.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

public class DriverAssignUITest {
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

    @Test
    void testAssignDriverToBooking() {
        // Open login page
        driver.get("http://localhost:8080/cab-service/pages/login.jsp");

        //  Log in as management
        driver.findElement(By.name("email")).sendKeys("jane.smith@example.com");
        driver.findElement(By.name("password")).sendKeys("hashed_password_456");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        
         //Handle welcome manager alert
        try {
            // Wait for the alert and accept it
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            assertEquals("Welcome Manager!", alert.getText());
            alert.accept();
        } catch (Exception e) {
            System.out.println("No welcome alert found.");
        }

        // Wait for dashboard to load
        wait.until(ExpectedConditions.urlContains("managementDashboard.jsp"));

        // 4️⃣ Navigate to driver assignment page
        driver.findElement(By.linkText("Assign a driver")).click();
        wait.until(ExpectedConditions.urlContains("driverAssign.jsp"));

        // Verify page loaded correctly
        assertTrue(driver.findElement(By.tagName("h2")).getText().contains("Assign Drivers"));

        // 6️⃣ Check if there are pending bookings
        WebElement table = driver.findElement(By.tagName("table"));
        assertTrue(table.isDisplayed(), "Booking table should be visible");

        // If a booking exists, assign a driver
        if (driver.findElements(By.name("bookingID")).size() > 0) {
            WebElement bookingIDInput = driver.findElement(By.name("bookingID"));
            WebElement driverSelect = driver.findElement(By.name("driverID"));
            WebElement assignButton = driver.findElement(By.xpath("//button[@type='submit']"));

            assertTrue(driverSelect.isDisplayed(), "Driver dropdown should be visible");
            assertTrue(assignButton.isDisplayed(), "Assign button should be visible");

            //  Select first available driver
            Select driverDropdown = new Select(driverSelect);
            driverDropdown.selectByIndex(1);

            // Click assign button
            assignButton.click();

            // Verify success message
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            assertEquals("Driver assigned successfully", alert.getText());
            alert.accept();
        } else {
            System.out.println("No pending bookings available to test assignment.");
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
