import pyautogui, os,autoit,ocrspace,re,pytesseract,cv2
import time
import os
import time
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.edge.service import Service as EdgeService
from webdriver_manager.microsoft import EdgeChromiumDriverManager
import pyautogui
import cv2
import argparse
from pywinauto.application import Application

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
                    pyautogui.click(direcao)
                elif direcao == 'rigth':
                    direcao = pyautogui.rightClick(localizacao)
                elif direcao == 'left':
                    direcao = pyautogui.leftClick(localizacao)
                else:
                    pyautogui.click(direcao)
                print("Clique efetuado!")
                return True
        except pyautogui.ImageNotFoundException:
            print(f"Tentativa {tentativa + 1}: Imagem não encontrada. Aguardando {tempo_entre_tentativas} segundos para tentar novamente...")
        
        time.sleep(tempo_entre_tentativas)
        tentativa += 1
    
    print("Imagem não encontrada após várias tentativas.")
    return False


def clicar_na_imagem_focus_janela(caminho_img, janela_titulo_regex, direcao='center', confidence=0.9, tentativas=5, tempo_entre_tentativas=2, intervalo_maximo=30):
    """
    Tenta localizar e clicar em uma imagem na tela, focando na janela correta se necessário.
    
    :param caminho_img: Caminho para a imagem a ser localizada.
    :param direcao: Direção do clique ('center', 'right', 'left'). Padrão é 'center'.
    :param confidence: Nível de confiança para localizar a imagem. Valor entre 0 e 1.
    :param tentativas: Número de tentativas para localizar a imagem.
    :param tempo_entre_tentativas: Tempo de espera (em segundos) entre cada tentativa.
    :param intervalo_maximo: Tempo máximo (em segundos) para tentar localizar a imagem antes de desistir.
    :param janela_titulo_regex: Regex para o título da janela a ser focada antes de clicar. Padrão é None.
    :return: True se a imagem foi encontrada e clicada, False caso contrário.
    """
    inicio = time.time()
    tentativa = 0

    # Focar na janela específica se o regex do título for fornecido
    if janela_titulo_regex:
        app = Application().connect(title_re=janela_titulo_regex)
        janela = app.window(title_re=janela_titulo_regex)
        janela.set_focus()

    while tentativa < tentativas and (time.time() - inicio) < intervalo_maximo:
        try:
            localizacao = pyautogui.locateOnScreen(caminho_img, confidence=confidence)
            if localizacao:
                print(f"Imagem encontrada na tentativa {tentativa + 1} na localização:", localizacao)
                
                if direcao == 'center':
                    direcao = pyautogui.center(localizacao)
                    pyautogui.click(direcao)
                elif direcao == 'right':
                    pyautogui.rightClick(localizacao)
                elif direcao == 'left':
                    pyautogui.leftClick(localizacao)
                else:
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


