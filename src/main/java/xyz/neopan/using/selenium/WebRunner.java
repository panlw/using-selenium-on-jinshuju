package xyz.neopan.using.selenium;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * @author panlw
 * @since 2021/2/20
 */
public class WebRunner {

    private final int count;
    private final Consumer<WebDriverFactory> callback;

    public WebRunner(int count, Consumer<WebDriverFactory> callback) {
        this.count = count;
        this.callback = callback;
    }

    public void run() {
        run(WebDriverFactory.values());
    }

    public void runFast() {
        final var factories = Arrays.stream(WebDriverFactory.values())
            .filter(WebDriverFactory::isFast)
            .toArray(WebDriverFactory[]::new);
        run(factories);
    }

    private void run(WebDriverFactory[] factories) {
        final int max = Runtime.getRuntime().availableProcessors();
        final var cnt = Math.min(max, factories.length);
        final var latch = new CountDownLatch(cnt);
        IntStream.range(0, cnt).forEach(i -> runAsync(latch, factories[i]));
        runAwait(latch);
    }

    private static void runAwait(CountDownLatch latch) {
        try {
            latch.await();
            System.out.println("all ends.");
        } catch (InterruptedException e) {
            System.out.println("interrupted!");
        }
    }

    private void runAsync(CountDownLatch latch, WebDriverFactory factory) {
        System.out.println("-----------------------------");
        System.out.println("launch with " + factory.name());
        new Thread(() -> {
            IntStream.range(0, count).forEach(x -> callback.accept(factory));
            latch.countDown();
        }).start();
    }

}
