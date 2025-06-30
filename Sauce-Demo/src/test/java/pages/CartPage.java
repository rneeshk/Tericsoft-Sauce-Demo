package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By pageTitle = By.xpath("//span[@class='title' and text()='Your Cart']");
    private By checkoutButton = By.id("checkout");
    private By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
    }
    
    public ProductsPage clickContinueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton)).click();
        // RECTIFIED: Wait for the products page to load and return its object.
        By productsPageTitle = By.xpath("//span[@class='title' and text()='Products']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(productsPageTitle));
        return new ProductsPage(driver);
    }

    public CheckoutPage clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        // RECTIFIED: Wait for the checkout page to load and return its object.
        By checkoutPageTitle = By.xpath("//span[@class='title' and text()='Checkout: Your Information']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutPageTitle));
        return new CheckoutPage(driver);
    }

    // Other methods remain the same...
    public String getProductName(String productName) {
        By productLocator = By.xpath("//div[text()='" + productName + "']");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productLocator)).getText();
    }

    public String getProductPrice(String productName) {
        By priceLocator = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='cart_item']//div[@class='inventory_item_price']");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(priceLocator)).getText();
    }

    public void removeProduct(String productName) {
        By removeButton = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='cart_item']//button");
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
    }
}