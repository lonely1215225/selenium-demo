package org.taichiman.twitter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.WheelInput;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TwitterBot {
    private WebDriver driver;
    private Map<String, List<String>> followers;

    private String id = "ndike_o";

    public TwitterBot() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    public void twitterAt() throws InterruptedException {
        driver.get("https://twitter.com/i/flow/login");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"layers\"]/div/div/div/div/div/div/div[2]/div[2]/div/div/div[2]/div[2]/div/div/div/div[5]/label/div/div[2]/div/input")))
                .sendKeys("@taichizuoy");
        driver.findElement(By.xpath("//*[@id=\"layers\"]/div/div/div/div/div/div/div[2]/div[2]/div/div/div[2]/div[2]/div/div/div/div[6]/div")).click();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"layers\"]/div/div/div/div/div/div/div[2]/div[2]/div/div/div[2]/div[2]/div[1]/div/div/div[3]/div/label/div/div[2]/div[1]/input")))
                .sendKeys("Alonezh46y4e");

        driver.findElement(By.xpath("//*[@id=\"layers\"]/div/div/div/div/div/div/div[2]/div[2]/div/div/div[2]/div[2]/div[2]/div/div[1]/div/div/div/div")).click();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"react-root\"]/div/div/div[2]/header/div/div/div/div[1]/div[2]/nav/a[1]/div")));

        driver.get("https://twitter.com/" + id + "/followers");

        // 加载followers
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"react-root\"]/div/div/div[2]/main/div/div/div/div/div/section/div/div/div[1]")));

        List<String> list = new ArrayList<>();
        followers = new HashMap<>();
        followers.put(id, list);
        // 滚动followers
        WebElement iframe = driver.findElement(By.id("react-root"));
        String jsScript = "window.scrollTo(0, document.body.scrollHeight)";
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript(jsScript,iframe);

        Thread.sleep(3000);
        List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"react-root\"]/div/div/div[2]/main/div/div/div/div[1]/div/section/div/div/div"));
        elements.forEach(webElement -> {
            System.out.println(webElement.findElement(By.xpath("./div/div/div/div/div[2]/div[1]/div[1]/div/div[2]/div/a/div/div/span")).getText());
        });

    }

    public static void main(String[] args) throws InterruptedException {
        new TwitterBot().twitterAt();
    }
}
