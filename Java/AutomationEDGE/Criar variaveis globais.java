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

  //################### CRIAR VARIAVEL ###########################
  logBasic("Criando variáveis...");
  GlobalStorage gs = GlobalStorage.getInstance();
  gs.add("variavel1",1);
  gs.add("variavel2",2);
  gs.add("variavel3",3);
  gs.add("variavel4",4);
  gs.add("variavel5",5);
  gs.add("variavel6",6);
  gs.add("variavel7",7);
  gs.add("variavel8",8);
  gs.add("variavel9",9);
  gs.add("variavel10",10);
  //################################################################


  //################### LISTAR VARIAVEIS ###########################
  logBasic("Listando variáveis...");
  for (Object key: gs.keySet()) {
  	logBasic(key.toString());
  }
  //################################################################


  //################### RECUPERAR VALOR DA VARIAVEL ################
  logBasic("Recuperando valor das variáveis...");
  int v1 = (Integer)gs.get("variavel1");
  int v2 = (Integer)gs.get("variavel2");
  int v3 = (Integer)gs.get("variavel3");
  int v4 = (Integer)gs.get("variavel4");
  int v5 = (Integer)gs.get("variavel5");
  int v6 = (Integer)gs.get("variavel6");
  int v7 = (Integer)gs.get("variavel7");
  int v8 = (Integer)gs.get("variavel8");
  int v9 = (Integer)gs.get("variavel9");
  int v10 = (Integer)gs.get("variavel10");
  logBasic("valores recuperados...");
  //################################################################


  //################### EXCLUIR VARIAVEL ###########################
  logBasic("Removendo variáveis...");
  gs.remove("variavel1");
  gs.remove("variavel2");
  gs.remove("variavel3");
  gs.remove("variavel4");
  gs.remove("variavel5");
  gs.remove("variavel6");
  gs.remove("variavel7");
  gs.remove("variavel8");
  gs.remove("variavel9");
  gs.remove("variavel10");
  //################################################################

  putRow(data.outputRowMeta, r);
  return true;
}