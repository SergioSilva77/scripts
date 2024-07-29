import pyautogui, os,autoit,ocrspace,re,pytesseract,cv2
from datetime import datetime
import time
import pandas as pd
import psutil
from pywinauto.application import Application
import os
import time
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.edge.service import Service as EdgeService
from webdriver_manager.microsoft import EdgeChromiumDriverManager
import pyautogui
import cv2
import numpy as np

def iniciar_edge():
    # Configura o serviço do Edge WebDriver
    service = EdgeService(EdgeChromiumDriverManager().install())
    # service = EdgeService(executable_path=webdriver_path)

    # Opções para o Edge
    options = webdriver.EdgeOptions()
    options.use_chromium = True
    options.add_experimental_option("detach", True)
    
    # Inicia o driver do Edge
    driver = webdriver.Edge(service=service, options=options)

    # Abre a URL desejada
    driver.get('https://edi.sawluznet.com/RDWeb/Pages/en-US/login.aspx?ReturnUrl=/RDWeb/Pages/en-US/Default.aspx')

    driver.find_element(By.CSS_SELECTOR, 'input#DomainUserName').send_keys('darlei.damaceno@sawluznet.com')
    driver.find_element(By.CSS_SELECTOR, 'input#UserPass').send_keys('Pirelli@123!')
    driver.find_element(By.CSS_SELECTOR, 'input#UserPass').send_keys(Keys.ENTER)
    driver.find_element(By.XPATH, "//div[@class='tswa_ttext' and contains(text(),'Automotivo')]").click()
    return driver


def clicar_na_imagem(caminho_img, direcao='center', confidence=0.9, tentativas=5, tempo_entre_tentativas=2, intervalo_maximo=30):
    """
    Tenta localizar e clicar em uma imagem na tela.
    
    :param caminho_img: Caminho para a imagem a ser localizada.
    :param confidence: Nível de confiança para localizar a imagem. Valor entre 0 e 1.
    :param tentativas: Número de tentativas para localizar a imagem.
    :param tempo_entre_tentativas: Tempo de espera (em segundos) entre cada tentativa.
    :param intervalo_maximo: Tempo máximo (em segundos) para tentar localizar a imagem antes de desistir.
    """
    inicio = time.time()
    tentativa = 0

    while tentativa < tentativas and (time.time() - inicio) < intervalo_maximo:
        try:
            localizacao = pyautogui.locateOnScreen(caminho_img, confidence=confidence)
            if localizacao:
                print(f"Imagem encontrada na tentativa {tentativa + 1} na localização:", localizacao)
                
                if direcao == 'center':
                    direcao = pyautogui.center(localizacao)
                elif direcao == 'rigth':
                    direcao = pyautogui.rightClick(localizacao)
                elif direcao == 'left':
                    direcao = pyautogui.leftClick(localizacao)
                pyautogui.click(direcao)
                print("Clique efetuado!")
                return True
        except pyautogui.ImageNotFoundException:
            print(f"Tentativa {tentativa + 1}: Imagem não encontrada. Aguardando {tempo_entre_tentativas} segundos para tentar novamente...")
        
        time.sleep(tempo_entre_tentativas)
        tentativa += 1
    
    print("Imagem não encontrada após várias tentativas.")
    return False


def escrever(value='',tipo="a",intervalo=0,enter=False):
	"""
	Segue a explicação de como usar a Função (escrever) usaremos a letra "P" substituindo a expressão Parametro.\n
	P1) Value:		Refere-se ao texto ou qualquer valor desejado | sera convertido para string.\n
	P2) Tipo:		Refere-se ao metodo de entrada que são:\n
				 		>> a  = metodo send (função Autoit de digitação).\n
				 		>> p  = metodo typewrite (função pyautogui de digitação).\n
				 		>> ph = metodo typewrite (função pyautogui de digitação com velocidade gerenciavel).\n
	P3) Intervalo:	Refere-se ao velociade da inserção dos dados metodo ph + intervalo
	P4) Enter: 		Refere-se a opção de usar o precionar o "entrer" quando 
	"""
	text_value = str(value)
	if(tipo=='a'):autoit.send(text_value)
	if(tipo=='p'):pyautogui.typewrite(text_value)
	if(tipo=='ph'):pyautogui.typewrite(text_value,intervalo)
	pyautogui.sleep(0.2)
	if(enter==True):pyautogui.press('enter')
     

