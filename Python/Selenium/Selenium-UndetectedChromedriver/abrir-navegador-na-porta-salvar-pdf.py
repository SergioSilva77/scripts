import threading
import undetected_chromedriver as uc
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
import json

def selenium_script():
    # Assuming these variables are defined earlier in your script
    path_Loc = df['diretorioDownload'][0]
    
    driver_version = '126'
    driver_path = r'${caminhoDriver}'
    url = '${url}'

    caps = DesiredCapabilities.CHROME.copy()
    caps['acceptInsecureCerts'] = True

    my_user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36"

    options = uc.ChromeOptions()
    options.add_argument(f"user-agent={my_user_agent}")
    options.debugger_address = f"127.0.0.1:9222"
    
    # Print settings configuration
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

    prefs = {
        "printing.print_preview_sticky_settings.appState": json.dumps(settings),
        "savefile.default_directory": path_Loc,
        "download.default_directory": path_Loc,
        "download.prompt_for_download": False,
        "download.directory_upgrade": True,
        "safebrowsing.enabled": True
    }
    
    options.add_experimental_option("prefs", prefs)
    options.add_argument("--kiosk-printing")

    driver = uc.Chrome(driver_path=driver_path, options=options, desired_capabilities=caps)

    driver.get(url)
    driver.execute_script("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})")

    

selenium_script()