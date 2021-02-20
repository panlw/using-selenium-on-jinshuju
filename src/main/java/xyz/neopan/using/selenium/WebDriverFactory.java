package xyz.neopan.using.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.function.Supplier;

/**
 * @author panlw
 * @since 2021/2/20
 */
public enum WebDriverFactory implements Supplier<WebDriver> {

    firefox {
        @Override
        public WebDriver get() {
            return new FirefoxDriver();
        }

        @Override
        boolean isFast() {
            return false;
        }
    },
    chrome {
        @Override
        public WebDriver get() {
            return new ChromeDriver();
        }
    },
    edge {
        @Override
        public WebDriver get() {
            return new EdgeDriver();
        }
    },
    ;

    boolean isFast() {
        return true;
    }

    static {
        // https://github.com/mozilla/geckodriver/releases
        // Latest stable release (win64)
        System.setProperty("webdriver.gecko.driver",
            "C:\\Users\\neopan\\Desktop\\app\\WebDrivers\\geckodriver.exe");
        // https://chromedriver.chromium.org/
        // Latest stable release (win32)
        System.setProperty("webdriver.chrome.driver",
            "C:\\Users\\neopan\\Desktop\\app\\WebDrivers\\chromedriver.exe");
        // https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/
        // Latest stable release (x64)
        System.setProperty("webdriver.edge.driver",
            "C:\\Users\\neopan\\Desktop\\app\\WebDrivers\\msedgedriver.exe");
    }

}
