package com.pdf;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.print.PrintOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BaixarPdf {

    public static void main(String[] args) {
        baixarPdf2();
    }

    public static void baixarPdf3(String saida)
    {
        try
        {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
            ChromeOptions o= new ChromeOptions();
            //o.addArguments("--headless");
            ChromeDriver driver = new ChromeDriver(o);
            driver.get("https://suporte-homolog.tecconsulting.com.br/glpi/index.php?redirect=%2Ffront%2Fdocument.send.php%3Fdocid%3D10%26tickets_id%3D19&error=3");
            driver.findElement(By.cssSelector("#login_name")).sendKeys("rpa.quality");
            driver.findElement(By.cssSelector("body > div.page-anonymous > div > div > div.card.card-md > div > form > div > div > div.mb-4 > input")).sendKeys("@Edgeauto@22");
            driver.findElement(By.cssSelector("body > div.page-anonymous > div > div > div.card.card-md > div > form > div > div > div.form-footer > button")).click();
            Thread.sleep(10000);
            Map<String, Object> params = new HashMap<>();
            try
            {
                String command = "Page.printToPDF";
                Map<String, Object> output = driver.executeCdpCommand(command, params);
                FileOutputStream fileOutputStream = new FileOutputStream(saida);
                byte[] byteArray = java.util.Base64.getDecoder().decode((String) output.get("data"));
                fileOutputStream.write(byteArray);
                fileOutputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
    }

    public static void baixarPdf2() {
        try {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
            ChromeOptions o= new ChromeOptions();
            o.addArguments("--headless");
            ChromeDriver driver = new ChromeDriver(o);
            Cookie cookie1 = new Cookie("glpi_3f946f74140a3178722cb675d5bf6b47_rememberme","%5B18%2C%22Jgp5MdiRtEcDtnYXHiiSXpA32wnKPVms2QxM868j%22%5D");
            //Cookie cookie2 = new Cookie("glpi_3f946f74140a3178722cb675d5bf6b47","lauiilvu4fmgept7jhji20jnss");
            driver.manage().addCookie(cookie1);
            driver.manage().getCookies();
            //driver.manage().addCookie(cookie2);
            driver.get("https://suporte-homolog.tecconsulting.com.br/glpi/index.php?redirect=%2Ffront%2Fdocument.send.php%3Fdocid%3D10%26tickets_id%3D19&error=3");
            driver.findElement(By.cssSelector("#login_name")).sendKeys("rpa.quality");
            driver.findElement(By.cssSelector("body > div.page-anonymous > div > div > div.card.card-md > div > form > div > div > div.mb-4 > input")).sendKeys("@Edgeauto@22");
            driver.findElement(By.cssSelector("body > div.page-anonymous > div > div > div.card.card-md > div > form > div > div > div.form-footer > button")).click();
            Thread.sleep(10000);
            Pdf pdf = ((PrintsPage) driver).print(new PrintOptions());
            Files.write(Paths.get("test2.pdf"), OutputType.BYTES.convertFromBase64Png(pdf.getContent()));
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void baixarPdf1(){
        System.out.println("opening connection");
        URL url = null;
        try {
            url = new URL("https://suporte-homolog.tecconsulting.com.br/glpi/front/document.send.php?docid=10&tickets_id=19");
            InputStream in = null;
            try {
                in = url.openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            FileOutputStream fos = new FileOutputStream(new File("yourFile.pdf"));
            int length = -1;
            byte[] buffer = new byte[1024];// buffer for portion of data from connection
            while ((length = in.read(buffer)) > -1) {
                fos.write(buffer, 0, length);
            }
            fos.close();
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
