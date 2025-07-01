package tests;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ConfigReader;

@Epic("SauceDemo Application")
@Feature("End-to-End Checkout")
public class SauceDemoE2ETest {
    public WebDriver driver;

    @BeforeTest
    public void setup() {
        // RECTIFIED: This line now handles all driver management.
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    // The rest of your test remains unchanged.
    @Test(description = "Validates the full checkout workflow from login to order confirmation.")
    @Story("Successful Order Placement")
    @Severity(SeverityLevel.BLOCKER)
    @Description("This test logs in, adds products, validates details, modifies the cart, and completes checkout.")
    public void sauceDemoTest() {
        Faker faker = new Faker();
        
        navigateToLoginPage();
        
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = performLogin(loginPage, ConfigReader.getProperty("username"), ConfigReader.getProperty("password"));
        
        productsPage = filterAndAddProducts(productsPage);
        
        CartPage cartPage = productsPage.goToCart();
        validateCart(cartPage, "Sauce Labs Onesie", "Sauce Labs Bike Light");
        
        productsPage = tweakCartAndReturn(cartPage, "Sauce Labs Onesie");
        
        CheckoutPage checkoutPage = proceedToCheckout(productsPage);
        
        fillAndContinueCheckout(checkoutPage, faker.name().firstName(), faker.name().lastName(), faker.address().zipCode());
        
        finalizeOrder(checkoutPage);
        
        productsPage = returnToHomePageAndLogout(checkoutPage);
        productsPage.logout();
    }
    
    @Step("Navigate to the SauceDemo login page")
    private void navigateToLoginPage() {
        driver.get(ConfigReader.getProperty("url"));
    }
    
    @Step("Login with username: {username}")
    private ProductsPage performLogin(LoginPage page, String username, String password) {
        return page.login(username, password);
    }
    
    @Step("Apply price filter and add two products to the cart")
    private ProductsPage filterAndAddProducts(ProductsPage page) {
        page.applyPriceLowToHighFilter();
        page.addProductToCart("Sauce Labs Onesie");
        page.addProductToCart("Sauce Labs Bike Light");
        return page;
    }
    
    @Step("Validate product details in the cart for {product1} and {product2}")
    private void validateCart(CartPage page, String product1, String product2) {
        Assert.assertEquals(page.getProductName(product1), product1);
        Assert.assertEquals(page.getProductPrice(product1), "$7.99");
        Assert.assertEquals(page.getProductName(product2), product2, "$9.99");
    }
    
    @Step("Remove {productName}, continue shopping, and re-add the product")
    private ProductsPage tweakCartAndReturn(CartPage page, String productName) {
        page.removeProduct(productName);
        ProductsPage productsPage = page.clickContinueShopping();
        productsPage.addProductToCart(productName);
        return productsPage;
    }
    
    @Step("Proceed from the Products page to the Checkout page")
    private CheckoutPage proceedToCheckout(ProductsPage page) {
        CartPage cartPage = page.goToCart();
        return cartPage.clickCheckout();
    }
    
    @Step("Enter checkout information and continue")
    private void fillAndContinueCheckout(CheckoutPage page, String firstName, String lastName, String zip) {
        page.enterCheckoutInformation(firstName, lastName, zip);
        page.clickContinue();
    }
    
    @Step("Verify total price and finalize the order")
    private void finalizeOrder(CheckoutPage page) {
        Assert.assertEquals(page.getTotalPrice(), "Total: $19.42");
        page.clickFinish();
        Assert.assertEquals(page.getSuccessMessage(), "Thank you for your order!");
    }
    
    @Step("Return to the home page")
    private ProductsPage returnToHomePageAndLogout(CheckoutPage page) {
        return page.clickBackToHome();
    }
    
    @AfterTest
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}