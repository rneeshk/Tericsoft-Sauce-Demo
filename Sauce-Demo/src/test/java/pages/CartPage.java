package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By pageTitle = By.xpath("//span[@class='title' and text()='Your Cart']");
    private By checkoutButton = By.id("checkout");
    private By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)); // Wait for page to be ready
    }

    public ProductsPage clickContinueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton)).click();
        return new ProductsPage(driver);
    }

    public CheckoutPage clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        return new CheckoutPage(driver);
    }

    public String getProductName(String productName) {
        By productLocator = By.xpath("//div[@class='inventory_item_name' and text()='" + productName + "']");
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