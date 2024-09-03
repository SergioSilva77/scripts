package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        // Carregar a classe JSONObject
        Class<?> jsonObjectClass = Class.forName("org.json.JSONObject");

        // Criar uma instância de JSONObject
        Constructor<?> constructor = jsonObjectClass.getConstructor();
        Object jsonObject = constructor.newInstance();

        // Acessar e invocar o método 'put'
        Method putMethod = jsonObjectClass.getMethod("put", String.class, Object.class);
        putMethod.invoke(jsonObject, "chave", "valor");

        // Acessar e invocar o método 'toString'
        Method toStringMethod = jsonObjectClass.getMethod("toString");
        String jsonString = (String) toStringMethod.invoke(jsonObject);

        System.out.println(jsonString);

        int codigoPonto = 100;
        try {
//            codigoPonto = 101;
//            String caminhoDriver = args[0];
//            codigoPonto = 102;
//            String caminhoDownloadPdf = args[1];
//            codigoPonto = 103;
//            String ipNavegador = args[2];
//            codigoPonto = 104;
//            boolean landscape = Boolean.parseBoolean(args[3]);
//            codigoPonto = 105;
//            String colorMode = args[4];
//            codigoPonto = 106;
//            String pageSize = args[5];
//            codigoPonto = 107;
//            String header = args[6];
//            codigoPonto = 108;
//            String footer = args[7];
//            codigoPonto = 109;
//            double scale = Double.parseDouble(args[8].replace(",","."));
//            codigoPonto = 110;
//            double marginBottom = Double.parseDouble(args[9].replace(",","."));
//            codigoPonto = 111;
//            double marginLeft = Double.parseDouble(args[10].replace(",","."));
//            codigoPonto = 112;
//            double marginRight = Double.parseDouble(args[11].replace(",","."));
//            codigoPonto = 113;
//            double marginTop = Double.parseDouble(args[12].replace(",","."));
//            codigoPonto = 114;
//            double opacity = Double.parseDouble(args[13].replace(",","."));
//            codigoPonto = 115;
//            double fontSize = Double.parseDouble(args[14].replace(",","."));
//            codigoPonto = 116;
//            String textAlign = args[15];
//            codigoPonto = 117;
//            double borderWidth = Double.parseDouble(args[16].replace(",","."));
//            codigoPonto = 118;
//            String pageRanges = args[17];



            String caminhoDriver = "C:\\Drivers navegadores\\CHROME120.exe";
            String caminhoDownloadPdf = "C:\\Drivers navegadores\\test.pdf";
            String ipNavegador = "127.0.0.1:9223";
            boolean landscape = false;
            String colorMode = "RGB";
            String pageSize = "A4";
            String header = "Cabeçalho da Página";
            String footer = "Rodapé da Página";
            int scale = 1;
            int marginBottom = 0;
            double marginLeft = 0;
            int marginRight = 0;
            int marginTop = 0;
            double opacity = 0.5;
            int fontSize = 12;
            String textAlign = "left";
            int borderWidth = 1;
            String pageRanges = "1-3"; // ex 1-2 ou 1-10 paginas. no caso vc pode escolher todas paginas


            /**********************************************/
            // Caminho do driver do navegador
            String pathLoc = "C:\\Drivers navegadores";

            // Configurações para imprimir em PDF
            JSONObject settings = new JSONObject();
            JSONArray recentDestinations = new JSONArray();
            JSONObject destination = new JSONObject();
            destination.put("id", "Save as PDF");
            destination.put("origin", "local");
            destination.put("account", "");
            recentDestinations.put(destination);
            settings.put("recentDestinations", recentDestinations);
            settings.put("selectedDestinationId", "Save as PDF");
            settings.put("version", 2);

            // Opções do Chrome
            ChromeOptions options = new ChromeOptions();

            Map<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("printing.print_preview_sticky_settings.appState", settings.toString());
            chromePrefs.put("download.prompt_for_download", false);
            chromePrefs.put("plugins.always_open_pdf_externally", true);
            chromePrefs.put("download.open_pdf_in_system_reader", false);
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("printing.print_to_pdf", true);
            chromePrefs.put("download.default_directory", pathLoc);
            chromePrefs.put("savefile.default_directory", pathLoc);

            options.setExperimentalOption("prefs", chromePrefs);
            options.addArguments("--kiosk-printing");
            options.addArguments("--kiosk-pdf-printing");
            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

            ChromeDriver driver = new ChromeDriver(options);
            /**********************************************/



//            codigoPonto = 1;
//            System.setProperty("webdriver.chrome.driver", caminhoDriver);
//            ChromeOptions options = new ChromeOptions();
//            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
//            options.setExperimentalOption("debuggerAddress", ipNavegador);
//            ChromeDriver driver = new ChromeDriver(options);

            driver.switchTo().frame(driver.findElement(By.cssSelector("#conteudo")));

            codigoPonto = 2;
            Map<String, Object> commandParams = new HashMap<>();
            commandParams.put("landscape",landscape);
            commandParams.put("colorMode", colorMode);
            commandParams.put("pageSize", pageSize);
            commandParams.put("header", header);
            commandParams.put("footer", footer);

            codigoPonto = 3;
            JSONObject params = new JSONObject();
            params.put("scale", scale);
            params.put("marginBottom", marginBottom);
            params.put("marginLeft", marginLeft);
            params.put("marginRight", marginRight);
            params.put("marginTop", marginTop);
            params.put("opacity", opacity);
            params.put("fontSize", fontSize);
            params.put("textAlign", textAlign);
            params.put("borderWidth", borderWidth);
            if (!Objects.equals(pageRanges, "todas")){
                commandParams.put("pageRanges", pageRanges);
            }

            codigoPonto = 4;
            Map<String, Object> response = (Map<String, Object>) driver.executeCdpCommand("Page.printToPDF", commandParams);

            codigoPonto = 5;
            byte[] pdf = java.util.Base64.getDecoder().decode((String) response.get("data"));
            java.nio.file.Files.write(java.nio.file.Paths.get(caminhoDownloadPdf), pdf);

            codigoPonto = 6;

            System.exit(codigoPonto);
        } catch (IOException e){
            System.exit(codigoPonto);
        } catch (Exception e){
            System.exit(codigoPonto);
        }
        System.exit(codigoPonto);
    }
}