import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.automationedge.ps.core.globalstorage.GlobalStorage;
import java.util.*;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Method;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import java.io.IOException;
import java.util.Objects;

public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws ProcessStudioException, InterruptedException {
  if (first) {
    first = false;
  }

  Object[] r = getRow();
  if (r == null) {
    setOutputDone();
    return false;
  }
  r = createOutputRow(r, data.outputRowMeta.size());
  limparTodasInstancias();

  String caminhoDriver = get(Fields.In, "caminhoDriver").getString(r);
  String url = get(Fields.In, "url").getString(r);
  String caminhoDownloads = get(Fields.In, "caminhoDownloads").getString(r);
  abrirBrowser(url,caminhoDownloads, caminhoDriver);

  putRow(data.outputRowMeta, r);
  return true;
}


public void abrirBrowser(String url, String pathLoc, String caminhoDriver) throws InterruptedException {
  System.setProperty("webdriver.chrome.driver", caminhoDriver);

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

Map < String, Object > chromePrefs = new HashMap < String, Object > ();
chromePrefs.put("printing.print_preview_sticky_settings.appState", settings.toString());
            chromePrefs.put("download.prompt_for_download", false);
            chromePrefs.put("plugins.always_open_pdf_externally", true);
            chromePrefs.put("download.open_pdf_in_system_reader", false);
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("printing.print_to_pdf", true);
            chromePrefs.put("download.default_directory", pathLoc);
            chromePrefs.put("savefile.default_directory", pathLoc);


  ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", chromePrefs);
	    options.addArguments("--incognito");
	    options.addArguments("--kiosk-printing");
            options.addArguments("--kiosk-pdf-printing");
            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            
            ChromeDriver driver = new ChromeDriver(options);
  driver.get(url);

  String instance = getVariable(EnvironmentVariable.WF_INSTANCE_ID.name());
  if (instance != null) {
    //Rodando pelo agent
    GlobalStorage gs = GlobalStorage.getInstance();
    gs.add(String.format("BROWSER1_%s_driver", instance), driver);
    gs.add(String.format("BROWSER1_%s_delay", instance), 50000);
    gs.add(String.format("BROWSER1_%s_retryCount", instance), 5);
    logBasic(String.format("BROWSER1_%s_retryCount", instance));
  } else {
    //Rodando pelo studio
    GlobalStorage gs = GlobalStorage.getInstance();
    gs.add(String.format("BROWSER1_driver"), driver);
    gs.add(String.format("BROWSER1_delay"), 50000);
    gs.add(String.format("BROWSER1_retryCount"), 5);
  }
  driver.manage().window().maximize();

}

public void limparTodasInstancias() {
  List < Object > keys = new ArrayList < Object > ();
  GlobalStorage gs = GlobalStorage.getInstance();
  for (Object key: gs.keySet()) {
    keys.add(key);
    try {
      WebDriver d = (WebDriver) gs.get(key);
      d.quit();
      logBasic(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + key + " fechado.");
    } catch (Exception e) {}
  }
}