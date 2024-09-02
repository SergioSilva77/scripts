import subprocess
import sys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from webdriver_manager.chrome import ChromeDriverManager
import json
from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
import os

def navegar(url, caminhoDownloads):
    # Configura o serviço do Chrome WebDriver com o caminho fixo
    install_chromedriver = ChromeDriverManager().install()
    folder_chromedriver = os.path.dirname(install_chromedriver)
    path_chromedriver = os.path.join(folder_chromedriver, 'chromedriver.exe')
    service = ChromeService(executable_path=path_chromedriver)


    # Definir options
    my_user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36"
    options = webdriver.ChromeOptions()
    options.add_argument(f"user-agent={my_user_agent}")
    options.add_argument("--kiosk-printing")
    options.add_argument("--kiosk-pdf-printing")
    options.add_experimental_option("detach", True)

    # Configurações de impressão
    settings = {
        "recentDestinations": [
            {
                "id": "Save as PDF",
                "origin": "local",
                "account": ""
            }
        ],
        "selectedDestinationId": "Save as PDF",
        "version": 2
    }

    # Configurações do Chrome
    chrome_prefs = {
        "printing.print_preview_sticky_settings.appState": json.dumps(settings),
        "download.prompt_for_download": False,
        "plugins.always_open_pdf_externally": False,
        "download.open_pdf_in_system_reader": False,
        "profile.default_content_settings.popups": 0,
        "printing.print_to_pdf": True,
        "download.default_directory": caminhoDownloads,
        "savefile.default_directory": caminhoDownloads
    }
    options.add_experimental_option("prefs", chrome_prefs)

    # Setar capabilities
    caps = DesiredCapabilities.CHROME.copy()
    caps['acceptInsecureCerts'] = True
    for key, value in caps.items():
        options.set_capability(key, value)

    driver = webdriver.Chrome(service=service, options=options)
    driver.get(url)
    driver.execute_script("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})")
    return driver


navegar('https://cav.receita.fazenda.gov.br/autenticacao/login', r'C:\TEMP')
input('aaaaa')