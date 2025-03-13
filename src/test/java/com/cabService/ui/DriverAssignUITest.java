package com.cabService.ui;

import com.cabService.ui.components.ManagerLoginComponent;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class DriverAssignUITest {
    private WebDriver driver;
    private WebDriverWait wait;
    private ManagerLoginComponent loginComponent;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginComponent = new ManagerLoginComponent(driver);
    }

    @Test
    void testAssignDriverToBooking() {
         loginComponent.loginAsManager("jane.smith@example.com", "hashed_password_456");

        // 4️⃣ Navigate to driver assignment page
        driver.get("http://localhost:8080/cab-service/pages/driverAssign.jsp");
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
List<WebElement> options = driverDropdown.getOptions();

if (options.size() > 1) { // Ensure at least two options exist
    driverDropdown.selectByIndex(1);
} else {
    System.out.println("Not enough drivers available to assign.");
}


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
