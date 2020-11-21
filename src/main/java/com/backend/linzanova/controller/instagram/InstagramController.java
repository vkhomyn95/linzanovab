package com.backend.linzanova.controller.instagram;


import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class InstagramController {

    @RequestMapping(value = "unfollow")
    public void unfollow() {
        System.setProperty("webdriver.chrome.driver","/Users/volodymyr/Downloads/chromedriver");
        ChromeDriver driver = new ChromeDriver();
        driver.get("https://www.instagram.com/accounts/login/?hl=en&source=auth_switcher");

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.findElement(By.name("username")).sendKeys("linza.nova");
        driver.findElement(By.name("password")).sendKeys("khomyn.vg");
        driver.findElement(By.xpath("//div[text()='Log In']")).click();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.findElement(By.xpath("//button[contains(text(), 'Not Now')]")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver.findElement(By.xpath("//button[contains(text(), 'Not Now')]")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver.findElement(By.xpath("/html/body/div[1]/section/nav/div[2]/div/div/div[3]/div/div[5]/span/img")).click();
        driver.findElement(By.xpath("/html/body/div[1]/section/nav/div[2]/div/div/div[3]/div/div[5]/div[2]/div/div[2]/div[2]/a[1]/div/div[2]/div/div/div/div")).click();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        driver.findElement(By.xpath("//a[contains(@href, '/following')]")).click();

        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        driver.findElement(By.xpath("/html/body/div[4]/div/div[2]"));
//        int prevHeight = 0; int height = 1;
//        while (prevHeight != height) {
//            prevHeight = height;
//            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//            height = (int) js.executeScript("arguments[0].scrollTo(0, arguments[0].scrollHeight); return arguments[0].scrollHeight;", scrollBox);
//            scrollBox.findElement(By.tagName("a"));
//
//        }





    }

}
