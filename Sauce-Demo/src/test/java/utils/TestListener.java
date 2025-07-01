package utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test failed: " + result.getName());
        WebDriver driver = getDriverFromResult(result);
        if (driver != null) {
            saveScreenshotPNG(driver);
        }
    }

    // RECTIFIED: This method now uses getDeclaredField and setAccessible(true)
    // to correctly access the private 'driver' field from the test class.
    private WebDriver getDriverFromResult(ITestResult result) {
        Object testInstance = result.getInstance();
        try {
            Field driverField = testInstance.getClass().getDeclaredField("driver");
            driverField.setAccessible(true); // Allow access to private fields
            return (WebDriver) driverField.get(testInstance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Error getting WebDriver instance for screenshot: " + e.getMessage());
            return null;
        }
    }

    @Attachment(value = "Screenshot on Failure", type = "image/png")
    public byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    // ENHANCEMENT: This method runs before the suite starts.
    // It reads your environment.properties and writes it to a file
    // that Allure understands, populating the 'Environment' widget in the report.
    @Override
    public void onStart(ITestContext context) {
        System.out.println("Writing Allure environment properties...");
        Properties props = System.getProperties();
        try {
            props.load(ConfigReader.class.getClassLoader().getResourceAsStream("environment.properties"));
            FileOutputStream fos = new FileOutputStream("target/allure-results/environment.properties");
            props.store(fos, "Allure Environment Properties");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}