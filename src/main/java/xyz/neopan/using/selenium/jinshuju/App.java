package xyz.neopan.using.selenium.jinshuju;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import xyz.neopan.using.selenium.WebDriverFactory;
import xyz.neopan.using.selenium.WebRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

/**
 * @author panlw
 * @since 2021/2/19
 */
public class App {

    private static final boolean DEBUG_ON = false;
    private static final long WAIT_IN_SECONDS = 600L; // 10m

    public static void main(String[] args) {
        new WebRunner(2, App::run).runFast();
    }

    private static final By finishSelector = By.cssSelector("i.gd-icon-check-circle");
    private static final By submitSelector = By.cssSelector("button.published-form__submit");

    private static void run(WebDriverFactory factory) {
        final var driver = factory.get();
        final var wait = new WebDriverWait(driver, WAIT_IN_SECONDS);
        try {
            driver.get("https://jinshuju.net/f/urE11h");

            final var submit = wait.until(presenceOfElementLocated(submitSelector));
            System.out.println(submit.getAttribute("textContent"));

            driver.findElements(By.cssSelector(".field-container"))
                .forEach(App::handleField);
            handleClick(submit);
            // capture(driver);

            wait.until(presenceOfElementLocated(finishSelector));
            // capture(driver);
        } catch (Throwable unexpected) {
            unexpected.printStackTrace();
        } finally {
            driver.quit();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {
            }
            System.out.println("------------ end ------------");
        }
    }

    private static final By labelSelector = By.cssSelector(".label-item");
    private static final By radioSelector = By.cssSelector("input.ant-radio-input");
    private static final By checkSelector = By.cssSelector("input.ant-checkbox-input");

    private static void capture(TakesScreenshot driver) throws IOException {
        File src = driver.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(src, new File("out/capture.png"));
    }

    private static boolean handleField(WebElement field) {
        return confirmLabel(field) && (
            handleRadioOptions(field.findElements(radioSelector)) ||
                handleCheckOptions(field.findElements(checkSelector))
        );
    }

    private static boolean confirmLabel(WebElement field) {
        try {
            final var label = field.findElement(labelSelector);
            if (DEBUG_ON) System.out.println(label.getText());
            return true;
        } catch (Exception ignore) {
            if (DEBUG_ON) System.out.println("no label");
            return false;
        }
    }

    private static boolean handleRadioOptions(List<WebElement> options) {
        if (options.isEmpty()) {
            if (DEBUG_ON) System.out.println("no radio option");
            return false;
        }
        final int i = Double.valueOf(Math.random() * options.size()).intValue();
        handleClick(options.get(i));
        return true;
    }

    private static boolean handleCheckOptions(List<WebElement> options) {
        if (options.isEmpty()) {
            if (DEBUG_ON) System.out.println("no check option");
            return false;
        }
        final int n = options.size() - 1;
        IntStream.range(0, n)
            .mapToObj(x -> Double.valueOf(Math.random() * n).intValue())
            .collect(Collectors.toSet())
            .forEach(i -> handleClick(options.get(i)));
        return true;
    }

    private static void handleClick(WebElement element) {
        element.click();
        if (DEBUG_ON) {
            final var type = element.getAttribute("type");
            final var value = element.getAttribute("value");
            System.out.println(type + "[" + value + "] is clicked");
        }
    }

}
