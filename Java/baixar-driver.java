package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class BaixarDriver {
    public static void main(String[] args) throws IOException {
        test();

//        String directoryDownloadDriver = args[0];
//        String navegador = args[1];
//        String versao = args[2];

        String directoryDownloadDriver = "C:\\Users\\sserg\\OneDrive\\Área de Trabalho\\Nova pasta";
        String navegador = "chrome";
        String versao = "n/a";

        if (Objects.equals(versao, "n/a")) {
            downloadDriver(directoryDownloadDriver, navegador);
        } else {
            downloadDriver(directoryDownloadDriver, navegador, versao);
        }

        
    }

    public static void setarPropertiesJava(String navegador, String pathDriver){
        switch (navegador) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", pathDriver);
                break;
            case "edge":
                System.setProperty("webdriver.edge.driver", pathDriver);
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", pathDriver);
                break;
            case "safari":
//                System.setProperty("webdriver.edge.driver", pathDriver);
                break;
            case "opera":
                System.setProperty("webdriver.opera.driver", pathDriver);
                break;
            case "iexplorer":
                System.setProperty("webdriver.ie.driver", pathDriver);
                break;
        }
    }

    public static void test(){
        System.setProperty("webdriver.chrome.driver", "C:\\AutomationEdge\\Workflows\\WF2\\CEF80.2\\Drivers\\121\\CHROME121.exe");
        String url = "https://www.allianznet.com.br/ngx-epac/public/home";
        Map< String, Object > chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", "");
        chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
        chromePrefs.put("download.prompt_for_download", false);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);

        ChromeDriver driver = new ChromeDriver(options);
        driver.get(url);

        WebElement inputField = driver.findElement(By.name("username"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement inputUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        WebElement inputPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));

        inputUsername.sendKeys("valor_que_voce_quer_inserir");
        inputPassword.sendKeys("valor_que_voce_quer_inserir");

    }

    public static void findAndZipChromeDriver(String directoryPath, String navegador, boolean replaceIfExists, boolean ziparDriver) throws IOException {
        Files.walk(Paths.get(directoryPath, new String[0]), new java.nio.file.FileVisitOption[0])
                .filter(p -> p.toString().contains(".exe"))
                .findFirst()
                .ifPresent(path -> {
                    try {
                        String version_original = path.getParent().getFileName().toString();
                        String version = version_original.split("\\.")[0];
                        if (ziparDriver) {
                            String zipFileName = String.format("%s%s%s%s.zip", new Object[] { path.getParent(), File.separator, navegador, version });
                            String newEntryName = String.format("%s%s.exe", new Object[] { navegador, version });
                            Path txtFilePath = Paths.get(path.getParent().toString(), new String[] { "versao.txt" });
                            Files.writeString(txtFilePath, version_original, new java.nio.file.OpenOption[0]);
                            zipFileWithTxt(path.toString(), zipFileName, newEntryName, txtFilePath.toString());
                            moverPara(zipFileName, directoryPath, replaceIfExists);
                            String folderDriver = path.getParent().getParent().getParent().toString();
                            FileUtils.deleteDirectory(new File(folderDriver));
                        } else {
                            moverPara(path.toString(), directoryPath, replaceIfExists);
                            String folderDriver = path.getParent().getParent().getParent().toString();
                            FileUtils.deleteDirectory(new File(folderDriver));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static void zipFileWithTxt(String filePath, String zipFileName, String newEntryName, String txtFilePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFileName);
        try {
            ZipOutputStream zos = new ZipOutputStream(fos);
            try {
                addToZipFile(filePath, newEntryName, zos);
                addToZipFile(txtFilePath, "versao.txt", zos);
                zos.close();
            } catch (Throwable throwable) {
                try {
                    zos.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            fos.close();
        } catch (Throwable throwable) {
            try {
                fos.close();
            } catch (Throwable throwable1) {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
    }

    private static void addToZipFile(String filePath, String fileNameInZip, ZipOutputStream zos) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        try {
            ZipEntry zipEntry = new ZipEntry(fileNameInZip);
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0)
                zos.write(bytes, 0, length);
            zos.closeEntry();
            fis.close();
        } catch (Throwable throwable) {
            try {
                fis.close();
            } catch (Throwable throwable1) {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
    }

    private static boolean downloadDriver(String directoryDownloadDriver, String navegador) {
        switch (navegador) {
            case "chrome":
                WebDriverManager.chromedriver().cachePath(directoryDownloadDriver).setup();
                break;
            case "edge":
                WebDriverManager.edgedriver().cachePath(directoryDownloadDriver).setup();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().cachePath(directoryDownloadDriver).setup();
                break;
            case "safari":
                WebDriverManager.safaridriver().cachePath(directoryDownloadDriver).setup();
                break;
            case "opera":
                WebDriverManager.operadriver().cachePath(directoryDownloadDriver).setup();
                break;
            case "iexplorer":
                WebDriverManager.iedriver().cachePath(directoryDownloadDriver).setup();
                break;
        }
        return true;
    }

    private static boolean downloadDriver(String directoryDownloadDriver, String navegador, String versao) {
        switch (navegador) {
            case "chrome":
                WebDriverManager.chromedriver().driverVersion(versao).cachePath(directoryDownloadDriver).setup();
                break;
            case "edge":
                WebDriverManager.edgedriver().driverVersion(versao).cachePath(directoryDownloadDriver).setup();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().driverVersion(versao).cachePath(directoryDownloadDriver).setup();
                break;
            case "safari":
                WebDriverManager.safaridriver().driverVersion(versao).cachePath(directoryDownloadDriver).setup();
                break;
            case "opera":
                WebDriverManager.operadriver().driverVersion(versao).cachePath(directoryDownloadDriver).setup();
                break;
            case "iexplorer":
                WebDriverManager.iedriver().driverVersion(versao).cachePath(directoryDownloadDriver).setup();
                break;
        }
        return true;
    }

    private static void moverPara(String pathZip, String directoryDestination, boolean replaceZip) {
        Path sourcePath = Paths.get(pathZip, new String[0]);
        Path destinationPath = Paths.get(directoryDestination, new String[0]);
        try {
            if (Files.notExists(destinationPath, new java.nio.file.LinkOption[0]))
                throw new Exception("Nfoi possivel encontrar o diretdo AE.");
            Path targetPath = destinationPath.resolve(sourcePath.getFileName());
            if (replaceZip) {
                Files.move(sourcePath, targetPath, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
            } else if (Files.notExists(targetPath, new java.nio.file.LinkOption[0])) {
                Files.move(sourcePath, targetPath, new CopyOption[0]);
                System.out.println("Arquivo movido para: " + targetPath);
            } else {
                System.out.println("O arquivo jexiste e o par'replaceZip' falso.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}