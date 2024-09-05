

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
    node_text_g = ''
    # Obtém todas as chaves de nós na árvore
    all_node_keys = tree.GetAllNodeKeys()
    
    # Itera sobre cada nó na árvore
    for i in range(len(all_node_keys)):
        node_key = all_node_keys[i]
        
        # Obtem o texto do nó da primeira coluna

        node_text = tree.GetNodeTextByKey(node_key)
        print(node_text)
        if (node_text != node_text_g):
            node_text_g = node_text
            
        if (node_text_g.startswith('100 Produto acabado')):
            print(node_text)
            
            column_text = tree.GetItemText(node_key, f"C          1")
            print(node_text,column_text)

# Exemplo de uso
tree = sap.session.findById("wnd[0]/usr/cntlCC_CONTAINER/shellcont/shell/shellcont[1]/shell[1]")
select_node_by_text(tree)




def find_and_traverse_node(tree, target_name):
    # Obtém todas as chaves de nós na árvore
    all_node_keys = tree.GetAllNodeKeys()
    
    # Itera sobre cada nó para encontrar o nó pelo nome
    for node_key in all_node_keys:
        node_text = tree.GetNodeTextByKey(node_key)
        
        # Se o nome do nó corresponder ao nome alvo, percorre esse nó
        if node_text == target_name:
            print(f"Nó encontrado: {node_text}")
            
            # Agora percorre todos os filhos desse nó
            child_keys = tree.GetNodeChildrenCountByPath(node_key)
            for i in range(child_keys):
                child_node_key = tree.GetNextNodeKey(node_key, i)
                child_node_text = tree.GetNodeTextByKey(child_node_key)
                print(f"Filho do nó {node_text}: {child_node_text}")
            break  # Encerra após encontrar e percorrer o nó alvo

# Exemplo de uso
tree = sap.session.findById("wnd[0]/usr/cntlCC_CONTAINER/shellcont/shell/shellcont[1]/shell[1]")
find_and_traverse_node(tree, "100 Produto acabado")

