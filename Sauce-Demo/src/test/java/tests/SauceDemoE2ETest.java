package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.github.javafaker.Faker;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SauceDemoE2ETest {

    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeTest
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        ExtentSparkReporter spark = new ExtentSparkReporter("SauceDemoReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @Test
    public void sauceDemoTest() {
        test = extent.createTest("Sauce Demo End-to-End Test", "This test validates the full checkout workflow.");

        driver.get("https://www.saucedemo.com/");
        test.info("Navigated to saucedemo.com");

        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.login("standard_user", "secret_sauce");
        test.info("Logged in with standard_user");

        productsPage.applyPriceLowToHighFilter();
        test.info("Applied Price (Low to High) filter");

        String product1Name = "Sauce Labs Onesie";
        String product2Name = "Sauce Labs Bike Light";
        productsPage.addProductToCart(product1Name);
        productsPage.addProductToCart(product2Name);
        test.info("Added '" + product1Name + "' and '" + product2Name + "' to cart");

        CartPage cartPage = productsPage.goToCart();
        test.info("Navigated to cart page");

        Assert.assertEquals(cartPage.getProductName(product1Name), product1Name);
        Assert.assertEquals(cartPage.getProductPrice(product1Name), "$7.99");
        Assert.assertEquals(cartPage.getProductName(product2Name), product2Name);
        Assert.assertEquals(cartPage.getProductPrice(product2Name), "$9.99");
        test.pass("SUCCESS: Product details in cart are validated.");

        cartPage.removeProduct(product1Name);
        test.info("Removed '" + product1Name + "' from cart");

        productsPage = cartPage.clickContinueShopping();
        test.info("Navigated back to products page");
        
        productsPage.addProductToCart(product1Name);
        test.info("Re-added '" + product1Name + "' to cart");

        cartPage = productsPage.goToCart();
        test.info("Returned to cart page");

        CheckoutPage checkoutPage = cartPage.clickCheckout();
        test.info("Clicked on Checkout");

        Faker faker = new Faker();
        checkoutPage.enterCheckoutInformation(faker.name().firstName(), faker.name().lastName(), faker.address().zipCode());
        test.info("Entered randomly generated checkout information");

        checkoutPage.clickContinue();
        test.info("Clicked on Continue on checkout page");

        Assert.assertEquals(checkoutPage.getTotalPrice(), "Total: $19.42");
        test.pass("SUCCESS: Total price on overview page is validated.");

        checkoutPage.clickFinish();
        test.info("Clicked on Finish");

        Assert.assertEquals(checkoutPage.getSuccessMessage(), "Thank you for your order!");
        test.pass("SUCCESS: Order success message is validated.");

        productsPage = checkoutPage.clickBackToHome();
        test.info("Clicked on Back to Home");

        productsPage.logout();
        test.info("Logged out successfully");
    }

    @AfterTest
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
        extent.flush();
    }
}