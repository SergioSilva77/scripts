from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# Configuração do caminho fixo do ChromeDriver
caminho_chromedriver = r"C:\caminho\para\chromedriver.exe"  # Atualize para o caminho do ChromeDriver no seu sistema
service = ChromeService(executable_path=caminho_chromedriver)

# Inicialização do navegador
driver = webdriver.Chrome(service=service)

# URL da página que contém o botão
url = "https://sua-url.com"  # Substitua com a URL real
driver.get(url)

# Configuração da espera explícita
wait = WebDriverWait(driver, 10)  # Espera até 10 segundos para que a condição seja satisfeita

# Esperar até que o botão "Salvar relatório" esteja clicável e então clicar
botao_salvar = wait.until(EC.element_to_be_clickable((By.XPATH, "//button[contains(.,'Salvar relatório')]")))
botao_salvar.click()

# Continue com outras ações...

# Fechar o navegador
driver.quit()
