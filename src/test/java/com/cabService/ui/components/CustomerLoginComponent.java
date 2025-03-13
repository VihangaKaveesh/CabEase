/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.ui.components;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerLoginComponent {
    private WebDriver driver;
    private WebDriverWait wait;

    public CustomerLoginComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void loginAsCustomer(String email, String password) {
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
            assertEquals("Welcome Customer!", alert.getText());
            alert.accept();
        } catch (NoAlertPresentException e) {
            fail("Login success alert not found: " + e.getMessage());
        }

        // Wait for redirection to the customer dashboard
        wait.until(ExpectedConditions.urlContains("customerDashboard.jsp"));
    }
}