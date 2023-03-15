import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class FirstAutomation {
    WebDriver driver;
    @Before
    public void setub(){
        WebDriverManager.chromedriver().setup();
        driver =new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }
    @Test
    public void getTitle(){
        driver.get("https://demoqa.com/");
        String title_actual= driver.getTitle();
        String title_expected="DEMOQA";
        System.out.println(title_actual);
        Assert.assertEquals(title_actual,title_expected);
    }
    @Test
    public void checkIfImageExists(){
        driver.get("https://demoqa.com/");
        boolean status= driver.findElement(By.xpath("//header/a[1]/img[1]")).isDisplayed();
        System.out.println(status);
        Assert.assertTrue(status);
    }
    @Test
    public void submitForm(){
        driver.get("https://demoqa.com/text-box");
//        driver.findElement(By.id("userName")).sendKeys("Test User");
//        driver.findElement(By.cssSelector("[type=text]")).sendKeys("Test User");
//        driver.findElements(By.className("form-control")).get(0).sendKeys("Test User");
        List<WebElement> formControls=driver.findElements(By.className("form-control"));
        formControls.get(0).sendKeys("Test User"); //username
//        driver.findElement(By.id("userEmail")).sendKeys("testuser@gmail.com");
//        driver.findElements(By.className("form-control")).get(1).sendKeys("testuser@gmail.com");
        formControls.get(1).sendKeys("testuser@gmail.com"); //email

        WebElement txtCurrentAddress=driver.findElement(By.cssSelector("[placeholder='Current Address']"));
        txtCurrentAddress.sendKeys("Dhaka");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,400)");
        List <WebElement> btnSubmit=driver.findElements(By.tagName("button"));
//        driver.findElement(By.id("submit")).click();
        btnSubmit.get(1).click();

        String name_actual= driver.findElement(By.id("name")).getText();
        String name_expected= "Test User";
        Assert.assertTrue(name_actual.contains(name_expected));


    }
    @Test
    public void clickOnButton(){
        driver.get("https://demoqa.com/buttons");
        List<WebElement> buttons= driver.findElements(By.cssSelector("[type=button]"));
        Actions actions=new Actions(driver);
        actions.doubleClick(buttons.get(1)).perform();
        actions.contextClick(buttons.get(2)).perform();
        actions.click(buttons.get(3)).perform();

        String actual_message1= driver.findElement(By.id("doubleClickMessage")).getText();
        String expected_message1="You have done a double click";

        String actual_message2= driver.findElement(By.id("rightClickMessage")).getText();
        String expected_message2="You have done a right click";

        String actual_message3= driver.findElement(By.id("dynamicClickMessage")).getText();
        String expected_message3="You have done a dynamic click";

        Assert.assertTrue(actual_message1.contains(expected_message1));
        Assert.assertTrue(actual_message2.contains(expected_message2));
        Assert.assertTrue(actual_message3.contains(expected_message3));
    }
    @Test
    public void alert() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("alertButton")).click();
        Thread.sleep(6000);
        driver.switchTo().alert().accept();

        Thread.sleep(8000);
        driver.findElement(By.id(("confirmButton"))).click();
        driver.switchTo().alert().accept();
    }
    @Test
    public void selectDate(){
        driver.get("https://demoqa.com/date-picker");
        WebElement calendar=driver.findElement(By.id("datePickerMonthYearInput"));
        calendar.sendKeys(Keys.CONTROL + "a",Keys.BACK_SPACE);
        calendar.sendKeys("03/02/2023");
        calendar.sendKeys(Keys.ENTER);
    }
    @Test
    public void selectDropdownClassic(){
        driver.get("https://demoqa.com/select-menu");
        Select select=new Select(driver.findElement(By.id("oldSelectMenu")));
//        select.selectByValue("4");
        select.selectByIndex(3);
//        select.selectByVisibleText("White");
    }
    @Test
    public void takeScreenShot() throws IOException {
        driver.get("https://demoqa.com");
        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String time = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss-aa").format(new Date());
        String fileWithPath = "./src/test/resources/screenshots/" + time + ".png";
        File DestFile = new File(fileWithPath);
        FileUtils.copyFile(screenshotFile, DestFile);
    }
    @Test
    public void handleMultipleTab() throws InterruptedException {
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("tabButton")).click();
        Thread.sleep(3000);
        ArrayList<String> w = new ArrayList(driver.getWindowHandles());  // switch to open tab
        driver.switchTo().window(w.get(1));
        System.out.println("New tab title: " + driver.getTitle());
        String text = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertEquals(text,"This is a sample page");
        driver.close();
        driver.switchTo().window(w.get(0));
    }
    @Test
    public void handelChildWindow() throws InterruptedException {
        driver.get("https://demoqa.com/browser-windows");
//        Thread.sleep(5000);
//        WebDriverWait wait = new WebDriverWait(driver, 30);
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("windowButton")));
        driver.findElement(By.id(("windowButton"))).click();
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();

        while (iterator.hasNext()) {
            String ChildWindow = iterator.next();

           if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
                driver.switchTo().window(ChildWindow);
                       String text= driver.findElement(By.id("sampleHeading")).getText();
                    Assert.assertTrue(text.contains("This is a sample page"));
           }
        }
        driver.close();
        driver.switchTo().window(mainWindowHandle);
    }
    @Test
    public void scrapData(){
        driver.get("https://demoqa.com/webtables");
        WebElement table = driver.findElement(By.className("rt-tbody"));
        List<WebElement> allRows = table.findElements(By.className("rt-tr"));
        int i=0;
            for (WebElement row : allRows) {
                List<WebElement> cells = row.findElements(By.className("rt-td"));
                  for (WebElement cell : cells) {
                          i++;
                      System.out.println("num["+i+"] "+ cell.getText());
                  }
              }
    }
    @Test
    public void handleIframe(){
        driver.get("https://demoqa.com/frames");
        driver.switchTo().frame("frame1");
           String text= driver.findElement(By.id("sampleHeading")).getText();
           Assert.assertTrue(text.contains("This is a sample page"));
            driver.switchTo().defaultContent();
        }
    @After
    public void closeBrowser(){
//        driver.quit();
    }
}