def esperar_imagem_aparecer(caminho_img, confidence=0.9, tentativas=5, tempo_entre_tentativas=2, intervalo_maximo=30):
    """
    Tenta localizar e clicar em uma imagem na tela.
    
    :param caminho_img: Caminho para a imagem a ser localizada.
    :param confidence: Nível de confiança para localizar a imagem. Valor entre 0 e 1.
    :param tentativas: Número de tentativas para localizar a imagem.
    :param tempo_entre_tentativas: Tempo de espera (em segundos) entre cada tentativa.
    :param intervalo_maximo: Tempo máximo (em segundos) para tentar localizar a imagem antes de desistir.
    """
    inicio = time.time()
    tentativa = 0

    while tentativa < tentativas and (time.time() - inicio) < intervalo_maximo:
        try:
            localizacao = pyautogui.locateOnScreen(caminho_img, confidence=confidence)
            if localizacao:
                print(f"Imagem encontrada na tentativa {tentativa + 1} na localização:", localizacao)
                return True
        except pyautogui.ImageNotFoundException:
            print(f"Tentativa {tentativa + 1}: Imagem não encontrada. Aguardando {tempo_entre_tentativas} segundos para tentar novamente...")
        
        time.sleep(tempo_entre_tentativas)
        tentativa += 1
    
    print("Imagem não encontrada após várias tentativas.")
    return False


def ler_coluna_excel(caminho_arquivo):
    """
    Lê os dados da coluna D a partir da linha 2 de uma planilha.
    
    :param caminho_arquivo: Caminho para o arquivo da planilha (ex: 'dados.xlsx').
    :return: Lista de valores da coluna D.
    """
    # Lê a planilha a partir da linha 2 (índice 1)
    df = pd.read_excel(caminho_arquivo, skiprows=1)
    
    # Obtém a coluna D (índice 3, já que o índice começa em 0)
    coluna_d = df.iloc[:, 3]
    
    # Remove valores NaN (em branco)
    coluna_d_limpa = coluna_d.dropna().tolist()
    
    return coluna_d_limpa


def reiniciar_chrome(caminho_chrome, url):
    # Fechar todas as instâncias do Chrome
    for process in psutil.process_iter():
        if process.name() == "chrome.exe":
            try:
                process.terminate()
            except:
                pass
    
    # Esperar um pouco para garantir que todos os processos sejam terminados
    time.sleep(3)
    
    # Abrir o Chrome no link específico
    autoit.run(caminho_chrome)

    try:
        clicar_na_imagem('inputUrlChrome.PNG', 0.8, 5, 2, 30)
    except:
        clicar_na_imagem('inputUrlChrome1.png', 0.8, 5, 2, 30)

    escrever(value='https://consultadanfe.com/', tipo='a', intervalo=1000, enter=True)

    
def imagem_presente(image_path, confidence=0.9):
    """
    Verifica se uma imagem está presente na tela.

    :param image_path: Caminho para a imagem a ser verificada.
    :param confidence: Nível de confiança (entre 0 e 1) para a correspondência da imagem.
    :return: True se a imagem estiver presente, False caso contrário.
    """
    try:
        location = pyautogui.locateOnScreen(image_path, confidence=confidence)
        if location:
            return True
        else:
            return False
    except Exception as e:
        print(f"Erro ao tentar localizar a imagem: {e}")
        return False


