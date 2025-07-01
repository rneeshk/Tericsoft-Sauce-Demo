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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private WebDriver getDriverFromResult(ITestResult result) {
        Object testInstance = result.getInstance();
        try {
            Field driverField = testInstance.getClass().getDeclaredField("driver");
            driverField.setAccessible(true);
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

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Writing Allure environment properties...");
        try {
            Properties props = new Properties();
            props.load(ConfigReader.class.getClassLoader().getResourceAsStream("environment.properties"));
            Path allureResultsDir = Paths.get("target", "allure-results");
            Files.createDirectories(allureResultsDir);
            FileOutputStream fos = new FileOutputStream(allureResultsDir.resolve("environment.properties").toFile());
            props.store(fos, "Allure Environment Properties");
            fos.close();
        } catch (IOException e) {
            System.err.println("Failed to write Allure environment properties: " + e.getMessage());
            e.printStackTrace();
        }
    }
}