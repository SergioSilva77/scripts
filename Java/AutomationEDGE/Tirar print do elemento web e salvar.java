import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.automationedge.ps.core.globalstorage.GlobalStorage;
import org.openqa.selenium.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws ProcessStudioException, InterruptedException, IOException {
  if (first) {
    first = false;
  }

  Object[] r = getRow();
  if (r == null) {
    setOutputDone();
    return false;
  }
  r = createOutputRow(r, data.outputRowMeta.size());

  String CaminhoImagem = get(Fields.In, "CaminhoImagem").getString(r);
  String TipoSeletor = get(Fields.In, "TipoSeletor").getString(r);
  String Seletor = get(Fields.In, "Seletor").getString(r);

  obterInstanciaDoBrowser(CaminhoImagem,TipoSeletor,Seletor);

  putRow(data.outputRowMeta, r);
  return true;
}

public void obterInstanciaDoBrowser(String caminhoImagem, String tipoSeletor, String seletor) throws InterruptedException, IOException{
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

    WebElement element = null;
	if (tipoSeletor.equals("xpath")){
		element = (WebElement)driver.findElement(By.xpath(seletor));
	} else if (tipoSeletor.equals("css")){
		element = (WebElement)driver.findElement(By.cssSelector(seletor));
	}

    WrapsDriver wrapsDriver = (WrapsDriver) element;
    File screenshot = (File)((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
    Rectangle rectangle = new Rectangle(element.getSize().width, element.getSize().height, element.getSize().height, element.getSize().width);
    Point location = element.getLocation();
    BufferedImage bufferedImage = ImageIO.read(screenshot);
    BufferedImage destImage = bufferedImage.getSubimage(location.x, location.y, rectangle.width, rectangle.height);
 
    ImageIO.write(destImage, getExtension(caminhoImagem), screenshot);
    File file = new File(caminhoImagem);
    FileUtils.copyFile(screenshot, file);

}

public String getExtension(String imageName) {
	return imageName.substring(imageName.lastIndexOf(".") + 1);
}