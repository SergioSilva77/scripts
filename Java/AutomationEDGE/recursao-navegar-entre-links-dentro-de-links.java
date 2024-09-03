import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.automationedge.ps.core.globalstorage.GlobalStorage;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;

// ################################################
// #		VARIAVEIS
// ################################################

private List < Dados > db = new ArrayList < String > ();
private WebDriver driver;
private String caminho;

// ################################################
// #		PRINCIPAL
// ################################################

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

  // ################################################
  // #		CHAMAR METODOS
  // ################################################

  String path = get(Fields.Out, "caminho").getString(r);
  this.caminho = path;
  obterInstanciaDoBrowser();
  dadosUnidade1(null, null);

  putRow(data.outputRowMeta, r);
  return true;
}



// ####################################################################
// #	OBTER A INSTANCIA DO BROWSER QUE FOI ABERTA ANTERIORMENTE
// ####################################################################

public void obterInstanciaDoBrowser() throws InterruptedException {
  String instance = getVariable(EnvironmentVariable.WF_INSTANCE_ID.name());
  GlobalStorage gs = GlobalStorage.getInstance();
  WebDriver driver = null;

  if (instance != null) {
    driver = (FirefoxDriver) gs.get("BROWSER1_" + instance + "_driver");
  } else {
    driver = (FirefoxDriver) gs.get("BROWSER1_driver");
  }

  if (driver == null) {
    return;
  }
  this.driver = driver;
}

// ################################################
// #	PERCORRE A TABELA E SUAS UNIDADES
// ################################################

public void dadosUnidade1(List < Dados > links, Dados ds) throws InterruptedException {
  List < Dados > dadosLink = new ArrayList < String > ();

  // ###############################################################################
  // ############### CLICA NO LINK DE CADA ITEM DA TABELA ##########################
  // ###############################################################################

  if (links != null && ds == null) {
    for (int i = 0; i < links.size(); i++) {
      // IR PARA A URL DE CADA ITEM DA TABELA
      Dados d = (Dados) links.get(i);
      // SE TABELA CONTEM MAIS SUBUNIDADE, ENTÃO CHAMAR A MESMA FUNCAO ATÉ QUE CHEGUE NA TELA DE DESTINO
      if (d.getContemSubUnidade()) {
        driver.get(d.getUrl());
        dadosUnidade1(links, d);
      }
    }
  }

  // ###############################################################################
  // ############### VERIFICA SE HÁ MAIS SUBUNIDADES ###############################
  // ###############################################################################

  WebElement tabela = driver.findElement(By.cssSelector("#tableRelatorio"));
  List < WebElement > linhas = tabela.findElements(By.tagName("tr"));
  for (int i = 4; i < linhas.size(); i++) { // COMEÇAR A PARTIR DA LINHA 4 DA TABELA			
    List < WebElement > colUnidade = driver.findElements(By.xpath("//table[@id='tableRelatorio']//tr[" + (i + 1) + "]//td[1]//a"));
    if (colUnidade.size() <= 0) {
      continue;
    }

    WebElement unidade = (WebElement) colUnidade.get(0);
    String href = unidade.getAttribute("href");
    String textUnidade = unidade.getText().trim();
    Dados d = new Dados();

    WebDriverWait wait = new WebDriverWait(driver, 2000);
    WebElement subUnidade = (WebElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//table[@id='tableRelatorio']//tr[" + (i + 1) + "]//td[@id='tdMain']//a")));

	// CASO EXISTA SUBUNIDADE, ADICIONAR NA LISTA
    if (subUnidade != null) {
      d.setUnidade(textUnidade);
      d.setUrl(href);
      d.setContemSubUnidade(true);
      dadosLink.add(d);
    }
  }

  // ##################################################################################
  // ############### SAIDA/CAUDA DO MÉTODO RECURSIVO ABAIXO ###########################
  // ##################################################################################

  // SE NA LISTA HÁ LINKS, PRECISA CHAMAR A MESMA FUNCAO P/ CHAMAR CADA LINK
  if (dadosLink.size() > 0) {
    dadosUnidade1(dadosLink, null);
  } else if (ds != null) {
    LocalDate dataAtual = LocalDate.now();
    String nomeMes = dataAtual.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("pt", "BR")).toUpperCase();
    String dataNormal = dataAtual.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
    String novoNome = String.format("%s\\HE_%s_%s_%s.xls",caminho ,ds.getUnidade(), dataNormal, nomeMes);
    File file = new File(novoNome);
    
    if (!file.exists()){
    	db.add(ds);
    	// CLICAR EM DOWNLOAD
    	driver.findElement(By.xpath("/html/body/div[3]/center/div/table[1]/tbody/tr/td[4]/a/img")).click();
    	// VERIFICAR E RENOMEAR ARQUIVO
    	verificarArquivoComBarrasComTempoLimite(caminho, ds);
    }
  }
}

// ################################################
// #	TRATA O ARQUIVO BAIXADO
// ################################################

public void verificarArquivoComBarrasComTempoLimite(String folder, Dados ds) throws InterruptedException {
    File pasta = new File(folder);
    int i = 0;
    while (i <= 3) {
        i++;
        File[] arquivos = pasta.listFiles();
        if (arquivos != null) {
            for (int l = 0; l < arquivos.length; l++) {
                String name = arquivos[l].getName();
                if (name.split("_").length < 3) {
                    // Obter a data atual
                    LocalDate dataAtual = LocalDate.now();
                    String nomeMes = dataAtual.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("pt", "BR")).toUpperCase();
                    String dataNormal = dataAtual.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
                    // RENOMEAR ARQUIVO
                    String novoNome = String.format("HE_%s_%s_%s.xls", ds.getUnidade(), dataNormal, nomeMes);
                    File novoArquivo = new File(folder, novoNome);
                    File arquivoOriginal = arquivos[l];
                    Instant now = Instant.now();
                    Instant threshold = now.minusSeconds(20);
                    Instant lastModified = Instant.ofEpochMilli(arquivoOriginal.lastModified());
                    if (lastModified.isAfter(threshold)) {
                        if (arquivoOriginal.renameTo(novoArquivo)) {
                            logBasic("Arquivo " + name + " renomeado para: " + novoNome);
                        } else {
                            logBasic("Não foi possível renomear o arquivo " + name + " para: " + novoNome);
                        }
                        return;
                    }
                }
            }
        }
        Thread.sleep(2000);
    }
}

// ################################################
// #	CRIA A CLASSE PARA GUARDAR VALORES
// ################################################

public class Dados {
  private String url;
  private String unidade;
  private boolean contemSubUnidade;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUnidade() {
    return unidade;
  }

  public void setUnidade(String unidade) {
    this.unidade = unidade;
  }

  public boolean getContemSubUnidade() {
    return contemSubUnidade;
  }

  public void setContemSubUnidade(boolean contemSubUnidade) {
    this.contemSubUnidade = contemSubUnidade;
  }
}