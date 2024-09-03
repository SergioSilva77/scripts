package com.selenium;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ShadowRoot {
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, InterruptedException {

        JSONArray jsonArray = new JSONArray("[{\"key\":\"value\"}]");
        for (Object obj : jsonArray){
            JSONObject jobj = (JSONObject) obj;
            String key = jobj.getString("key");
            System.out.println(key);
        }

        // coloque no segundo parametro o caminho do chromedriver.exe
        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver2.exe");

        ChromeOptions o = new ChromeOptions();
        //o.setExperimentalOption("debuggerAddress", "127.0.0.1:9898");
        ChromeDriver driver = new ChromeDriver(o);
        driver.get("http://127.0.0.1:5600/shadow/shadow%20closed/index.html");


        //WebElement el = driver.findElement(By.cssSelector("#meu-elemento")).getShadowRoot().findElement(By.cssSelector("iframe"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement shadowRoot = (WebElement) js.executeScript("return document.querySelector('#meu-elemento').shadowRoot");
        WebElement elementInsideShadowRoot = shadowRoot.findElement(By.cssSelector("table"));
        driver.switchTo().frame(elementInsideShadowRoot);


//        driver.findElement(By.cssSelector("#history-app"))
//                .getShadowRoot().findElement(By.cssSelector("#toolbar"))
//                .getShadowRoot().findElement(By.cssSelector("#mainToolbar"))
//                .getShadowRoot().findElement(By.cssSelector("#search"))
//                .getShadowRoot().findElement(By.cssSelector("#searchInput")).sendKeys("testt");

//        ClassLoader customClassLoader = new URLClassLoader(new URL[] { new File("C:\\Users\\sergi\\Downloads\\selenium-chrome-driver-4.8.1.jar").toURI().toURL() }, ShadowRoot.class.getClassLoader());
//        Class<?> driverClass = customClassLoader.loadClass("org.openqa.selenium.chrome.ChromeDriver");
//        Object instance = driverClass.newInstance();
//        Method metodo = driverClass.getMethod("get", String.class);
//        metodo.invoke(instance, "https://www.google.com");


    }
}
