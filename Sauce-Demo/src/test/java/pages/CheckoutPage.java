package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By pageTitle = By.xpath("//span[@class='title' and text()='Checkout: Your Information']");
    private By firstNameField = By.id("first-name");
    private By lastNameField = By.id("last-name");
    private By zipCodeField = By.id("postal-code");
    private By continueButton = By.id("continue");
    private By finishButton = By.id("finish");
    private By successMessage = By.className("complete-header");
    private By backToHomeButton = By.id("back-to-products");
    private By totalLabel = By.className("summary_total_label");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
    }

    public void enterCheckoutInformation(String firstName, String lastName, String zipCode) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField)).sendKeys(lastName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(zipCodeField)).sendKeys(zipCode);
    }

    public void clickContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }

    public String getTotalPrice() {
        By overviewTitle = By.xpath("//span[@class='title' and text()='Checkout: Overview']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(overviewTitle));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(totalLabel)).getText();
    }

    public void clickFinish() {
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
    }

    public String getSuccessMessage() {
        By thankYouTitle = By.xpath("//span[@class='title' and text()='Checkout: Complete!']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(thankYouTitle));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
    }

    public ProductsPage clickBackToHome() {
        wait.until(ExpectedConditions.elementToBeClickable(backToHomeButton)).click();
        return new ProductsPage(driver);
    }
}