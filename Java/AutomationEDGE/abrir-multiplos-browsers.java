/*
FAVOR INSERIR O CÓDIGO ANTES

var limparTodasInstancias = 'sim'
var browsers = JSON.stringify([
	{
		nome: 'BROWSER1',
		acao: 'abrir_instancia', //abrir_instancia / recuperar_instancia
		url: 'https://www.google.com/finance/quote/USD-BRL?sa=X&ved=2ahUKEwjK9KTmo979AhXpppUCHU-WDRcQmY0JegQICxAd',
		caminhoDownload: '',
		caminhoDriver: 'C:\\drivers\\chromedriver3.exe',
		permitirMultiplosDownloads: 'sim',
		tipoInicio: 'anonimo', // headless, anonimo, normal
		ignorarCertificados: 'sim',
		limparInstancia: 'sim',
		maximizarJanela: 'sim' // sim = maximizado / senão normal
	},
	{
		nome: 'BROWSER2',
		acao: 'abrir_instancia', //abrir_instancia / recuperar_instancia
		url: 'https://www.google.com/finance/quote/USD-BRL?sa=X&ved=2ahUKEwjK9KTmo979AhXpppUCHU-WDRcQmY0JegQICxAd',
		caminhoDownload: '',
		caminhoDriver: 'C:\\drivers\\chromedriver3.exe',
		permitirMultiplosDownloads: 'sim',
		tipoInicio: 'headless', // headless, anonimo, normal
		ignorarCertificados: 'sim',
		limparInstancia: 'sim',
		maximizarJanela: 'sim' // sim = maximizado / senão normal
	},
	{
		nome: 'BROWSER3',
		acao: 'abrir_instancia', //abrir_instancia / recuperar_instancia
		url: 'https://www.google.com/finance/quote/USD-BRL?sa=X&ved=2ahUKEwjK9KTmo979AhXpppUCHU-WDRcQmY0JegQICxAd',
		caminhoDownload: '',
		caminhoDriver: 'C:\\drivers\\chromedriver3.exe',
		permitirMultiplosDownloads: 'sim',
		tipoInicio: 'normal', // headless, anonimo, normal
		ignorarCertificados: 'sim',
		limparInstancia: 'sim',
		maximizarJanela: 'sim' // sim = maximizado / senão normal
	}
])

*/


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.automationedge.ps.core.globalstorage.GlobalStorage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

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

	String browsers = get(Fields.Out, "browsers").getString(r);
	String limparTodasInstancias = get(Fields.Out, "limparTodasInstancias").getString(r);
	main(browsers,limparTodasInstancias);


  putRow(data.outputRowMeta, r);
  return true;
}

public void main(String browsers, String limparTodasInstancias) throws InterruptedException{
	if (limparTodasInstancias.equals("sim")){
		limparTodasInstancias();
	}

    JSONArray jsonArray = new JSONArray(browsers);
	int i = 0;
    for (Object obj : jsonArray){
		i++;
        JSONObject jobj = (JSONObject) obj;
        String nome = jobj.getString("nome");
        String url = jobj.getString("url");
        String caminhoDownload = jobj.getString("caminhoDownload");
        String caminhoDriver = jobj.getString("caminhoDriver");
        String permitirMultiplosDownloads = jobj.getString("permitirMultiplosDownloads");
        String tipoInicio = jobj.getString("tipoInicio");
        String ignorarCertificados = jobj.getString("ignorarCertificados");
        String limparInstancia = jobj.getString("limparInstancia");
        String maximizarJanela = jobj.getString("maximizarJanela");
        String acao = (String)jobj.getString("acao").trim();

		logBasic(nome);

		if (acao.equals("recuperar_instancia")){
			recuperarInstancia();
		} else if (acao.equals("abrir_instancia")) {

			if (limparInstancia.equals("sim")){
				logBasic(">>>>>>>>>>>> limpando instancia antes de abrir: "+nome);
				//limparInstancia(nome);
			}

			DesiredCapabilities capacidade = configOptions(caminhoDriver, 
										caminhoDownload, 
										permitirMultiplosDownloads, 
										tipoInicio, 
										ignorarCertificados);
		
			abrirBrowser(capacidade, url, maximizarJanela, nome);
		}
    }
}

public void abrirBrowser(DesiredCapabilities cap, String url, String maximizarJanela, String nome) throws InterruptedException{	
	GlobalStorage gs = GlobalStorage.getInstance();
	String instance = getVariable(EnvironmentVariable.WF_INSTANCE_ID.name());
	ChromeDriver driver = new ChromeDriver(cap);
	if (maximizarJanela.equals("sim")){
		driver.manage().window().maximize();
	}
	driver.get(url);

	if (instance != null) {
    	//Rodando pelo agent
    	gs.add(String.format("%s_%s_driver", nome, instance), driver);
    	gs.add(String.format("%s_%s_delay", nome, instance), 50000);
    	gs.add(String.format("%s_%s_retryCount", nome, instance), 5);
    	logBasic(String.format("%s_%s_driver", nome, instance));
  	} else {
    	//Rodando pelo studio
    	gs.add(String.format("%s_driver", nome), driver);
    	gs.add(String.format("%s_delay", nome), 50000);
    	gs.add(String.format("%s_retryCount", nome), 5);
		logBasic(String.format(">>>>>>>>>>>>>>>>>>>>>>>> %s_driver",nome));
  	}
}

public DesiredCapabilities configOptions(String caminhoDriver, String caminhoDownload, String... params) {

	System.setProperty("webdriver.chrome.driver", caminhoDriver);
	Map<String, Object> chromePrefs = new HashMap<String, Object>();
	chromePrefs.put("download.default_directory", caminhoDownload);

	if (params[0] == "sim"){ 
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1 );
		chromePrefs.put("download.prompt_for_download", false);
	}

	ChromeOptions options = new ChromeOptions();
	if (params[1].equals("headless")){ 
		options.addArguments("--headless");
	} else if (params[1].equals("anonimo")){
		options.addArguments("--incognito");
	}	
	options.setExperimentalOption("prefs", chromePrefs);

	DesiredCapabilities cap = DesiredCapabilities.chrome();
	cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
	cap.setCapability(ChromeOptions.CAPABILITY, options);
	return cap;
}

public void limparTodasInstancias(){
List<Object> keys = new ArrayList<Object>();
	GlobalStorage gs = GlobalStorage.getInstance();
for (Object key: gs.keySet()) {
	keys.add(key);
  try {
    WebDriver d = (WebDriver) gs.get(key);
    d.quit();
  } catch (Exception e) {}
}


}

public void limparInstancia(String browser){
	List<Object> keys = new ArrayList<Object>();
	GlobalStorage gs = GlobalStorage.getInstance();
	for (Object key: gs.keySet()) {
		keys.add(key);
		try {
			if (key.toString().startsWith(browser)){
				WebDriver d = (WebDriver) gs.get(key);
				d.quit();
				logBasic(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+key+" fechado.");
			}	
			
		} catch (Exception e) {}
	}
}

public void recuperarInstancia(){
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
	acao(driver);
}

public void acao(ChromeDriver driver){
	// aqui vc manipula o driver que ja foi aberto
}