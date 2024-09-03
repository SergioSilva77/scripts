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
import java.io.File;

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
  String caminhoExtensao = get(Fields.In, "caminhoExtensao").getString(r);
  String url = get(Fields.In, "url").getString(r);
  String diretorioDownloads = get(Fields.In, "diretorioDownloads").getString(r);
  abrirBrowser(url,diretorioDownloads, caminhoDriver, caminhoExtensao);

  putRow(data.outputRowMeta, r);
  return true;
}


public void abrirBrowser(String url, String diretorioDownloads, String caminhoDriver, String caminhoExtensao) throws InterruptedException {
  System.setProperty("webdriver.chrome.driver", caminhoDriver);
  Map < String, Object > chromePrefs = new HashMap < String, Object > ();
  chromePrefs.put("profile.default_content_settings.popups", 0);
  chromePrefs.put("download.default_directory", diretorioDownloads);
  chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
  chromePrefs.put("download.prompt_for_download", false);
  ChromeOptions options = new ChromeOptions();
  options.addExtensions(new File(caminhoExtensao));
  options.setExperimentalOption("prefs", chromePrefs);
  DesiredCapabilities cap = DesiredCapabilities.chrome();
  cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
  cap.setCapability(ChromeOptions.CAPABILITY, options);
  ChromeDriver driver = new ChromeDriver(cap);
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