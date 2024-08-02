from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
import sys

diretorio_pdf = sys.argv[1]
diretorio_chrome_driver = sys.argv[2]
link_url = sys.argv[3]

print(diretorio_pdf)
print(diretorio_chrome_driver)
print(link_url)


def abrir_navegador():

    chrome_options = Options()
    chrome_options.add_experimental_option('prefs',  {
        "download.default_directory": diretorio_pdf,
        "download.prompt_for_download": False,
        "download.directory_upgrade": True,
        "plugins.always_open_pdf_externally": True
        }
    )

    driver = webdriver.Chrome(options=chrome_options, executable_path=diretorio_chrome_driver)
    driver.maximize_window()
    driver.get(link_url)
    return driver


driver = abrir_navegador()