

# coding: utf-8
# # Instalar Bibliotecas
# !pip install pywin32
# !pip install python-dotenv
# # Importar Bibliotecas
import win32com.client
import sys
import subprocess
import requests
import os
import json
from base64 import b64encode
import pandas as pd
import datetime
import psutil
import openpyxl
import time
import warnings
from datetime import datetime, timedelta
import autoit

warnings.filterwarnings("ignore")
# autoit = win32com.client.Dispatch("AutoItX3.Control")
wShell = win32com.client.Dispatch("WScript.Shell")
# Lista de processos em execução
process_name = "saplogon.exe"
user_id = os.getlogin()
# # Criar objeto SAP
class SapGui():
    def __init__(self):
        self.SapGuiAuto = win32com.client.GetObject("SAPGUI")
        if not type(self.SapGuiAuto) == win32com.client.CDispatch:
            raise Exception('Erro ao criar objeto!')
        application = self.SapGuiAuto.GetScriptingEngine
        # Verifique se há alguma conexão aberta
        if application.Connections.Count > 0:
            self.connection = application.Connections.Item(0)  # obtenha a primeira conexão
        else:
            raise Exception('Nenhuma conexão SAP aberta encontrada!')
        # Agora obtenha a sessão
        if self.connection.Sessions.Count > 0:
            self.session = self.connection.Sessions.Item(0)  # obtenha a primeira sessão
        else:
            raise Exception('Nenhuma sessão SAP aberta encontrada!')
    def Login(self):
        try:
            self.session.findById("wnd[0]/usr/txtRSYST-MANDT").Text = MANDT
            self.session.findById("wnd[0]/usr/txtRSYST-BNAME").Text = BNAME
            self.session.findById("wnd[0]/usr/pwdRSYST-BCODE").Text = BCODE
            self.session.findById("wnd[0]/usr/txtRSYST-LANGU").Text = LANGU
            self.session.findById("wnd[0]").sendVKey(0)
            try:
                self.session.findById("wnd[1]/usr/radMULTI_LOGON_OPT2").select()
                self.session.findById("wnd[1]/usr/radMULTI_LOGON_OPT2").setFocus()
                self.session.findById("wnd[1]/tbar[0]/btn[0]").press()
            except:
                pass
        except:
            raise Exception('Erro login SAP: ' + str(sys.exc_info()[0]))
# # Iniciar
sap = SapGui()


resultado = 'Falha'
def select_node_by_text(tree):
    # Obtém todas as chaves de nós na árvore
    all_node_keys = tree.GetAllNodeKeys()
    
    # Itera sobre cada nó na árvore
    for i in range(len(all_node_keys)):
        node_key = all_node_keys[i]
        print(f"Nó chave: {node_key}")
        
        # Obtem o texto do nó da primeira coluna
        node_text = tree.GetNodeTextByKey(node_key)
        print(f"Primeira coluna (texto do nó): {node_text}")
        
        # Obtém o número de colunas (geralmente indexadas a partir de 0)
        # É uma suposição, pois SAP pode ter colunas diferentes por tabela
        try:
            # Tentativa de capturar e imprimir todas as colunas
            column_count = tree.GetColumnNames().Length
            print(f"Número de colunas: {column_count}")
            
            # Percorre todas as colunas e imprime os valores
            for col_index in range(1, column_count):  # Começa em 1 para pular a primeira coluna
                try:
                    column_text = tree.GetItemText(node_key, f"C          {col_index}")
                    print(f"Coluna {col_index}: {column_text}")
                except Exception as e:
                    print(f"Erro ao tentar capturar a coluna {col_index}: {e}")
        
        except Exception as e:
            print(f"Erro ao obter o número de colunas: {e}")
        print('-' * 50)  # Separador para melhor visualização

# Exemplo de uso
tree = sap.session.findById("wnd[0]/usr/cntlCC_CONTAINER/shellcont/shell/shellcont[1]/shell[1]")
select_node_by_text(tree)
