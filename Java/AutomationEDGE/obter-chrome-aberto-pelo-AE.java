import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.automationedge.ps.core.globalstorage.GlobalStorage;

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


  obterInstanciaDoBrowser();

  putRow(data.outputRowMeta, r);
  return true;
}

public void obterInstanciaDoBrowser() throws InterruptedException{
 	String instance=getVariable(EnvironmentVariable.WF_INSTANCE_ID.name());
    GlobalStorage gs = GlobalStorage.getInstance();
	ChromeDriver driver = null;
    if (instance != null){
        driver = (ChromeDriver)gs.get("BROWSER1_"+instance+"_driver");
    } else {
        driver = (ChromeDriver)gs.get("BROWSER1_driver");
	}

	if (driver == null){
		return;
	}
    // use o seu código aqui abaixo
	driver.findElement(By.cssSelector("body > div.L3eUgb > div.o3j99.ikrT4e.om7nvf > form > div:nth-child(1) > div.A8SBwf > div.RNNXgb > div > div.a4bIc > input")).sendKeys("ola mundo");
}