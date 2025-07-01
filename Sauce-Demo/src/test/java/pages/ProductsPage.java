package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By pageTitle = By.xpath("//span[@class='title' and text()='Products']");
    private By productSortDropdown = By.className("product_sort_container");
    private By cartLink = By.className("shopping_cart_link");
    private By menuButton = By.id("react-burger-menu-btn");
    private By logoutLink = By.id("logout_sidebar_link");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
    }
    private String formatProductNameForDataTest(String productName) {
        return productName.toLowerCase().replace(" ", "-")
                .replace("(", "").replace(")", "");
    }

    public void applyPriceLowToHighFilter() {
        WebElement dropdownElement = wait.until(ExpectedConditions.visibilityOfElementLocated(productSortDropdown));
        Select dropdown = new Select(dropdownElement);
        dropdown.selectByValue("lohi");
        By firstItemName = By.xpath("(//div[@data-test='inventory-item-name'])[1]");
        wait.until(ExpectedConditions.textToBe(firstItemName, "Sauce Labs Onesie"));
    }

    public void addProductToCart(String productName) {
        String dataTestSelector = "add-to-cart-" + formatProductNameForDataTest(productName);
        By addToCartButton = By.cssSelector("[data-test='" + dataTestSelector + "']");
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        String removeDataTestSelector = "remove-" + formatProductNameForDataTest(productName);
        By removeButton = By.cssSelector("[data-test='" + removeDataTestSelector + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(removeButton));
    }

    public CartPage goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
        return new CartPage(driver);
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }
}