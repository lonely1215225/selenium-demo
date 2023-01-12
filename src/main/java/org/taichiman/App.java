package org.taichiman;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Hello world!
 *
 */
public class App 
{
    private WebDriver driver;

    public App() {
        WebDriverManager.chromedriver().setup();
//        System.setProperty("webdriver.chrome.driver","/Users/taichiman/bsdriver/chromedriver");
        driver = new ChromeDriver();
    }
    public static void main( String[] args ) throws InterruptedException {
        new App().selenium();
        System.out.println( "Hello World!" );
    }

    public void selenium() throws InterruptedException {
        driver.get(txInfosUrl());

//        driver.switchTo().frame("tokentxnsiframe");
        Map<String, String> address2Tx = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            List<TransactionEntity> collect = driver.findElement(By.id("body"))
                    .findElement(By.id("maindiv"))
                    .findElement(By.tagName("tbody"))
                    .findElements(By.tagName("tr")).stream()
                    .map(tr -> {
                        List<WebElement> elements = tr.findElements(By.tagName("td"));
                        TransactionEntity.TransactionEntityBuilder builder = TransactionEntity.builder()
                                .txnHash(elements.get(0).getText())
                                .method(elements.get(1).getText())
                                .to(elements.get(6).getText())
                                .quantity(elements.get(7).getText());

                        String attribute = elements.get(4).findElement(By.tagName("a")).getAttribute("href");
                        String from = attribute.substring(attribute.indexOf("=") + 1);
                        TransactionEntity transaction = builder.from(from).build();

                        if (address2Tx.containsKey(transaction.getFrom())) {
                            String quantity = address2Tx.get(transaction.getFrom()).replace(",", "");
                            address2Tx.put(transaction.getFrom(), new BigDecimal(quantity)
                                    .add(new BigDecimal(transaction.getQuantity().replace(",", ""))).toString());
                        }
                        address2Tx.put(transaction.getFrom(), transaction.getQuantity().replace(",", ""));
                        return transaction;
                    }).collect(Collectors.toList());


//            collect.forEach(tx -> {
//                String quantity = tx.getQuantity().replace(",", "");
//                BigDecimal bigDecimal = new BigDecimal(quantity);
//                if (bigDecimal.compareTo(BigDecimal.valueOf(100 / 0.25)) > 0) {
//                    System.out.println("该地址：" + tx.getFrom()
//                            + "通过" + tx.getMethod()
//                            + "方法向地址：" + tx.getTo()
//                            + "交易了：" + quantity + " tokens");
//                }
//            });
            List<Map.Entry<String, String>> list = new ArrayList<>(address2Tx.entrySet());
            Collections.sort(list, Collections.reverseOrder(Comparator.comparing(o -> new BigDecimal(o.getValue()))));


            list.forEach(e -> System.out.println("该地址：" + e.getKey() + "最近累计交易量为：" + e.getValue() + " tokens"));
//            address2Tx.forEach((k, v) -> System.out.println("该地址：" + k + "最近累计交易量为：" + v + " tokens"));

            System.out.println("=========================================================");

            Thread.sleep(20000);
            driver.navigate().refresh();
        }
    }

    public String txInfosUrl() {
        // 2.打开百度首页
        driver.get("https://bscscan.com/token/0x3019BF2a2eF8040C242C9a4c5c4BD4C81678b2A1");
        return driver.findElement(By.id("tokentxnsiframe")).getAttribute("src");
    }
}
