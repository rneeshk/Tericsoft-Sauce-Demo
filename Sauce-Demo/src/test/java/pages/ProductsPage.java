//package pages;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.Select;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import java.time.Duration;
//
//public class ProductsPage {
//
//    private WebDriver driver;
//    private WebDriverWait wait;
//
//    // Locators
//    private By pageTitle = By.xpath("//span[@class='title' and text()='Products']");
//    private By productSortDropdown = By.className("product_sort_container");
//    private By cartLink = By.className("shopping_cart_link");
//    private By menuButton = By.id("react-burger-menu-btn");
//    private By logoutLink = By.id("logout_sidebar_link");
//
//    public ProductsPage(WebDriver driver) {
//        this.driver = driver;
//        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//    }
//
//    public void waitForPageToLoad() {
//        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
//    }
//    
//    public CartPage goToCart() {
//        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
//        // RECTIFIED: Wait for the cart page to load and return its object.
//        By cartPageTitle = By.xpath("//span[@class='title' and text()='Your Cart']");
//        wait.until(ExpectedConditions.visibilityOfElementLocated(cartPageTitle));
//        return new CartPage(driver);
//    }
//
//    // Other methods remain the same...
//    public void applyPriceLowToHighFilter() {
//        WebElement dropdownElement = wait.until(ExpectedConditions.visibilityOfElementLocated(productSortDropdown));
//        Select dropdown = new Select(dropdownElement);
//        dropdown.selectByValue("lohi");
//    }
//
//    public void addProductToCart(String productName) {
//        By addToCartButton = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button[text()='Add to cart']");
//        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
//    }
//
//    public void removeProductFromCart(String productName) {
//        By removeFromCartButton = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button[text()='Remove']");
//        wait.until(ExpectedConditions.elementToBeClickable(removeFromCartButton)).click();
//    }
//    
//    public void openMenu() {
//        wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
//    }
//    
//    public void logout() {
//        openMenu();
//        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
//    }
//}


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

    // Locators
    private By pageTitle = By.xpath("//span[@class='title' and text()='Products']");
    private By productSortDropdown = By.className("product_sort_container");
    private By cartLink = By.className("shopping_cart_link");
    private By menuButton = By.id("react-burger-menu-btn");
    private By logoutLink = By.id("logout_sidebar_link");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    public void addProductToCart(String productName) {
        By addToCartButton = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button[text()='Add to cart']");
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();

        // RECTIFIED: Add a confirmation wait. After clicking, the button's text changes to "Remove".
        // This wait ensures the action is complete before the test proceeds, eliminating the race condition.
        By removeButton = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button[text()='Remove']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(removeButton));
    }

    // ... (other methods are unchanged)
    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
    }
    
    public CartPage goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
        By cartPageTitle = By.xpath("//span[@class='title' and text()='Your Cart']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartPageTitle));
        return new CartPage(driver);
    }

    public void applyPriceLowToHighFilter() {
        WebElement dropdownElement = wait.until(ExpectedConditions.visibilityOfElementLocated(productSortDropdown));
        Select dropdown = new Select(dropdownElement);
        dropdown.selectByValue("lohi");
    }

    public void removeProductFromCart(String productName) {
        By removeFromCartButton = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button[text()='Remove']");
        wait.until(ExpectedConditions.elementToBeClickable(removeFromCartButton)).click();
    }
    
    public void openMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
    }
    
    public void logout() {
        openMenu();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }
}