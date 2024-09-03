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
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import java.net.URL;

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
  String caminho = get(Fields.Out, "caminho").getString(r);
  String url = get(Fields.Out, "url").getString(r);
  String driverPath = get(Fields.Out, "driverPath").getString(r);
  abrirBrowser(url, caminho,driverPath);

  putRow(data.outputRowMeta, r);
  return true;
}


public void abrirBrowser(String url, String caminho, String driverPath) throws InterruptedException {
  System.setProperty("webdriver.gecko.driver", driverPath);
  
  FirefoxOptions options = new FirefoxOptions();
  FirefoxProfile profile = new FirefoxProfile();
  profile.setPreference("browser.download.manager.showWhenStarting", false);
  profile.setPreference("browser.download.folderList", 2);
  profile.setPreference("browser.download.manager.showWhenStarting", false);
  profile.setPreference("browser.download.dir", caminho);
  profile.setPreference("browser.helperApps.neverAsk.openFile","application/msexcel,text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
  profile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/msexcel,text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
  profile.setPreference("browser.helperApps.alwaysAsk.force", false);
  profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
  profile.setPreference("browser.download.manager.focusWhenStarting", false);
  profile.setPreference("browser.download.manager.useWindow", false);
  profile.setPreference("browser.download.manager.showAlertOnComplete", false);
  profile.setPreference("browser.download.manager.closeWhenDone", false);
  //options.setHeadless(true);
  //options.merge(new FirefoxOptions().setProfile(profile));

  DesiredCapabilities capabilities = DesiredCapabilities.firefox();
  capabilities.setCapability(FirefoxDriver.PROFILE, profile);
  capabilities.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, 1);
  capabilities.setCapability("moz:firefoxOptions", options);

  WebDriver driver = new FirefoxDriver(capabilities);
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