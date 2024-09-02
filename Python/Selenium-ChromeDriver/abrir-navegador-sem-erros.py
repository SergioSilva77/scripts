from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.service import Service as ChromeService
from webdriver_manager.chrome import ChromeDriverManager
import os

def iniciar_chrome(url):
    options = webdriver.ChromeOptions()

    # Caminho fixo para o ChromeDriver
    caminho_chromedriver = r"C:\Users\sserg\.wdm\drivers\chromedriver\win64\128.0.6613.119\chromedriver-win32\chromedriver.exe"  # Atualize com o caminho correto no seu sistema
    
    # Configura o serviço do Chrome WebDriver com o caminho fixo
    install_chromedriver = ChromeDriverManager().install()
    folder_chromedriver = os.path.dirname(install_chromedriver)
    path_chromedriver = os.path.join(folder_chromedriver, 'chromedriver.exe')
    service = ChromeService(executable_path=path_chromedriver)
    
    # Opções para o Chrome
    options.add_experimental_option("detach", True)
    
    # Inicia o driver do Chrome com o serviço e as opções configuradas
    driver = webdriver.Chrome(service=service, options=options)
    
    # Abre a URL desejada
    driver.get(url)
    
    return driver