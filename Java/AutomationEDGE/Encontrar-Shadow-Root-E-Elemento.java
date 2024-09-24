import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.JavascriptExecutor;
import com.automationedge.ps.core.globalstorage.GlobalStorage;
import org.openqa.selenium.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

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


  String instance=getVariable(EnvironmentVariable.WF_INSTANCE_ID.name());
  obterInstanciaDoBrowser(instance);

  putRow(data.outputRowMeta, r);
  return true;
}

public WebElement obterInstanciaDoBrowser(String instance) throws InterruptedException {
    GlobalStorage gs = GlobalStorage.getInstance();
    ChromeDriver driver = null;

    if (instance != null) {
        driver = (ChromeDriver) gs.get("BROWSER1_" + instance + "_driver");
    } else {
        driver = (ChromeDriver) gs.get("BROWSER1_driver");
    }

    if (driver == null) {
        return null;
    }

    // Definir os critÃ©rios de busca
    String tag = "input"; // Tag que estamos procurando
    String classe = null; // Nenhuma classe especÃ­fica
    String id = null; // Nenhum id especÃ­fico
    List<String[]> atributos = Arrays.asList(
            new String[]{"formcontrolname", "ag", "igual"},
            new String[]{"mask", "0000", "igual"}
    );
    String texto = null; // Nenhum texto especÃ­fico

    // Obter o primeiro elemento que atenda aos critÃ©rios
    WebElement resultado = obterPrimeiroElemento(tag, classe, id, atributos, texto, driver, null);

    if (resultado != null) {
        System.out.println("Elemento encontrado: " + resultado);
    } else {
        System.out.println("Nenhum elemento encontrado.");
    }

    return resultado;
}

public boolean query(WebElement elemento, String tag, String containsClasse, String containsId, String containsTexto, List<String[]> atributos) {
    if (elemento == null) {
        return false;
    }

    List<Boolean> resultado = new ArrayList<>();

    // Verifica a tag do elemento
    if (tag != null) {
        boolean valTag = elemento.getTagName().equalsIgnoreCase(tag);
        resultado.add(valTag);
    }

    // Verifica atributos do elemento
    if (atributos != null && !atributos.isEmpty()) {
        for (String[] atributoArray : atributos) {
            String atributo = atributoArray[0];
            String valorAtributo = atributoArray[1];
            String tipo = atributoArray[2];

            boolean resultAtributo = false;
            String attrValue = elemento.getAttribute(atributo);

            if (attrValue != null) {
                switch (tipo) {
                    case "igual":
                        resultAtributo = attrValue.trim().equals(valorAtributo.trim());
                        break;
                    case "includes":
                        resultAtributo = attrValue.contains(valorAtributo);
                        break;
                    default:
                        resultAtributo = false;
                        break;
                }
            }
            resultado.add(resultAtributo);
        }
    }

    // Verifica classe do elemento
    if (containsClasse != null) {
        boolean valClass = elemento.getAttribute("class").contains(containsClasse);
        resultado.add(valClass);
    }

    // Verifica id do elemento
    if (containsId != null) {
        boolean valId = elemento.getAttribute("id").contains(containsId);
        resultado.add(valId);
    }

    // Verifica texto do elemento
    if (containsTexto != null) {
        boolean valTexto = false;
        if (elemento.getTagName().equalsIgnoreCase("input")) {
            valTexto = elemento.getAttribute("value").contains(containsTexto);
        } else {
            valTexto = elemento.getText().contains(containsTexto);
        }
        resultado.add(valTexto);
    }

    // Se algum resultado for falso, retorna false, senÃ£o retorna true
	logBasic(""+!resultado.contains(false));
    return !resultado.contains(false);
}

// FunÃ§Ã£o que faz a busca de todos os elementos no DOM, incluindo shadow roots, retornando o primeiro encontrado
public WebElement obterPrimeiroElemento(String tag, String classe, String id, List<String[]> atributos, String texto, WebDriver driver, WebElement root) {
    // Executa JS para obter todos os elementos do shadow DOM
    JavascriptExecutor js = (JavascriptExecutor) driver;
    String script = "return arguments[0].shadowRoot.querySelectorAll('*');";
    List<WebElement> elementos;

    if (root == null) {
        elementos = driver.findElements(By.cssSelector("*"));
    } else {
        elementos = (List<WebElement>) js.executeScript(script, root);
    }

    for (WebElement elemento : elementos) {
        // Verifica se o elemento tem um Shadow DOM e faz a busca recursiva
        Object shadowRoot = js.executeScript("return arguments[0].shadowRoot", elemento);

        if (shadowRoot != null) {
            WebElement encontrado = obterPrimeiroElemento(tag, classe, id, atributos, texto, driver, elemento);
            if (encontrado != null) {
                return encontrado; // Retorna o primeiro elemento encontrado no Shadow DOM
            }
        }

        // Executa a query para verificar se o elemento atende aos critÃ©rios
        if (query(elemento, tag, classe, id, texto, atributos)) {
            return elemento; // Retorna o primeiro elemento encontrado
        }
    }
    return null; // Retorna null se nenhum elemento for encontrado
}