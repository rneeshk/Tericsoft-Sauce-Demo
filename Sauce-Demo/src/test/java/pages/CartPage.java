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
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
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
        By productLocator = By.xpath("//div[@data-test='inventory-item-name' and text()='" + productName + "']");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productLocator)).getText();
    }

    public String getProductPrice(String productName) {
        By priceLocator = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='cart_item']//div[@data-test='inventory-item-price']");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(priceLocator)).getText();
    }

    public void removeProduct(String productName) {
        String dataTestSelector = "remove-" + productName.toLowerCase().replace(" ", "-");
        By removeButton = By.cssSelector("[data-test='" + dataTestSelector + "']");
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
    }
}