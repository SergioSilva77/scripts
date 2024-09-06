from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.edge.service import Service as EdgeService
from webdriver_manager.microsoft import EdgeChromiumDriverManager

def connect_to_existing_browser_path_edgedriver(edge_driver_path, porta):
    """
    Conecta-se a uma sessão existente do navegador Microsoft Edge usando o WebDriver na porta especificada.
    
    :param porta: A porta na qual o navegador Edge está rodando com a depuração ativada.
    :return: Instância WebDriver conectada ao navegador existente.
    """
    
    service = EdgeService(executable_path=edge_driver_path)
    options = webdriver.EdgeOptions()
    options.add_experimental_option("debuggerAddress", f"localhost:{porta}")
    driver = webdriver.Edge(service=service, options=options)
    return driver