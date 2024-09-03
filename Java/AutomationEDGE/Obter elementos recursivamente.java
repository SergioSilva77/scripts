import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;

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

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws InterruptedException, ProcessStudioException {
  if (first) {
    first = false;

    /* TODO: Your code here. (Using info fields)

    FieldHelper infoField = get(Fields.Info, "info_field_name");

    RowSet infoStream = findInfoRowSet("info_stream_tag");

    Object[] infoRow = null;

    int infoRowCount = 0;

    // Read all rows from info step before calling getRow() method, which returns first row from any
    // input rowset. As rowMeta for info and input steps varies getRow() can lead to errors.
    while((infoRow = getRowFrom(infoStream)) != null){

      // do something with info data
      infoRowCount++;
    }
    */
  }

  Object[] r = getRow();

  if (r == null) {
    setOutputDone();
    return false;
  }

  // It is always safest to call createOutputRow() to ensure that your output row's Object[] is large
  // enough to handle any new fields you are creating in this step.
  r = createOutputRow(r, data.outputRowMeta.size());


  String elemento = "//button[@aria-label='Fechar (Esc)' and contains(@class,'close-btn')]";
  ChromeDriver driver = obterInstanciaDoBrowser();
  WebElement element = null;
  driver.switchTo().defaultContent();
  try{
		logBasic("-------------------> tentando clicar no elemento sem entrar no iframe");
		element = driver.findElement(By.xpath(elemento));
		element.click();
		logBasic("-------------------> sucesso ao clicar sem entrar no iframe");
	} catch (Exception e){
		  logBasic("-------------------> Nao houve sucesso ao clicar no elemento sem entrar no iframe");
		  for (int i = 0; i <= 10;i++){
			element = findElementInIFramesXpath(driver, elemento, "");
			Thread.sleep(1000);
			if (element != null){
				try{
					logBasic("-------------------> elemento encontrado");
					element.click();
					logBasic("-------------------> clique efetuado");
					break;
				} catch (Exception e){}
			}
			logBasic("-------------------> Tentativa: "+i);
  		}
	}  


  
  
                

  // Send the row on to the next step.
  putRow(data.outputRowMeta, r);

  return true;
}

 public static WebElement findElementInIFramesXpath(WebDriver driver, String xpath, String texto) {
        return (WebElement)findElementInIFramesXpath(driver, xpath, texto, (List<WebElement>)driver.findElements(By.tagName("iframe")), driver);
    }

    private static WebElement findElementInIFramesXpath(WebDriver driver, String xpath, String texto, List<WebElement> iframes, WebDriver originalDriver) {
        for (int i = 0; i < iframes.size(); i++) {
            WebElement iframe = (WebElement)iframes.get(i);
            try {
                originalDriver.switchTo().frame(iframe);
                List<WebElement> elements = (List<WebElement>)originalDriver.findElements(By.xpath(xpath));
                for (int j = 0; j < elements.size(); j++) {
                    WebElement element = (WebElement)elements.get(j);
                    if (texto.isEmpty() || element.getText().equals(texto)) {
                        return element;
                    }
                }

                List<WebElement> nestedIframes = (List<WebElement>)originalDriver.findElements(By.tagName("iframe"));
                if (!nestedIframes.isEmpty()) {
                    WebElement nestedElement = (WebElement)findElementInIFramesXpath(originalDriver, xpath, texto, nestedIframes, originalDriver);
                    if (nestedElement != null) {
                        return nestedElement; 
                    }
                }
                originalDriver.switchTo().defaultContent();
            } catch (Exception e) {
                originalDriver.switchTo().defaultContent();
            }
        }
        return null;
    }


public ChromeDriver obterInstanciaDoBrowser() throws InterruptedException{
 	String instance=getVariable(EnvironmentVariable.WF_INSTANCE_ID.name());
    GlobalStorage gs = GlobalStorage.getInstance();
	ChromeDriver driver = null;
    if (instance != null){
		logBasic("Instancia obtida, rodando pelo agent");
        driver = (ChromeDriver)gs.get("BROWSER1_"+instance+"_driver");
    } else {
		logBasic("Instancia obtida, rodando pelo studio");
        driver = (ChromeDriver)gs.get("BROWSER1_driver");
	}
	return driver;
}
