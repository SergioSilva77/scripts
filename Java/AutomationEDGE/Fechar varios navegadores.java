import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.automationedge.ps.core.globalstorage.GlobalStorage;
import java.util.*;

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

  //##########################################
  limparTodasInstancias();
  //##########################################

  putRow(data.outputRowMeta, r);
  return true;
}

public void limparTodasInstancias(){
	List<Object> keys = new ArrayList<Object>();
	GlobalStorage gs = GlobalStorage.getInstance();
	for (Object key: gs.keySet()) {
		keys.add(key);
		try {
			WebDriver d = (WebDriver) gs.get(key);
			d.quit();
			logBasic(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+key+" fechado.");
		} catch (Exception e) {}
	}
}