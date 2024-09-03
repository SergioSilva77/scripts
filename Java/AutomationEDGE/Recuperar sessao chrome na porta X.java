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

  String Porta = get(Fields.In, "Porta").getString(r);
  String CaminhoDriver = get(Fields.Out, "CaminhoDriver").getString(r);
  abrirBrowser("https://www.google.com","Diretório dos downloads",Integer.parseInt(Porta), CaminhoDriver);

  putRow(data.outputRowMeta, r);
  return true;
}


public void abrirBrowser(String url, String Pasta_Vivo, Integer porta, String caminhoDriver) throws InterruptedException {
  System.setProperty("webdriver.chrome.driver", caminhoDriver);
  ChromeOptions options = new ChromeOptions();
  options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
  options.setExperimentalOption("debuggerAddress", String.format("localhost:%s",porta));
  
  ChromeDriver driver = new ChromeDriver(options);
  //driver.get(url);

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