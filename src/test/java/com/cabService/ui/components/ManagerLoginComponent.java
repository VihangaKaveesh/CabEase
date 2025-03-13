package com.cabService.ui.components;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerLoginComponent {
    private WebDriver driver;
    private WebDriverWait wait;

    public ManagerLoginComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void loginAsManager(String email, String password) {
        driver.get("http://localhost:8080/cab-service/pages/login.jsp");

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        loginButton.click();

        try {
            // Wait for and accept login alert
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            assertEquals("Welcome Manager!", alert.getText());
            alert.accept();
        } catch (NoAlertPresentException e) {
            fail("Login success alert not found: " + e.getMessage());
        }

        // Wait for redirection to the management dashboard
        wait.until(ExpectedConditions.urlContains("managementDashboard.jsp"));
    }
}
