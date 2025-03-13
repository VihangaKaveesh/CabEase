package com.cabService.ui;

import com.cabService.ui.components.ManagerLoginComponent;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManageCustomersUITest {

    private WebDriver driver;
    private WebDriverWait wait;
    private ManagerLoginComponent loginComponent;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().driverVersion("134.0.6998.36").setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginComponent = new ManagerLoginComponent(driver);
    }

    @Test
    void testCustomerTableLoads() {
         loginComponent.loginAsManager("jane.smith@example.com", "hashed_password_456");
        driver.get("http://localhost:8080/cab-service/pages/manageCustomers.jsp");

        // Wait for the customer table to be visible
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")));
        assertNotNull(table, "Customer table is not visible");

        // Verify that the table headers exist
        List<WebElement> headers = table.findElements(By.tagName("th"));
        assertEquals(6, headers.size(), "Table headers do not match expected columns");

        // Check if the headers match expected values
        assertEquals("Customer ID", headers.get(0).getText());
        assertEquals("NIC", headers.get(1).getText());
        assertEquals("Name", headers.get(2).getText());
        assertEquals("Email", headers.get(3).getText());
        assertEquals("Phone", headers.get(4).getText());
        assertEquals("Actions", headers.get(5).getText());

        // Check if at least one row of customer data exists
        List<WebElement> rows = table.findElements(By.xpath("//tr[position()>1]"));
        assertTrue(rows.size() > 0, "No customer data found in the table");

        // Verify each row has 6 columns
        for (WebElement row : rows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            assertEquals(6, columns.size(), "Row does not have expected 6 columns");
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