def clicar_nas_imagens(imagem_path, janela_titulo_regex, tempo_espera, direcao='center', confidence=0.9, tentativas=5, tempo_entre_tentativas=2, intervalo_maximo=30):
    """
    Clica nas imagens encontradas na tela, focando na janela correta se necessário.

    Args:
    imagem_path (str): Caminho para a imagem a ser localizada.
    tempo_espera (float): Tempo de espera entre cliques.
    direcao (str): Direção do clique ('center', 'right', 'left'). Padrão é 'center'.
    confidence (float): Nível de confiança para a localização da imagem. Padrão é 0.9.
    tentativas (int): Número de tentativas para encontrar e clicar na imagem. Padrão é 5.
    tempo_entre_tentativas (float): Tempo entre as tentativas de encontrar a imagem. Padrão é 2 segundos.
    intervalo_maximo (float): Tempo máximo para todas as tentativas. Padrão é 30 segundos.
    janela_titulo_regex (str): Regex para o título da janela a ser focada antes de clicar. Padrão é None.
    
    Returns:
    bool: True se um clique foi efetuado, False caso contrário.
    """
    inicio = time.time()
    tentativa = 0

    # Focar na janela específica se o regex do título for fornecido
    if janela_titulo_regex:
        app = Application().connect(title_re=janela_titulo_regex)
        janela = app.window(title_re=janela_titulo_regex)
        janela.set_focus()

    while tentativa < tentativas and (time.time() - inicio) < intervalo_maximo:
        # Encontra todas as posições da imagem na tela
        posicoes = list(pyautogui.locateAllOnScreen(imagem_path, confidence=confidence))

        if not posicoes:
            tentativa += 1
            time.sleep(tempo_entre_tentativas)
            continue

        clique_efetuado = False
        for posicao in posicoes:
            if direcao == 'center':
                posicao_central = pyautogui.center(posicao)
                pyautogui.click(posicao_central)
            elif direcao == 'right':
                posicao_direita = (posicao.left + posicao.width, posicao.top + posicao.height // 2)
                pyautogui.click(posicao_direita)
            elif direcao == 'left':
                posicao_esquerda = (posicao.left, posicao.top + posicao.height // 2)
                pyautogui.click(posicao_esquerda)
            else:
                raise ValueError("Direção inválida. Use 'center', 'right' ou 'left'.")
            clique_efetuado = True
            time.sleep(tempo_espera)

        if clique_efetuado:
            return True

        tentativa += 1

    return False


def obter_posicoes(imagem_path, janela_titulo_regex=None, confidence=0.9, tentativas=5, tempo_entre_tentativas=2, intervalo_maximo=30):
    """
    Tenta localizar todas as posições de uma imagem na tela e retorna suas coordenadas em formato JSON.

    :param imagem_path: Caminho para a imagem a ser localizada.
    :param janela_titulo_regex: Regex para o título da janela a ser focada antes de tentar localizar a imagem. Padrão é None.
    :param confidence: Nível de confiança para localizar a imagem. Valor entre 0 e 1.
    :param tentativas: Número de tentativas para localizar a imagem.
    :param tempo_entre_tentativas: Tempo de espera (em segundos) entre cada tentativa.
    :param intervalo_maximo: Tempo máximo (em segundos) para tentar localizar a imagem antes de desistir.
    :return: JSON string com as coordenadas das posições encontradas.
    """
    inicio = time.time()
    tentativa = 0
    coordenadas = []

    if janela_titulo_regex:
        app = Application().connect(title_re=janela_titulo_regex)
        janela = app.window(title_re=janela_titulo_regex)
        janela.set_focus()

    while tentativa < tentativas and (time.time() - inicio) < intervalo_maximo:
        posicoes = list(pyautogui.locateAllOnScreen(imagem_path, confidence=confidence))
        if not posicoes:
            tentativa += 1
            time.sleep(tempo_entre_tentativas)
            continue

        for posicao in posicoes:
            posicao_central = pyautogui.center(posicao)
            coordenadas.append({"x": posicao_central.x, "y": posicao_central.y})

        if coordenadas:
            return json.dumps(coordenadas)

        tentativa += 1

    return json.dumps(coordenadas)

        


posicoes = obter_posicoes(r"C:\TEMP\img1.png", r'Pasta1')
print(posicoes)