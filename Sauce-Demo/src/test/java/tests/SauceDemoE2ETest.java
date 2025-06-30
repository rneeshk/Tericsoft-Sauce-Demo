//package tests;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.testng.Assert;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.Test;
//
//import com.github.javafaker.Faker;
//
//import pages.CartPage;
//import pages.CheckoutPage;
//import pages.LoginPage;
//import pages.ProductsPage;
//
//public class SauceDemoE2ETest {
//
//    private WebDriver driver;
//
//    @BeforeTest
//    public void setup() {
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless=new");
//
//        driver = new ChromeDriver(options);
//        driver.manage().window().maximize();
//    }
//
//    @Test
//    public void sauceDemoTest() {
//
//        driver.get("https://www.saucedemo.com/");
//
//        LoginPage loginPage = new LoginPage(driver);
//        ProductsPage productsPage = loginPage.login("standard_user", "secret_sauce");
//
//        productsPage.applyPriceLowToHighFilter();
//
//        String product1Name = "Sauce Labs Onesie";
//        String product2Name = "Sauce Labs Bike Light";
//        productsPage.addProductToCart(product1Name);
//        productsPage.addProductToCart(product2Name);
//
//        CartPage cartPage = productsPage.goToCart();
//
//        Assert.assertEquals(cartPage.getProductName(product1Name), product1Name);
//        Assert.assertEquals(cartPage.getProductPrice(product1Name), "$7.99");
//        Assert.assertEquals(cartPage.getProductName(product2Name), product2Name);
//        Assert.assertEquals(cartPage.getProductPrice(product2Name), "$9.99");
//
//        cartPage.removeProduct(product1Name);
//
//        productsPage = cartPage.clickContinueShopping();
//        
//        productsPage.addProductToCart(product1Name);
//
//        cartPage = productsPage.goToCart();
//
//        CheckoutPage checkoutPage = cartPage.clickCheckout();
//
//        Faker faker = new Faker();
//        checkoutPage.enterCheckoutInformation(faker.name().firstName(), faker.name().lastName(), faker.address().zipCode());
//
//        checkoutPage.clickContinue();
//
//        Assert.assertEquals(checkoutPage.getTotalPrice(), "Total: $19.42");
//
//        checkoutPage.clickFinish();
//
//        Assert.assertEquals(checkoutPage.getSuccessMessage(), "Thank you for your order!");
//
//        productsPage = checkoutPage.clickBackToHome();
//
//        productsPage.logout();
//    }
//
//    @AfterTest
//    public void teardown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
//}


package tests;

import io.qameta.allure.*;
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

@Epic("SauceDemo Application")
@Feature("End-to-End Checkout")
public class SauceDemoE2ETest {

    private WebDriver driver;

    @BeforeTest
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test(description = "Validates the full checkout workflow from login to order confirmation.")
    @Story("Successful Order Placement")
    @Severity(SeverityLevel.BLOCKER)
    @Description("This test logs in, adds products, validates the cart, modifies it, and completes checkout.")
    public void sauceDemoTest() {
        Faker faker = new Faker();
        
        navigateToLoginPage();
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = performLogin(loginPage, "standard_user", "secret_sauce");
        
        filterAndAddProducts(productsPage);
        
        CartPage cartPage = productsPage.goToCart();
        validateCart(cartPage, "Sauce Labs Onesie", "Sauce Labs Bike Light");
        
        // RECTIFIED: Capture the new ProductsPage object returned by the helper method.
        productsPage = tweakCartAndReturn(cartPage, "Sauce Labs Onesie");
        
        // RECTIFIED: This now proceeds from the correct, non-stale page object.
        CheckoutPage checkoutPage = proceedToCheckout(productsPage);
        
        fillAndContinueCheckout(checkoutPage, faker.name().firstName(), faker.name().lastName(), faker.address().zipCode());
        
        finalizeOrder(checkoutPage);
        
        returnToHomePageAndLogout(checkoutPage);
    }
    
    // ... (other helper methods are unchanged)

    @Step("Remove {productName}, continue shopping, and re-add the product")
    // RECTIFIED: Changed the method to return the new ProductsPage object.
    private ProductsPage tweakCartAndReturn(CartPage page, String productName) {
        page.removeProduct(productName);
        ProductsPage productsPage = page.clickContinueShopping();
        // This now calls the more robust method from the ProductsPage class (see Part 2).
        productsPage.addProductToCart(productName);
        // RECTIFIED: Return the newly created and updated page object.
        return productsPage;
    }
    
    @Step("Proceed from the Products page to the Checkout page")
    private CheckoutPage proceedToCheckout(ProductsPage page) {
        CartPage cartPage = page.goToCart();
        return cartPage.clickCheckout();
    }
    
    // ... (other helper methods are unchanged)
    @Step("Navigate to the SauceDemo login page")
    private void navigateToLoginPage() {
        driver.get("https://www.saucedemo.com/");
    }

    @Step("Login with username: {username}")
    private ProductsPage performLogin(LoginPage page, String username, String password) {
        return page.login(username, password);
    }

    @Step("Apply price filter and add two products to the cart")
    private void filterAndAddProducts(ProductsPage page) {
        page.applyPriceLowToHighFilter();
        page.addProductToCart("Sauce Labs Onesie");
        page.addProductToCart("Sauce Labs Bike Light");
    }
    
    @Step("Validate product details in the cart for {product1} and {product2}")
    private void validateCart(CartPage page, String product1, String product2) {
        Assert.assertEquals(page.getProductName(product1), product1);
        Assert.assertEquals(page.getProductPrice(product1), "$7.99");
        Assert.assertEquals(page.getProductName(product2), product2);
        Assert.assertEquals(page.getProductPrice(product2), "$9.99");
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

    @Step("Return to the home page and log out")
    private void returnToHomePageAndLogout(CheckoutPage page) {
        ProductsPage productsPage = page.clickBackToHome();
        productsPage.logout();
    }


    @AfterTest
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}