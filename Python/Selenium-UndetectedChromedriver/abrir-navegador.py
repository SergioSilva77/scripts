import subprocess
import sys
import undetected_chromedriver as uc
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from webdriver_manager.chrome import ChromeDriverManager

subprocess.run(["pip", "install", "--upgrade", "undetected-chromedriver"])


driver_path = ChromeDriverManager(driver_version='124').install()

caps = DesiredCapabilities.CHROME.copy()
caps['acceptInsecureCerts'] = True

my_user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36"

options = uc.ChromeOptions()
options.add_argument(f"user-agent={my_user_agent}")
options.debugger_address = f"127.0.0.1:9222"

driver = uc.Chrome(driver_path=driver_path, options=options, desired_capabilities=caps)

driver.get('https://cav.receita.fazenda.gov.br/autenticacao/login')
driver.execute_script("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})")