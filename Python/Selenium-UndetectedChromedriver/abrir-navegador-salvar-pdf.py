import subprocess
import sys
import undetected_chromedriver as uc
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from webdriver_manager.chrome import ChromeDriverManager
import json

def navegar(url, caminhoDownloads):
    # subprocess.run(["pip", "install", "--upgrade", "undetected-chromedriver"])
    driver_path = ChromeDriverManager(driver_version='124').install()

    caps = DesiredCapabilities.CHROME.copy()
    caps['acceptInsecureCerts'] = True
    my_user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36"
    options = uc.ChromeOptions()
    options.add_extension(r'C:\Users\sserg\OneDrive\Documentos\Dev\Nova pasta\buster_captcha.crx')
    options.add_argument(f"user-agent={my_user_agent}")
    options.add_argument("--incognito")  # Modo anônimo
    options.debugger_address = f"127.0.0.1:9222"

    pathLoc = '/caminho/para/o/diretorio'

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

    # Opções do Chrome
    options.add_experimental_option("prefs", chrome_prefs)
    options.add_argument("--kiosk-printing")
    options.add_argument("--kiosk-pdf-printing")
    options.add_argument("--remote-debugging-port=9222") # porta para debugging

    driver = uc.Chrome(driver_path=driver_path, options=options, desired_capabilities=caps)
    driver.get(url)
    driver.execute_script("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})")
    return driver


navegar('https://cav.receita.fazenda.gov.br/autenticacao/login', r'C:\TEMP')
input('')