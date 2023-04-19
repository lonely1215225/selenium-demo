package org.taichiman.discord;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DiscordReptile {
    private WebDriver driver;

    public DiscordReptile() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins-discovery");
        options.addArguments("--disable-bundled-ppapi-flash");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    public void login(String email, String password) {
        driver.get("https://aigc.yizhentv.com/app");
        new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"uid_5\"]")))
                .sendKeys(email);
        driver.findElement(By.xpath("//*[@id=\"uid_7\"]")).sendKeys(password);

        driver.findElement(By.xpath("//*[@id=\"app-mount\"]/div[2]/div[1]/div[1]/div/div/div/div/form/div[2]/div/div[1]/div[2]/button[2]")).click();
    }

    public static void main(String[] args) {
        DiscordReptile discordReptile = new DiscordReptile();
        discordReptile.login("","");
    }
}