def main():
    parser = argparse.ArgumentParser(description="Automação com pyautogui e autoit")
    subparsers = parser.add_subparsers(dest='command')

    clicar_nas_imagens_parser = subparsers.add_parser('clicar_nas_imagens')
    clicar_nas_imagens_parser.add_argument('imagem_path')
    clicar_nas_imagens_parser.add_argument('--titulo_janela', type=str, default=None)
    clicar_nas_imagens_parser.add_argument('--tempo_espera', type=int, required=True)
    clicar_nas_imagens_parser.add_argument('--direcao', default='center')
    clicar_nas_imagens_parser.add_argument('--confidence', type=float, default=0.9)
    clicar_nas_imagens_parser.add_argument('--tentativas', type=int, default=5)
    clicar_nas_imagens_parser.add_argument('--tempo_entre_tentativas', type=int, default=2)
    clicar_nas_imagens_parser.add_argument('--intervalo_maximo', type=int, default=30)

    clicar_na_imagem_parser = subparsers.add_parser('clicar_na_imagem')
    clicar_na_imagem_parser.add_argument('caminho_img')
    clicar_na_imagem_parser.add_argument('--direcao', default='center')
    clicar_na_imagem_parser.add_argument('--confidence', type=float, default=0.9)
    clicar_na_imagem_parser.add_argument('--tentativas', type=int, default=5)
    clicar_na_imagem_parser.add_argument('--tempo_entre_tentativas', type=int, default=2)
    clicar_na_imagem_parser.add_argument('--intervalo_maximo', type=int, default=30)

    clicar_na_imagem_focus_janela_parse = subparsers.add_parser('clicar_na_imagem_focus_janela')
    clicar_na_imagem_focus_janela_parse.add_argument('caminho_img')
    clicar_na_imagem_focus_janela_parse.add_argument('--titulo_janela')
    clicar_na_imagem_focus_janela_parse.add_argument('--direcao', default='center')
    clicar_na_imagem_focus_janela_parse.add_argument('--confidence', type=float, default=0.9)
    clicar_na_imagem_focus_janela_parse.add_argument('--tentativas', type=int, default=5)
    clicar_na_imagem_focus_janela_parse.add_argument('--tempo_entre_tentativas', type=int, default=2)
    clicar_na_imagem_focus_janela_parse.add_argument('--intervalo_maximo', type=int, default=30)

    escrever_parser = subparsers.add_parser('escrever')
    escrever_parser.add_argument('value')
    escrever_parser.add_argument('--tipo', default='a')
    escrever_parser.add_argument('--intervalo', type=float, default=0)
    escrever_parser.add_argument('--enter', action='store_true')

    esperar_parser = subparsers.add_parser('esperar_imagem_aparecer')
    esperar_parser.add_argument('caminho_img')
    esperar_parser.add_argument('--confidence', type=float, default=0.9)
    esperar_parser.add_argument('--tentativas', type=int, default=5)
    esperar_parser.add_argument('--tempo_entre_tentativas', type=int, default=2)
    esperar_parser.add_argument('--intervalo_maximo', type=int, default=30)

    imagem_presente_parser = subparsers.add_parser('imagem_presente')
    imagem_presente_parser.add_argument('image_path')
    imagem_presente_parser.add_argument('--confidence', type=float, default=0.9)

    obter_posicoes_parser = subparsers.add_parser('obter_posicoes')
    obter_posicoes_parser.add_argument('imagem_path')
    obter_posicoes_parser.add_argument('--titulo_janela', type=str, default=None)
    obter_posicoes_parser.add_argument('--confidence', type=float, default=0.9)
    obter_posicoes_parser.add_argument('--tentativas', type=int, default=5)
    obter_posicoes_parser.add_argument('--tempo_entre_tentativas', type=int, default=2)
    obter_posicoes_parser.add_argument('--intervalo_maximo', type=int, default=30)

    args = parser.parse_args()

    if args.command == 'clicar_na_imagem':
        clicar_na_imagem(args.caminho_img, args.direcao, args.confidence, args.tentativas, args.tempo_entre_tentativas, args.intervalo_maximo)
    elif args.command == 'clicar_nas_imagens':
        clicar_nas_imagens(args.imagem_path, args.titulo_janela, args.tempo_espera, args.direcao, args.confidence, args.tentativas, args.tempo_entre_tentativas, args.intervalo_maximo)
    elif args.command == 'escrever':
        escrever(args.value, args.tipo, args.intervalo, args.enter)
    elif args.command == 'esperar_imagem_aparecer':
        esperar_imagem_aparecer(args.caminho_img, args.confidence, args.tentativas, args.tempo_entre_tentativas, args.intervalo_maximo)
    elif args.command == 'imagem_presente':
        imagem_presente(args.image_path, args.confidence)
    elif args.command == 'obter_posicoes':
        coordenadas = obter_posicoes(args.imagem_path, args.titulo_janela, args.confidence, args.tentativas, args.tempo_entre_tentativas, args.intervalo_maximo)
        print(coordenadas)
    elif args.command == 'clicar_na_imagem_focus_janela':
        coordenadas = clicar_na_imagem_focus_janela(args.caminho_img, args.titulo_janela, args.direcao, args.confidence, args.tentativas, args.tempo_entre_tentativas, args.intervalo_maximo)

if __name__ == "__main__":
    main()