package org.taichiman.twitter;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.WheelInput;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.github.bonigarcia.wdm.WebDriverManager;
import redis.clients.jedis.Jedis;

public class TwitterBot {
    private WebDriver driver;
    private static Jedis jedis;

    static {
        jedis = new Jedis("127.0.0.1", 6379);
        System.setProperty("webdriver.chrome.driver", "/Users/taichiman/Downloads/chromedriver_mac_arm64/chromedriver");
    }

    public TwitterBot(String account, String password) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins-discovery");
        options.addArguments("--disable-bundled-ppapi-flash");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        twitterLogin(account, password);
    }

    public void twitterLogin(String account, String password) {
        driver.get("https://twitter.com/i/flow/login");
        new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"layers\"]/div/div/div/div/div/div/div[2]/div[2]/div/div/div[2]/div[2]/div/div/div/div[5]/label/div/div[2]/div/input")))
                .sendKeys(account);
        driver.findElement(By.xpath("//*[@id=\"layers\"]/div/div/div/div/div/div/div[2]/div[2]/div/div/div[2]/div[2]/div/div/div/div[6]/div")).click();

        new WebDriverWait(driver, Duration.ofSeconds(50))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"layers\"]/div/div/div/div/div/div/div[2]/div[2]/div/div/div[2]/div[2]/div[1]/div/div/div[3]/div/label/div/div[2]/div[1]/input")))
                .sendKeys(password);

        driver.findElement(By.xpath("//*[@id=\"layers\"]/div/div/div/div/div/div/div[2]/div[2]/div/div/div[2]/div[2]/div[2]/div/div[1]/div/div/div/div")).click();

        new WebDriverWait(driver, Duration.ofSeconds(50))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"react-root\"]/div/div/div[2]/header/div/div/div/div[1]/div[2]/nav/a[1]/div")));
    }

    public Set<String> getFollowers(String id) throws InterruptedException {
        driver.get("https://twitter.com/" + id + "/followers");

        // 加载followers
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"react-root\"]/div/div/div[2]/main/div/div/div/div/div/section/div/div/div[1]")));

        Set<String> set = new HashSet<>();

        long startTime = System.currentTimeMillis();
        Set<Integer> tempSet = new HashSet<>();
        String origin = "";
        WebElement element = driver.findElement(By.xpath("//*[@id=\"react-root\"]/div/div/div[2]/main/div/div/div/div[1]/div/section/div/div"));
        Thread.sleep(1000);
        for (int i = 1; ; i++) {
            Thread.sleep(500);
            if (set.size() > 1000) {
                break;
            }
//            String height = element.getAttribute("style").split(";")[1];
//            String value = height.split(":")[1].replaceAll("px", "").strip();
//            if (origin.equals(value)) {
//                break;
//            }
//            origin = value;
            new Actions(driver)
                    .scrollFromOrigin(WheelInput.ScrollOrigin.fromViewport(), 0, 2000)
                    .perform();
            List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"react-root\"]/div/div/div[2]/main/div/div/div/div[1]/div/section/div/div/div"));
            for (int j = 0; j < elements.size(); j++) {
                String[] split = new String[0];
                try {
                    // 提高效率
                    // 耗时操作
                    split = elements.get(j).getText().split("\n");
                } catch (Exception e) {
                    elements = driver.findElements(By.xpath("//*[@id=\"react-root\"]/div/div/div[2]/main/div/div/div/div[1]/div/section/div/div/div"));
                    continue;
                }
                if (split.length < 2) {
                    break;
                }
                String userName = split[1];
//                System.out.println(userName);
                set.add(userName);
            }
            System.out.println(set.size());
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime) / 1000 / 60);
        System.out.println(set.size());
        return set;
    }

    public void comment(Set<String> userNames, Integer atPerCount, String twitterMsgUrl) {
        driver.get(twitterMsgUrl);
        driver.navigate().refresh();
        Set<String> set = new HashSet<>();
        for (String userName : userNames) {
            if (set.size() == atPerCount) {
                comment(set);
                set = new HashSet<>();
            }
            set.add(userName);
        }


    }

    public void comment(Set<String> userNames) {
        new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.elementToBeClickable(By.xpath
                        ("//*[@id=\"react-root\"]/div/div/div[2]/main/div/div/div/div[1]/div/section/div/div/div[1]/div/div/div/div/div[2]/div[1]/div/div/div/div[2]/div[1]/div/div/div/div/div[1]/div/div/div/div/div/label/div[1]/div/div/div/div/div/div[2]/div/div/div/div")))
                .click();

        StringBuilder content = new StringBuilder("");
        for (String userName : userNames) {
            content.append(userName).append(" ");
        }
        new Actions(driver)
                .sendKeys(
                        content.toString()
                )
                .perform();

        new Actions(driver)
                .keyDown(Keys.COMMAND)
                .keyDown(Keys.ENTER)
                .keyUp(Keys.ENTER)
                .keyUp(Keys.COMMAND)
                .perform();

    }

    public static void main(String[] args) {

        new Thread(() -> {
            TwitterBot twitterBot = new TwitterBot("@lonely1215225", "alonezh46y4e");
            String id = "laoyu123123";
            Set<String> sets = null;
            try {
                sets = twitterBot.getFollowers(id);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(sets);
            String[] sArray = new String[sets.size()];
            jedis.del(id);
            jedis.lpush(id, sets.toArray(sArray));
            twitterBot.driver.close();
        }).start();
        new Thread(() -> {
            TwitterBot twitterBot = new TwitterBot("@lonely1215225", "alonezh46y4e");
            Set<String> sets = null;
            String id = "MMCrypto";
            try {
                sets = twitterBot.getFollowers(id);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String[] sArray = new String[sets.size()];
            jedis.lpush(id, sets.toArray(sArray));
            twitterBot.driver.close();
        }).start();
        new Thread(() -> {
            TwitterBot twitterBot = new TwitterBot("@lonely1215225", "alonezh46y4e");
            Set<String> sets = null;
            String id = "jasonman88";
            try {
                sets = twitterBot.getFollowers(id);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String[] sArray = new String[sets.size()];
            jedis.lpush(id, sets.toArray(sArray));
            twitterBot.driver.close();
        }).start();


    }
}
