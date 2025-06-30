package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;

public class SauceDemoE2ETest {

    private WebDriver driver;

    @BeforeTest
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    public void sauceDemoTest() {

        driver.get("https://www.saucedemo.com/");

        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.login("standard_user", "secret_sauce");

        productsPage.applyPriceLowToHighFilter();

        String product1Name = "Sauce Labs Onesie";
        String product2Name = "Sauce Labs Bike Light";
        productsPage.addProductToCart(product1Name);
        productsPage.addProductToCart(product2Name);

        CartPage cartPage = productsPage.goToCart();

        Assert.assertEquals(cartPage.getProductName(product1Name), product1Name);
        Assert.assertEquals(cartPage.getProductPrice(product1Name), "$7.99");
        Assert.assertEquals(cartPage.getProductName(product2Name), product2Name);
        Assert.assertEquals(cartPage.getProductPrice(product2Name), "$9.99");

        cartPage.removeProduct(product1Name);

        productsPage = cartPage.clickContinueShopping();
        
        productsPage.addProductToCart(product1Name);

        cartPage = productsPage.goToCart();

        CheckoutPage checkoutPage = cartPage.clickCheckout();

        Faker faker = new Faker();
        checkoutPage.enterCheckoutInformation(faker.name().firstName(), faker.name().lastName(), faker.address().zipCode());

        checkoutPage.clickContinue();

        Assert.assertEquals(checkoutPage.getTotalPrice(), "Total: $19.42");

        checkoutPage.clickFinish();

        Assert.assertEquals(checkoutPage.getSuccessMessage(), "Thank you for your order!");

        productsPage = checkoutPage.clickBackToHome();

        productsPage.logout();
    }

    @AfterTest
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}